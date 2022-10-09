package com.frame.basic.base.utils.lifecycle

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Description:    绑定Lifecycle，自动维护一些animation的生命周期
 * @Author:         fanj
 * @CreateDate:     2021/7/12 16:50
 * @Version:        1.0
 */
class AnimateLifecycle(
    var lifecycleOwner: LifecycleOwner,
    var animViews: ArrayList<View>
) : LifecycleObserver {
    companion object {
        private val owers = HashMap<LifecycleOwner, AnimateLifecycle>()
        fun put(view: View) {
            if (view.context == null) {
                return
            }
            if (view.context !is LifecycleOwner) {
                return
            }
            val owner = view.context as LifecycleOwner
            var lifecycleAgent = owers[owner]
            if (lifecycleAgent == null) {
                val list = ArrayList<View>()
                lifecycleAgent = AnimateLifecycle(owner, list)
                owers[owner] = lifecycleAgent
                owner.lifecycle.addObserver(lifecycleAgent)
            }
            lifecycleAgent.animViews.add(view)
        }

        private fun clearRunnable(lifecycleOwner: LifecycleOwner) {
            owers.remove(lifecycleOwner)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestory() {
        animViews.forEach {
            it.clearAnimation()
        }
        animViews.clear()
        clearRunnable(lifecycleOwner)
    }
}