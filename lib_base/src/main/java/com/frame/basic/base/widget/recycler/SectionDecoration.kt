package com.frame.basic.base.widget.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

/**
 * RecyclerView分组装饰器
 * @param callback 协议
 * @param hover 是否启用悬停
 */
class SectionDecoration(private val callback: SectionDecorationCallback, private val hover: Boolean = true) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        val sectionTag = callback.getSectionTag(pos)
        if (sectionTag.isEmpty()) return
        outRect.top = if (isFirstInSection(pos)) {
            callback.getSectionHeight()
        } else {
            0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (!hover){
            return
        }
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val sectionTag = callback.getSectionTag(position)
            if (sectionTag.isEmpty()) return
            if (isFirstInSection(position)){
                val top = view.top - callback.getSectionHeight()
                val bottom = view.top
                callback.drawSection(c, position, left, top, right, bottom)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        var preSectionTag = ""
        var sectionTag = ""
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            preSectionTag = sectionTag
            sectionTag = callback.getSectionTag(position)
            if (sectionTag.isEmpty() || sectionTag == preSectionTag) {
                continue
            }
            val viewBottom = view.bottom
            var textY = max(callback.getSectionHeight(), view.top)
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                val nextSectionTag = callback.getSectionTag(position + 1)
                if (nextSectionTag != sectionTag && viewBottom < textY) { //组内最后一个view进入了header
                    textY = viewBottom
                }
            }
            callback.drawSection(c, position, left, textY-callback.getSectionHeight(), right, textY)
        }
    }

    /**
     * 判断与上一个是否是同一分组
     */
    private fun isFirstInSection(position: Int): Boolean {
        if (position == 0) {
            return true
        }
        val prevSectionTag = callback.getSectionTag(position - 1)
        val sectionTag = callback.getSectionTag(position)
        return prevSectionTag != sectionTag
    }
}

interface SectionDecorationCallback {
    /**
     * 返回分组标识
     * @param position item的位置
     */
    fun getSectionTag(position: Int): String

    /**
     * 绘制分组样式
     * @param position item的位置
     * @param left 左边的坐标
     * @param top 顶部的坐标
     * @param right 右边的坐标
     * @param bottom 底部的坐标
     */
    fun drawSection(canvas: Canvas, position: Int, left: Int, top: Int, right: Int, bottom: Int)

    /**
     *  获取分组高度
     */
    fun getSectionHeight(): Int
}