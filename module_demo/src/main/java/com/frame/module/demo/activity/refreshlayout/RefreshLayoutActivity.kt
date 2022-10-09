package com.frame.module.demo.activity.refreshlayout

import com.frame.basic.base.mvvm.c.RefreshLayoutPlugin
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityRefreshLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/12 9:04
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class RefreshLayoutActivity : CommonBaseActivity<DemoActivityRefreshLayoutBinding, RefreshLayoutVM>(), RefreshLayoutPlugin {
    override val mBindingVM: RefreshLayoutVM by vms()

    override fun DemoActivityRefreshLayoutBinding.initView() {
    }

    override fun DemoActivityRefreshLayoutBinding.initListener() {
        mBindingVM.data.observe(this@RefreshLayoutActivity){
            text.text = it
        }
    }

    override fun title() = "RefreshLayoutPlugin插件Demo"

    override fun enableLoadMore() = false
}