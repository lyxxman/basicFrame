package com.frame.basic.base.ktx

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewParent
import android.view.animation.Animation
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.frame.basic.base.utils.lifecycle.AnimateLifecycle
import com.frame.basic.base.utils.lifecycle.RunnableLifecycle
import com.frame.basic.base.widget.NestRadioGroup
import kotlin.math.roundToInt

/**
 * <pre>
 *     author : June Yang
 *     time   : 2020/5/7
 *     desc   : View扩展
 *     version: 1.0.0
 * </pre>
 */

/**
 * View点击事件，限定时间之内只取第一个点击事件，防止重复点击
 */
fun View.onClick(delay: Long = 500L, click: (View) -> Unit) {
    setOnClickListener {
        if (!isClickable) {
            return@setOnClickListener
        }
        click.invoke(this)
        if (delay > 0) {
            isClickable = false
            runDelay(delay) { isClickable = true }
        }
    }
}

fun View.visible(tag: String) {
    if (parent is ConstraintLayout) {
        val parentLayout = parent as ConstraintLayout
        parentLayout.forEach {
            val constraintTag =
                (it.layoutParams as ConstraintLayout.LayoutParams).getConstraintTag()
            if (tag == constraintTag) {
                it.visibility = View.VISIBLE
            }
        }
    }
}

fun View.gone(tag: String) {
    if (parent is ConstraintLayout) {
        val parentLayout = parent as ConstraintLayout
        parentLayout.forEach {
            val constraintTag =
                (it.layoutParams as ConstraintLayout.LayoutParams).getConstraintTag()
            if (tag == constraintTag) {
                it.visibility = View.GONE
            }
        }
    }
}

fun View.onFocus(focus: (hasFocus: Boolean) -> Unit) {
    this.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        focus.invoke(hasFocus)
    }
}

fun RadioGroup.onChecked(check: (id: Int) -> Unit) {
    this.setOnCheckedChangeListener { _, i ->
        check.invoke(i)
    }
}

fun NestRadioGroup.onChecked(check: (id: Int) -> Unit) {
    this.setOnCheckedChangeListener { group, checkedId ->
        check.invoke(checkedId)
    }
}

fun CheckBox.onChecked(state: (checked: Boolean) -> Unit) {
    this.setOnCheckedChangeListener { _, b ->
        state.invoke(b)
    }
}

fun EditText.addChangeAfter(after: (text: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            after.invoke(p0.toString())
        }
    })
}

/**
 * 延迟响应输入值
 * @param delay 延迟时间
 * @param enableDelay 是否允许延迟 默认延迟
 * @param after 执行
 */
fun EditText.addDelayChangeAfter(
    delay: Long = 350,
    enableDelay: ((text: String) -> Boolean)? = null,
    after: ((text: String) -> Unit)?
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            removeDelayRunnables()
            val textString = p0.toString()
            if (enableDelay == null || enableDelay.invoke(textString)) {
                runDelay(delay = delay, runnable = {
                    after?.invoke(textString)
                })
            } else {
                after?.invoke(textString)
            }
        }
    })
}

fun View.runDelay(delay: Long, runnable: Runnable) {
    RunnableLifecycle.put(this, runnable)
    this.postDelayed(runnable, delay)
}

fun View.removeDelayRunnables() {
    RunnableLifecycle.clearRunnable(this)
}

fun View.runAnimation(animation: Animation) {
    AnimateLifecycle.put(this)
    this.startAnimation(animation)
}

/**
 * 测量宽度
 */
fun View.measureWidth(): Int {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(widthSpec, heightSpec)
    return measuredWidth
}

/**
 * 获取View距离父视图的高度
 */
fun View.measureTopFromParent(): Int {
//转换为直接父窗口的坐标
    var top = top.toFloat()
    var viewParent: ViewParent = parent
    //循环获得父窗口的父窗口，并且依次计算在每个父窗口中的坐标
    while (viewParent is View) {
        val view = viewParent as View
        //转换为相当于可视区左上角的坐标，scrollX，scollY是去掉滚动的影响
        top += view.top - view.scrollY
        viewParent = view.parent
    }
    return top.roundToInt()
}
