package com.frame.module.demo.activity.mainlayout

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.mvvm.c.vms
import com.frame.basic.base.mvvm.v.base.ContainerMargin
import com.frame.basic.base.mvvm.v.base.ContainerShowModel
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.databinding.DemoActivityMainLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/11 15:45
 * @Version:        1.0.2
 */
@AndroidEntryPoint
class MainLayoutActivity : CommonBaseActivity<DemoActivityMainLayoutBinding, MainLayoutVM>() {
    override val mBindingVM: MainLayoutVM by vms()

    override fun DemoActivityMainLayoutBinding.initView() {
    }

    override fun DemoActivityMainLayoutBinding.initListener() {
        mBindingVM.desc.observe(this@MainLayoutActivity) {
            desc.text = "内容区/loading区/empty区/error区 ==> $it"
        }
    }

    override fun title(): String = "布局Demo"

    override fun getTopContainerView() = AppCompatTextView(this).apply {
        height = 20.dp
        width = ViewGroup.LayoutParams.MATCH_PARENT
        text = "顶部栏"
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER
        setBackgroundColor(Color.parseColor("#80ff0000"))
    }

    override fun getBottomContainerView() = AppCompatTextView(this).apply {
        height = 20.dp
        width = ViewGroup.LayoutParams.MATCH_PARENT
        text = "底部栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.parseColor("#80ff0000"))
    }

    override fun getLeftContainerView() = AppCompatTextView(this).apply {
        width = 20.dp
        height = ViewGroup.LayoutParams.MATCH_PARENT
        text = "左侧栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.parseColor("#80ff0000"))
    }

    override fun getRightContainerView() = AppCompatTextView(this).apply {
        width = 20.dp
        height = ViewGroup.LayoutParams.MATCH_PARENT
        text = "右侧栏"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.parseColor("#80ff0000"))
    }
    /**** 定义四周的View的间距， 默认是0 ****/
    override fun getTopContainerMargin() = ContainerMargin(0, 20.dp, 0, 0)
    override fun getBottomContainerMargin() = ContainerMargin(20.dp, 0, 0, 0)
    override fun getLeftContainerMargin() = ContainerMargin(0, 0.dp, 20.dp, 0)
    override fun getRightContainerMargin() = ContainerMargin(0, 0.dp, 0, 20.dp)

    /**** 定义四周的View的覆盖模式，默认是临边 ****/
    override fun getTopContainerShowModel() = ContainerShowModel.COVER
    override fun getBottomContainerShowModel() = ContainerShowModel.COVER
    override fun getLeftContainerShowModel() = ContainerShowModel.COVER
    override fun getRightContainerShowModel() = ContainerShowModel.COVER
}