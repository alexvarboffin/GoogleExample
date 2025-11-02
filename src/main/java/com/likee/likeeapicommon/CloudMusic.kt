package com.likee.likeeapicommon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CloudMusic {
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("musicId")
    @Expose
    var musicId: String? = null

    @SerializedName("musicName")
    @Expose
    var musicName: String? = null

    override fun toString(): String {
        return "CloudMusic{" +
                "avatar='" + avatar + '\'' +
                ", musicId='" + musicId + '\'' +
                ", musicName='" + musicName + '\'' +
                '}'
    }
}
