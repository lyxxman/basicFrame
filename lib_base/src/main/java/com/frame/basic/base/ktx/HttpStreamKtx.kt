package com.frame.basic.base.ktx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okio.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile


/**
 * 写入文件
 * 这里不能主动关闭InputStream，因为okHttp还在使用，他会断开
 * @param totalLength 文件总大小
 * @param writeProgress 写入进度
 */
@Throws(Exception::class)
internal fun writeFile(
    inputStream: InputStream,
    totalLength: Long,
    writeProgress: DownloadProgress
) {
    val targetFile = writeProgress.getTargetFile()
    if (!targetFile.exists()) {
        if (!targetFile.parentFile.exists()) {
            targetFile.parentFile.mkdir()
        }
        try {
            targetFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            writeProgress.onError("createNewFile IOException")
        }
    }
    var os: RandomAccessFile? = null
    val existSize = targetFile.length()
    if (totalLength == -1L) {
        //todo 这里很坑，如果服务器不支持content-length，这个时候无法区分，且（服务器支持情况下）okhttp在下载完的情况下再去获取content-length就会没有值且抛异常
        writeProgress.onProgress(writeProgress.url, existSize, existSize)
        writeProgress.onSuccess(writeProgress.url, targetFile)
        return
    }
    val sBufferSize = 8192
    os = RandomAccessFile(targetFile, "rw")
    os.seek(existSize)
    var curSize = existSize
    val b = ByteArray(sBufferSize)
    var len = -1
    while (inputStream.read(b, 0, sBufferSize).also { len = it } != -1) {
        if (!writeProgress.isCancel() && writeProgress.context.isActive) {
            os.write(b, 0, len)
            curSize += len.toLong()

            writeProgress.onProgress(
                writeProgress.url, curSize, if (totalLength <= 0) {
                    curSize
                } else {
                    existSize + totalLength
                }
            )
        } else {
            break
        }
    }
    if (!writeProgress.isCancel()) {
        writeProgress.onProgress(writeProgress.url, curSize, curSize)
        writeProgress.onSuccess(writeProgress.url, targetFile)
    }
    //不能在这里close - inputStream，交给okHttp处理
    //inputStream.close()
    os.close()
}

class ProgressRequestBody(
    private val requestBody: RequestBody,
    private val onProgress: ((byte: Long, totalLength: Long) -> Unit)? = null,
) : RequestBody() {
    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType? = requestBody.contentType()

    @Throws(IOException::class)
    override fun contentLength(): Long = requestBody.contentLength()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (sink is Buffer) {
            requestBody.writeTo(sink)
        } else {
            if (bufferedSink == null) {
                bufferedSink = sink(sink).buffer()
            }
            requestBody.writeTo(bufferedSink!!)
            bufferedSink!!.flush()
        }
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            var bytesWritten = 0L
            var contentLength = 0L
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调
                onProgress?.invoke(bytesWritten, contentLength)
            }
        }
    }
}

class ProgressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(
                ProgressResponseBody(
                    chain.request().url.toUrl().toString(),
                    originalResponse.body!!
                )
            )
            .build()
    }
}


class ProgressResponseBody(
    private val url: String,
    private val responseBody: ResponseBody,
) : ResponseBody() {
    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? = responseBody.contentType()
    override fun source(): BufferedSource {
        val downloadProgress = DownloadProgress.downloadProgressMap[url]
        if (downloadProgress != null) {
            writeFile(responseBody.byteStream(), responseBody.contentLength(), downloadProgress)
        }
        return responseBody.source()
    }
}

abstract class DownloadProgress(val context: CoroutineScope, val url: String, val directory: File) {
    companion object {
        internal val downloadProgressMap by lazy {
            HashMap<String, DownloadProgress>()
        }

        /**
         * 停止下载
         */
        fun cancel(progress: DownloadProgress) {
            progress.cancel()
        }

        /**
         * 停止下载
         */
        fun cancel(url: String) {
            downloadProgressMap[url.toHttpUrl().toUrl().toString()]?.let {
                it.cancel()
            }
        }

        private fun destroy(progress: DownloadProgress) {
            downloadProgressMap.remove(progress.okhttpUrl)
        }

        private fun addProgress(progress: DownloadProgress) {
            downloadProgressMap[progress.okhttpUrl] = progress
        }
    }

    private var okhttpUrl: String = url.toHttpUrl().toUrl().toString()

    init {
        addProgress(this@DownloadProgress)
    }

    private var canceled = false

    /**
     * 下载进度
     */
    abstract fun onProgress(url: String, byte: Long, totalLength: Long)

    /**
     * 下载成功
     */
    open fun onSuccess(url: String, file: File) {
        destroy(this)
    }

    /**
     * 下载失败
     */
    open fun onError(error: String) {
        destroy(this)
    }

    /**
     * 终止下载/上传
     */
    fun cancel() {
        canceled = true
        destroy(this)
        context.cancel()
    }

    /**
     * 是否已终止
     */
    internal fun isCancel() = canceled

    /**
     * 获取目标文件
     */
    fun getTargetFile() = File(directory, url.toUrlFileName())

    /**
     * 获取断点header
     */
    fun getRangeHttpHeader() = if (getTargetFile().exists()) {
        getTargetFile().length()
    } else {
        0
    }.toRangeHttpHeader()
}
