package com.frame.module.demo.fragment.list

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import com.frame.basic.base.mvvm.c.vms
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.frame.basic.base.mvvm.c.MultiItemProvider
import com.frame.basic.base.mvvm.c.RecyclerViewMultiPlugin
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.R
import com.frame.module.demo.activity.list.ImageItemProvider
import com.frame.module.demo.activity.list.LinearLayoutDecoration
import com.frame.module.demo.activity.list.MultiTypeRecyclerViewVM
import com.frame.module.demo.activity.list.TextItemProvider
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:25
 * @Version:        1.0.2
 */
@AndroidEntryPoint
open class MultiTypeRecyclerViewFragment: CommonBaseFragment<DemoRecyclerViewBinding, MultiTypeRecyclerViewVM>(),
    RecyclerViewMultiPlugin<ArticleImageBean> {
    override val mBindingVM: MultiTypeRecyclerViewVM by vms()
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun getMutableLiveData(): MutableLiveData<MutableList<ArticleImageBean>> {
        return mBindingVM.articleImageList
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        super.initRecyclerView(recyclerView)
        recyclerView.setBackgroundColor(Color.parseColor("#F3F3F3"))
        recyclerView.addItemDecoration(LinearLayoutDecoration(10, 10, true))
    }

    override fun getItemType(data: MutableList<ArticleImageBean>, position: Int): Int {
        val article = data[position]
        return if (article.image.isNullOrBlank()) {
            1
        } else {
            2
        }
    }

    override fun getMultiItemProviders(): MutableList<MultiItemProvider<ArticleImageBean, out ViewDataBinding>> {
        return mutableListOf(
            TextItemProvider(1, R.layout.demo_item_style_text),
            ImageItemProvider(2, R.layout.demo_item_style_image),
        )
    }

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun isAttachToViewPager() = true
}