package com.frame.basic.common.demo.ui

import android.view.View
import androidx.viewbinding.ViewBinding
import com.frame.basic.base.mvvm.v.BaseFragment
import com.frame.basic.base.mvvm.vm.BaseVM

/**
 * Fragment基类
 *
 * @author Qu Yunshuo
 * @since 8/27/20
 */
abstract class CommonBaseFragment<VB : ViewBinding, VM : BaseVM> : BaseFragment<VB, VM>(), CommonTitleBarControl{
    private val commonContainerStyle by lazy { CommonContainerStyle(this,requireContext(), this,this, mBindingVM) }
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
}