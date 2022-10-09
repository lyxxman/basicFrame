package com.frame.module.demo.fragment.callparams

import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.activity.callparams.CallParamsSecondVM
import com.frame.module.demo.databinding.DemoActivityCallParamsSecondBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:13
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class CallParamsFragment :
    CommonBaseFragment<DemoActivityCallParamsSecondBinding, CallParamsSecondVM>() {

    override val mBindingVM: CallParamsSecondVM by vms()

    override fun DemoActivityCallParamsSecondBinding.initView() {
        mBinding.data = mBindingVM
    }

    override fun DemoActivityCallParamsSecondBinding.initListener() {
    }

    override fun isAttachToViewPager() = true
    override fun statusBarDarkFont() = true
}