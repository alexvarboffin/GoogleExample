package com.likee.likeeapicommon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class VideoList {
    @SerializedName("coverUrl")
    @Expose
    var coverUrl: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("likeCount")
    @Expose
    var likeCount: Int = 0

    @SerializedName("videoUrl")
    @Expose
    var videoUrl: String? = null

    @SerializedName("videoWidth")
    @Expose
    var videoWidth: Int = 0

    @SerializedName("videoHeight")
    @Expose
    var videoHeight: Int = 0

    @SerializedName("postId")
    @Expose
    var postId: String? = null

    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("likeeId")
    @Expose
    var likeeId: String? = null

    @SerializedName("musicName")
    @Expose
    var musicName: String? = null

    @SerializedName("shareUrl")
    @Expose
    var shareUrl: String? = null

    @SerializedName("msgText")
    @Expose
    var msgText: String? = null

    @SerializedName("commentCount")
    @Expose
    var commentCount: Int = 0

    @SerializedName("shareCount")
    @Expose
    var shareCount: Int = 0

    @SerializedName("hashtagInfos")
    @Expose
    var hashtagInfos: String? = null

    @SerializedName("atUserInfos")
    @Expose
    var atUserInfos: Any? = null

    @SerializedName("yyuid")
    @Expose
    var yyuid: String? = null

    @SerializedName("postTime")
    @Expose
    var postTime: Int = 0

    @SerializedName("sound")
    @Expose
    var sound: Sound? = null

    @SerializedName("cloudMusic")
    @Expose
    var cloudMusic: CloudMusic? = null

    override fun toString(): String {
        return "VideoList{" +
                "coverUrl='" + coverUrl + '\'' +
                ", title='" + title + '\'' +
                ", avatar='" + avatar + '\'' +
                ", likeCount=" + likeCount +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", postId='" + postId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", likeeId='" + likeeId + '\'' +
                ", musicName='" + musicName + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", msgText='" + msgText + '\'' +
                ", commentCount=" + commentCount +
                ", shareCount=" + shareCount +
                ", hashtagInfos='" + hashtagInfos + '\'' +
                ", atUserInfos=" + atUserInfos +
                ", yyuid='" + yyuid + '\'' +
                ", postTime=" + postTime +
                ", sound=" + sound +
                ", cloudMusic=" + cloudMusic +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val videoList = o as VideoList
        return likeCount == videoList.likeCount && videoWidth == videoList.videoWidth && videoHeight == videoList.videoHeight && commentCount == videoList.commentCount && shareCount == videoList.shareCount && postTime == videoList.postTime &&
                coverUrl == videoList.coverUrl &&
                title == videoList.title &&
                avatar == videoList.avatar &&
                videoUrl == videoList.videoUrl &&
                postId == videoList.postId &&
                nickname == videoList.nickname &&
                likeeId == videoList.likeeId &&
                musicName == videoList.musicName &&
                shareUrl == videoList.shareUrl &&
                msgText == videoList.msgText &&
                hashtagInfos == videoList.hashtagInfos &&
                atUserInfos == videoList.atUserInfos &&
                yyuid == videoList.yyuid &&
                sound == videoList.sound &&
                cloudMusic == videoList.cloudMusic
    }

    override fun hashCode(): Int {
        return Objects.hash(
            coverUrl,
            title,
            avatar,
            likeCount,
            videoUrl,
            videoWidth,
            videoHeight,
            postId,
            nickname,
            likeeId,
            musicName,
            shareUrl,
            msgText,
            commentCount,
            shareCount,
            hashtagInfos,
            atUserInfos,
            yyuid,
            postTime,
            sound,
            cloudMusic
        )
    }
}