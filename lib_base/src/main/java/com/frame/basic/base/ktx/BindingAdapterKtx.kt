package com.frame.basic.base.ktx

import android.os.Build
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.frame.basic.base.widget.NestRadioGroup

/*********************常见的控件避免双向绑定死循环的参数设置方法***********************/
fun TextView.bindText(text: CharSequence) {
    if (getText().toString() != text) {
        setText(text)
    }
}

fun TextView.bindText(@StringRes resId: Int) {
    val text = context.resources.getText(resId)
    if (getText().toString() != text) {
        setText(resId)
    }
}

fun CompoundButton.bindChecked(checked: Boolean) {
    if (isChecked != checked) {
        isChecked = checked
    }
}

fun RadioGroup.bindCheck(@IdRes id: Int) {
    if (checkedRadioButtonId != id) {
        check(id)
    }
}

fun NestRadioGroup.bindCheck(@IdRes id: Int){
    if (checkedRadioButtonId != id) {
        check(id)
    }
}

fun ProgressBar.bindProgress(progress: Int) {
    if (getProgress() != progress) {
        setProgress(progress)
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun ProgressBar.bindProgress(progress: Int, animate: Boolean) {
    if (getProgress() != progress) {
        setProgress(progress, animate)
    }
}

fun ViewPager.bindCurrentItem(index: Int) {
    if (index != currentItem) {
        currentItem = index
    }
}

fun ViewPager.bindCurrentItem(index: Int, smoothScroll: Boolean) {
    if (index != currentItem) {
        setCurrentItem(index, smoothScroll)
    }
}

fun ViewPager2.bindCurrentItem(index: Int) {
    if (index != currentItem) {
        currentItem = index
    }
}

fun ViewPager2.bindCurrentItem(index: Int, smoothScroll: Boolean) {
    if (index != currentItem) {
        setCurrentItem(index, smoothScroll)
    }
}

fun RatingBar.bindRating(rating: Float) {
    if (getRating() != rating) {
        setRating(rating)
    }
}
