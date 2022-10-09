package com.frame.basic.base.utils

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.frame.basic.base.BaseApplication
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * <pre>
 *     author : June Yang
 *     time   : 2020/5/7
 *     desc   : 资源工具类
 *     version: 1.0.0
 * </pre>
 */
object ResourcesUtil {

    val resources: Resources by lazy { BaseApplication.application.resources}

    fun getColor(@ColorRes id: Int): Int =
        ContextCompat.getColor(BaseApplication.application, id)

    fun getBitmap(id: Int): Bitmap = BitmapFactory.decodeResource(resources, id)

    fun getDrawable(@DrawableRes id: Int): Drawable? =
        ContextCompat.getDrawable(BaseApplication.application, id)

    fun getString(@StringRes id: Int): String = resources.getString(id)

    fun getStringArray(@ArrayRes id: Int): Array<String> = resources.getStringArray(id)

    fun getDimension(@DimenRes id: Int): Float = resources.getDimension(id)

    fun getDimensionPixelOffset(@DimenRes id: Int): Int = resources.getDimensionPixelOffset(id)

    fun getDimensionPixelSize(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)

    fun getInterger(@IntegerRes id: Int) = resources.getInteger(id)

    /**
     * 获取资源路径
     * @param context
     * @param id
     * @return
     */
    fun getResourceUri(context: Context, id: Int): String? {
        val resources = context.resources
        return (ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id))
    }

    fun getResourcePath(context: Context, id: Int): String {
        val uri = getResourceUri(context, id)
        if (!TextUtils.isEmpty(uri)) {
            return Uri.parse(uri).toString()
        }

        return ""
    }

    /**
     * 根据路径获取json文件
     */
    fun loadAssetsString(context: Context, path: String): String {
        var `is`: InputStream? = null
        var bos: ByteArrayOutputStream? = null
        try {
            `is` = context.assets.open(path)
            bos = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var len: Int
            while (`is`.read(b).also { len = it } != -1) {
                bos.write(b, 0, len)
            }
            return bos.toString("utf-8")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.close()
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}