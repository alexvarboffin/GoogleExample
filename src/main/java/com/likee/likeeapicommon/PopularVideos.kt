package com.likee.likeeapicommon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PopularVideos {
    @SerializedName("code")
    @Expose
    var code: Int = 0

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    override fun toString(): String {
        return "PopularVideos{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}'
    }
}
