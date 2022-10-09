package com.frame.basic.base.ktx

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.view.*
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * 开启支持emoji表情
 * 1.开启后字数限制会默认认为一个表情为一个字符
 */
fun TextView.useEmoji() {
    val lengthIputFilters = filters.filter {
        it is InputFilter.LengthFilter
    }
    if (lengthIputFilters.strIsNotNullAndEmpty()) {
        return
    }
    val lengthIputFilter =
        lengthIputFilters[lengthIputFilters.size - 1] as InputFilter.LengthFilter
    val oldInputFilters = filters.filter {
        it !is InputFilter.LengthFilter
    }
    val size = oldInputFilters.size + 1
    val newInputFilters = Array<InputFilter>(size) {
        if (size == 1) {
            EmojiLengthFilter(lengthIputFilter.max)
        } else {
            if (it <= size - 2) {
                oldInputFilters[it]
            } else {
                EmojiLengthFilter(lengthIputFilter.max)
            }
        }
    }
    filters = newInputFilters
}

/**
 * 设置最大输入长度
 */
fun TextView.setMaxLength(length: Int) {
    val oldInputFilters = filters.filter {
        it !is InputFilter.LengthFilter
    }
    val size = oldInputFilters.size + 1
    val newInputFilters = Array<InputFilter>(size) {
        if (size == 1) {
            InputFilter.LengthFilter(length)
        } else {
            if (it <= size - 2) {
                oldInputFilters[it]
            } else {
                InputFilter.LengthFilter(length)
            }
        }
    }
    filters = newInputFilters
}

/**
 * 禁用表情包
 */
fun TextView.forbidEmoji() {
    val size = filters.size + 1
    val newInputFilters = Array<InputFilter>(size) {
        if (it == filters.size) {
            ForbidEmojiFilter()
        } else {
            filters[it]
        }
    }
    filters = newInputFilters
}

/**
 * 获取文本行数（即所有文本不折叠的情况下可以显示多少行）
 * @param textViewWidth 控件的宽度
 */
fun TextView.getTextViewLines(textViewWidth: Int): Int {
    val width = textViewWidth - compoundPaddingLeft - compoundPaddingRight;
    val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getStaticLayout23(width)
    } else {
        getStaticLayout(width)
    }
    val lines = staticLayout.lineCount
    val maxLines = maxLines
    if (maxLines > lines) {
        return lines
    }
    return maxLines
}

@RequiresApi(Build.VERSION_CODES.M)
private fun TextView.getStaticLayout23(width: Int): StaticLayout {
    val builder = StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
        .setTextDirection(TextDirectionHeuristics.FIRSTSTRONG_LTR)
        .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
        .setIncludePad(includeFontPadding)
        .setBreakStrategy(breakStrategy)
        .setHyphenationFrequency(hyphenationFrequency)
        .setMaxLines(
            if (maxLines == -1) {
                Integer.MAX_VALUE
            } else {
                maxLines
            }
        )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setJustificationMode(justificationMode)
    }
    if (ellipsize != null && keyListener == null) {
        builder.setEllipsize(ellipsize).setEllipsizedWidth(width)
    }
    return builder.build()
}

/**
 * sdk<23
 */
private fun TextView.getStaticLayout(width: Int): StaticLayout {
    return StaticLayout(
        text,
        0, text.length,
        paint, width, Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra, includeFontPadding, ellipsize,
        width
    )
}

/**
 * 是否是emoji表情
 */
private fun isEmoji(source: Char): Boolean {
    val type = Character.getType(source)
    return type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()
}

/**
 * 删除emoji表情中的空格符号
 */
private fun deleteEmojiBlank(source: CharSequence): String {
    val sb = StringBuffer()
    for (i in source.indices) {
        val ch = source[i]
        if (Char(65039) != ch) {
            sb.append(ch)
        }
    }
    return sb.toString()
}

private fun getMaxInputPosition(source: String, @IntRange(from = 0) keep: Int): Int {
    if (source.length <= keep) {
        return source.length - 1
    }
    if (source.isEmpty()) {
        return -1
    }
    var preEmojiFirstPos = -5
    var rangePos = 0
    for (i in source.indices) {
        val ch = source[i]
        if (Char(55357) == ch) {
            preEmojiFirstPos = i
            rangePos++
            if (rangePos == keep) {
                return i + 1
            }
        } else {
            if (i >= preEmojiFirstPos + 2) {
                rangePos++
                if (rangePos == keep) {
                    return i
                }
            }
        }
    }
    if (rangePos < keep) {
        return source.length - 1
    }
    return -1
}

private class EmojiLengthFilter(private var max: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        var keep: Int = max - (dest!!.toString().containsEmojiLength() - (dend - dstart))
        return when {
            keep <= 0 -> {
                ""
            }
            keep >= end - start -> {
                null
            }
            else -> {
                val source2 = deleteEmojiBlank(source!!.toString())
                keep += start
                val maxPos = getMaxInputPosition(source2, keep)
                if (source2.emojiCount() > 0 && maxPos > 0) {
                    if (keep == start) {
                        return ""
                    }
                    try {
                        return source2.subSequence(start, maxPos + 1)
                    } catch (e: Exception) {
                    }

                } else if (Character.isHighSurrogate(source2[keep - 1])) {
                    --keep
                    if (keep == start) {
                        return ""
                    }
                }
                return source2.subSequence(start, keep)
            }
        }
    }
}

private class ForbidEmojiFilter() : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?.let {
            if (it.toString().isEmoji()) {
                return "";
            }
            return it
        }
        return ""
    }

}

//图片设置
fun TextView.setDrawableLeft(resId: Int) {
    val drawable = ContextCompat.getDrawable(this.context, resId)
    setDrawableLeft(drawable)
}

fun TextView.setDrawableLeft(drawable: Drawable?) {
    if (drawable == null) {
        setCompoundDrawables(
            null,
            compoundDrawables[1],
            compoundDrawables[2],
            compoundDrawables[3]
        )
    } else {
        drawable.apply {
            setBounds(0, 0, minimumWidth, minimumHeight)
            setCompoundDrawables(
                drawable,
                compoundDrawables[1],
                compoundDrawables[2],
                compoundDrawables[3]
            )
        }
    }
}

fun TextView.setDrawableRight(resId: Int) {
    val drawable = ContextCompat.getDrawable(this.context, resId)
    setDrawableRight(drawable)
}

fun TextView.setDrawableRight(drawable: Drawable?) {
    if (drawable == null) {
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            null,
            compoundDrawables[3]
        )
    } else {
        drawable.apply {
            setBounds(0, 0, minimumWidth, minimumHeight)
            setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                drawable,
                compoundDrawables[3]
            )
        }
    }
}

fun TextView.setDrawableTop(resId: Int) {
    val drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.apply {
        setBounds(0, 0, minimumWidth, minimumHeight)
        setCompoundDrawables(
            compoundDrawables[0],
            drawable,
            compoundDrawables[2],
            compoundDrawables[3]
        )
    }
}

fun TextView.setDrawableBottom(resId: Int) {
    val drawable = ContextCompat.getDrawable(this.context, resId)
    drawable?.apply {
        setBounds(0, 0, minimumWidth, minimumHeight)
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            compoundDrawables[2],
            drawable
        )
    }
}

/**
 * 禁止输入框复制粘贴菜单
 */
fun TextView.disableCopyAndPaste() {
    try {
        //处理长按事件
        setOnLongClickListener { true }
        //禁用长按
        isLongClickable = false
        setTextIsSelectable(false)
        //禁用ActonMode弹窗
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

//View 边距赋值/取值
var View.bottomMargin: Int
    get():Int {
        return if (layoutParams == null) {
            0
        } else {
            return (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
        }
    }
    set(value) {
        layoutParams?.let {
            (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = value
        }
    }


var View.topMargin: Int
    get():Int {
        return if (layoutParams == null) {
            0
        } else {
            return (layoutParams as ViewGroup.MarginLayoutParams).topMargin
        }
    }
    set(value) {
        layoutParams?.let {
            (layoutParams as ViewGroup.MarginLayoutParams).topMargin = value
        }
    }


var View.rightMargin: Int
    get():Int {
        return if (layoutParams == null) {
            0
        } else {
            return (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
        }
    }
    set(value) {
        layoutParams?.let {
            (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = value
        }
    }

var View.leftMargin: Int
    get():Int {
        return if (layoutParams == null) {
            0
        } else {
            (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
        }
    }
    set(value) {
        layoutParams?.let {
            (it as ViewGroup.MarginLayoutParams).leftMargin = value
        }
    }