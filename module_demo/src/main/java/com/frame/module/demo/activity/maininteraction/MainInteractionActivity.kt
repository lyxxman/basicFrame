package com.frame.module.demo.activity.maininteraction

import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.viewBindings
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.databinding.DemoActivityMainInteractionBinding
import com.frame.module.demo.databinding.DemoMainInteractionTopViewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 11:09
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class MainInteractionActivity :
    CommonBaseActivity<DemoActivityMainInteractionBinding, MainInteractionVM>() {
    override val mBindingVM: MainInteractionVM by vms()

    override fun DemoActivityMainInteractionBinding.initView() {
    }

    override fun DemoActivityMainInteractionBinding.initListener() {
        mBindingVM.data.observe(this@MainInteractionActivity) {
            text.text = it
        }
    }

    override fun title() = "交互Demo"
    override fun getTopContainerView() =
        R.layout.demo_main_interaction_top_view.viewBindings<DemoMainInteractionTopViewBinding>(this)
            .apply {
                loadEmpty.onClick {
                    mBindingVM.requestEmpty()
                }
                loadError.onClick {
                    mBindingVM.requestFail()
                }
                submit.onClick {
                    mBindingVM.submitData()
                }
            }.root
}