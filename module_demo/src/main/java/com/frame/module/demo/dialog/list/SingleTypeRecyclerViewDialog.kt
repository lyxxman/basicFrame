package com.frame.module.demo.dialog.list

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frame.basic.base.mvvm.c.vms
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.ktx.addDelayChangeAfter
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.mvvm.c.RecyclerViewBasicPlugin
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.ui.CommonBaseDialog
import com.frame.module.demo.R
import com.frame.module.demo.activity.list.LinearLayoutDecoration
import com.frame.module.demo.activity.list.SingleTypeRecyclerViewVM
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.databinding.DemoItemStyleTextBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import com.frame.module.demo.databinding.DemoTopSearchBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 15:02
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class SingleTypeRecyclerViewDialog :
    CommonBaseDialog<DemoRecyclerViewBinding, SingleTypeRecyclerViewVM>(),
    RecyclerViewBasicPlugin<ArticleImageBean, DemoItemStyleTextBinding> {
    override fun getWidth() = 300.dp
    override fun getHeight() = ViewGroup.LayoutParams.MATCH_PARENT
    override fun getAnimationStyle() = R.style.base_dialog_right_animation
    override fun getGravity() = Gravity.RIGHT
    override val mBindingVM: SingleTypeRecyclerViewVM by vms()
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun title() = "RecyclerViewBasicPlugin插件Demo"
    override fun bindAdapterListener(adapter: BaseQuickAdapter<ArticleImageBean, BaseDataBindingHolder<DemoItemStyleTextBinding>>) {
        adapter.setOnItemClickListener { _, _, position ->
            getMutableLiveData().value?.get(position)?.apply {
                ToastUtils.showShort(title)
            }
        }
    }

    override fun getItemId() = R.layout.demo_item_style_text
    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun convert(
        holder: BaseDataBindingHolder<DemoItemStyleTextBinding>,
        item: ArticleImageBean
    ) {
        holder.dataBinding?.apply {
            titleText.text = item.title
            descText.text = item.desc
        }
    }

    override fun getMutableLiveData(): MutableLiveData<MutableList<ArticleImageBean>> {
        return mBindingVM.articleList
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        super.initRecyclerView(recyclerView)
        recyclerView.setBackgroundColor(Color.parseColor("#F3F3F3"))
        recyclerView.addItemDecoration(LinearLayoutDecoration(10, 10, true))
    }

    override fun getTopContainerView(): View {
        val searchBinding = DemoTopSearchBinding.inflate(LayoutInflater.from(requireContext()))
        searchBinding.searchEdit.addDelayChangeAfter {
            mBindingVM.searchKey.value = it
        }
        mBindingVM.searchKey.observe(viewLifecycleOwner){
            mBindingVM.onRefresh(this)
        }
        return searchBinding.root
    }
}