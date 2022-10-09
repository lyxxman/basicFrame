package com.frame.module.demo.dialog.maininteraction

import android.view.Gravity
import android.view.View
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.viewBindings
import com.frame.basic.common.demo.ui.CommonBaseDialog
import com.frame.module.demo.R
import com.frame.module.demo.activity.maininteraction.MainInteractionVM
import com.frame.module.demo.databinding.DemoActivityMainInteractionBinding
import com.frame.module.demo.databinding.DemoMainInteractionTopViewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 14:58
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class MainInteractionDialog: CommonBaseDialog<DemoActivityMainInteractionBinding, MainInteractionVM>()  {
    override val mBindingVM: MainInteractionVM by vms()
    override fun getAnimationStyle() = R.style.base_dialog_center_animation
    override fun getGravity() = Gravity.CENTER
    override fun getWidth() = 300.dp
    override fun getHeight() = 300.dp
    override fun DemoActivityMainInteractionBinding.initView() {
    }

    override fun DemoActivityMainInteractionBinding.initListener() {
        mBindingVM.data.observe(this@MainInteractionDialog){
            text.text = it
        }
    }

    override fun title() = "交互Demo"
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
}