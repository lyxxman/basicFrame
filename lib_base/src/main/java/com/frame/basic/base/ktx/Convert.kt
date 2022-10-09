package com.frame.basic.base.ktx

import android.util.TypedValue
import com.frame.basic.base.BaseApplication

/**
 * <pre>
 *     author : June Yang
 *     time   : 2020/5/7
 *     desc   : 尺寸转换扩展
 *     version: 1.0.0
 * </pre>
 */

/**
 * get px(Int) by convert dp(Int)
 */
val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), BaseApplication.application.resources.displayMetrics
    ).toInt()

/**
 * get px(Float) by convert dp(Int)
 */
val Int.dpF: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), BaseApplication.application.resources.displayMetrics
    )

/**
 * get px(Float) by convert dp(Float)
 */
val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, BaseApplication.application.resources.displayMetrics
    )

/**
 * 四舍五入取整,如果要向下取整用Float.dp.toInt()
 * <p>
 * get px(Float) by convert dp(Int)
 */
val Float.dpI: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, BaseApplication.application.resources.displayMetrics
    ) + 0.5).toInt()

/**
 * get px(Int) by convert sp(Int)
 */
val Int.sp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), BaseApplication.application.resources.displayMetrics
    ).toInt()

/**
 * get px(Float) by convert sp(Int)
 */
val Int.spF: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), BaseApplication.application.resources.displayMetrics
    )

/**
 * get px(Float) by convert sp(Float)
 */
val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, BaseApplication.application.resources.displayMetrics
    )

/**
 * get dp(Int) by convert px(Int)
 */
val Int.toDp: Int
    get() = (this / BaseApplication.application.resources.displayMetrics.density).toInt()

/**
 * get dp(Int) by convert px(Float)
 */
val Int.toDpF: Float
    get() = this / BaseApplication.application.resources.displayMetrics.density

/**
 * get dp(Float) by convert px(Float)
 */
val Float.toDp: Float
    get() = this / BaseApplication.application.resources.displayMetrics.density

/**
 * get sp(Int) by convert px(Int)
 */
val Int.toSp: Int
    get() = (this / BaseApplication.application.resources.displayMetrics.scaledDensity).toInt()

/**
 * get sp(Float) by convert px(Int)
 */
val Int.toSpF: Float
    get() = this / BaseApplication.application.resources.displayMetrics.scaledDensity

/**
 * get sp(Float) by convert px(Float)
 */
val Float.toSp: Float
    get() = this / BaseApplication.application.resources.displayMetrics.scaledDensity