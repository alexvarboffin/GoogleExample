package com.likee.likeeapicommon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("videoList")
    @Expose
    var videoList: List<VideoList>? = null

    @SerializedName("uid")
    @Expose
    var uid: String? = null

    override fun toString(): String {
        return "Data{" +
                "videoList=" + videoList +
                ", uid='" + uid + '\'' +
                '}'
    }
}
