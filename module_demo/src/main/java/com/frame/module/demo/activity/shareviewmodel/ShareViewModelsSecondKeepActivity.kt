package com.frame.module.demo.activity.shareviewmodel

import com.frame.basic.base.ktx.addChangeAfter
import com.frame.basic.base.ktx.bindText
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.EmptyVM
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityShareViewModelsKeepSecondBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:20
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsSecondKeepActivity: CommonBaseActivity<DemoActivityShareViewModelsKeepSecondBinding, EmptyVM>() {
    override val mBindingVM: EmptyVM by vms()
    private val keepVM: ShareViewModelsKeepVM by vms()

    override fun DemoActivityShareViewModelsKeepSecondBinding.initView() {
    }

    override fun DemoActivityShareViewModelsKeepSecondBinding.initListener() {
        text.addChangeAfter {
            keepVM.data.value = it.toIntOrNull()
        }
        keepVM.data.observe(this@ShareViewModelsSecondKeepActivity){
            text.bindText("$it")
        }
    }

    override fun title() = "ViewModels共享Keep_Demo输入页"
}