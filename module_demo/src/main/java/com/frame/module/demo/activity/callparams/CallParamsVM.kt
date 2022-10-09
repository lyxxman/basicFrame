package com.frame.module.demo.activity.callparams

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 9:02
 * @Version:        1.0.2
 */
@HiltViewModel
class CallParamsVM @Inject constructor(handle: SavedStateHandle, homeRepository: HomeRepository): BaseVM(handle) {
    val toNextParams by savedStateLiveData<String>("toNextParams")
    override fun onRefresh(owner: LifecycleOwner) {
    }

    override fun autoOnRefresh() = false
}