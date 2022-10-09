package com.frame.basic.base.ktx

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * 转换成可以支持上传进度的文件类型
 */
fun File.toProgressFile(
    partName: String = "",
    onProgress: ((byte: Long, totalLength: Long) -> Unit)? = null
): ProgressFile {
    return ProgressFile(this, partName, onProgress)
}

/**
 * 生成单文件的MultipartBody.Part
 */
fun File.toMultipartBodyPart(
    partName: String = "",
    onProgress: ((byte: Long, totalLength: Long) -> Unit)? = null
): MultipartBody.Part {
    val body = ProgressRequestBody(asRequestBody("multipart/form-data".toMediaType()), onProgress)
    return MultipartBody.Part.createFormData(partName, name, body)
}

/**
 * 生成多文件的MultipartBody.Part
 */
fun List<ProgressFile>.toMultipartBodyPart(): ArrayList<MultipartBody.Part> {
    val parts = ArrayList<MultipartBody.Part>()
    forEach {
        parts.add(it.file.toMultipartBodyPart(it.partName, it.onProgress))
    }
    return parts
}

/**
 * 支持上传进度的File包装类
 */
class ProgressFile(
    val file: File,
    val partName: String = "",
    val onProgress: ((byte: Long, totalLength: Long) -> Unit)? = null
) {
    /**
     * 生成单文件的MultipartBody.Part
     */
    fun toMultipartBodyPart(): MultipartBody.Part {
        val body =
            ProgressRequestBody(file.asRequestBody("multipart/form-data".toMediaType()), onProgress)
        return MultipartBody.Part.createFormData(partName, file.name, body)
    }
}