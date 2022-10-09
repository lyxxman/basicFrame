package com.frame.module.demo.activity.mainlayout

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
 * @CreateDate:     2021/11/11 15:46
 * @Version:        1.0.2
 */
@HiltViewModel
class MainLayoutVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) : BaseVM(handle) {
    val desc by savedStateLiveData<String>("desc")
    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getDesc().let {
                    desc.postValue(it)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }
}