package com.frame.module.demo.activity.shareviewmodel

import android.content.Intent
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.getViewModel
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityShareViewModelsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 17:10
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class ShareViewModelsActivity :
    CommonBaseActivity<DemoActivityShareViewModelsBinding, ShareViewModelsVM>() {
    override val mBindingVM: ShareViewModelsVM by vms()

    override fun DemoActivityShareViewModelsBinding.initView() {
    }

    override fun DemoActivityShareViewModelsBinding.initListener() {
        mBindingVM.data.observe(this@ShareViewModelsActivity) {
            text.text = "您输入了$it"
        }
        text.onClick {
            startActivity(
                Intent(
                    this@ShareViewModelsActivity,
                    ShareViewModelsSecondActivity::class.java
                )
            )
        }
        btnUpdate.onClick {
            getViewModel<ShareViewModelsVM>()?.data?.postValue(10023)
        }
    }

    override fun title() = "ViewModels共享Demo"
}