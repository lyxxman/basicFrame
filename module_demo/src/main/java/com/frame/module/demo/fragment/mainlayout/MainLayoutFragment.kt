package com.frame.module.demo.fragment.mainlayout

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.mvvm.v.base.ContainerMargin
import com.frame.basic.common.demo.ui.CommonBaseFragment
import com.frame.module.demo.activity.mainlayout.MainLayoutVM
import com.frame.module.demo.databinding.DemoActivityMainLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/15 10:04
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class MainLayoutFragment: CommonBaseFragment<DemoActivityMainLayoutBinding, MainLayoutVM>() {
    override val mBindingVM: MainLayoutVM by vms()

    override fun DemoActivityMainLayoutBinding.initView() {
    }

    override fun DemoActivityMainLayoutBinding.initListener() {
        mBindingVM.desc.observe(this@MainLayoutFragment){
            desc.text = "内容区/loading区/empty区/error区 ==> $it"
        }
    }

    override fun getTopContainerView() = AppCompatTextView(requireContext()).apply {
        height = 20.dp
        width = ViewGroup.LayoutParams.MATCH_PARENT
        text = "顶部栏"
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER
        setBackgroundColor(Color.RED)
    }

    override fun getBottomContainerView() = AppCompatTextView(requireContext()).apply {
        height = 20.dp
        width = ViewGroup.LayoutParams.MATCH_PARENT
        text = "底部栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.BLUE)
    }

    override fun getLeftContainerView() = AppCompatTextView(requireContext()).apply {
        width = 20.dp
        height = ViewGroup.LayoutParams.MATCH_PARENT
        text = "左侧栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.GRAY)
    }

    override fun getRightContainerView() = AppCompatTextView(requireContext()).apply {
        width = 20.dp
        height = ViewGroup.LayoutParams.MATCH_PARENT
        text = "右侧栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.GRAY)
    }

    override fun getTopContainerMargin() = ContainerMargin(0, 20.dp, 0, 0)

    override fun getBottomContainerMargin() = ContainerMargin(20.dp, 0, 0, 0)

    override fun getLeftContainerMargin() = ContainerMargin(0, 0.dp, 20.dp, 0)

    override fun getRightContainerMargin() = ContainerMargin(0, 0.dp, 0, 20.dp)
    override fun isAttachToViewPager() = true
}