package com.frame.basic.common.demo.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.frame.basic.base.ktx.onClick
import com.frame.basic.base.ktx.viewBindings
import com.frame.basic.base.mvvm.v.BaseActivity
import com.frame.basic.base.mvvm.v.BaseDialog
import com.frame.basic.base.mvvm.v.BaseFragment
import com.frame.basic.base.mvvm.v.base.ContainerStyle
import com.frame.basic.base.mvvm.vm.BaseVM
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.common.demo.common.R
import com.frame.basic.common.demo.common.databinding.CommonDemoCommonTitleBarBinding
import com.frame.basic.common.demo.common.databinding.CommonDemoEmptyContainerViewBinding
import com.frame.basic.common.demo.common.databinding.CommonDemoErrorContainerViewBinding
import com.frame.basic.common.demo.common.databinding.CommonDemoLoadingContainerViewBinding

class CommonContainerStyle(
    private val owner: LifecycleOwner,
    private val context: Context,
    private val ui: Any,
    private val control: CommonTitleBarControl,
    private val vm: BaseVM
) : ContainerStyle {
    private val layoutInflater by lazy { LayoutInflater.from(context) }
    private fun inflate(layoutId: Int) = layoutInflater.inflate(layoutId, null, false)

    override fun getErrorContainerView(error: Int, msg: String?): View? {
        val vb =
            inflate(R.layout.common_demo_error_container_view).viewBindings<CommonDemoErrorContainerViewBinding>()
                .apply {
                    message.text = "操作失败！错误码：${error}  错误信息：${msg}"
                    message.onClick {
                        vm.loading()
                        vm.onRefresh(owner)
                    }
                }
        return vb.root
    }

    override fun getLoadingContainerView(): View? {
        val vb =
            inflate(R.layout.common_demo_loading_container_view).viewBindings<CommonDemoLoadingContainerViewBinding>()
                .apply {
                    message.text = "假装loading...中"
                }
        return vb.root
    }

    override fun getEmptyContainerView(): View? {
        val vb =
            inflate(R.layout.common_demo_empty_container_view).viewBindings<CommonDemoEmptyContainerViewBinding>()
                .apply {
                    message.text = "空空如也"
                    message.onClick {
                        vm.loading()
                        vm.onRefresh(owner)
                    }
                }
        return vb.root
    }

    override fun getContentView(): View {
        TODO("Not yet implemented")
    }

    private val titleBar by lazy { CommonTitleBar(context, ui, control) }
    override fun getTitleBar(): ViewGroup = titleBar

    /**
     * 显示弹出等待框
     */
    fun showPopLoading(text: String) {
        ToastUtils.showLong("我是个poploading, ${text}")
    }

    /**
     * 关闭弹出等待框
     */
    fun dismissPopLoading() {
        ToastUtils.showShort("关闭了poploading")
    }
}

class CommonTitleBar(context: Context, ui: Any,control: CommonTitleBarControl) : FrameLayout(context) {
    var titleText : TextView? = null
    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.common_demo_common_title_bar, null, false)
        addView(view)
        setBackgroundColor(Color.parseColor("#6633b5e5"))
        view.viewBindings<CommonDemoCommonTitleBarBinding>().apply {
            tvBack.onClick {
                if (control.back() == null || control.back()!!.invoke()){
                    when (ui) {
                        is BaseActivity<*, *> -> {
                            ui.finish()
                        }
                        is BaseFragment<*, *> -> {
                            ui.activity?.finish()
                        }
                        is BaseDialog<*, *> -> {
                            ui.dismissDialog()
                        }
                    }
                }
            }
            titleText = tvTitle
            tvTitle.text = control.title()
        }
    }
}

interface CommonTitleBarControl {
    fun title(): String = ""
    fun back(): (() -> Boolean)? = null
}