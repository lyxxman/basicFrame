package com.frame.module.demo.dialog.refreshlayout

import android.view.Gravity
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.mvvm.c.RefreshLayoutPlugin
import com.frame.basic.common.demo.ui.CommonBaseDialog
import com.frame.module.demo.R
import com.frame.module.demo.activity.refreshlayout.RefreshLayoutVM
import com.frame.module.demo.databinding.DemoActivityRefreshLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 15:05
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class RefreshLayoutDialog : CommonBaseDialog<DemoActivityRefreshLayoutBinding, RefreshLayoutVM>(),
    RefreshLayoutPlugin {
    override val mBindingVM: RefreshLayoutVM by vms()
    override fun getAnimationStyle() = R.style.base_dialog_center_animation
    override fun getGravity() = Gravity.CENTER
    override fun getWidth() = 300.dp
    override fun getHeight() = 300.dp
    override fun title() = "RefreshLayoutPlugin插件Demo"
    override fun DemoActivityRefreshLayoutBinding.initView() {
    }

    override fun DemoActivityRefreshLayoutBinding.initListener() {
        mBindingVM.data.observe(viewLifecycleOwner) {
            text.text = it
        }
    }

    override fun enableLoadMore() = false
}