package com.frame.module.demo.dialog.shareviewmodel

import android.content.Intent
import android.view.Gravity
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.getViewModel
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseDialog
import com.frame.module.demo.R
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsSecondActivity
import com.frame.module.demo.activity.shareviewmodel.ShareViewModelsVM
import com.frame.module.demo.databinding.DemoActivityShareViewModelsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 15:05
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsDialog :
    CommonBaseDialog<DemoActivityShareViewModelsBinding, ShareViewModelsVM>() {
    override val mBindingVM: ShareViewModelsVM by vms()
    override fun getAnimationStyle() = R.style.base_dialog_center_animation
    override fun getGravity() = Gravity.CENTER
    override fun getWidth() = 300.dp
    override fun getHeight() = 300.dp
    override fun title() = "ViewModels共享Demo"
    override fun DemoActivityShareViewModelsBinding.initView() {

    }

    override fun DemoActivityShareViewModelsBinding.initListener() {
        mBindingVM.data.observe(viewLifecycleOwner) {
            text.text = "您输入了$it"
        }
        text.onClick {
            startActivity(Intent(requireContext(), ShareViewModelsSecondActivity::class.java))
        }
        btnUpdate.onClick {
            getViewModel<ShareViewModelsVM>()?.data?.postValue(10023)
        }
    }
}