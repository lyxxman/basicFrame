package com.frame.module.demo.fragment.maininteraction

import android.view.View
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.viewBindings
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.R
import com.frame.module.demo.activity.maininteraction.MainInteractionVM
import com.frame.module.demo.databinding.DemoActivityMainInteractionBinding
import com.frame.module.demo.databinding.DemoMainInteractionTopViewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:09
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class MainInteractionFragment:CommonBaseFragment<DemoActivityMainInteractionBinding, MainInteractionVM>() {
    override val mBindingVM: MainInteractionVM by vms()

    override fun DemoActivityMainInteractionBinding.initView() {
    }

    override fun DemoActivityMainInteractionBinding.initListener() {
        mBindingVM.data.observe(this@MainInteractionFragment){
            text.text = it
        }
    }

    override fun getTopContainerView(): View? {
        val topView = layoutInflater.inflate(R.layout.demo_main_interaction_top_view, null, false)
        topView.viewBindings<DemoMainInteractionTopViewBinding>().apply {
            loadEmpty.onClick {
                mBindingVM.requestEmpty()
            }
            loadError.onClick {
                mBindingVM.requestFail()
            }
            submit.onClick {
                mBindingVM.submitData()
            }
        }
        return topView
    }
    override fun isAttachToViewPager() = true
}