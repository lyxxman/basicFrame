package com.frame.module.demo.activity.tab

import androidx.lifecycle.LifecycleOwner
import com.frame.basic.base.mvvm.c.vms
import androidx.lifecycle.SavedStateHandle
import com.frame.basic.base.ktx.addChangeAfter
import com.frame.basic.base.ktx.bindText
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.databinding.DemoFragmentMenu1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Menu1Fragment : CommonBaseFragment<DemoFragmentMenu1Binding, Menu1VM>(){
    override fun DemoFragmentMenu1Binding.initView() {
    }

    override fun DemoFragmentMenu1Binding.initListener() {
        testEt.addChangeAfter {
            mBindingVM.testData.value = it
        }
        mBindingVM.testData.observe(this@Menu1Fragment){
            testEt.bindText(it)
        }
    }
    override val mBindingVM: Menu1VM by vms()
}
class Menu1VM(handle: SavedStateHandle): BaseVM(handle) {
    val testData by savedStateLiveData<String>("testData")
    override fun autoOnRefresh() = false
    override fun onRefresh(owner: LifecycleOwner) {}
}
