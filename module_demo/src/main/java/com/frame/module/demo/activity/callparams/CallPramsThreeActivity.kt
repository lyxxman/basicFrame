package com.frame.module.demo.activity.callparams

import android.content.Intent
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.onClick
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityCallPramsThreeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/12/20 10:16
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class CallPramsThreeActivity: CommonBaseActivity<DemoActivityCallPramsThreeBinding, CallParamsThreeVM>() {
    override fun DemoActivityCallPramsThreeBinding.initView() {
        mBinding.data = mBindingVM
    }

    override fun DemoActivityCallPramsThreeBinding.initListener() {
        btnResult.onClick {
            setResult(RESULT_OK, Intent().apply {
                putExtra("data", mBindingVM.result.value)
            })
            finish()
        }
    }
    override val mBindingVM: CallParamsThreeVM by vms()
}