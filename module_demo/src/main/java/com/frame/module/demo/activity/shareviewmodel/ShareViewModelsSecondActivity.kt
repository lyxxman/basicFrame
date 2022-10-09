package com.frame.module.demo.activity.shareviewmodel

import com.frame.basic.base.ktx.addChangeAfter
import com.frame.basic.base.ktx.bindText
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityShareViewModelsSecondBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:20
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsSecondActivity :
    CommonBaseActivity<DemoActivityShareViewModelsSecondBinding, ShareViewModelsVM>() {
    override val mBindingVM: ShareViewModelsVM by vms()

    override fun DemoActivityShareViewModelsSecondBinding.initView() {
    }

    override fun DemoActivityShareViewModelsSecondBinding.initListener() {
        text.addChangeAfter {
            mBindingVM.data.value = it.toLongOrNull()
        }
        mBindingVM.data.observe(this@ShareViewModelsSecondActivity) {
            text.bindText("$it")
        }
    }

    override fun title() = "ViewModels共享Demo输入页"
}