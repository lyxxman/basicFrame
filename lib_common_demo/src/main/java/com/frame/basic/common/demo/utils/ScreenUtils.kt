package com.frame.basic.common.demo.utils

import android.content.Context
import kotlin.math.sqrt

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/12/3 9:51
 * @Version:        1.0.2
 */
object ScreenUtils {
    /**
     * 获取当前手机屏幕的尺寸(单位:像素)
     */
    fun getPhysicalSize(mContext: Context): Float {
        val dm = mContext.resources.displayMetrics
        val width = (dm.widthPixels / dm.xdpi) * (dm.widthPixels / dm.xdpi)
        val height = (dm.heightPixels / dm.ydpi) * (dm.widthPixels / dm.xdpi)
        return sqrt(width + height)
    }
}