package com.frame.basic.base.widget.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frame.basic.base.ktx.dp
/**
 * RecyclerView列表分组装饰器
 */
class LinearLayoutDecoration : RecyclerView.ItemDecoration {
    private var horizontalSpace: Int
    private var verticalSpace: Int
    private var needHorizontalBorder = false
    private var needVerticalBorder: Boolean
    private var ignorePos = 0
    private var ignoreCount: Int

    constructor(horizontalSpace: Int, verticalSpace: Int, needVerticalBorder: Boolean) {
        this.needVerticalBorder = needVerticalBorder
        this.horizontalSpace = horizontalSpace.dp
        this.verticalSpace = verticalSpace.dp
        ignoreCount = 0
    }

    constructor(
        horizontalSpace: Int,
        verticalSpace: Int,
        needVerticalBorder: Boolean,
        needHorizontalBorder: Boolean
    ) {
        this.needVerticalBorder = needVerticalBorder
        this.needHorizontalBorder = needHorizontalBorder
        this.horizontalSpace = horizontalSpace.dp
        this.verticalSpace = verticalSpace.dp
        ignoreCount = 0
    }

    constructor(
        horizontalSpace: Int,
        verticalSpace: Int,
        needVerticalBorder: Boolean,
        ignorePos: Int
    ) {
        this.needVerticalBorder = needVerticalBorder
        this.ignorePos = ignorePos
        this.horizontalSpace = horizontalSpace.dp
        this.verticalSpace = verticalSpace.dp
        ignoreCount = 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val orientation = layoutManager.orientation
            if (orientation == RecyclerView.HORIZONTAL) {
                if (position == 0) {
                    if (needHorizontalBorder) {
                        outRect.left = horizontalSpace
                        outRect.right = horizontalSpace / 2
                    } else {
                        outRect.left = 0
                        outRect.right = horizontalSpace / 2
                    }
                } else if (position == state.itemCount - 1) {
                    if (needHorizontalBorder) {
                        outRect.left = horizontalSpace / 2
                        outRect.right = horizontalSpace
                    } else {
                        outRect.left = horizontalSpace / 2
                        outRect.right = 0
                    }
                } else {
                    outRect.left = horizontalSpace / 2
                    outRect.right = horizontalSpace / 2
                }
            } else {
                outRect.left = horizontalSpace
                outRect.right = horizontalSpace
                if (ignoreCount != 0 && ignorePos == position) {
                    outRect[0, 0, 0] = 0
                } else if (position - ignoreCount == 0) {
                    if (needVerticalBorder) {
                        outRect.top = verticalSpace
                    } else {
                        outRect.top = 0
                    }
                    outRect.bottom = verticalSpace / 2
                } else if (position - ignoreCount == state.itemCount - 1) {
                    outRect.top = verticalSpace / 2
                    if (needVerticalBorder) {
                        outRect.bottom = verticalSpace
                    } else {
                        outRect.bottom = 0
                    }
                } else {
                    outRect.top = verticalSpace / 2
                    outRect.bottom = verticalSpace / 2
                }
            }
        }
    }
}