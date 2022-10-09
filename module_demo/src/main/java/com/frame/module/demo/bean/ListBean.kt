package com.frame.module.demo.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ArticleImageBean: Serializable{
    @SerializedName("superChapterName")
    var title: String = ""
    @SerializedName("title")
    var desc: String = ""
    var image: String? = ""
    var niceDate: String = ""
}