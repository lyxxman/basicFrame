package com.frame.basic.base.utils

import android.app.Activity
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.frame.basic.base.mvvm.c.keepVms
import com.frame.basic.base.mvvm.vm.DeviceStateVM
import com.frame.basic.base.utils.DownloadManager.Companion.DEFAULT_LIMIT_SPEED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.io.Serializable
import java.net.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.regex.Pattern
import javax.net.ssl.SSLException

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/6/28 20:12
 * @Version:
 */
class DownloadManager(private val scope: CoroutineScope, private val maxTask: Int) {
    companion object {
        private const val READ_TIME_OUT = 20 * 1000
        private const val CHAR_SET = "utf-8"
        private const val READ_BUFFER_SIZE = 1024
        internal const val DEFAULT_LIMIT_SPEED = Long.MAX_VALUE

        private var downloadManager: DownloadManager? = null

        /**
         * @param maxTask 最大同时下载任务数
         */
        @Synchronized
        fun build(scope: CoroutineScope, @IntRange(from = 1, to = 100) maxTask: Int) {
            if (downloadManager == null) {
                downloadManager = DownloadManager(scope, maxTask)
            }
            downloadManager?.build()
        }

        fun get() = downloadManager!!
    }

    private val mDeviceStateVM by keepVms<DeviceStateVM>()
    private val executePool by lazy { LinkedBlockingQueue<DownloadTask>() }
    private val executingPool by lazy { LinkedBlockingQueue<DownloadTask>() }
    private val reconnectPool by lazy { LinkedBlockingQueue<DownloadTask>() }
    private val noSupportNetworkPool by lazy { LinkedBlockingQueue<DownloadTask>() }

    //安全网速，用以限制总下载速度
    private var safeSpeed = 0L
    internal fun build() {
        //观察网络类型，当网络类型变更后，将网络状态不满足条件的任务放回等待执行的任务队列
        mDeviceStateVM.networkTypeState.observeForever { networkType ->
            scope.launch(Dispatchers.Default) {
                executePool.addAll(noSupportNetworkPool)
                noSupportNetworkPool.clear()
            }
        }
        //观察网络状态，当网络连接成功后，将需要自动重连的任务加到等待执行的任务队列
        mDeviceStateVM.networkConnectState.observeForever { connected ->
            if (connected) {
                scope.launch(Dispatchers.Default) {
                    executePool.addAll(reconnectPool)
                    reconnectPool.clear()
                }
            }
        }
        //观察网络速度，当需要限速时，则进行限速
        mDeviceStateVM.networkDownloadSpeed.observeForever { speed ->
            scope.launch(Dispatchers.Default) {
                //如果安全网速>0，则限制当前下载任务的速度
                if (safeSpeed > 0L) {
                    val availableSpeed = speed - safeSpeed
                    if (availableSpeed > 0) {
                        executingPool.filter { it.builder.allowSafeSpeedModel }
                            .let { allowSafeSpeedModelTasks ->
                                if (allowSafeSpeedModelTasks.isEmpty()) {
                                    return@launch
                                }
                                val averageTaskSpeed =
                                    availableSpeed / allowSafeSpeedModelTasks.size
                                val executeSpeed = if (averageTaskSpeed < 1024) {
                                    1024 //最低速度1kb/s
                                } else {
                                    averageTaskSpeed
                                }
                                allowSafeSpeedModelTasks.forEach { task ->
                                    task.speedLimit(executeSpeed)
                                }
                            }
                    }
                } else {
                    executingPool.filter { it.builder.allowSafeSpeedModel }
                        .let { allowSafeSpeedModelTasks ->
                            if (allowSafeSpeedModelTasks.isEmpty()) {
                                return@launch
                            }
                            allowSafeSpeedModelTasks.forEach { task ->
                                task.restoreSpeed()
                            }
                        }
                }
            }
        }
        for (i in 0 until maxTask) {
            scope.launch(Dispatchers.IO) {
                while (true) {
                    val task = executePool.take()
                    executingPool.offer(task)
                    // 不支持的网络类型任务放到等待执行队列
                    if (!checkNetwork(task)) {
                        task.state.postValue(DownloadTask.State.NO_SUPPORT_NETWORK_TYPE)
                        noSupportNetworkPool.offer(task)
                        return@launch
                    }
                    //开始执行
                    downloadToFile(task)
                    executingPool.remove(task)
                }
            }
        }
    }

    /**
     * 检查网络是否满足下载条件
     */
    private fun checkNetwork(task: DownloadTask): Boolean {
        if (mDeviceStateVM.networkTypeState.value != null) {
            when (task.builder.networkType) {
                DownloadTask.NetworkType.ONLY_WIFI -> {
                    if (!mDeviceStateVM.networkTypeState.value!!.wifi) {
                        return false
                    }
                }
                DownloadTask.NetworkType.ONLY_MOBILE_NETWORK -> {
                    if (!mDeviceStateVM.networkTypeState.value!!.mobile) {
                        return false
                    }
                }
                DownloadTask.NetworkType.WIFI_OR_MOBILE_NETWORK -> {
                    if (!mDeviceStateVM.networkTypeState.value!!.wifi && !mDeviceStateVM.networkTypeState.value!!.mobile) {
                        return false
                    }
                }
                else -> {
                    return true
                }
            }
        }
        return true
    }

    /**
     * 启动网速保护模式，用于特定场景使用，以确保特定功能正常运行，如音视频通话、视频播放等
     * 1.开启后，除指定不受该模式限制的任务外，其余所有任务将分摊除保留网速外的剩余网速资源
     * 2.当退出特定场景后，务必调用exitSafeSpeedModel退出网速保护模式，已确保下载任务的网速恢复正常
     * 3.当剩余网速过低时甚至是0时，为确保下载任务正常进行，会把网速限制在1kb/s
     * @param safeSpeed 安全网速（bit/s），该部分网速会被空闲出来给特定功能使用
     */
    fun startSafeSpeedModel(@IntRange(from = 1024, to = Long.MAX_VALUE) safeSpeed: Long) {
        this.safeSpeed = safeSpeed
    }

    /**
     * 退出网速保护模式
     * 1.除特定不受该模式限制的任务外，所有下载任务的速度恢复正常
     */
    fun exitSafeSpeedModel() {
        safeSpeed = 0L
    }

    /**
     *  不受网速保护模式限制的任务
     *  注意：该任务会占用特定场景的带宽，酌情使用
     *  @param speed 不受网速保护模式限制时的速度控制 (byte/s)
     */
    fun ignoreSafeSpeedModel(
        task: DownloadTask,
        @IntRange(
            from = 1024,
            to = Long.MAX_VALUE
        ) speed: Long = Long.MAX_VALUE
    ): DownloadManager {
        task.ignoreSafeSpeedModel(speed)
        return this
    }

    /**
     *  不受网速保护模式限制的任务
     *  注意：该任务会占用特定场景的带宽，酌情使用
     *  @param speed 不受网速保护模式限制时的速度控制 (byte/s)
     */
    fun ignoreSafeSpeedModel(
        url: String,
        @IntRange(
            from = 1024,
            to = Long.MAX_VALUE
        ) speed: Long = Long.MAX_VALUE
    ): DownloadManager {
        scope.launch(Dispatchers.Default) {
            executePool.find { it.url == url }?.let {
                it.ignoreSafeSpeedModel(speed)
            }
            executingPool.find { it.url == url }?.let {
                it.ignoreSafeSpeedModel(speed)
            }
            reconnectPool.find { it.url == url }?.let {
                it.ignoreSafeSpeedModel(speed)
            }
            noSupportNetworkPool.find { it.url == url }?.let {
                it.ignoreSafeSpeedModel(speed)
            }
        }
        return this
    }

    /**
     *  允许网速保护模式限制的任务
     */
    fun allowSafeSpeedModel(task: DownloadTask): DownloadManager {
        task.allowSafeSpeedModel()
        return this
    }

    /**
     *  允许网速保护模式限制的任务
     */
    fun allowSafeSpeedModel(url: String): DownloadManager {
        scope.launch(Dispatchers.Default) {
            executePool.find { it.url == url }?.let {
                it.allowSafeSpeedModel()
            }
            executingPool.find { it.url == url }?.let {
                it.allowSafeSpeedModel()
            }
            reconnectPool.find { it.url == url }?.let {
                it.allowSafeSpeedModel()
            }
            noSupportNetworkPool.find { it.url == url }?.let {
                it.allowSafeSpeedModel()
            }
        }
        return this
    }

    /**
     * 取消任务
     */
    fun cancel(task: DownloadTask) {
        task.cancel()
        executePool.remove(task)
        executingPool.remove(task)
        reconnectPool.remove(task)
        noSupportNetworkPool.remove(task)
    }

    /**
     * 取消任务
     */
    fun cancel(url: String) {
        scope.launch(Dispatchers.Default) {
            executePool.find { it.url == url }?.let {
                cancel(it)
                executePool.remove(it)
            }
            executingPool.find { it.url == url }?.let {
                cancel(it)
                executePool.remove(it)
            }
            reconnectPool.find { it.url == url }?.let {
                cancel(it)
                executePool.remove(it)
            }
            noSupportNetworkPool.find { it.url == url }?.let {
                cancel(it)
                executePool.remove(it)
            }
        }
    }

    /**
     * 添加下载任务
     */
    @Synchronized
    fun download(task: DownloadTask) {
        if (!executePool.contains(task) && !executingPool.contains(task) && !reconnectPool.contains(
                task
            ) && !noSupportNetworkPool.contains(task)
        ) {
            executePool.offer(task)
        }
    }

    /**
     * 创建下载任务
     * 1.如果有相同下载地址的任务则返回已存在的任务
     * @param url 下载地址(通过下载地址来区分是否存在相同任务)
     * @param onNew 当没有相同任务时，则通过Builder创建新的任务
     */
    @Synchronized
    fun createTask(url: String, onNew: (DownloadTask.Builder) -> Unit): DownloadTask {
        var task = executePool.find { it.url == url }
        if (task == null) {
            task = executingPool.find { it.url == url }
        }
        if (task == null) {
            task = reconnectPool.find { it.url == url }
        }
        if (task == null) {
            task = noSupportNetworkPool.find { it.url == url }
        }
        if (task == null) {
            val builder = DownloadTask.Builder()
            task = DownloadTask(url, builder.apply {
                onNew.invoke(this)
            })
            //创建完了，将owner删掉，避免内存泄漏
            builder.owner = null
        }
        return task
    }

    /**
     * 下载到文件
     */
    private suspend fun downloadToFile(task: DownloadTask) {
        task.state.postValue(DownloadTask.State.DOWNLOADING)
        if (task.builder.targetDict == null) {
            throw RuntimeException("targetDict is null!")
        }
        if (!task.builder.targetDict!!.exists()) {
            task.builder.targetDict!!.mkdirs()
        }
        if (!task.builder.targetDict!!.isDirectory) {
            throw RuntimeException("${task.builder.targetDict!!.absolutePath} is not a directory!")
        }
        val targetFile = task.getTargetFile()
        //存在即认为已经下载完了，如果不存在不代表没下载，后面将先生成临时文件，下载完后再改名
        if (targetFile.exists()) {
            task.progress.postValue(
                DownloadTask.Progress(
                    targetFile.length(),
                    targetFile.length(),
                    0
                )
            )
            task.state.postValue(DownloadTask.State.SUCCESS)
            return
        }
        val tempTargetFile = task.getTempFile()
        if (!tempTargetFile.exists()) {
            tempTargetFile.createNewFile()
        }
        var state: DownloadTask.State? = null
        try {
            var size = tempTargetFile.length()
            val downloadUri = URL(task.url)
            val connection = (downloadUri.openConnection() as HttpURLConnection).apply {
                readTimeout = READ_TIME_OUT
                requestMethod = "GET"
                setRequestProperty("Accept-Charset", CHAR_SET)
                setRequestProperty("Accept-Encoding", "identity")
                setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"
                )
                setRequestProperty("range", "bytes=$size-")
            }
            val responseCode = connection.responseCode
            val contentLength = connection.contentLength
            var inputStream: InputStream? = null
            var randomAccessFile: RandomAccessFile? = null
            var lastTime = System.currentTimeMillis()
            var bytesReadBandwidth = 0L
            var startDownloadTime = System.currentTimeMillis()
            var actualSpeed = 0L
            when (responseCode) {
                206, 200 -> {
                    if (contentLength >= 0) {
                        if (responseCode == 200 && size == contentLength.toLong() && size != 0L) {
                            state = DownloadTask.State.SUCCESS
                            task.progress.postValue(DownloadTask.Progress(size, size, 0))
                            return
                        } else {
                            val serverSize = contentLength + size
                            inputStream = connection.inputStream
                            randomAccessFile = RandomAccessFile(tempTargetFile, "rw")
                            randomAccessFile.seek(size)
                            val readBuffer = ByteArray(READ_BUFFER_SIZE)
                            var len = -1
                            while ((inputStream.read(readBuffer).also { len = it }) != -1) {
                                if (!task.cancel) {
                                    if (checkNetwork(task)) {
                                        randomAccessFile.write(readBuffer, 0, len)
                                        size += len
                                        if (size >= serverSize) {
                                            //下载完成
                                            state = DownloadTask.State.SUCCESS
                                            task.progress.postValue(
                                                DownloadTask.Progress(serverSize, serverSize, 0)
                                            )
                                            break
                                        } else {
                                            //实时下载速度
                                            actualSpeed += len
                                            val offsetTime2 =
                                                System.currentTimeMillis() - startDownloadTime
                                            if (offsetTime2 >= 1000L) {
                                                task.progress.postValue(
                                                    DownloadTask.Progress(
                                                        serverSize,
                                                        size,
                                                        actualSpeed
                                                    )
                                                )
                                                actualSpeed = 0
                                                startDownloadTime = System.currentTimeMillis()
                                            }
                                            //实时限速
                                            bytesReadBandwidth += len
                                            if (bytesReadBandwidth >= task.builder.speed) {
                                                var offsetTime =
                                                    System.currentTimeMillis() - lastTime
                                                if (offsetTime < 1000L) {
                                                    if (offsetTime < 0) {
                                                        offsetTime = 0
                                                    }
                                                    delay(1000L - offsetTime)
                                                }
                                                lastTime = System.currentTimeMillis()
                                                bytesReadBandwidth = 0
                                            }
                                        }
                                    } else {
                                        //不符合网络要求
                                        state = DownloadTask.State.NO_SUPPORT_NETWORK_TYPE
                                        break
                                    }
                                } else {
                                    //任务取消
                                    state = DownloadTask.State.CANCEL
                                    break
                                }
                            }
                        }
                    } else {
                        //不支持断点下载的服务器
                        if (tempTargetFile.exists() && tempTargetFile.length() > 0) {
                            tempTargetFile.delete()
                            tempTargetFile.createNewFile()
                        }
                        inputStream = connection.inputStream
                        randomAccessFile = RandomAccessFile(tempTargetFile, "rw")
                        randomAccessFile.seek(0L)
                        val readBuffer = ByteArray(READ_BUFFER_SIZE)
                        var len = -1
                        while ((inputStream.read(readBuffer).also { len = it }) != -1) {
                            if (!task.cancel) {
                                if (checkNetwork(task)) {
                                    randomAccessFile.write(readBuffer, 0, len)
                                    size += len
                                    //实时下载速度
                                    actualSpeed += len
                                    val offsetTime2 = System.currentTimeMillis() - startDownloadTime
                                    if (offsetTime2 >= 1000L) {
                                        task.progress.postValue(
                                            DownloadTask.Progress(
                                                -1,
                                                size,
                                                actualSpeed
                                            )
                                        )
                                        actualSpeed = 0
                                        startDownloadTime = System.currentTimeMillis()
                                    }
                                    //实时限速
                                    bytesReadBandwidth += len
                                    if (bytesReadBandwidth >= task.builder.speed) {
                                        var offsetTime = System.currentTimeMillis() - lastTime;
                                        if (offsetTime < 1000L) {
                                            if (offsetTime < 0) {
                                                offsetTime = 0
                                            }
                                            delay(1000L - offsetTime)
                                        }
                                        lastTime = System.currentTimeMillis()
                                        bytesReadBandwidth = 0
                                    }
                                } else {
                                    //不符合网络要求
                                    state = DownloadTask.State.NO_SUPPORT_NETWORK_TYPE
                                    break
                                }
                            } else {
                                //任务取消
                                state = DownloadTask.State.CANCEL
                                break
                            }
                        }
                    }
                }
                416 -> {
                    state = DownloadTask.State.SUCCESS
                    task.progress.postValue(DownloadTask.Progress(size, size, 0))
                }
                else -> {
                    state = DownloadTask.State.ERROR
                }
            }
            try {
                randomAccessFile?.close()
                inputStream?.close()
                connection.disconnect()
            } catch (e: Exception) {
            }
        } catch (e: Exception) {
            state =
                if (state == DownloadTask.State.NO_SUPPORT_NETWORK_TYPE || !checkNetwork(task)) {
                    DownloadTask.State.NO_SUPPORT_NETWORK_TYPE
                } else {
                    when (e) {
                        //网络原因造成的失败放到重连队列
                        is UnknownHostException, is ProtocolException, is SocketTimeoutException, is SocketException, is ConnectException, is SSLException -> {
                            if (task.builder.autoReconnect) {
                                DownloadTask.State.RECONNECTION
                            } else {
                                DownloadTask.State.ERROR
                            }
                        }
                        else -> {
                            DownloadTask.State.ERROR
                        }
                    }
                }
        } finally {
            when (state) {
                DownloadTask.State.SUCCESS -> {
                    if (!targetFile.exists()) {
                        tempTargetFile.renameTo(targetFile)
                    }
                    task.progress.postValue(
                        DownloadTask.Progress(
                            targetFile.length(),
                            targetFile.length(),
                            0
                        )
                    )
                }
                DownloadTask.State.RECONNECTION -> {
                    reconnectPool.offer(task)
                    task.progress.postValue(
                        DownloadTask.Progress(
                            task.progress.value?.total ?: 0L,
                            task.progress.value?.current ?: 0L,
                            0
                        )
                    )
                }
                DownloadTask.State.NO_SUPPORT_NETWORK_TYPE -> {
                    noSupportNetworkPool.offer(task)
                    task.progress.postValue(
                        DownloadTask.Progress(
                            task.progress.value?.total ?: 0L,
                            task.progress.value?.current ?: 0L,
                            0
                        )
                    )
                }
                DownloadTask.State.ERROR, DownloadTask.State.CANCEL -> {
                    task.progress.postValue(
                        DownloadTask.Progress(
                            task.progress.value?.total ?: 0L,
                            task.progress.value?.current ?: 0L,
                            0
                        )
                    )
                }
                else -> {}
            }
            if (task.state.value != state) {
                task.state.postValue(state)
            }
        }
    }

}

/**
 * 下载单元
 * @param url 下载地址
 */
class DownloadTask internal constructor(
    val url: String,
    internal val builder: Builder
) : LifecycleEventObserver {
    internal var cancel = false

    //下载状态
    val state by lazy { MutableLiveData<State>(State.WAIT) }

    //下载进度 每秒更新一次
    val progress by lazy { MutableLiveData<Progress>() }

    init {
        builder.owner?.let {
            it.lifecycle.addObserver(this)
        }
    }

    class Builder internal constructor() {

        internal var autoReconnect: Boolean = true
        internal var allowSafeSpeedModel: Boolean = true
        internal var speed: Long = DEFAULT_LIMIT_SPEED
        internal var targetDict: File? = null
        internal var owner: LifecycleOwner? = null
        internal var networkType: NetworkType = NetworkType.ALL

        /**
         * 设置下载目录
         */
        fun setTargetDict(targetDict: File): Builder {
            this.targetDict = targetDict
            return this
        }

        /**
         * 绑定生命周期
         */
        fun attachTo(owner: LifecycleOwner): Builder {
            this.owner = owner
            return this
        }

        /**
         * 是否允许断网自动重连，默认允许
         */
        fun autoReconnect(allow: Boolean): Builder {
            autoReconnect = allow
            return this
        }

        /**
         * 限速(byte/s)
         * @param speed 速度
         *         限制1Mbps（1/8M byte/s）的话，就填 1024x1024/8=131072
         *         限制1Mbps（1M byte/s）的话，就填 1024x1024=1048576
         */
        fun speedLimit(@IntRange(from = 1024, to = Long.MAX_VALUE) speed: Long): Builder {
            this.speed = speed
            return this
        }

        /**
         *  不受网速保护模式限制
         *  注意：该任务会占用特定场景的带宽，酌情使用
         *  @param speed 不受网速保护模式限制时的速度控制 (byte/s)
         */
        fun ignoreSafeSpeedModel(
            @IntRange(
                from = 1024,
                to = Long.MAX_VALUE
            ) speed: Long = Long.MAX_VALUE
        ): Builder {
            allowSafeSpeedModel = false
            speedLimit(speed)
            return this
        }

        /**
         *  允许网速保护模式限制的任务
         */
        fun allowSafeSpeedModel(): Builder {
            this.allowSafeSpeedModel = true
            return this
        }

        /**
         * 网络类型要求，默认所有
         */
        fun networkType(networkType: NetworkType): Builder {
            this.networkType = networkType
            return this
        }
    }


    /**
     * 下载状态
     */
    enum class State {
        //排队等待中
        WAIT,

        //下载错误
        ERROR,

        //下载成功
        SUCCESS,

        //下载取消
        CANCEL,

        //重连中
        RECONNECTION,

        //下载中
        DOWNLOADING,

        //不满足网络条件
        NO_SUPPORT_NETWORK_TYPE
    }

    /**
     * 下载进度 每秒更新一次
     * @param total 文件总大小 bit  如果返回-1，则是当前链接不支持断点下载，无法获取总大小
     * @param current 当前已下载大小 bit
     * @param speed 实时速度 bit/s
     */
    data class Progress(val total: Long, val current: Long, val speed: Long) : Serializable

    /**
     * 网络要求
     */
    enum class NetworkType {
        //仅Wifi下载
        ONLY_WIFI,

        //仅数据网络下载
        ONLY_MOBILE_NETWORK,

        //wifi和数据网络下载
        WIFI_OR_MOBILE_NETWORK,

        //所有
        ALL
    }

    internal fun cancel() {
        cancel = true
        if (state.value != State.SUCCESS) {
            state.postValue(State.CANCEL)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                if ((source is Activity && source.isFinishing) || (source is Fragment && source.activity != null && source.requireActivity().isFinishing)) {
                    DownloadManager.get().cancel(this)
                    this.state.removeObservers(source)
                    this.progress.removeObservers(source)
                }
            }
            Lifecycle.Event.ON_DESTROY -> {
                DownloadManager.get().cancel(this)
                this.state.removeObservers(source)
                this.progress.removeObservers(source)
            }
            else -> {}
        }
    }

    /**
     * 限速(byte/s)
     * @param speed 速度
     *         限制1Mbps（1/8M byte/s）的话，就填 1024x1024/8=131072
     *         限制1Mbps（1M byte/s）的话，就填 1024x1024=1048576
     */
    fun speedLimit(@IntRange(from = 1024, to = Long.MAX_VALUE) speed: Long): DownloadTask {
        builder.speed = speed
        return this
    }

    /**
     * 释放速度
     */
    fun restoreSpeed() {
        builder.speed = Long.MAX_VALUE
    }

    /**
     *  不受网速保护模式限制
     *  注意：该任务会占用特定场景的带宽，酌情使用
     *  @param speed 不受网速保护模式限制时的速度控制 (byte/s)
     */
    fun ignoreSafeSpeedModel(
        @IntRange(
            from = 1024,
            to = Long.MAX_VALUE
        ) speed: Long = Long.MAX_VALUE
    ): DownloadTask {
        builder.allowSafeSpeedModel = false
        speedLimit(speed)
        return this
    }

    /**
     *  允许网速保护模式限制的任务
     */
    fun allowSafeSpeedModel(): DownloadTask {
        builder.allowSafeSpeedModel = true
        return this
    }

    private val pattern by lazy { Pattern.compile("\\S*[?]\\S*") }

    /**
     * 获取文件类型
     */
    private fun getMediaType(): String {
        val askSymbolPos = url.lastIndexOf("?")
        return try {
            if (askSymbolPos > 0) {
                val temp = url.substring(0, askSymbolPos)
                temp.substring(temp.lastIndexOf(".") + 1)
            } else {
                url.substring(url.lastIndexOf(".") + 1)
            }
        } catch (e: Throwable) {
            ""
        }
    }

    /**
     * 获取临时文件的名称
     */
    internal fun getTempFile(): File {
        val md5FileName = MD5Utils.parseStrToMd5L32(url)
        return File(builder.targetDict, md5FileName)
    }

    /**
     * 获取最终目标文件
     */
    fun getTargetFile(): File {
        val md5FileName = MD5Utils.parseStrToMd5L32(url)
        val fileName = "${md5FileName}.${getMediaType()}"
        return File(builder.targetDict, fileName)
    }

}

