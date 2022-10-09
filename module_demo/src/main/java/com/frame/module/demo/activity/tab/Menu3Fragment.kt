package com.frame.module.demo.activity.tab

import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.EmptyVM
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.databinding.DemoFragmentMenu3Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Menu3Fragment : CommonBaseFragment<DemoFragmentMenu3Binding, EmptyVM>(){
    override fun DemoFragmentMenu3Binding.initView() {
    }

    override fun DemoFragmentMenu3Binding.initListener() {
    }
    override val mBindingVM: EmptyVM by vms()
}
