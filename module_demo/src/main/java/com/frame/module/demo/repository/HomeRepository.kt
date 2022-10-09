package com.frame.module.demo.repository

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.reflect.TypeToken
import com.frame.basic.base.ktx.toMultipartBodyPart
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.bean.DialogMenu
import com.frame.module.demo.bean.Menu
import com.frame.module.demo.net.HomeApiService
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject


/**
 * 首页M层
 */
class HomeRepository @Inject constructor() {
    @Inject
    lateinit var mApi: HomeApiService

    suspend fun getMenu(): MutableList<Menu> {
        delay(1000L)
        return Menu.values().asList().toMutableList()
    }

    suspend fun getDialogMenu(): MutableList<DialogMenu> {
        delay(1000L)
        return DialogMenu.values().asList().toMutableList()
    }

    suspend fun getDesc(): String {
        delay(1000L)
        return "加载成功"
    }

    suspend fun getArticleTextBean(
        searchKey: String? = null,
        currentPage: Int = 0
    ): List<ArticleImageBean> {
        delay(1000L)
        if (currentPage < 4) {
            val list = ResourceUtils.readAssets2String("list.json")
            val articleList = GsonUtils.fromJson<List<ArticleImageBean>>(
                list,
                object : TypeToken<List<ArticleImageBean>>() {}.type
            ).filter {
                if (!searchKey.isNullOrBlank()) {
                    it.title.contains(searchKey, true) || it.desc.contains(searchKey, true)
                } else {
                    true
                }
            }
            if (currentPage > 0) {
                articleList.forEach {
                    it.title = "第${currentPage + 1}页  ->  ${it.title}"
                }
            }
            return articleList
        } else {
            return mutableListOf()
        }
    }

    suspend fun getRefreshLayoutData(): String {
        delay(1000L)
        return "继续下拉"
    }

    suspend fun getInteractionData(): String {
        delay(1000L)
        return "成功获取交互数据"
    }

    /**
     * 上传文件
     * 不支持断点上传，需后台配合才能支持
     */
    suspend fun uploadFiles(
        file: File,
        onProgress: ((byte: Long, totalLength: Long) -> Unit)? = null
    ): ResponseBody {
        return mApi.uploadFile(file.toMultipartBodyPart(file.name, onProgress))
    }
}