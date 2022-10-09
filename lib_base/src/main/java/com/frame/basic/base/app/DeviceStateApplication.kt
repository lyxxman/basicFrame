package com.frame.basic.base.app

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Service
import android.content.Context
import android.telephony.TelephonyManager
import com.google.auto.service.AutoService
import com.hjq.permissions.XXPermissions
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.ktx.intervalTask
import com.frame.basic.base.ktx.nowTime
import com.frame.basic.base.mvvm.c.keepVms
import com.frame.basic.base.mvvm.vm.DeviceStateVM
import com.frame.basic.base.receiver.GlobalReceiverManager
import com.frame.basic.base.receiver.IntentFilterGroup
import com.frame.basic.base.utils.DownLoadSpeedUtils
import com.frame.basic.base.utils.NetworkStateUtil

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/6/1 14:03
 * @Version:
 */
@AutoService(ApplicationLifecycle::class)
class DeviceStateApplication : ApplicationLifecycle {
    private val mDeviceStateVM: DeviceStateVM by keepVms()
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }

    override fun initByFrontDesk(): InitDepend? = null


    override fun initByBackstage() {
        //初始化网络状态监听客户端
        initNetworkStateClient()
        //初始化通话状态监听器
        initPhoneStateListener(BaseApplication.application)
        //初始化其他设备状态监听
        initOtherDeviceStateListener(BaseApplication.application)
        //初始化网络下行速度监听
        initNetworkDownloadSpeedListener(BaseApplication.application)
    }

    override fun onForeground(application: Application) {
        super.onForeground(application)
        mDeviceStateVM.appForegroundState.let {
            if (it.value != true) {
                it.value = true
            }
        }
    }

    override fun onBackground(application: Application) {
        super.onBackground(application)
        mDeviceStateVM.appForegroundState.let {
            if (it.value != false) {
                it.value = false
            }
        }
    }

    /**
     * 初始化通话状态监听器
     */
    @SuppressLint("MissingPermission")
    private fun initPhoneStateListener(application: Application): String {
        intervalTask(3) {
            if (XXPermissions.isGranted(application, Manifest.permission.READ_PHONE_STATE)) {
                try {
                    val state = mDeviceStateVM.phoneState
                    val manager =
                        application.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
                    if (state.value != manager.callState) {
                        state.postValue(manager.callState)
                    }
                } catch (e: Exception) {
                    //没有权限就不处理
                }
            }
        }
        return "PhoneStateListener -->> init complete"
    }

    /**
     * 初始化网络状态监听客户端
     * @return Unit
     */
    private fun initNetworkStateClient(): String {
        NetworkStateUtil.register()
        return "NetworkStateClient -->> init complete"
    }

    /**
     * 初始化其他设备状态监听
     * @return Unit
     */
    private fun initOtherDeviceStateListener(application: Application): String {
        GlobalReceiverManager
            .get(application)
            .register(IntentFilterGroup.screenStateFilters) //监听息屏、亮屏状态
            .start()
        return "OtherDeviceStateListener -->> init complete"
    }

    /**
     * 初始化网络下行速度监听
     */
    private fun initNetworkDownloadSpeedListener(application: Application): String {
        intervalTask(2, startTime = nowTime() + 1000L) {
            val speed = DownLoadSpeedUtils.getNetSpeed(application.applicationInfo.uid)
            mDeviceStateVM.networkDownloadSpeed.postValue(speed)
        }
        return "tNetworkDownloadSpeedListener -->> init complete"
    }
}