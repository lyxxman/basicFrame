package com.frame.basic.base.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle

/**
 * @Description:    应用异常重启恢复时自动跳转启动页
 * @Author:         fanj
 * @CreateDate:     2022/9/28 15:30
 * @Version:
 */
class ApplicationRebootHelper : Application.ActivityLifecycleCallbacks {
    companion object {
        const val APPLICATION_PROCESS_ID = "APPLICATION_PROCESS_ID"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val preProcessId = savedInstanceState?.getInt(APPLICATION_PROCESS_ID)
        val curProcessId = android.os.Process.myPid()
        if (preProcessId != null && preProcessId != android.os.Process.myPid()) {
            //跳转到启动页
            activity.overridePendingTransition( 0,0)
            activity.packageManager.getLaunchIntentForPackage(activity.packageName)?.apply {
                // 下面这个Flag至关重要，会清空栈里所有的Activity
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity.startActivity(this)
            }
        } else {
            savedInstanceState?.putInt(APPLICATION_PROCESS_ID, curProcessId)
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val curProcessId = android.os.Process.myPid()
        outState.putInt(APPLICATION_PROCESS_ID, curProcessId)
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}