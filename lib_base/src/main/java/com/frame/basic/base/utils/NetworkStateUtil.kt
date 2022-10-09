package com.frame.basic.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.mvvm.c.keepVms
import com.frame.basic.base.mvvm.vm.DeviceStateVM

/**
 * 网络状态监听
 *
 * @author Qu Yunshuo
 * @since 2021/7/11 3:58 下午
 */
object NetworkStateUtil {

    private val mNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        private val mDeviceStateVM: DeviceStateVM by keepVms()
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val state = mDeviceStateVM.networkConnectState
            if (state.value != true) {
                state.postValue(true)
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            val state = mDeviceStateVM.networkConnectState
            if (state.value != false) {
                state.postValue(false)
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                val bluetooth = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                val mobile = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                val wifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val newState = NetworkGroup(mobile, wifi, bluetooth)
                if (mDeviceStateVM.networkTypeState.value != newState) {
                    mDeviceStateVM.networkTypeState.postValue(newState)
                }
            }
        }

    }

    /**
     * 注册网络监听客户端
     * @return Unit
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun register() {
        val build = NetworkRequest.Builder().build()
        val cm =
            BaseApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(build, mNetworkCallback)
    }
}

/**
 * 网络状态
 * @param mobile 数据网络是否连接
 * @param wifi wifi是否连接
 * @param bluetooth 蓝牙是否连接
 */
data class NetworkGroup(val mobile: Boolean, val wifi: Boolean, val bluetooth: Boolean)