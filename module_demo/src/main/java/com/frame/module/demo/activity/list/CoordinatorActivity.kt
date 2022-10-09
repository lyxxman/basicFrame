package com.frame.module.demo.activity.list

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.mvvm.c.CoordinatorPlugin

/**
 * @Description:
 * @Author:         fanj
 * @CreateDate:     2022/5/31 14:58
 * @Version:
 */
class CoordinatorActivity: MultiTypeRecyclerViewActivity(), CoordinatorPlugin {
    override fun getHeadLayoutView(): View = AppCompatTextView(this).apply {
        height = 60.dp
        text = "This is Header!"
        gravity = Gravity.CENTER
        setBackgroundColor(Color.GRAY)
    }
    override fun title() = "伸缩插件demo"
    override fun getBodyLayoutView(): View? = null

    override fun initCollapsingToolbarLayoutLayoutParams(layoutParams: CollapsingToolbarLayout.LayoutParams) {
        layoutParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
    }
}