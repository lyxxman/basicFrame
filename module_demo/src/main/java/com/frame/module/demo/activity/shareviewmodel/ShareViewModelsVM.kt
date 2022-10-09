package com.frame.module.demo.activity.shareviewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:13
 * @Version:        1.0.2
 */
@HiltViewModel
class ShareViewModelsVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) : BaseVM(handle) {
    val data by savedStateLiveData<Long>("data", 0L)

    override fun onRefresh(owner: LifecycleOwner) {
    }

    override fun autoOnRefresh() = false
}