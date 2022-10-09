package com.frame.module.demo.activity.callparams

import android.content.Intent
import com.frame.basic.base.ktx.addChangeAfter
import com.frame.basic.base.ktx.bindText
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.putExtra
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityCallParamsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 9:01
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class CallParamsActivity : CommonBaseActivity<DemoActivityCallParamsBinding, CallParamsVM>() {
    override val mBindingVM: CallParamsVM by vms()
    override fun DemoActivityCallParamsBinding.initView() {
        mBindingVM.toNextParams.observe(this@CallParamsActivity) {
            etParams.bindText(it)
        }
        etParams.addChangeAfter {
            mBindingVM.toNextParams.value = it
        }
    }

    override fun DemoActivityCallParamsBinding.initListener() {
        btnNext.onClick {
            val intent = Intent(this@CallParamsActivity, CallParamsSecondActivity::class.java)
            intent.putExtra("params", mBindingVM.toNextParams.value)
            intent.putExtra(this@CallParamsActivity, "callback") { text: String ->
                mBindingVM.toNextParams.postValue(text)
            }
            startActivity(intent)
        }
    }

    override fun title() = "界面传参Demo"
}