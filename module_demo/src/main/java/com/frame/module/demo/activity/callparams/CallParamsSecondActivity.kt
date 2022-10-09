package com.frame.module.demo.activity.callparams

import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityCallParamsSecondBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 9:09
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class CallParamsSecondActivity: CommonBaseActivity<DemoActivityCallParamsSecondBinding, CallParamsSecondVM>() {
    override val mBindingVM: CallParamsSecondVM by vms()

    override fun DemoActivityCallParamsSecondBinding.initView() {
        mBinding.data = mBindingVM
    }

    override fun DemoActivityCallParamsSecondBinding.initListener() {
        callback.onClick {
            mBindingVM.callback.value?.invoke("1000")
            finish()
        }
    }
    override fun title() = "界面传参Demo"
}