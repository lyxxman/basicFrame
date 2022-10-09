package com.frame.module.demo.dialog.callparams

import android.view.Gravity
import androidx.lifecycle.LifecycleOwner
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.ktx.onClick
import com.frame.basic.common.demo.ui.CommonBaseDialog
import com.frame.module.demo.R
import com.frame.module.demo.activity.callparams.CallParamsSecondVM
import com.frame.module.demo.databinding.DemoActivityCallParamsSecondBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 15:00
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class CallParamsDialog :
    CommonBaseDialog<DemoActivityCallParamsSecondBinding, CallParamsSecondVM>() {
    companion object {
        private const val PARAMS = "params"
        private const val CALLBACK = "callback"
        fun build(owner: LifecycleOwner, params: String, callback: (String) -> Unit) = CallParamsDialog().putExtra(PARAMS, params).putExtra(owner, CALLBACK, callback)
    }

    override fun getWidth() = 300.dp
    override fun getHeight() = 300.dp
    override fun getAnimationStyle() = R.style.base_dialog_center_animation
    override fun getGravity() = Gravity.CENTER
    override val mBindingVM: CallParamsSecondVM by vms()
    override fun title() = "界面传参Demo"
    override fun DemoActivityCallParamsSecondBinding.initView() {
        mBinding.data = mBindingVM
    }

    override fun DemoActivityCallParamsSecondBinding.initListener() {
        callback.onClick {
            mBindingVM.callback.value?.invoke("1000")
            dismissDialog()
        }
    }
}