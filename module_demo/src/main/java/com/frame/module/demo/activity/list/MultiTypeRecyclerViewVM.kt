package com.frame.module.demo.activity.list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.VMCall

import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


@HiltViewModel
open class MultiTypeRecyclerViewVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) :
    BaseVM(handle) {
    val articleImageList by savedStateLiveData<MutableList<ArticleImageBean>>("articleImageList")
    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getArticleTextBean().let {
                    articleImageList.postValue(it as MutableList<ArticleImageBean>)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }
}