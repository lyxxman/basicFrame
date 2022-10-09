package com.frame.module.demo.activity.dialog

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.mvvm.c.RecyclerViewBasicPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.bean.DialogMenu
import com.frame.module.demo.databinding.DemoDialogMenuItemBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import com.frame.module.demo.dialog.callparams.CallParamsDialog
import com.frame.module.demo.dialog.list.CoordinatorDialog
import com.frame.module.demo.dialog.list.MultiTypeRecyclerViewDialog
import com.frame.module.demo.dialog.list.SingleTypeRecyclerViewDialog
import com.frame.module.demo.dialog.maininteraction.MainInteractionDialog
import com.frame.module.demo.dialog.mainlayout.MainLayoutDialog
import com.frame.module.demo.dialog.page.PageDialog
import com.frame.module.demo.dialog.refreshlayout.RefreshLayoutDialog
import com.frame.module.demo.dialog.shareviewmodel.ShareViewModelsDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 14:06
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class DialogActivity : CommonBaseActivity<DemoRecyclerViewBinding, DialogVM>(),
    RecyclerViewBasicPlugin<DialogMenu, DemoDialogMenuItemBinding> {
    override val mBindingVM: DialogVM by vms()
    override fun title() = "Dialog相关Demo"
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun bindAdapterListener(adapter: BaseQuickAdapter<DialogMenu, BaseDataBindingHolder<DemoDialogMenuItemBinding>>) {
        adapter.setOnItemClickListener { _, _, position ->
            getMutableLiveData().value?.get(position)?.let { it ->
                when (it) {
                    DialogMenu.MAIN_LAYOUT_DEMO -> {
                        MainLayoutDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.MAIN_INTERACTION_DEMO -> {
                        MainInteractionDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.REFRESH_LAYOUT_PLUGIN_DEMO -> {
                        RefreshLayoutDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.RECYCLERVIEW_BASIC_PLUGIN_DEMO -> {
                        SingleTypeRecyclerViewDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.RECYCLERVIEW_MULTI_PLUGIN_DEMO -> {
                        MultiTypeRecyclerViewDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.COORDINATOR_PLUGIN_DEMO -> {
                        CoordinatorDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.PAGING_CONTROL_DEMO -> {
                        PageDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.SHARE_VIEW_MODELS_DEMO -> {
                        ShareViewModelsDialog().showDialog(supportFragmentManager)
                    }
                    DialogMenu.CALL_PARAMS_DEMO -> {
                        CallParamsDialog.build(this,"我是传输的参数，看到我了吗"){ text ->
                            ToastUtils.showShort("返回参数${text}")
                        }.showDialog(supportFragmentManager)
                    }
                }
            }
        }
    }

    override fun getMutableLiveData(): MutableLiveData<MutableList<DialogMenu>> = mBindingVM.menus

    override fun getItemId() = R.layout.demo_dialog_menu_item
    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun convert(
        holder: BaseDataBindingHolder<DemoDialogMenuItemBinding>,
        item: DialogMenu
    ) {
        holder.dataBinding?.tvTitle?.text = "${item.ordinal + 1}.  ${item.desc}"
    }
}