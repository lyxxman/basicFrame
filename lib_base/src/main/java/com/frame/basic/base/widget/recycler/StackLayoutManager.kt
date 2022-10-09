package com.frame.basic.base.widget.recycler

import android.graphics.PointF
import android.view.Gravity
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description:    无限循环滚动中间放大
 * @Author:         fanj
 * @CreateDate:     2022/8/29 13:57
 * @Version:
 */
class StackLayoutManager : RecyclerView.LayoutManager(),RecyclerView.SmoothScroller.ScrollVectorProvider {
    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.WRAP_CONTENT,
        RecyclerView.LayoutParams.WRAP_CONTENT
    )

    override fun canScrollHorizontally() = true
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)

    }

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
        //缩放所有的item
        scaleAllItem()
        //回收不可见区的View
        recyclerChildView(dx > 0, recycler)
        return dx
    }

    /**
     * 缩放所有的item
     */
    private fun scaleAllItem() {
        if (childCount == 0) {
            return
        }
        for (i in 0 until childCount) {
            getChildAt(i)?.let { childView ->
                val scale = calculateScale(childView.left, childView.width)
                childView.scaleX = scale
                childView.scaleY = scale
                childView.translationY = when (getItemGravity()) {
                    Gravity.TOP -> -childView.height * (1 - scale) / 2
                    Gravity.BOTTOM -> childView.height * (1 - scale) / 2
                    else -> 0f
                }
            }
        }
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
        } else {
            //首部填充
            val anchorView = getChildAt(0)
            val anchorPosition = getPosition(anchorView!!)
            if (anchorView.left > paddingLeft) {
                val position = if (anchorPosition == 0) {
                    itemCount - 1
                } else {
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
    private fun recyclerChildView(fillEnd: Boolean, recycler: RecyclerView.Recycler) {
        if (childCount == 0) {
            return
        }
        if (fillEnd) {
            //回收头部
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val needRecycler = view != null && view.right <= paddingLeft
                if (needRecycler) {
                    removeAndRecycleView(view!!, recycler)
                }
            }
        } else {
            //回收尾部
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val needRecycler = view != null && view.left >= width - paddingRight
                if (needRecycler) {
                    removeAndRecycleView(view!!, recycler)
                }
            }
        }
    }


    /**
     * 中心位置x坐标 越靠近中心点放大，越远离中心点则缩小
     * 1.默认是控件中央
     * 2.中心位置会填充满RecyclerView的高度，两边则根据最小-最大缩放系数进行变换
     */
    open fun getCenterX(): Int = width / 2

    /**
     * 最小缩放系数
     */
    @FloatRange(from = 0.1, to = 1.0)
    open fun getMinScale(): Float = 0.6f

    /**
     * Item的对齐方式
     * Gravity.CENTER 居中
     * Gravity.TOP 顶部
     * Gravity.BOTTOM 底部
     */
    open fun getItemGravity() = Gravity.BOTTOM

    /**
     * 计算缩放倍数
     */
    private fun calculateScale(left: Int, width: Int): Float {
        //ItemView的中心点坐标
        val itemViewCenterX = width / 2 + left
        //RecyclerView的中心点坐标
        val centerViewX = getCenterX()
        return when {
            //右侧
            itemViewCenterX > centerViewX -> {
                //中心点之间的间距
                val distance = itemViewCenterX - centerViewX
                distanceScale(distance)
            }
            //左侧
            itemViewCenterX < centerViewX -> {
                //中心点之间的间距
                val distance = centerViewX - itemViewCenterX
                distanceScale(distance)
            }
            else -> 1.0f
        }
    }

    /**
     * 获取距离中心点最大距离
     */
    private fun getMaxDistance(): Int {
        val parentCenterX = width / 2
        val centerViewX = getCenterX()
        return when {
            centerViewX > parentCenterX -> parentCenterX
            centerViewX < parentCenterX -> width - centerViewX
            else -> centerViewX
        }
    }

    /**
     * 根据中心点距离计算缩放倍数
     */
    private fun distanceScale(distance: Int): Float {
        //最小缩放倍数
        val minScale = getMinScale()
        //最小距离
        val minDistance = 0
        //最大距离
        val maxDistance = getMaxDistance()
        return when (distance) {
            minDistance -> {
                1.0f
            }
            maxDistance -> {
                minScale
            }
            else -> {
                minScale + (1.0f - minScale) * ((maxDistance - distance.toFloat()) / maxDistance)
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