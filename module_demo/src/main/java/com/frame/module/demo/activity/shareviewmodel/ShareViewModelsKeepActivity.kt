package com.frame.module.demo.activity.shareviewmodel

import android.content.Intent
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.EmptyVM
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityShareViewModelsKeepBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:10
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsKeepActivity :
    CommonBaseActivity<DemoActivityShareViewModelsKeepBinding, EmptyVM>() {
    override val mBindingVM: EmptyVM by vms()
    private val keepVM by vms<ShareViewModelsKeepVM>()
    override fun DemoActivityShareViewModelsKeepBinding.initView() {

    }

    override fun DemoActivityShareViewModelsKeepBinding.initListener() {
        keepVM.data.observe(this@ShareViewModelsKeepActivity) {
            text.text = "您输入了$it"
        }
        text.onClick {
            startActivity(
                Intent(
                    this@ShareViewModelsKeepActivity,
                    ShareViewModelsSecondKeepActivity::class.java
                )
            )
        }
    }

    override fun title() = "ViewModels共享Keep_Demo"
}