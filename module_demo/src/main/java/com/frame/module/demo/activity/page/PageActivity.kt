package com.frame.module.demo.activity.page

import com.frame.basic.base.mvvm.c.vms
import androidx.lifecycle.MutableLiveData
import com.frame.basic.base.mvvm.c.RecyclerViewMultiPlugin
import com.frame.basic.common.demo.ui.refresh.CommonBaseRefreshLayoutPlugin
import com.frame.module.demo.activity.list.MultiTypeRecyclerViewActivity
import com.frame.module.demo.bean.ArticleImageBean
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageActivity :
    MultiTypeRecyclerViewActivity(),
    RecyclerViewMultiPlugin<ArticleImageBean>, CommonBaseRefreshLayoutPlugin {
    override fun title() = "分页列表"
    override val mBindingVM: PageVM by vms()
    override fun getMutableLiveData(): MutableLiveData<MutableList<ArticleImageBean>> {
        return mBindingVM.articleImagePageList
    }
}
