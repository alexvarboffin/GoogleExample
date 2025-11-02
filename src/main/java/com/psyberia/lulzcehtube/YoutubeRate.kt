package com.psyberia.lulzcehtube

import com.google.api.services.samples.Config
import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTube.Videos.Rate
import com.google.common.collect.Lists
import java.io.IOException

/**
 * Created by combo on 15.09.2016.
 */
class YoutubeRate {
    private var counter = 0

    /*
     *poFxaLdqGLMsSoOpY0ebgSnan2sBPHtXCcMhsuCjyv0
     */
    init {
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.

        try {
            val scopes: List<String> = Lists.newArrayList(
                "https://www.googleapis.com/auth/youtube.readonly",
                "https://www.googleapis.com/auth/youtube",
                "https://www.googleapis.com/auth/youtube.force-ssl",
                "https://www.googleapis.com/auth/youtubepartner",
                "https://www.googleapis.com/auth/youtube.upload"
            )
            val cfg = Config.cfgs[2]
            val credential = Auth.authorize2(
                scopes, cfg.credentialDataStoreName + "1aa",
                System.getProperty("user.home") + "/" + "." + cfg.credentialDataStoreName + "1aa",
                cfg.client_secret
            )

            tube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName("youtube-cmdline-myrate-sample-00").build()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun rate(var0: String?, var1: String?) {
        var rate: Rate? = null
        counter++
        /*
            POST https://www.googleapis.com/youtube/v3/
            videos/rate?id=68jKxTvub7Y&rating=dislike&key={YOUR_API_KEY}
            */
        try {
            rate = tube!!.videos().rate(var0, var1)
            //rate.setId("68jKxTvub7Y");
            //rate.setRating("like");//dislike none "like"
            //rate.setOauthToken("");
            rate.execute()
        } catch (e: Exception) {
            println(e.localizedMessage)
        }

        println("            | DISLIKE COUNTER: $counter |")
    }

    companion object {
        private var tube: YouTube? = null
    }
}
