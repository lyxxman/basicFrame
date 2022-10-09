package com.frame.basic.common.demo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.google.auto.service.AutoService
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.app.ApplicationLifecycle
import com.frame.basic.base.app.InitDepend
import com.frame.basic.base.utils.ProcessUtils
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.ScreenUtils

/**
 * 项目相关的Application
 *
 * @author Qu Yunshuo
 * @since 4/16/21 3:37 PM
 */
@AutoService(ApplicationLifecycle::class)
class CommonApplication : ApplicationLifecycle {

    companion object {
        // 全局CommonApplication
        @SuppressLint("StaticFieldLeak")
        lateinit var mCommonApplication: CommonApplication
    }

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mCommonApplication = this
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {}

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {}

    /**
     * 需要立即进行初始化的放在这里进行并行初始化
     * 需要必须在主线程初始化的放在[InitDepend.mainThreadDepends],反之放在[InitDepend.workerThreadDepends]
     * @return InitDepend 初始化方法集合
     */
    override fun initByFrontDesk(): InitDepend {
        val worker = mutableListOf<() -> String>()
        val main = mutableListOf<() -> String>()
        // 以下只需要在主进程当中初始化 按需要调整
        if (ProcessUtils.isMainProcess(BaseApplication.application)) {
            main.add { initAutoSize() }
        }
        return InitDepend(main, worker)
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackstage() {
    }


    /**
     * 屏幕自适应
     */
    private fun initAutoSize(): String{
        AutoSizeConfig.getInstance().apply {
            val physicalSize = com.frame.basic.common.demo.utils.ScreenUtils.getPhysicalSize(BaseApplication.application)
            if (physicalSize >= 9f){
                //平板
                designHeightInDp = 375
                designWidthInDp = 667
            }else{
                //手机
                designHeightInDp = 667
                designWidthInDp = 375
            }
            //开启Fragment支持自定义适配参数，主要用于splash界面更新弹窗
            isCustomFragment = true
            onAdaptListener = object : onAdaptListener {
                override fun onAdaptBefore(target: Any?, activity: Activity?) {
                    //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                    //根据屏幕方向，设置设计尺寸
                    activity?.let {
                        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            //设置横屏设计尺寸
                            screenWidth = ScreenUtils.getScreenSize(activity)[1]
                            screenHeight = ScreenUtils.getScreenSize(activity)[0]
                        } else {
                            //设置竖屏设计尺寸
                            screenWidth = ScreenUtils.getScreenSize(activity)[0]
                            screenHeight = ScreenUtils.getScreenSize(activity)[1]
                        }
                    }
                }

                override fun onAdaptAfter(target: Any?, activity: Activity?) {
                }

            }
        }
        return "AutoSizeConfig -->> init complete"
    }
}