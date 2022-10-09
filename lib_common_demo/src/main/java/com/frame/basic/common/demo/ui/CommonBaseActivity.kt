package com.frame.basic.common.demo.ui

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.frame.basic.base.mvvm.v.BaseActivity
import com.frame.basic.base.mvvm.vm.BaseVM

/**
 * Activity基类
 *
 * @author Qu Yunshuo
 * @since 8/27/20
 */
abstract class CommonBaseActivity<VB : ViewBinding, VM : BaseVM> : BaseActivity<VB, VM>(),
    CommonTitleBarControl {
    private val commonContainerStyle by lazy { CommonContainerStyle(this,this, this, this, mBindingVM) }
    override fun getErrorContainerView(error: Int, msg: String?): View? =
        commonContainerStyle.getErrorContainerView(error, msg)

    override fun getLoadingContainerView(): View? = commonContainerStyle.getLoadingContainerView()

    override fun showPopLoading(text: String) {
        commonContainerStyle.showPopLoading(text)
    }

    override fun dismissPopLoading() {
        commonContainerStyle.dismissPopLoading()
    }

    override fun getEmptyContainerView(): View? = commonContainerStyle.getEmptyContainerView()

    override fun getTitleBar(): ViewGroup? = commonContainerStyle.getTitleBar()

    override fun statusBarDarkFont() = false
}