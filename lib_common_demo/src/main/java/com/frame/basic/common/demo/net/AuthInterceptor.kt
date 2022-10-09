package com.frame.basic.common.demo.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description:    令牌Demo
 * @Author:         fanj
 * @CreateDate:     2022/2/14 15:37
 * @Version:        1.0.2
 */
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("platform", "E05-BEE-CLIENT-WEB")
        builder.addHeader("userType", "0")
        val token = "toeknsadasd"
        val key = "asdfsadf"
        if(!token.isNullOrEmpty()&&!key.isNullOrEmpty()){
            builder.addHeader("userToken", token)
            builder.addHeader("userKey", key)
        }
        request = builder.build()
        return chain.proceed(request)
    }
}