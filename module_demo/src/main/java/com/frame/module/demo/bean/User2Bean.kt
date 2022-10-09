package com.frame.module.demo.bean

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/12/2 15:09
 * @Version:        1.0.2
 */
class User2Bean(var name: String) {
    fun speak(desc: String) {
        name = "$name: $desc"
    }
}