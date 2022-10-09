package com.frame.module.demo.activity.refreshlayout

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.VMCall

import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 9:05
 * @Version:        1.0.2
 */
@HiltViewModel
class RefreshLayoutVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) : BaseVM(handle) {
    val data by savedStateLiveData<String>("data")
    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getRefreshLayoutData().let {
                    data.postValue(it)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {}
        })
    }

    override fun autoOnRefresh() = false
}