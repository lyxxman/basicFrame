package com.frame.module.demo.activity.list

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.ktx.loadImage
import com.frame.basic.base.mvvm.c.MultiItemProvider
import com.frame.basic.base.mvvm.c.RecyclerViewMultiPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.databinding.DemoItemStyleImageBinding
import com.frame.module.demo.databinding.DemoItemStyleTextBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MultiTypeRecyclerViewActivity :
    CommonBaseActivity<DemoRecyclerViewBinding, MultiTypeRecyclerViewVM>(),
    RecyclerViewMultiPlugin<ArticleImageBean> {
    override val mBindingVM: MultiTypeRecyclerViewVM by vms()
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun title() = "多布局列表"

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

    override fun getRecyclerView(): RecyclerView  = mBinding.recyclerView
}

class TextItemProvider(override val itemViewType: Int, override val layoutId: Int) :
    MultiItemProvider<ArticleImageBean, DemoItemStyleTextBinding>() {
    override fun convert(
        holder: BaseDataBindingHolder<DemoItemStyleTextBinding>,
        item: ArticleImageBean
    ) {
        holder.dataBinding?.apply {
            titleText.text = item.title
            descText.text = item.desc
        }
    }

}

class ImageItemProvider(override val itemViewType: Int, override val layoutId: Int) :
    MultiItemProvider<ArticleImageBean, DemoItemStyleImageBinding>() {
    override fun convert(
        holder: BaseDataBindingHolder<DemoItemStyleImageBinding>,
        item: ArticleImageBean
    ) {
        holder.dataBinding?.apply {
            titleText.text = item.title
            descText.text = item.desc
            image.loadImage(item.image)
        }
    }
}