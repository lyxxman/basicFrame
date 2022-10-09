package com.frame.basic.base.mvvm.vm

import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.frame.basic.base.utils.NetworkGroup

/**
 * 设备状态监听
 */
class DeviceStateVM : CoreVM() {
    /**
     * 网络连接状态监听
     * true: 连接中  false：已断开
     */
    val networkConnectState = MutableLiveData<Boolean>()

    /**
     * 网络连接类型状态监听
     */
    val networkTypeState = MutableLiveData<NetworkGroup>()

    /**
     * 通话状态监听
     * TelephonyManager.CALL_STATE_IDLE: 空闲中
     * TelephonyManager.CALL_STATE_OFFHOOK: 振铃中
     * TelephonyManager.CALL_STATE_RINGING: 通话中
     */
    val phoneState = MutableLiveData<Int>()

    /**
     * 屏幕状态监听
     * 0:亮屏  1:息屏  2:解锁
     */
    val screenState = MutableLiveData<Int>()

    /**
     * 前台状态监听
     * true: 前台  false:后台
     */
    val appForegroundState = MutableLiveData<Boolean>()

    /**
     * 网络下行速度
     * 单位（b/s）
     */
    val networkDownloadSpeed = MutableLiveData<Long>()
}