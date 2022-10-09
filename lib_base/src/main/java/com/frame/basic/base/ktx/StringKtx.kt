package com.frame.basic.base.ktx

import android.text.TextUtils
import com.frame.basic.base.utils.MD5Utils
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * 获取emoji的个数
 */
fun String.emojiCount(): Int {
    var emojiCount = 0
    for (i in 0 until length) {
        val type = Character.getType(this[i])
        if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
            emojiCount++
        }
    }
    return emojiCount / 2
}

/**
 * 是否是表情符
 */
fun String.isEmoji(): Boolean {
    val pattern = Pattern.compile(
        "(?:[\uD83C\uDF00-\uD83D\uDDFF]" +  // 杂项符号及图形
                "|[\uD83E\uDD00-\uD83E\uDDFF]" +  // 增补符号及图形
                "|[\uD83D\uDE00-\uD83D\uDE4F]" +  // 表情符号
                "|[\uD83D\uDE80-\uD83D\uDEFF]" +  // 交通及地图符号
                "|[\u2600-\u26FF]\uFE0F?" +  // 杂项符号
                "|[\u2700-\u27BF]\uFE0F?" +  // 装饰符号
                "|\u24C2\uFE0F?" +  // 封闭式字母数字符号
                "|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}" +  // 封闭式字母数字补充符号-区域指示符号
                "|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?" +  // 其他封闭式字母数字补充emoji符号
                "|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3" +  //  键帽符号
                "|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?" +  // 箭头符号
                "|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?" +  // 杂项符号及箭头
                "|[\u2934\u2935]\uFE0F?" +  // 补充箭头符号
                "|[\u3030\u303D]\uFE0F?" +  // CJK 符号和标点
                "|[\u3297\u3299]\uFE0F?" +  //  封闭式 CJK 字母和月份符号
                "|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?" +  // 封闭式表意文字补充符号
                "|[\u203C\u2049]\uFE0F?" +  // 一般标点
                "|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?" +  // 几何图形
                "|[\u00A9\u00AE]\uFE0F?" +  // 拉丁文补充符号
                "|[\u2122\u2139]\uFE0F?" +  // 字母符号
                "|\uD83C\uDC04\uFE0F?" +  // 麻将牌
                "|\uD83C\uDCCF\uFE0F?" +  // 扑克牌
                "|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)"
    ) // 杂项技术符号
    val matcher = pattern.matcher(this)
    return matcher.find()
}


/**
 * 获取包含了emoji表情的字符长度
 */
fun String.containsEmojiLength(): Int {
    val emojiCount = emojiCount()
    val tempStr = deleteEmojiBlank()
    return tempStr.length - emojiCount
}

/**
 * 删除emoji表情中的空格符号
 */
fun String.deleteEmojiBlank(): String {
    val sb = StringBuffer()
    for (i in this.indices) {
        val ch = this[i]
        if (Char(65039) != ch) {
            sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * 安全转换为Enum
 */
inline fun <reified T : Enum<T>> CharSequence?.convertEnum(defaultEnum: T): T {
    if (this.isNullOrEmpty()) {
        return defaultEnum
    }
    return try {
        enumValueOf(this.toString())
    } catch (e: Exception) {
        defaultEnum
    }
}

fun Any?.toNoNullString(defValue: String = ""): String {
    val str = this.toString()
    return if (str.isBlank() || "null" == str) {
        defValue
    } else {
        str
    }
}

fun Any?.strIsNotNullAndEmpty(): Boolean {
    return toNoNullString().isNotEmpty()
}

fun String?.nullToEmpty(): String {
    return nullToDefault("")
}

fun String?.nullToDefault(defValue: String): String {
    return if (this.isNullOrBlank()) {
        defValue
    } else {
        this
    }
}

fun String?.toSafeDouble(): Double {
    return try {
        nullToEmpty().toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun String?.toSafeInt(): Int {
    if (this.isNullOrEmpty()) {
        return 0
    }
    return if (this.isNotEmpty()) {
        try {
            //toInt() 会走到异常
            BigDecimal(this).toInt()
        } catch (e: Exception) {
            0
        }
    } else {
        0
    }
}

fun String.stripTrailingZeros(): String? {
    return try {
        if (BigDecimal(this).toFloat() == 0F) {
            //0.0比较特殊
            "0"
        } else {
            BigDecimal(this).stripTrailingZeros().toPlainString()
        }
    } catch (exception: java.lang.Exception) {
        this
    }
}

/**
 * url自动补全
 */
fun String.completionUrl(): String {
    val HTTP = "http://"
    val HTTPS = "https://"
    val FILE = "file://"
    var keyword = trim()
    if (keyword.startsWith("www.")) {
        keyword = HTTP + keyword;
    } else if (keyword.startsWith("ftp.")) {
        keyword = "ftp://" + keyword;
    }

    val containsPeriod = keyword.contains(".")
    val isIPAddress = (TextUtils.isDigitsOnly(keyword.replace(".", ""))
            && (keyword.replace(".", "").length >= 4) && keyword.contains("."))
    val aboutScheme = keyword.contains("about:")
    val validURL = (keyword.startsWith("ftp://") || keyword.startsWith(HTTP)
            || keyword.startsWith(FILE) || keyword.startsWith(HTTPS))
            || isIPAddress
    val isSearch = ((keyword.contains(" ") || !containsPeriod) && !aboutScheme)

    if (isIPAddress
        && (!keyword.startsWith(HTTP) || !keyword.startsWith(HTTPS))
    ) {
        keyword = HTTP + keyword;
    }

    val converUrl: String?
    if (isSearch) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        converUrl = "http://www.baidu.com/s?wd=${keyword}&ie=UTF-8"
    } else if (!validURL) {
        converUrl = HTTP + keyword
    } else {
        converUrl = keyword
    }
    return converUrl
}

/**
 * url转文件名称
 * @return MD5(url) + 文件后缀
 */
fun String.toUrlFileName() = if(this.contains(".")){
    "${MD5Utils.parseStrToMd5L32(this)}${this.substring(this.lastIndexOf("."))}"
}else{
    MD5Utils.parseStrToMd5L32(this)
}


