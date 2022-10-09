package com.frame.basic.base.utils

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.view.*
import androidx.annotation.ColorInt
import com.jaeger.library.StatusBarUtil
import com.frame.basic.base.mvvm.c.UIControl


/**
 * 状态栏、导航栏自定义工具
 */
object UIBarUtils {
    fun setStatusBarDarkFont(activity: Activity, dark: Boolean) {
        if (dark) {
            StatusBarUtil.setLightMode(activity)
        } else {
            StatusBarUtil.setDarkMode(activity)
        }
    }

    fun setNavigationBarDarkIcon(activity: Activity, dark: Boolean) {
        if (isSupportNavigationBar(activity)) {
            var vis = activity.window.decorView.systemUiVisibility
            vis = if (dark) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // 黑色
            } else {
                //白色
                vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            activity.window.decorView.systemUiVisibility = vis
        }
    }

    fun setNavigationBarColor(activity: Activity, @ColorInt color: Int) {
        if (isSupportNavigationBar(activity)) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = color
        }
    }

    fun initStatusAndNavigationBar(activity: Activity, uiControl: UIControl<*>) {
        uiControl.statusBarDarkFont()?.let {
            setStatusBarDarkFont(activity, it)
        }
        uiControl.navigationBarDarkIcon()?.let {
            setNavigationBarDarkIcon(activity, it)
        }
        uiControl.navigationBarColor()?.let {
            setNavigationBarColor(activity, it)
        }
    }

    /**
     * 是否支持虚拟导航栏
     */
    fun isSupportNavigationBar(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm = activity.windowManager ?: return false
            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y !== size.y || realSize.x !== size.x
        }
        val menu: Boolean = ViewConfiguration.get(activity).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !menu && !back
    }
}