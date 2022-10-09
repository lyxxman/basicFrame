package com.frame.module.demo.activity.page

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.mvvm.vm.VMCall

import com.frame.basic.base.mvvm.c.PagingControl
import com.frame.module.demo.activity.list.MultiTypeRecyclerViewVM
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


@HiltViewModel
class PageVM @Inject constructor(
    handle: SavedStateHandle,
    private val mRepository: HomeRepository
) :
    MultiTypeRecyclerViewVM(handle, mRepository), PagingControl {
    val articleImagePageList by savedStateLiveData<MutableList<ArticleImageBean>>("articleImagePageList")

    override fun onRefresh(owner: LifecycleOwner) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getArticleTextBean().let {
                    articleImagePageList.postValue(it as MutableList<ArticleImageBean>)
                    loadSuccess()
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }

    override fun onLoadMore(owner: LifecycleOwner, pageNo: Int) {
        launch(object : VMCall {
            override val execute: suspend CoroutineScope.() -> Unit = {
                mRepository.getArticleTextBean(currentPage = pageNo).let {
                    articleImagePageList.postValue(articleImagePageList.value?.apply {
                        addAll(it)
                    })
                    loadSuccess(it.isNullOrEmpty())
                }
            }

            override fun onError(code: Int, error: String?, e: Throwable) {
            }

        })
    }

}