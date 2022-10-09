package com.frame.basic.base.utils.lifecycle

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Description:    绑定Lifecycle，自动维护一些runnble的生命周期
 * @Author:         fanj
 * @CreateDate:     2021/5/17 15:01
 * @Version:        1.0
 */
class RunnableLifecycle(
    var lifecycleOwner: LifecycleOwner,
    var runnables: HashMap<View, HashSet<Runnable>>
) : LifecycleObserver {
    companion object {
        private val owers = HashMap<LifecycleOwner, RunnableLifecycle>()
        fun put(view: View, runnable: Runnable) {
            if (view.context == null) {
                return
            }
            if (view.context !is LifecycleOwner) {
                return
            }
            val owner = view.context as LifecycleOwner
            var lifecycleAgent = owers[owner]
            if (lifecycleAgent == null) {
                val list = HashMap<View, HashSet<Runnable>>()
                lifecycleAgent = RunnableLifecycle(owner, list)
                owers[owner] = lifecycleAgent
                owner.lifecycle.addObserver(lifecycleAgent)
            }
            var runnableList = lifecycleAgent.runnables[view]
            if (runnableList == null) {
                runnableList = HashSet<Runnable>()
                lifecycleAgent.runnables[view] = runnableList
            }
            runnableList.add(runnable)
        }

        fun clearRunnable(view: View) {
            if (view.context !is LifecycleOwner) {
                return
            }
            val runnableLifecycle = owers[view.context as LifecycleOwner]
            val runnableMap = runnableLifecycle?.runnables
            runnableMap?.get(view)?.forEach {
                view.removeCallbacks(it)
            }
            runnableMap?.remove(view)
        }

        private fun clearRunnable(lifecycleOwner: LifecycleOwner) {
            owers.remove(lifecycleOwner)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestory() {
        runnables.forEach { entry ->
            entry.value.forEach { runnable ->
                entry.key.removeCallbacks(runnable)
            }
        }
        runnables.clear()
        clearRunnable(lifecycleOwner)
    }
}