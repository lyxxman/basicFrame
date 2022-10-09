package com.frame.module.demo.fragment

import androidx.fragment.app.Fragment
import com.google.common.collect.HashBiMap
import com.frame.basic.base.mvvm.c.TabPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.activity.tab.Menu1Fragment
import com.frame.module.demo.activity.tab.Menu2Fragment
import com.frame.module.demo.activity.tab.Menu3Fragment
import com.frame.module.demo.activity.tab.TabVM
import com.frame.module.demo.databinding.DemoActivityTabBinding

class TabFragment :CommonBaseFragment<DemoActivityTabBinding, TabVM>(), TabPlugin {
    override fun DemoActivityTabBinding.initView() {
    }

    override fun DemoActivityTabBinding.initListener() {
    }

    override val mBindingVM: TabVM by vms()
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
    override fun isAttachToViewPager() = true
}