package com.google.api.services.samples.youtube.cmdline.data

import com.google.api.client.util.Lists
import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.samples.youtube.cmdline.Auth.authorize
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.VideoListResponse
import com.psyberia.lulzcehtube.YoutubeRate
import java.io.IOException

object MyVideos {
    private val video_id: String? = null

    /*
     * Videos: list
     *
     * com/google/api/services/samples/youtube/cmdline/data/MyVideos.java:24
     *
     * getVideoInformation
     * */
    private var youtube: YouTube? = null


    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val lists = getAllVideo0("exJvh5u_t_s").items

            for (current in lists) {
                current.snippet
            }

            //.get(1).getSnippet()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //    public static void print(String s) {
    //        System.out.println("### " + s + " ###");
    //    }
    @JvmStatic
    @Throws(IOException::class)
    fun getAllVideo0(video_id: String?): VideoListResponse {
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        val scopes: MutableList<String> =
            Lists.newArrayList()
        scopes.add("https://www.googleapis.com/auth/youtube")


        // Authorize the request.
        val credential = authorize(scopes, "testmyvideoapi")


        /*
             https://www.googleapis.com/youtube/v3/
             videos?key={API-key}&fields=items(snippet(title,description,tags))&part=snippet&id={video_id}
            */

        // This object is used to make YouTube Data API requests.
        youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
            "youtube-cmdline-myuploads-sample"
        ).build()


        // Call the API's channels.list method to retrieve the
        // resource that represents the authenticated user's channel.
        // In the API response, only include channel information needed for
        // this use case. The channel's contentDetails part contains
        // playlist IDs relevant to the channel, including the ID for the
        // list that contains videos uploaded to the channel.
        val mm = youtube!!.videoCategories().list("id") //or "id"

        //        mm.setFields(
//                "kind,etag,items(kind,etag,id,snippet/title,snippet/assignable)," +
//                        "pageInfo/resultsPerPage,pageInfo/totalResults," +
//                        "nextPageToken,prevPageToken,pageInfo(totalResults,resultsPerPage)");

//        mm.setPart("snippet");
//        mm.setHl("");

        //items [] --> items(...)
        //snippet": {} -->snippet/
//        {
//            "kind": "youtube#videoCategory",
//                "etag": etag,
//                "id": string,
//                "snippet": {
//            "channelId": "UCBR8-60-B28hp2BmDPdntcQ",
//                    "title": string,
//                    "assignable": boolean
//        }
//        }
        mm.setId(video_id) //<---------------- id list (video_id);video_id


        //        or
//        mm.setRegionCode("US");id
        //@@@      VideoCategoryListResponse r0esponse = mm.execute();
        //@@@      System.out.println("77" + r0esponse.toPrettyString().trim());
        //@@@      System.out.println(r0esponse.getItems().toString());
        //@@@      System.out.println("" + r0esponse.getPageInfo());


        //=========================================
        //val videos = youtube.videos()
        val viList = youtube!!.videos().list("id")


        //YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");


        //channelRequest.setMine(true);


        //channelRequest.setPart("contentDetails");
        //channelRequest.setForUsername(chennelID); // GoogleDevelopers XChannel.getChannelId()
        viList.setFields(
            "items(" +  //none "contentDetails/videoId," +
                    //none "snippet/title" +
                    "snippet/publishedAt" +
                    ",snippet/description" +
                    ",snippet/tags" +
                    ",etag,status" +
                    ",snippet/thumbnails" +
                    "),nextPageToken,pageInfo"
        )

        viList.setPart("snippet")

        viList.setId(video_id) //<---------------- id list (video_id);


        /*channelId, title, description, tags, and categoryId*/
        val response = viList.execute()
        val pinfo = response.pageInfo

        //@@@   print("Game info: " + pinfo);//{"resultsPerPage":1,"totalResults":1}
        //@@@   print("Response: " + response);
        //print(viList.getFields());//items(snippet/title,snippet/description,snippet/tags),nextPageToken,pageInfo
        //print(viList.getUserIp());
        //print(viList.getQuotaUser());

        //{"items":[{"snippet":{"description":"С помощью нашей программы, вы можете анонимно отправить письмо (email) с произвольными данными отправителя (имени и адреса электронной почты). Письмо отправляется полностью анонимно, с нашего IP адреса. Ваш адрес не где не остается. Вы сможете ввести совершенно любой адрес отправителя электронной почты, а также его имя, текст сообщения, и тему сообщения.\n\nВоспользовавшись программой отправки анонимной почты, вы можете отсылать электронные сообщения на нужный адрес, сохраняя при этом свою конфиденциальность. При отправке письма с помощью нашей программы для анонимной отправки email - ваш ip адрес, а также другие конфиденциальные данные не доступны получателю. Если вы хотите получить ответ на анонимное письмо, укажите обратный адрес (ваш email) непосредственно в тексте письма. HTML поддерживается.\n\n=====================\nПоддержка русского языка\nНеограниченное количество отправленных сообщения\nЗащищенная отпрака е-mail\n=====================\nWARNING!!!\nПравила и ограничения...\nЗапрещена спам рассылка, рассылка с угрозами, любая рассылка противоречащая законодательству вашей страны...","tags":["Anonimailer Pro","Анонимная отправка email","хакинг","взлом","андроид","почта"],"title":"Anonimailer Pro Взлом мыла, Подмена email"}}],"pageInfo":{"resultsPerPage":1,"totalResults":1}}
        //items(snippet/title,snippet/description,snippet/tags),nextPageToken,pageInfo

        //@@    print(response.getItems().get(0).getSnippet().getTags().toString());
        return response
    }


    /*
     * Print information about all of the items in the playlist.
     *
     * @param size size of list
     *
     * @param iterator of Playlist Items from uploaded Playlist
     */
    private fun prettyPrint(size: Int, playlistEntries: Iterator<PlaylistItem>) {
        println("=============================================================")
        println("\t\tTotal Videos Uploaded: $size")
        println("=============================================================")


        val rates = YoutubeRate()

        while (playlistEntries.hasNext()) {
            val playlistItem = playlistEntries.next()
            println(" video name  = " + playlistItem.snippet.title)
            println(" video id    = " + playlistItem.contentDetails.videoId)
            println(" upload date = " + playlistItem.snippet.publishedAt)
            println("-------------------------------------------------------------")


            //rates.rate(playlistItem.getContentDetails().getVideoId(), "dislike");
        }
    }
}
