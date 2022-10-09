package com.frame.module.demo.activity.tab

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.google.common.collect.HashBiMap
import com.frame.basic.base.mvvm.c.TabPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityTabBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * TabPlugin插件Demo
 */
@AndroidEntryPoint
class TabActivity : CommonBaseActivity<DemoActivityTabBinding, TabVM>(), TabPlugin {
    override fun DemoActivityTabBinding.initView() {}
    override fun DemoActivityTabBinding.initListener() {}
    override fun getRadioGroup() = mBinding.navigationRgp
    override fun getFrameLayout() = mBinding.frameContainer
    override fun getTargetTabFragment() = mBindingVM.targetFragment
    override fun bindFragment(map: HashBiMap<Int, Class<out Fragment>>) {
        map.apply {
            put(mBinding.menu1.id, Menu1Fragment::class.java)
            put(mBinding.menu2.id, Menu2Fragment::class.java)
            put(mBinding.menu3.id, Menu3Fragment::class.java)
        }
    }

    override fun title() = "TabPlugin插件Demo"
    override val mBindingVM: TabVM by vms()
}

class TabVM(handle: SavedStateHandle) : BaseVM(handle) {
    val targetFragment by savedStateLiveData<Class<out Fragment>>(
        "targetFragment",
        Menu1Fragment::class.java
    )

    override fun onRefresh(owner: LifecycleOwner) {
    }

    override fun autoOnRefresh() = false
}