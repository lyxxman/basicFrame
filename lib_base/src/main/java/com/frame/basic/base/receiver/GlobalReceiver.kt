package com.frame.basic.base.receiver

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.frame.basic.base.mvvm.c.keepVms
import com.frame.basic.base.mvvm.vm.DeviceStateVM

/**
 * 全局广播事件监听
 */
internal class GlobalReceiver : BroadcastReceiver() {
    private val mDeviceStateVM: DeviceStateVM by keepVms()
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    mDeviceStateVM.screenState.let { state ->
                        if (state.value != 1) {
                            state.postValue(1)
                        }
                    }
                }
                Intent.ACTION_SCREEN_ON -> {
                    mDeviceStateVM.screenState.let { state ->
                        if (state.value != 0) {
                            state.postValue(0)
                        }
                    }
                }
                Intent.ACTION_USER_PRESENT -> {
                    mDeviceStateVM.screenState.let { state ->
                        if (state.value != 2) {
                            state.postValue(2)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

internal class GlobalReceiverManager(private val app: Application) {
    companion object {
        fun get(app: Application) = GlobalReceiverManager(app)
    }

    private val intentFilters = ArrayList<String>()
    fun register(vararg action: String) {
        action.forEach {
            intentFilters.add(it)
        }
    }

    fun register(actions: ArrayList<String>): GlobalReceiverManager {
        intentFilters.addAll(actions)
        return this
    }

    fun start() {
        app.registerReceiver(GlobalReceiver(), IntentFilter().apply {
            intentFilters.forEach {
                addAction(it)
            }
        })
    }

}

internal object IntentFilterGroup {
    //屏幕状态组 息屏、亮屏、解锁
    val screenStateFilters by lazy { arrayListOf(Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON, Intent.ACTION_USER_PRESENT) }
}