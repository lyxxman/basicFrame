package com.frame.basic.base.widget.recycler

import android.graphics.PointF
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description:    无限循环滚动
 * @Author:         fanj
 * @CreateDate:     2022/8/29 13:57
 * @Version:
 */
class RepeatLayoutManager : RecyclerView.LayoutManager(),RecyclerView.SmoothScroller.ScrollVectorProvider {
    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.WRAP_CONTENT,
        RecyclerView.LayoutParams.WRAP_CONTENT
    )

    override fun canScrollHorizontally() = true
    override fun canScrollVertically() = true
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount <= 0) {
            return
        }
        if (recycler == null || state == null) {
            return
        }
        if (state.isPreLayout) {
            return
        }
        if (state.didStructureChange()){
            removeAllViews()
        }
        scrollHorizontallyBy(0, recycler, state)
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (recycler == null || state == null) {
            return dx
        }
        //填充到可见区
        fill(recycler, dx > 0)
        //子View进行整体移动
        offsetChildrenHorizontal(-dx)
        //回收不可见区的View
        recyclerChildView(dx > 0, recycler)
        return dx
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (recycler == null || state == null) {
            return dy
        }
        //填充到可见区
        fill(recycler, dy > 0)
        //子View进行整体移动
        offsetChildrenVertical(-dy)
        //回收不可见区的View
        recyclerChildView(dy > 0, recycler)
        return dy
    }
    /**
     * 空item填充
     */
    private fun fillEmpty(recycler: RecyclerView.Recycler) {
        //将所有Item分离至scrap
        detachAndScrapAttachedViews(recycler)
        var itemLeft = paddingLeft
        for (i in 0 until itemCount) {
            if (itemLeft >= width - paddingLeft) {
                //当超出屏幕可视区域后就不再添加
                break
            }
            val itemView = recycler.getViewForPosition(i % itemCount)
            //添加子View
            addView(itemView)
            //测量子View
            measureChildWithMargins(itemView, 0, 0)
            //getDecoratedMeasuredWidth 获取child的宽度，并将ItemDecoration考虑进来
            val itemViewWidth = getDecoratedMeasuredWidth(itemView)
            //getDecoratedMeasuredHeight 获取child的高度，并将ItemDecoration考虑进来
            val itemViewHeight = getDecoratedMeasuredHeight(itemView)

            val right = itemLeft + itemViewWidth
            val top = paddingTop
            val bottom = top + itemViewHeight - paddingBottom

            //对子View进行布局
            layoutDecorated(itemView, itemLeft, top, right, bottom)
            itemLeft = right
        }
    }
    /**
     * 滑动的时候，填充可见的未填充区域
     * @param fillEnd 是否是左滑或上滑
     */
    private fun fill(recycler: RecyclerView.Recycler, fillEnd: Boolean) {
        if (childCount == 0) {
            //不能在onLayoutChildren调用，因为onLayoutChildren在数据刷新、滚动或者界面可见等情况下会多次调用，造成重置
            fillEmpty(recycler)
            return
        }
        if (itemCount == 0){
            return
        }
        if (fillEnd) {
            //尾部填充
            val anchorView = getChildAt(childCount - 1)
            val anchorPosition = getPosition(anchorView!!)
            if (anchorView.right < width - paddingRight) {
                val position = (anchorPosition + 1) % itemCount
                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem)
                measureChildWithMargins(scrapItem, 0, 0)
                val left = anchorView.right
                val top = paddingTop
                val right = left + getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem) - paddingBottom
                layoutDecorated(scrapItem, left, top, right, bottom)
            }
        }else{
            //首部填充
            val anchorView = getChildAt(0)
            val anchorPosition = getPosition(anchorView!!)
            if (anchorView.left > paddingLeft){
                val position = if (anchorPosition == 0){
                    itemCount - 1
                }else{
                    (anchorPosition - 1) % itemCount
                }
                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem, 0)
                measureChildWithMargins(scrapItem, 0, 0)
                val right = anchorView.left
                val top = paddingTop
                val left = right - getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem) - paddingBottom
                layoutDecorated(scrapItem, left, top, right, bottom)
            }
        }
    }

    /**
     * 回收不可见的子View
     * @param fillEnd 是否是左滑或上滑
     */
    private fun recyclerChildView(fillEnd: Boolean, recycler: RecyclerView.Recycler){
        if (childCount == 0) {
            return
        }
        if (fillEnd){
            //回收头部
            for (i in 0 until childCount){
                val view = getChildAt(i)
                val needRecycler = view != null && view.right <= paddingLeft
                if (needRecycler){
                    removeAndRecycleView(view!!, recycler)
                }
            }
        }else{
            //回收尾部
            for (i in 0 until childCount){
                val view = getChildAt(i)
                val needRecycler = view != null && view.left >= width - paddingRight
                if (needRecycler) {
                    removeAndRecycleView(view!!, recycler)
                }
            }
        }
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0)!!)
        val direction = if (targetPosition < firstChildPos) -1 else 1
        return PointF(direction.toFloat(), 0f)
    }
}