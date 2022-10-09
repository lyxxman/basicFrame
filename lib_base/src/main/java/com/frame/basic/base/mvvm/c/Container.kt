package com.frame.basic.base.mvvm.v.base

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.frame.basic.base.BaseApplication
import com.frame.basic.base.mvvm.v.BaseDialog
import com.frame.basic.base.mvvm.v.BasePopWindow
import com.frame.basic.base.utils.ActivityStackManager
import com.frame.basic.base.utils.NotchUtils
import me.jessyan.autosize.utils.ScreenUtils

/**
 * 间距控制
 */
data class ContainerMargin(
    var left: Int,
    var right: Int,
    var top: Int,
    var bottom: Int
)

/**
 * 中间与四周的覆盖方式
 */
enum class ContainerShowModel {
    //覆盖，即中间区域在底部被覆盖
    COVER,

    //临边，即中间区域临边
    NEAR
}


/**
 * 布局样式描述
 *  如果同时包含左上右下的view可能会出现重叠，此时用ContainerMargin来控制重叠部分
 */
interface ContainerStyle {
    companion object {
        /**
         * 显示区标识
         * 用于loadingView/ErrorView/ContentView交叉替换
         */
        const val CONTENT_VIEW_TAG = "CONTENT_VIEW_TAG"
        private fun createViewId(oldId: Int) = if (oldId < 0) {
            View.generateViewId()
        } else {
            oldId
        }

        /**
         * 组装View
         */
        @JvmStatic
        fun packageContainers(containerStyle: ContainerStyle): ViewGroup {
            val contentView = containerStyle.getContentView().apply {
                tag = CONTENT_VIEW_TAG
            }
            val context = contentView.context
            val rootView = ConstraintLayout(context).apply {
                id = createViewId(id)
            }
            val titleBar = createTitleBar(containerStyle)?.apply {
                id = createViewId(id)
            }
            val topView = containerStyle.getTopContainerView()?.apply {
                id = createViewId(id)
            }
            val bottomView = containerStyle.getBottomContainerView()?.apply {
                id = createViewId(id)
            }
            val leftView = containerStyle.getLeftContainerView()?.apply {
                id = createViewId(id)
            }
            val rightView = containerStyle.getRightContainerView()?.apply {
                id = createViewId(id)
            }
            //添加内容View
            rootView.addView(contentView, ConstraintLayout.LayoutParams(0, if(containerStyle is BaseDialog<*,*> && containerStyle.getHeight() == WindowManager.LayoutParams.WRAP_CONTENT){
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            }else{
                0
            }).apply {
                if (containerStyle.contentViewFitTop()) {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                } else {
                    when {
                        topView != null -> {
                            if (containerStyle.getTopContainerShowModel() == ContainerShowModel.NEAR){
                                topToBottom = topView.id
                            }else{
                                topToTop = topView.id
                            }
                        }
                        titleBar != null -> {
                            topToBottom = titleBar.id
                        }
                        else -> {
                            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                        }
                    }
                }
                when {
                    bottomView != null -> {
                        if (containerStyle.getBottomContainerShowModel() == ContainerShowModel.NEAR){
                            bottomToTop = bottomView.id
                        }else{
                            bottomToBottom = bottomView.id
                        }
                    }
                    else -> {
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                }
                when {
                    leftView != null -> {
                        if (containerStyle.getLeftContainerShowModel() == ContainerShowModel.NEAR){
                            leftToRight = leftView.id
                        }else{
                            leftToLeft = leftView.id
                        }
                    }
                    else -> {
                        leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                }
                when {
                    rightView != null -> {
                        if (containerStyle.getRightContainerShowModel() == ContainerShowModel.NEAR){
                            rightToLeft = rightView.id
                        }else{
                            rightToRight = rightView.id
                        }
                    }
                    else -> {
                        rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                }
            })
            //添加底部View
            bottomView?.let {
                rootView.addView(
                    it,
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        val margins = containerStyle.getBottomContainerMargin()
                        topMargin = margins.top
                        leftMargin = margins.left
                        rightMargin = margins.right
                        bottomMargin = margins.bottom
                    })
            }
            //添加左边View
            leftView?.let {
                rootView.addView(
                    it,
                    ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 0)
                        .apply {
                            if (titleBar == null) {
                                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                            } else {
                                topToBottom = titleBar.id
                            }
                            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                            val margins = containerStyle.getLeftContainerMargin()
                            topMargin = margins.top
                            leftMargin = margins.left
                            rightMargin = margins.right
                            bottomMargin = margins.bottom
                        })
            }
            //添加右边View
            rightView?.let {
                rootView.addView(
                    it,
                    ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 0)
                        .apply {
                            if (titleBar == null) {
                                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                            } else {
                                topToBottom = titleBar.id
                            }
                            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                            rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                            val margins = containerStyle.getRightContainerMargin()
                            topMargin = margins.top
                            leftMargin = margins.left
                            rightMargin = margins.right
                            bottomMargin = margins.bottom
                        })
            }
            //添加TitleBar
            titleBar?.let {
                rootView.addView(
                    it,
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    })
            }
            //添加顶部View
            topView?.let {
                rootView.addView(
                    it,
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        if (titleBar == null) {
                            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                        } else {
                            topToBottom = titleBar.id
                        }
                        val margins = containerStyle.getTopContainerMargin()
                        topMargin = margins.top
                        leftMargin = margins.left
                        rightMargin = margins.right
                        bottomMargin = margins.bottom
                    })
            }
            return rootView
        }

        /**
         * 生成真正的titleBar
         */
        private fun createTitleBar(containerStyle: ContainerStyle): View? {
            return if (containerStyle.isShowTitleBar()) {
                containerStyle.getTitleBar()
            } else {
                null
            }?.apply {
                //如果支持沉浸式状态栏，则自动填充上状态栏的间距
                val topPadding = containerStyle.getTitleBarTopSafePadding()
                setPadding(0, topPadding, 0, 0)
            }
        }

        /**
         * 更换到内容布局
         */
        @JvmStatic
        fun replaceToContentView(rootView: ViewGroup, containerStyle: ContainerStyle) {
            val targetView = rootView.children.find {
                it.tag == CONTENT_VIEW_TAG
            }
            targetView?.let {
                val contentView = containerStyle.getContentView()
                if (it != contentView) {
                    val lp = it.layoutParams
                    rootView.removeView(it)
                    rootView.addView(
                        containerStyle.getContentView(),
                        rootView.indexOfChild(targetView) + 1,
                        lp
                    )
                }
            }
        }

        /**
         * 更换到错误布局
         */
        @JvmStatic
        fun replaceToErrorView(
            rootView: ViewGroup,
            containerStyle: ContainerStyle,
            error: Int,
            msg: String?
        ) {
            val targetView = rootView.children.find {
                it.tag == CONTENT_VIEW_TAG
            }
            targetView?.let {
                containerStyle.getErrorContainerView(error, msg)?.let { errorView ->
                    errorView.tag = CONTENT_VIEW_TAG
                    val lp = it.layoutParams
                    rootView.removeView(it)
                    rootView.addView(errorView, rootView.indexOfChild(targetView) + 1, lp)
                }
            }
        }

        /**
         * 更换到Loading布局
         */
        @JvmStatic
        fun replaceToLoadingView(rootView: ViewGroup, containerStyle: ContainerStyle) {
            val targetView = rootView.children.find {
                it.tag == CONTENT_VIEW_TAG
            }
            targetView?.let {
                containerStyle.getLoadingContainerView()?.let { loadingView ->
                    loadingView.tag = CONTENT_VIEW_TAG
                    val lp = it.layoutParams
                    rootView.removeView(it)
                    rootView.addView(loadingView, rootView.indexOfChild(targetView) + 1, lp)
                }
            }
        }

        /**
         * 更换到空布局
         */
        @JvmStatic
        fun replaceToEmptyView(rootView: ViewGroup, containerStyle: ContainerStyle) {
            val targetView = rootView.children.find {
                it.tag == CONTENT_VIEW_TAG
            }
            targetView?.let {
                containerStyle.getEmptyContainerView()?.let { emptyView ->
                    emptyView.tag = CONTENT_VIEW_TAG
                    val lp = it.layoutParams
                    rootView.removeView(it)
                    rootView.addView(emptyView, rootView.indexOfChild(targetView) + 1, lp)
                }
            }
        }
    }

    /**
     * 获取StatusBar高度
     * 因为使用了autosize，必须使用这个方法获取statusbar高度，一般的方法获取会不准确
     */
    fun getStatusBarHeight(): Int = ScreenUtils.getStatusBarHeight()

    /**
     * 获取NavigationBar高度
     * 因为使用了autosize，必须使用这个方法获取NavigationBar高度，一般的方法获取会不准确
     * 这个方法很安全，不管横竖屏，当底部没有NavigationBar返回0，有的话才会大于0
     */
    fun getNavigationBarHeight(): Int =
        ScreenUtils.getHeightOfNavigationBar(BaseApplication.application)

    /**
     * 是否显示标题栏
     */
    fun isShowTitleBar(): Boolean = true

    /**
     * 标题栏
     * 由于添加TitleBar时会默认给TitleBar顶部加上statusBar高度的padding，所以返回的TitleBar的背景色一定要设置在最外层，不然颜色不正常
     */
    fun getTitleBar(): ViewGroup? = null

    /**
     * 获取顶部的安全间距
     */
    fun getTitleBarTopSafePadding() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if(this is BaseDialog<*,*> && this !is BasePopWindow<*,*> && Build.VERSION.SDK_INT < 28 && ActivityStackManager.getCurrentActivity() != null && NotchUtils.hasNotchInScreen(ActivityStackManager.getCurrentActivity()) && com.blankj.utilcode.util.ScreenUtils.isPortrait()){
            0
        }else{
            getStatusBarHeight()
        }
    } else {
        0
    }
    /**
     * 顶部的View
     * 默认宽度填满，高度自适应
     */
    fun getTopContainerView(): View? = null

    /**
     * 底部的View
     * 默认宽度填满，高度自适应
     */
    fun getBottomContainerView(): View? = null

    /**
     * 左边的View
     * 默认高度填满，宽度自适应
     */
    fun getLeftContainerView(): View? = null

    /**
     * 右边的View
     * 默认高度填满，宽度自适应
     */
    fun getRightContainerView(): View? = null

    /**
     * 错误View
     * 1.在中间填满，和内容View的布局一样，相互覆盖
     * 2.错误码由业务层自定义，用于处理如无数据、未登录、无权限等等问题
     */
    fun getErrorContainerView(error: Int, msg: String?): View?

    /**
     * 等待框
     * 和内容View的布局一样，相互覆盖
     */
    fun getLoadingContainerView(): View?

    /**
     * 空布局
     * 和内容View的布局一样，相互覆盖
     */
    fun getEmptyContainerView(): View?

    /**
     * 获取内容View
     */
    fun getContentView(): View

    /**
     * 如返回true: 内容View区无论如何充满顶部
     * 即：无论有没有TitleBar/TopView，内容View区的顶部都与屏幕顶部对齐,但会被TitleBar/TopView盖住
     */
    fun contentViewFitTop() = false

    /**
     * 自定义底层View
     */
    fun customRootView(rootView: ViewGroup): ViewGroup = rootView

    /**
     * 顶部的View的四周间距
     */
    fun getTopContainerMargin(): ContainerMargin = ContainerMargin(0, 0, 0, 0)

    /**
     * 顶部的View相对于中间区域的显示模式，默认临边
     */
    fun getTopContainerShowModel(): ContainerShowModel = ContainerShowModel.NEAR

    /**
     * 底部的View的四周间距
     */
    fun getBottomContainerMargin(): ContainerMargin = ContainerMargin(0, 0, 0, 0)

    /**
     * 底部的View相对于中间区域的显示模式，默认临边
     */
    fun getBottomContainerShowModel(): ContainerShowModel = ContainerShowModel.NEAR

    /**
     * 左边的View的四周间距
     */
    fun getLeftContainerMargin(): ContainerMargin = ContainerMargin(0, 0, 0, 0)

    /**
     * 左边的View相对于中间区域的显示模式，默认临边
     */
    fun getLeftContainerShowModel(): ContainerShowModel = ContainerShowModel.NEAR

    /**
     * 右边的View的四周间距
     */
    fun getRightContainerMargin(): ContainerMargin = ContainerMargin(0, 0, 0, 0)

    /**
     * 右边的View相对于中间区域的显示模式，默认临边
     */
    fun getRightContainerShowModel(): ContainerShowModel = ContainerShowModel.NEAR
}
