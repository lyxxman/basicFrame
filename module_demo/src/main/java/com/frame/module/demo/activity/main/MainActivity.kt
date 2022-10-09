package com.frame.module.demo.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.mvvm.c.RecyclerViewBasicPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.bean.Menu
import com.frame.module.demo.databinding.DemoMainMenuItemBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 首页
 *
 * @author Qu Yunshuo
 * @since 5/22/21 2:26 PM
 */
@AndroidEntryPoint
class MainActivity : CommonBaseActivity<DemoRecyclerViewBinding, MainVM>(),
    RecyclerViewBasicPlugin<Menu, DemoMainMenuItemBinding> {
    override val mBindingVM: MainVM by vms()
    override fun DemoRecyclerViewBinding.initView() {
        mBindingVM.testWatch.observe(this@MainActivity){
            ToastUtils.showShort("$it")
        }
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun title() = "主页"
    override fun getMutableLiveData(): MutableLiveData<MutableList<Menu>> = mBindingVM.menus

    override fun getItemId() = R.layout.demo_main_menu_item
    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun convert(holder: BaseDataBindingHolder<DemoMainMenuItemBinding>, item: Menu) {
        holder.dataBinding?.tvTitle?.text = "${item.ordinal + 1}.  ${item.desc}"
    }

    override fun bindAdapterListener(adapter: BaseQuickAdapter<Menu, BaseDataBindingHolder<DemoMainMenuItemBinding>>) {
        adapter.setOnItemClickListener { _, _, position ->
            getMutableLiveData().value?.get(position)?.let {
                it.open(this)
            }
        }
    }
}
