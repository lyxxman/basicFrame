package com.frame.basic.base.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Description:    FragmentTab控制器
 * @Author:         fanj
 * @CreateDate:     2022/2/25 15:49
 * @Version:
 * @param containerId frameLayout容器Id
 */
class FragmentTabControl(
    private val ower: LifecycleOwner,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : LifecycleObserver {
    private val fragmentMap by lazy { HashMap<Int, Fragment>() }
    private var showingTabId = -1

    init {
        ower.lifecycle.addObserver(this)
    }

    /**
     * 绑定tab按钮和对应的fragment
     * @param tabId 标识，一般用触发按钮的id
     * @param fragment 绑定的Fragment实例
     */
    fun bind(tabId: Int, fragment: Fragment): FragmentTabControl {
        fragmentMap[tabId] = fragment
        return this
    }

    /**
     * 当前展示的Tab
     * @param tabId 标识，一般用触发按钮的id
     */
    fun show(tabId: Int) {
        if (tabId == showingTabId){
            return
        }
        fragmentMap[tabId]?.let { fragment ->
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentMap[showingTabId]?.let {
                if (it.isAdded && !it.isHidden) {
                    fragmentTransaction.hide(it)
                }
            }
            if (!fragment.isAdded) {
                fragmentTransaction.add(containerId, fragment)
            }
            if (fragment.isHidden) {
                fragmentTransaction.show(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
            showingTabId = tabId
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(){
        fragmentMap.clear()
    }
}