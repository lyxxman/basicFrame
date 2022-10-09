package com.frame.module.demo.activity.dialog

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.VMCall

import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.module.demo.bean.DialogMenu
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 14:07
 * @Version:        1.0.2
 */
@HiltViewModel
class DialogVM @Inject constructor(handle: SavedStateHandle, val homeRepository: HomeRepository) :
    BaseVM(handle) {
    val menus by savedStateLiveData<MutableList<DialogMenu>>("menus")
    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                homeRepository.getDialogMenu().let {
                    menus.postValue(it)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }
}