package com.frame.module.demo.net

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


/**
 * Home模块的接口
 * @author fanj
 * @since 11/20/21 15:42
 */
interface HomeApiService {
    /**
     * 上传文件
     */
    @POST("http://192.168.30.145:9999/hfs/")
    @Multipart
    suspend fun uploadFile(@Part file: MultipartBody.Part): ResponseBody
}