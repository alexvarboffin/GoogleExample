package com.likee.likeeapicommon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Sound {
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @JvmField
    @SerializedName("ownerName")
    @Expose
    var ownerName: String? = null

    @SerializedName("soundId")
    @Expose
    var soundId: String? = null

    @SerializedName("soundName")
    @Expose
    var soundName: String? = null
}

