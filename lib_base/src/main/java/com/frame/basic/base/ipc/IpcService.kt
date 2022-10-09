package com.frame.basic.base.ipc

import android.app.Service
import android.content.Intent
import android.os.*
import java.io.Serializable

/**
 * @Description:    IPC通讯服务
 * @Author:         fanj
 * @CreateDate:     2022/8/1 14:10
 * @Version:
 */
open class IpcService : Service() {
    private val serverHandler by lazy {
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == IpcHelper.MSG_FROM_CLIENT) {
                    val result = executeMethod(msg.data)
                    //得到发送者对象，方便进行回调
                    val messenger = msg.replyTo
                    val message = Message.obtain(null, IpcHelper.MSG_FROM_SERVER).apply {
                        data = Bundle().apply {
                            putSerializable("result", result as? Serializable?)
                        }
                    }
                    try {
                        messenger.send(message)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = Messenger(serverHandler).binder

    private fun executeMethod(bundle: Bundle): Any? {
        (bundle.getSerializable("method") as? MethodDesc)?.let {
            val parameterTypes = ArrayList<Class<*>>()
            val parameterValues = ArrayList<Any?>()
            it.params.forEach { paramsDesc ->
                parameterTypes.add(paramsDesc.type)
                parameterValues.add(paramsDesc.value)
            }
            val method = javaClass.getDeclaredMethod(it.name, *parameterTypes.toTypedArray())
            return method?.invoke(this, *parameterValues.toTypedArray()) ?: null
        }
        return null
    }
}
