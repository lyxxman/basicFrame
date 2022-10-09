package com.frame.basic.common.demo.ui

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.frame.basic.base.mvvm.v.BasePopWindow
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.utils.ViewBgUtil

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2021/11/29 11:06
 * @Version:        1.0.2
 */
abstract class CommonBasePopWindow<VB : ViewBinding, VM : BaseVM> : BasePopWindow<VB, VM>(), CommonTitleBarControl {
    private val commonContainerStyle by lazy { CommonContainerStyle(this,requireContext(), this, this, mBindingVM) }
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

    override fun customRootView(rootView: ViewGroup): ViewGroup {
        return super.customRootView(rootView).apply {
            ViewBgUtil.setShapeBg(this, Color.WHITE, 0)
        }
    }

    final override fun title(): String {
        return super.title()
    }
}