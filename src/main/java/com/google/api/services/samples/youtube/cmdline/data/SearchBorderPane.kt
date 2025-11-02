package com.google.api.services.samples.youtube.cmdline.data

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.samples.youtube.cmdline.data.MyVideos.getAllVideo0
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.*
import com.psyberia.QEvent
import com.psyberia.lulzcehtube.Utils.generateMessage
import com.psyberia.lulzcehtube.project.ChannelScrapperImpl
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SearchBorderPane(private val pr: ChannelScrapperImpl) : BorderPane() {
    private val checkBox = CheckBox("Оставить комментарий и лайк")


    private val executor: Executor = Executors.newSingleThreadExecutor()



    //var file: File? = null

    init {
        this.center = status_text_area
        this.right = status_text_area2

        val __keywords: MutableList<String> = ArrayList()

        //        __keywords.put("Знакомства", 0);//Scetch Comedy
//        __keywords.add("знакомства с девушкой на улице");
//        __keywords.add("съем девушек пикап");
//        __keywords.add("знакомства с красивыми девушками");
//        __keywords.add("пикап пранк 2017");
//        __keywords.add("социальные эксперименты");
//        __keywords.add("как познакомиться с красивой девушкой");
//        __keywords.add("вопросы на интимную тему");
//        __keywords.add("пикап видео");
//        __keywords.put("развод девушек видео",0);
//        __keywords.put("TikTok", 2);

        //__keywords.put("TikTok Watermark", 2);
        //__keywords.put("Download TikTok Videos Without Watermark For Free", 2);
        //__keywords.put("pill identifier", 0);
        //__keywords.put("dreambook", 90);

//        __keywords.add("How to Download TikTok Videos Without Watermark");
//        __keywords.add("Best Ways to Download Tik Tok without Watermark");
        __keywords.add("Video Downloader For Likee (Like)")
        __keywords.add("Likee Video Downloader – Download Likee Videos Without Watermark")
        __keywords.add("Скачать видео из Likee без ватермарки")
        __keywords.add("download videos from Likee")

        for (keyword in __keywords) {
            status_text_area.appendText(keyword)
            status_text_area.appendText("\n")
        }

        val events: MutableList<QEvent> = ArrayList()
        events.add(object : QEvent("Поиск по ключам") {
            override fun onCreate() {
                val keywords = status_text_area.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                pr.search(keywords, checkBox.isSelected)
            }
        })
        events.add(object : QEvent("GENERATE TEST MESSAGES") {
            override fun onCreate() {
                for (i in 0..99) {
                    println(generateMessage() + "\n")
                }
            }
        })

        events.add(object : QEvent("Спам по списку видео") {
            override fun onCreate() {
                count_of_comments = 0

                val txts = status_text_area2.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (txt in txts) {
                    if (!txt.isEmpty()) {
                        val aa = txt.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        println(aa.contentToString())
                        try {
                            makeNewComments(
                                aa[1].trim { it <= ' ' },
                                aa[0].trim { it <= ' ' },
                                ChannelScrapperImpl.youtube!!
                            )
                            count_of_comments++
                        } catch (e: GoogleJsonResponseException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                println("@@@@@@@@@@@@@@@@@@@@@" + count_of_comments + "@@@@@@@@@@@")
            }
        })

        events.add(object : QEvent("GET META TAGS") {
            override fun onCreate() {
                try {
                    getAllVideo0("MkyNaSz-zZA")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })

        val mNodeBottom = FlowPane(5.0, 5.0)
        mNodeBottom.children.add(checkBox)
        for (entry in events) {
            val b = Button("" + entry.name) //, imageView1);
            //b.setFont(new Font("Verdana", 8));
            mNodeBottom.children.add(b)
            b.onAction = EventHandler { event: ActionEvent? ->
                executor.execute { entry.onCreate() }
            }
        }


        //setLeft(listView());
        bottom = mNodeBottom
    }




    fun grabbMetaTags(current: SearchResult, keywords: Array<String?>?, tagsbuilder: StringBuilder) {
        val resourceId = current.id

        // Confirm that the result represents a video. Otherwise, the
        // item will not contain a video ID.
        if (resourceId.kind == "youtube#video") {
            val videoId = resourceId.videoId
            val channelId = current.snippet.channelId

            try {
//                List<Video> аа = MyVideos.getAllVideo(videoId).getItems();
//                VideoSnippet info = аа.get(0).getSnippet();

//                YouTube.VideoCategories.List list = youtube.videoCategories().list("id");
//                list.setId(videoId);

                //SearchResultSnippet info = current.getSnippet();

                val viList = ChannelScrapperImpl.youtube!!.videos().list("id")
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
                viList.setId(videoId)
                val response = viList.execute()
                //PageInfo info = response.getPageInfo();
                val aa = response.items[0].snippet

                var tags = "none"
                val mm = aa.tags
                if (mm != null) {
                    Collections.shuffle(mm)

                    tags = java.lang.String.join(",", mm)
                    for (meta in mm) {
                        var meta = meta
                        meta = meta.lowercase(Locale.getDefault())

                        //@@@    int count = (keywords.containsKey(meta)) ? keywords.get(meta).intValue() : 0;
                        //@@@    keywords.put(meta, ++count);
                    }
                }
                val title = current.snippet.title


                //String channel_id = current.getSnippet().getChannelId();


//                System.out.println("-------------------------------------------------------------");
//                Thumbnail thumbnail = current.getSnippet().getThumbnails().getDefault();
//                System.out.println(" Video Id" + resourceId.getVideoId());
//                System.out.println(" Title: " + title);
//                System.out.println(" Thumbnail: " + thumbnail.getUrl());
//                System.out.println(" Tags: " + tags);
//                System.out.println("-------------------------------------------------------------");
                tagsbuilder.append(title).append("\n")
                tagsbuilder.append(tags).append("\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var count_of_comments = 0



        var status_text_area: TextArea = TextArea()
        var status_text_area2: TextArea = TextArea()

        @JvmStatic
        fun main(args: Array<String>) {
        }

        @JvmStatic
        @Throws(IOException::class)
        fun makeNewComments(channel: String?, video: String, youtube: YouTube) {
            println("https://www.youtube.com/watch?v=$video")
            val rate = youtube.videos().rate(video, "like")
            //rate.setId("68jKxTvub7Y");
            //rate.setRating("like");//dislike none "like"
            //rate.setOauthToken("");
            rate.execute()
            setComments(channel, video, generateMessage(), youtube)

            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }


        //    private YouTube makeYoutube() {
        //        // This OAuth 2.0 access scope allows for full read/write access to the
        //        // authenticated user's account and requires requests to use an SSL connection.
        //        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
        //        try {
        //            // Authorize the request.
        //            Credential credential = Auth.authorize2(scopes, cfg.credentialDataStoreName + "a-commentthreads",
        //                    System.getProperty("user.home") + "/" + "." + cfg.credentialDataStoreName + "a-commentthreads",
        //                    cfg.client_secrets
        //            );
        //            // This object is used to make YouTube Data API requests.
        //            return new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
        //                    .setApplicationName("youtube-cmdline-commentthreads-sample").build();
        //        } catch (Exception e) {
        //            System.out.println(e.getLocalizedMessage());
        //            return null;
        //        }
        //    }
        fun setComments(CHANNEL_ID: String?, VIDEO_ID: String, message: String?, youtube: YouTube) {
            //System.out.println(lastVideo[0] + ":" + tmp);

            try {
                val __topLevelComment = Comment()
                val snippet = CommentSnippet()
                snippet.setTextOriginal(message)
                __topLevelComment.setSnippet(snippet)

                // Create a comment snippet1 snippet with channelId and top-level
                // comment.
                val commentThreadSnippet = CommentThreadSnippet()
                commentThreadSnippet.setChannelId(CHANNEL_ID)
                commentThreadSnippet.setTopLevelComment(__topLevelComment)

                // Create a comment snippet1 with snippet.
                val commentThread = CommentThread()
                commentThread.setSnippet(commentThreadSnippet)


                // Print information from the API response.
                //System.out.println("\n-------- Created Channel Comment --------\n");
                //snippet = thread.getSnippet().getTopLevelComment().getSnippet();
                //System.out.println(/*snippet1.toPrettyString() +*/ "==> [" + snippet.getVideoId() + "] (" + snippet.getAuthorDisplayName() + ") " + snippet.getTextDisplay());
                // Insert video comment
                commentThreadSnippet.setVideoId(VIDEO_ID)
                // Call the YouTube Data API's commentThreads.insert method to
                // create a comment.
                val thread = youtube.commentThreads().insert("snippet", commentThread).execute()
                println("https://www.youtube.com/watch?v=$VIDEO_ID")


                //Узнаем количество комментариев под видео
                // Call the YouTube Data API's commentThreads.list method to
                // retrieve video comment threads.
//            CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads()
//                    .list("snippet").setVideoId(VIDEO_ID).setTextFormat("plainText").execute();

//            List<CommentThread> videoComments = videoCommentsListResponse.getItems();
//            int commCount = videoComments.size();
//            System.out.println("TEST VIDEO COMMENT THREAD SIZE: " + commCount);


                // if (commCount < 10) {
                //    for (int i = 0; i < (10 - commCount); i++) {
                // Call the YouTube Data API's commentThreads.insert method to
                // create a comment.
                // Print information from the API response.

                //System.out.println(/*snippet1.toPrettyString() +*/ " [" + snippet.getVideoId() + "] (" + snippet.getAuthorDisplayName() + ") " + snippet.getTextDisplay());
                println("--> " + count_of_comments + " <--")

                //    }
//            } else {
//                System.out.println("Слишком много комментариев...");
//            }
            } catch (e: GoogleJsonResponseException) {
//            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode()
//                    + " : " + e.getDetails().getMessage()+" "+e.getDetails());
                e.printStackTrace()
            } catch (e: IOException) {
                System.err.println("IOException: " + e.message)
                e.printStackTrace()
            } catch (t: Throwable) {
                System.err.println("Throwable: " + t.message)
                t.printStackTrace()
            }
        }

        @get:Throws(IOException::class)
        private val inputQuery: String
            get() {
                var inputQuery = ""

                print("Please enter a search term: ")
                val bReader = BufferedReader(InputStreamReader(System.`in`))
                inputQuery = bReader.readLine()

                if (inputQuery.length < 1) {
                    // Use the string "YouTube Developers Live" as a default.
                    inputQuery = "YouTube Developers Live"
                }
                return inputQuery
            }

        /*
    * Prints out all results in the Iterator. For each result, print the
    * title, video ID, and thumbnail.
    *
    * @param iteratorSearchResults Iterator of SearchResults to print
    *
    * @param query Search query (String)
    */
        private fun prettyPrint(iteratorSearchResults: Iterator<SearchResult>, query: String) {
            //@@@   System.out.println("=============================================================");
            //@@@   System.out.println(
            //@@@           "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
            //@@@   System.out.println("=============================================================");

            if (!iteratorSearchResults.hasNext()) {
                //@@@        System.out.println(" There aren't any results for your query.");
            }
            while (iteratorSearchResults.hasNext()) {
                val singleVideo = iteratorSearchResults.next()
                val resourceId = singleVideo.id

                // Confirm that the result represents a video. Otherwise, the
                // item will not contain a video ID.
                if (resourceId.kind == "youtube#video") {
                    val thumbnail = singleVideo.snippet.thumbnails.default
                    //@@@    System.out.println("@@ Video Id" + resourceId.getVideoId());
                    //@@@    System.out.println("@@ Title: " + singleVideo.getSnippet().getTitle());
                    //@@@    System.out.println("@@ Thumbnail: " + thumbnail.getUrl());
                    println(singleVideo.snippet.title)
                    try {
                        println(singleVideo.toPrettyString())
                        //System.out.println(resourceId.toPrettyString());
                    } catch (e: IOException) {
                        // e.printStackTrace();
                    }
                    //System.out.println("-------------------------------------------------------------");
                }
            }
        }
    }
}
