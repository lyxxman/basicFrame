package com.frame.basic.common.demo.constant

/**
 * @Description:    接口公共地址
 * @Author:         fanj
 * @CreateDate:     2021/11/10 10:43
 * @Version:        1.0.2
 */
internal object NetBaseUrlConstant {

    val MAIN_URL = "http://www.baidu.com"
    get() {
        if (field.isEmpty()){
            throw NotImplementedError("请求改你的 MAIN_URL 的值为自己的请求地址")
        }
       return  field
    }
}