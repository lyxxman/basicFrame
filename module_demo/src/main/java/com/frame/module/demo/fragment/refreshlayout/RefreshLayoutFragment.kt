package com.frame.module.demo.fragment.refreshlayout

import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.c.RefreshLayoutPlugin
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.activity.refreshlayout.RefreshLayoutVM
import com.frame.module.demo.databinding.DemoActivityRefreshLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:20
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class RefreshLayoutFragment: CommonBaseFragment<DemoActivityRefreshLayoutBinding, RefreshLayoutVM>(),
    RefreshLayoutPlugin {
    override val mBindingVM: RefreshLayoutVM by vms()

    override fun DemoActivityRefreshLayoutBinding.initView() {
    }

    override fun DemoActivityRefreshLayoutBinding.initListener() {
        mBindingVM.data.observe(viewLifecycleOwner){
            text.text = it
        }
    }

    override fun enableLoadMore() = false
    override fun isAttachToViewPager() = true
}