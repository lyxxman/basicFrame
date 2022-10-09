package com.frame.basic.provider.service

/**
 * @Description:    Demo模块的服务
 * @Author:         fanj
 * @CreateDate:     2021/11/17 12:06
 * @Version:        1.0.2
 */
interface DemoService {
    fun toastSomething(text: String)
    fun speakSomething(user: Any, desc: String):Any
}