package com.frame.module.demo.activity.maininteraction

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.ApiException
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.mvvm.vm.VMCall
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 11:10
 * @Version:        1.0.2
 */
@HiltViewModel
class MainInteractionVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) : BaseVM(handle) {
    val data by savedStateLiveData<String>("data")
    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getInteractionData().let {
                    data.postValue(it)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }
        })
    }

    fun requestFail() {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                loading()
                delay(1000L)
                throw ApiException(-1, "获取失败")
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
                loadError(code, error)
            }

        })
    }

    fun requestEmpty() {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                loading()
                delay(1000L)
                loadSuccess(true)
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }

    fun submitData() {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                showPopLoading("提交中")
                delay(1000L)
                dismissPopLoading()
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }
}