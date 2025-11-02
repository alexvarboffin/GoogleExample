package com.psyberia.lulzcehtube.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TikTokVideo {
    @SerializedName("author_uniqueId")
    @Expose
    var authorUniqueId: String? = null

    @SerializedName("video_url")
    @Expose
    var videoUrl: String? = null

    @kotlin.jvm.JvmField
    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null
}
