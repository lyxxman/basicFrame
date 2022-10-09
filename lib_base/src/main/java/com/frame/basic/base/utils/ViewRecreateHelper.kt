package com.frame.basic.base.utils

import android.os.Bundle

object ViewRecreateHelper {
    /**
     * 重建标记key
     */
    const val KEY_RECREATE = "recreate"

    /**
     * 加载标记key
     */
    const val KEY_LOAD = "reload"

    /**
     * 保存重建状态
     */
    fun saveRecreateStatus(obj: Any, bundle: Bundle) {
        bundle.putBoolean("${obj.javaClass.name}_${KEY_RECREATE}", true)
    }

    /**
     * 保存load状态
     */
    fun saveLoadStatus(obj: Any, bundle: Bundle) {
        bundle.putBoolean("${obj.javaClass.name}_${KEY_LOAD}", true)
    }

    /**
     * 获取重建状态
     * @return 是否重建
     */
    fun getRecreateStatus(obj: Any, savedInstanceState: Bundle?): Boolean {
        return savedInstanceState?.getBoolean("${obj.javaClass.name}_${KEY_RECREATE}") ?: false
    }

    /**
     * 获取load状态
     * @return 是否重建
     */
    fun getLoadStatus(obj: Any, savedInstanceState: Bundle?): Boolean {
        return savedInstanceState?.getBoolean("${obj.javaClass.name}_${KEY_LOAD}") ?: false
    }

}