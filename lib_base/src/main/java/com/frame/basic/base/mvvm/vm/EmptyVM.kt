package com.frame.basic.base.mvvm.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle

/**
 * 空的ViewModel 主要给现阶段不需要ViewModel的界面使用
 *
 * @author Qu Yunshuo
 * @since 2021/7/10 11:04 上午
 */
class EmptyVM(handle: SavedStateHandle) : BaseVM(handle) {
    override fun onRefresh(owner: LifecycleOwner) {
    }
    override fun autoOnRefresh() = false
}