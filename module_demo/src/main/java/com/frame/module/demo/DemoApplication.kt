package com.frame.module.demo

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.frame.basic.base.app.ApplicationLifecycle
import com.frame.basic.base.app.InitDepend
import com.frame.basic.base.ipc.IpcHelper
import com.frame.basic.base.mvvm.c.keepVms
import com.frame.basic.base.utils.AppUtils
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsKeepVM
import com.frame.module.demo.ipc.LocalService
import com.frame.module.demo.ipc.Remote2Service
import com.frame.module.demo.ipc.RemoteService

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/5/13 13:52
 * @Version:
 */
@AutoService(ApplicationLifecycle::class)
class DemoApplication : ApplicationLifecycle {
    private val mViewModel: ShareViewModelsKeepVM by keepVms()
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        val processName = AppUtils.getProcessName(application)
        val packageName = application.packageName
        when(processName){
            packageName -> {
                IpcHelper.register(RemoteService::class.java,  Remote2Service::class.java)
            }
            "${packageName}:remote" -> {
                IpcHelper.register(LocalService::class.java, Remote2Service::class.java)
            }
            "${packageName}:remote2" -> {
                IpcHelper.register(LocalService::class.java, RemoteService::class.java)
            }

        }
    }

    override fun onTerminate(application: Application) {
    }

    override fun initByFrontDesk(): InitDepend? = null

    override fun initByBackstage() {
    }
}