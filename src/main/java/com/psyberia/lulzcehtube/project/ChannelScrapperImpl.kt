package com.psyberia.lulzcehtube.project

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.InputStreamContent
import com.google.api.services.CFG
import com.google.api.services.samples.Config
import com.google.api.services.samples.Config.OAUTH_CRE
import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.samples.youtube.cmdline.Auth.authorize2
import com.google.api.services.samples.youtube.cmdline.data.SearchBorderPane
import com.google.api.services.samples.youtube.cmdline.data.SearchBorderPane.Companion.makeNewComments
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import com.google.common.collect.Lists
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.likee.likeeapicommon.VideoList
import com.psyberia.QEvent
import com.psyberia.lulzcehtube.model.TTok
import com.psyberia.lulzcehtube.model.TikTokVideo
import com.psyberia.outh2.UploadVideoPane
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.IOException
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.logging.Level
import java.util.logging.Logger

class ChannelScrapperImpl : Application(), MediaHttpUploaderProgressListener, ChannelScrapper {

    lateinit var cfg: CFG

    //    private String mDescription = "";
    //    private File file;
    //            "???????? ????? https://www.youtube.com/channel/" + DONOR_CHANNEL_NAME + "/featured\n" +
    //            "????????? ? https://vk.com/dmops\n" +
    //            "???????? ?????? ? https://vk.com/mopsgavgav\n" +
    //            "YouTube ? https://goo.gl/C3lzh4\n" +
    //            "??????-????? ? https://goo.gl/GffVPx\n" +
    //            "???? ???? ????? ? https://goo.gl/CKKRBV\n" +
    //            "Instagram ? https://goo.gl/eRJ7iq\n\n";
    private var status_text_area: TextArea? = null
    private var channel_id: TextField? = null
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var config_position = 0

    private lateinit var listView: ListView<CFG>
    private lateinit var m: MultipleSelectionModel<CFG>

    private val aaaaaaaaaaaaaaaa = "EXAMPLE_AW"


    var nan: Int = 0


    override fun start(primaryStage: Stage) {
        for (cfg1 in Config.cfgs) {
            println("https://console.cloud.google.com/apis/dashboard?project=" + cfg1.projectName)
        }
        for (cfg1 in Config.cfgs) {
            println(cfg1.client_secret + " " + File(cfg1.client_secret).exists())
        }


        status_text_area = TextArea("DATA")

        val tabPane = TabPane()
        val mView = BorderPane()
        val tab1 = Tab("Канал", mView)
        val tab2 = Tab("Канал")
        val tab3 = Tab("Канал")
        val tab4 = Tab("Канал")
        val tab5 = Tab("Поиск по ключевым словам", SearchBorderPane(this))

        tabPane.tabs.addAll(Tab("UploadVideo", UploadVideoPane(this)), tab1, tab2, tab3, tab4, tab5)
        val box = VBox(tabPane)
        box.children.add(listView())

        val events = arrayOf(
            object : QEvent("Получить список плейлистов") {
                override fun onCreate() {
                    try {
                        youtube = makeYoutube(config_position)
                        val list = youtube!!.playlists().list("snippet")
                        list.setChannelId(CHANNEL_NAME())
                        val aa = list.execute()
                        if (aa != null) {
                            val a = aa.items
                            for (i in a.indices) {
                                updateStatus(i.toString() + "|" + a[i].toString())
                            }
                        }
                    } catch (e: GoogleJsonResponseException) {
                        println("xxxx $e")
                        ++config_position
                        if (config_position < Config.cfgs.size) {
                            m!!.select(config_position)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            },
            object : QEvent("22222222") {
                override fun onCreate() {
                    likeAllVideos()
                }
            }, object : QEvent("liveBroadcasts()") {
                override fun onCreate() {
                    try {
                        val aa = youtube!!.liveBroadcasts().list("id,snippet,contentDetails,status")
                        val response = aa.execute()
                        println(response)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            },
            object : QEvent("TIKTOK UPLOADER") {
                override fun onCreate() {
                    try {
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                        try {
                            //"windows-1251"
                            val file1 = File("D:\\promo\\tiktok\\data.json")
                            val gson = GsonBuilder().create()
                            //BufferedReader reader = Files.newBufferedReader(file1.toPath(), StandardCharsets.UTF_8);
                            val reader = FileReader(file1)
                            val aa =
                                gson.fromJson<List<TikTokVideo>>(reader, object : TypeToken<List<TikTokVideo?>?>() {
                                }.type)

                            updateStatus(formatDescription(aa[0].tags!![0]))
                            execute0(aa)
                            //rrr(aa);
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                        //                        }
//                    });
//                    t.start();
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            object : QEvent("Likee UPLOADER") {
                override fun onCreate() {
                    try {
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                        try {
                            //D:\promo\likee\data.json
                            //"windows-1251"
                            val file1 = File("D:\\promo\\likee\\data.json")
                            val gson = GsonBuilder().create()
                            //BufferedReader reader = Files.newBufferedReader(file1.toPath(), StandardCharsets.UTF_8);
                            val reader = FileReader(file1)
                            val aa = gson.fromJson<List<VideoList>>(reader, object : TypeToken<List<VideoList?>?>() {
                            }.type)

                            updateStatus(formatDescription(aa[0].hashtagInfos!!))
                            execute(aa)
                            //rrr(aa);
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                        //                        }
//                    });
//                    t.start();
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )

        val mNodeBottom = FlowPane(5.0, 5.0)
        val mNodeTop = FlowPane(5.0, 5.0)

        channel_id = TextField("UCni6C-l3h7CLQ1MpIQ6LbGg")
        mNodeTop.children.add(channel_id)

        for (entry in events) {
            val b = Button("" + entry.name) //, imageView1);
            //b.setFont(new Font("Verdana", 8));
            mNodeBottom.children.add(b)
            b.onAction = EventHandler { event: ActionEvent? ->
                executor.execute { entry.onCreate() }
            }
        }

        primaryStage.title = "Youtube"

        val scene = Scene(box, 1100.0, 800.0)
        primaryStage.scene = scene
        mView.center = status_text_area
        mView.top = mNodeTop
        mView.bottom = mNodeBottom
        primaryStage.show()
    }

    @Throws(IOException::class)
    private fun rrr(aa: List<TikTokVideo>) {
        for (video in aa) {
            println( /*video.href + " " +*/TTok.main0(
                video
            )
            )
            return
        }
    }

    private fun likeAllVideos() {
        println("1111111111111")
        try {
//            // https://www.youtube.com/watch?v=s9-vNL5oicE
//            //"???? ???? ???";
//            //https://www.youtube.com/channel/UCErYNylQlYYX3piVQocJlWQ
//            //XChannel channel0 = new XChannel("UCErYNylQlYYX3piVQocJlWQ");
//
//
//            //Mops Kalkalich
//            XChannel channel = new XChannel(DONOR_CHANNEL_NAME);
//            //channel.setVideoId("eyOFEbGofeU"); //last video in channel
//            //channel.setText(message[1]);
//            //uploaded_video_arr = new ArrayList<Video>();
//
//            //channel.setUploadPlaylistId("LLawi0CZ8i_T7t9aK8Xefm9w");
//
//
//            //get last video from channel playlist
//            //channel.getLastVideoId();
//            List<PlaylistItem> videoFromPlaylist = channel.gettAllVideoFromPlaylist(
//                    DONOR_CHANNEL_PLAYLIST
//
//            );
//            if(videoFromPlaylist!=null){
//                System.out.println(videoFromPlaylist.toString());
//            }
//            channel.setComments();
//            channel.setLikes();
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var end = false
        while (!end) {
            try {
                likeOne()
                end = true
            } catch (e: Exception) {
                e.printStackTrace()
                config_position++
                if (config_position > Config.cfgs.size) {
                    end = true
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun likeOne() {
        youtube = makeYoutube(config_position)

        //YoutubeRate youtubeRate = new YoutubeRate();
        var nextToken0 = ""
        val search = youtube!!.search().list("id,snippet")
        search.setType("video")
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url),nextPageToken,pageInfo,prevPageToken")
        //search.setFields("items(id)");
        search.setMaxResults(50L)
        //search.setPageToken(nextToken0);
        search.setChannelId(CHANNEL_NAME())
        var total = 0
        while (nextToken0 != null) {
            if (!nextToken0.isEmpty()) {
                search.setPageToken(nextToken0)
            }
            val searchResponse = search.execute()
            val aa = searchResponse.items
            total += aa.size

            for (result in aa) {
                //+ Like to video
                //youtubeRate.rate(result.getId().getVideoId(), "like");
//                    YouTube.Videos.Rate task = youtube.videos().rate(result.getId().getVideoId(), "like");
//                    task.setId(result.getId().getVideoId());
//                    task.setRating("like");
//                    Void mm = task.execute();
//                    if (mm != null) {
//                        System.out.println(mm.toString());
//                    }
                //Comments

                makeNewComments(
                    CHANNEL_NAME(), result.id.videoId,
                    youtube!!
                ) //IOException
                updateStatus("PLAYLIST ID __>" + result.id.videoId + "\t" + result.snippet.title)
            }
            nextToken0 = searchResponse.nextPageToken
            updateStatus("TOKEN-->$nextToken0")
        }
        updateStatus("Всего видео: $total")
    }

    @Throws(IOException::class)
    private fun makeYoutube(config_position: Int): YouTube {
        val scopes: List<String> = Lists.newArrayList(
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/youtube.force-ssl",
            "https://www.googleapis.com/auth/youtubepartner",
            "https://www.googleapis.com/auth/youtube.upload",
            "https://www.googleapis.com/auth/youtube.readonly"
        )

        val cfg = Config.cfgs[config_position]
        val credential = authorize2(
            scopes, cfg.credentialDataStoreName + "a",
            System.getProperty("user.home") + "/" + "." + cfg.credentialDataStoreName + "a",
            cfg.client_secret
        )
        // This object is used to make YouTube Data API requests.
        return YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
            "youtube-cmdline-uploadvideo-sample$nan"
        ).build()
    }


    @Throws(Exception::class)
    private fun execute(aa: List<VideoList>) {
        //search video by keywords, extract tags...
        //Map<String, Integer> result = new HashMap<>();
        //Search search = new Search(keywords);
//        for (String key : keywords) {
//            search.grabbSearch(key).forEach(searchResult -> {
//                //prettyPrint(searchResultList.iterator(), queryTerm);
//                StringBuilder sb = new StringBuilder();
//                search.grabbMetaTags(searchResult, result, sb);
//
//            });
//        }

        //Map<String, Integer> searchResult = search.tagsMap;

        //Set<String> uniqueWords = new HashSet<String>(/*Arrays.asList(array)*/array);
        //System.out.println("### " + uniqueWords);

        //Map<String, String> myMap = new HashMap<String, String>();


        //  printHeader(new PrettyPrintingMap<>(result));

//        List<Integer> rand = null;
//        List<String> list = null;
//        if (result.size() > 0) {
//            list = new ArrayList<>(result.keySet());
//            rand = new Randomizer().getRandomArray(list.size(), 25);
//        }D:\promo\tiktok


        val VIDEO_FOLDER = "D:\\promo\\likee"

        //======================================================

        //final File folder = new File(VIDEO_FOLDER);
        //File[] files = folder.listFiles();

        //UploadVideoManager uploader;

        // Total 6 video -> one key
        var file: File
        val index = 0 // Кого узнали ? И кого тут не хватает ?

        for (i in index..<index + 999) {
            if (nan > 5) {
                nan = 0
                config_position++
                println("===============================")

                if (config_position > Config.cfgs.size - 1) {
                    println("GAME_OVER")
                    return
                }
            }
            updateStatus("### config # [$config_position] [video # $i] ##")

            if (nan == 0) {
                println("init")
                //for (int i = 0; i < cfgs.length; i++) {
                val cfg = Config.cfgs[config_position]
                val credential = authorize2(
                    scopes,
                    cfg.credentialDataStoreName,
                    System.getProperty("user.home") + "/" + "." + cfg.credentialDataStoreName,
                    cfg.client_secret
                )
                // This object is used to make YouTube Data API requests.
                youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample$nan"
                ).build()

                //}
            }
            nan++

            val obj = aa[i]
            file = File(VIDEO_FOLDER + "\\" + obj.postId + ".mp4")
            if (!file.exists() || file.isDirectory) {
                //listFilesForFolder(fileEntry);
                println(file.absolutePath + " - not exist!")
                //break;
                return
            } else {
                //Tags
                val buffer = StringBuilder()
                val allTags = ArrayList<String?>()
                allTags.add(obj.nickname)
                allTags.add(obj.msgText)
                allTags.add(obj.sound!!.ownerName)

                val set: MutableSet<String> = HashSet()
                set.add("likee скачать видео")
                set.add("Video Downloader For Likee")
                set.add("скачать видео с лайка по ссылке")
                set.add("likee видео")
                set.add("скачать likee")
                set.add("скачать видео с лайка без водяного знака")
                set.add("скачать видео с likee")
                set.add("likee сайт")
                set.add("скачать видео из лайка посылки")
                set.add("скачать likee взлом")
                set.add("скачать like на пк")
                set.add("like")
                set.add("likee")
                set.add("скачать likee бесплатно")

                //set.add("");
                //set.add("");
                for (tag in set /*obj.allTagsobj.msgText.split(" ")*/) {
                    val rr = tag.trim { it <= ' ' }
                    if (rr.length > 0) {
                        if (("$buffer$rr,").length < 500) {
                            allTags.add(rr)
                            buffer.append(rr).append(",")
                        }
                    }
                }

                // Add extra information to the video before uploading.
                val videoObjectDefiningMetadata = Video()

                // Set the video to be publicly visible. This is the default
                // setting. Other supporting settings are "unlisted" and "private."
                val status = VideoStatus()
                status.setPrivacyStatus("public")
                videoObjectDefiningMetadata.setStatus(status)

                // Most of the video's metadata is set on the VideoSnippet object.
                val snippet = VideoSnippet()
                //videoCategories.list
                snippet.setCategoryId("23")


                //                StringBuilder description = new StringBuilder(getDescription());
//
//                if (uploaded_video_arr.size() > 0) {
//                    List<Integer> c = new Randomizer().getRandomArray(uploaded_video_arr.size(), 10);
//                    System.out.println("=====================" + c.toString());
//
//                    for (int randIndx : c) {
//                        Video current = uploaded_video_arr.get(randIndx);
//                        description
//                                .append("\n")
//                                .append(current.getSnippet().getTitle())
//                                .append(" - https://www.youtube.com/watch?v=")
//                                .append(current.getId());
//                    }
//                }
                val number = i + 1
                var _ttt = obj.title!!.replace("\n\n", "\n")
                    .replace("\n", " ")
                    .replace("  ", "")
                if (_ttt.isEmpty()) {
                    _ttt = "(" + obj.nickname + ") " + obj.msgText
                    if (_ttt.length > 100) {
                        _ttt = _ttt.substring(0, 100)
                    }
                }

                snippet.setDescription(formatDescription("(" + obj.nickname + ") " + obj.msgText))
                snippet.setTags(allTags)
                snippet.setTitle(_ttt)


                try {
                    kkkk(snippet, videoObjectDefiningMetadata, file)
                } catch (r: Exception) {
                    r.printStackTrace()
                }


                //System.out.println("@" + snippet.getTags().toString().replace(" ","").length());

//                snippet.setDescription("123");
//                snippet.setTags(Collections.singletonList("123"));
//                snippet.setTitle("123");

//                try {
//                    uploader = UploadVideoManager.newInstance(new UploadVideoManager.Callback() {
//                        @Override
//                        public String getVideoFilePath() {
//                            return VIDEO_FOLDER;
//                        }
//
//                        @Override
//                        public String getVideoFileName() {
//                            return file.getName();
//                        }
//
//                        @Override
//                        public void uploadCompleted() {
//                            System.out.println("UPLOAD_COMPLETE");
//                        }
//                    });
//                    Video returnedVideo = uploader.upload(snippet);
//                System.out.println(returnedVideo.toString());
//                    if (returnedVideo != null) {
//                        uploaded_video_arr.add(returnedVideo); //Saved upload video result
//                        //CVideo.printUploadResult(returnedVideo);
//
//                    } else {
//                        System.out.println("========================================");
//                        System.out.println("Error! === " + snippet.getDescription());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("@@@@" + _ttt);
//                }
            }
        }
    }

    @Throws(Exception::class)
    private fun execute0(aa: List<TikTokVideo>) {
        //=========================================================

        val keywords = arrayOf(
            "MOPS "
        )


        //search video by keywords, extract tags...
        //Map<String, Integer> result = new HashMap<>();
        //Search search = new Search(keywords);
//        for (String key : keywords) {
//            search.grabbSearch(key).forEach(searchResult -> {
//                //prettyPrint(searchResultList.iterator(), queryTerm);
//                StringBuilder sb = new StringBuilder();
//                search.grabbMetaTags(searchResult, result, sb);
//
//            });
//        }

        //Map<String, Integer> searchResult = search.tagsMap;

        //Set<String> uniqueWords = new HashSet<String>(/*Arrays.asList(array)*/array);
        //System.out.println("### " + uniqueWords);

        //Map<String, String> myMap = new HashMap<String, String>();


        //  printHeader(new PrettyPrintingMap<>(result));

//        List<Integer> rand = null;
//        List<String> list = null;
//        if (result.size() > 0) {
//            list = new ArrayList<>(result.keySet());
//            rand = new Randomizer().getRandomArray(list.size(), 25);
//        }D:\promo\tiktok
        val VIDEO_FOLDER = "D:\\promo\\tiktok\\mp4"

        //String VIDEO_FOLDER = "F:\\Video\\tiktok\\mp4\\blur";

        //======================================================

        //final File folder = new File(VIDEO_FOLDER);
        //File[] files = folder.listFiles();

        //UploadVideoManager uploader;

        // Total 6 video -> one key
        var file: File
        val index = 0 // Кого узнали ? И кого тут не хватает ?

        for (i in index..<index + 999) {
            if (nan > 5) {
                nan = 0
                config_position++
                println("===============================")

                if (config_position > Config.cfgs.size - 1) {
                    println("GAME_OVER")
                    return
                }
            }
            println("##################### [$config_position] [$i]")


            if (nan == 0) {
                println("init")
                //for (int i = 0; i < cfgs.length; i++) {
                val cfg = Config.cfgs[config_position]
                val credential = authorize2(
                    scopes,
                    cfg.credentialDataStoreName,
                    System.getProperty("user.home") + "/" + "." + cfg.credentialDataStoreName,
                    cfg.client_secret
                )
                // This object is used to make YouTube Data API requests.
                youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample$nan"
                ).build()

                //}
            }
            nan++

            val obj = aa[i]
            file = File(VIDEO_FOLDER + "\\" + obj.id + ".mp4")
            if (!file.exists() || file.isDirectory) {
                //listFilesForFolder(fileEntry);
                println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                //break;
                return
            } else {
                //Tags


                val buffer = StringBuilder()
                val tags = ArrayList<String>()
                for (tag in obj.tags!!) {
                    val rr = tag.trim { it <= ' ' }
                    if (rr.length > 0) {
                        if (("$buffer$rr,").length < 500) {
                            tags.add(rr)
                            buffer.append(rr).append(",")
                        }
                    }
                }

                // Add extra information to the video before uploading.
                val videoObjectDefiningMetadata = Video()

                // Set the video to be publicly visible. This is the default
                // setting. Other supporting settings are "unlisted" and "private."
                val status = VideoStatus()
                status.setPrivacyStatus("public")
                videoObjectDefiningMetadata.setStatus(status)

                // Most of the video's metadata is set on the VideoSnippet object.
                val snippet = VideoSnippet()
                //videoCategories.list
                snippet.setCategoryId("23")


                //                StringBuilder description = new StringBuilder(getDescription());
//
//                if (uploaded_video_arr.size() > 0) {
//                    List<Integer> c = new Randomizer().getRandomArray(uploaded_video_arr.size(), 10);
//                    System.out.println("=====================" + c.toString());
//
//                    for (int randIndx : c) {
//                        Video current = uploaded_video_arr.get(randIndx);
//                        description
//                                .append("\n")
//                                .append(current.getSnippet().getTitle())
//                                .append(" - https://www.youtube.com/watch?v=")
//                                .append(current.getId());
//                    }
//                }
                val number = i + 1
                var _ttt = obj.title!!.replace("\n\n", "\n")
                    .replace("\n", " ")
                    .replace("  ", "")

                if (_ttt.isEmpty()) {
                    _ttt = "(" + obj.nickname + ") " + obj.description
                    if (_ttt.length > 100) {
                        _ttt = _ttt.substring(0, 100)
                    }
                }

                snippet.setDescription(formatDescription(obj.description!!))
                snippet.setTags(tags)
                snippet.setTitle(_ttt)


                try {
                    kkkk(snippet, videoObjectDefiningMetadata, file)
                } catch (r: Exception) {
                    r.printStackTrace()
                }


                //System.out.println("@" + snippet.getTags().toString().replace(" ","").length());

//                snippet.setDescription("123");
//                snippet.setTags(Collections.singletonList("123"));
//                snippet.setTitle("123");

//                try {
//                    uploader = UploadVideoManager.newInstance(new UploadVideoManager.Callback() {
//                        @Override
//                        public String getVideoFilePath() {
//                            return VIDEO_FOLDER;
//                        }
//
//                        @Override
//                        public String getVideoFileName() {
//                            return file.getName();
//                        }
//
//                        @Override
//                        public void uploadCompleted() {
//                            System.out.println("UPLOAD_COMPLETE");
//                        }
//                    });
//                    Video returnedVideo = uploader.upload(snippet);
//                System.out.println(returnedVideo.toString());
//                    if (returnedVideo != null) {
//                        uploaded_video_arr.add(returnedVideo); //Saved upload video result
//                        //CVideo.printUploadResult(returnedVideo);
//
//                    } else {
//                        System.out.println("========================================");
//                        System.out.println("Error! === " + snippet.getDescription());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("@@@@" + _ttt);
//                }
            }
        }
    }

    @Throws(IOException::class)
    private fun kkkk(snippet: VideoSnippet, videoObjectDefiningMetadata: Video, file: File) {
        videoObjectDefiningMetadata.setSnippet(snippet)

        val mediaContent = InputStreamContent(
            VIDEO_FILE_FORMAT,  //getResourceAsStream("/sample-video.mp4")
            FileInputStream(file)
        )

        // Insert the video. The command sends three arguments. The first
        // specifies which information the API request is setting and which
        // information the API response should return. The second argument
        // is the video resource that contains metadata about the new video.
        // The third argument is the actual video content.
        val videoInsert = youtube!!.videos()
            .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent)

        // Set the upload type and add an event listener.
        val uploader = videoInsert.mediaHttpUploader

        // Indicate whether direct media upload is enabled. A value of
        // "True" indicates that direct media upload is enabled and that
        // the entire media content will be uploaded in a single request.
        // A value of "False," which is the default, indicates that the
        // request will use the resumable media upload protocol, which
        // supports the ability to resume an upload operation after a
        // network interruption or other transmission failure, saving
        // time and bandwidth in the event of network failures.
        uploader.setDirectUploadEnabled(false)
        uploader.setProgressListener(this)

        // Call the API and upload the video.
        val returnedVideo = videoInsert.execute()

        updateStatus("\n[+] Returned Video ==================\n")
        updateStatus("https://www.youtube.com/watch?v=" + returnedVideo.id)
        updateStatus("- Title: " + returnedVideo.snippet.title + "\tTags: " + returnedVideo.snippet.tags)
        updateStatus("- Privacy Status: " + returnedVideo.status.privacyStatus + "- Video Count: " + returnedVideo.statistics.viewCount)
    }

    private fun formatDescription(description: String): String {
        var mm = description
            .replace("  ", "")
            .replace("Шла третья неделя карантина", "")
            .replace("[\\\r\\\n]+".toRegex(), "\n")

        if (mm.length > 5000) {
            mm = mm.substring(0, 4999)
        }
        return mm
    }

    private fun updateStatus(message: String) {
        if (Platform.isFxApplicationThread()) {
            status_text_area!!.appendText(
                """
                    
                    $message
                    """.trimIndent()
            )
        } else {
            Platform.runLater {
                status_text_area!!.appendText(
                    """
                        
                        $message
                        """.trimIndent()
                )
            }
        }
    }

    @Throws(IOException::class)
    override fun progressChanged(uploader: MediaHttpUploader) {
        when (uploader.uploadState) {
            UploadState.INITIATION_STARTED -> println("Initiation Started")
            UploadState.INITIATION_COMPLETE -> println("Initiation Completed")
            UploadState.MEDIA_IN_PROGRESS -> {
                println("Upload in progress")
                println("Upload percentage: " + uploader.progress)
            }

            UploadState.MEDIA_COMPLETE -> println("Upload Completed!")
            UploadState.NOT_STARTED -> println("Upload Not Started!")
        }
    }


    override fun CHANNEL_NAME(): String {
        return channel_id!!.text.trim { it <= ' ' }
    }

    var properties: Properties? = null

    private fun listView(): Node {
        val tr = FXCollections.observableArrayList(*Config.cfgs /*"1", "2", "3"*/)
        listView = ListView(tr)

        listView!!.setPrefSize(240.0, 400.0)
        m = listView!!.selectionModel
        m.selectedItemProperty().addListener { observable: ObservableValue<out CFG>, oldValue: CFG?, newValue: CFG ->
            this.config_position = m.selectedIndex
            cfg = newValue
            try {
                // Read the developer key from the properties file.

                properties = Properties()
                //InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
                properties!!.load(
                    FileInputStream(
                        (cfg.client_secret.replace(
                            "client_secret.json", "youtube.properties"
                        ))
                    )
                )


                val credential = authorize2(
                    Config.scopes,
                    cfg!!.credentialDataStoreName + "amyrate",
                    OAUTH_CRE,
                    cfg!!.client_secret
                )


                //Credential credential = Auth.authorize(Config.scopes, cfg.credentialDataStoreName + "myrate");
                youtube = YouTube.Builder(
                    Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential /*
                    new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}}*/
                )
                    .setApplicationName(aaaaaaaaaaaaaaaa + cfg!!.credentialDataStoreName)
                    .build()
            } catch (e: Exception) {
                println("@@@@@@@@@@@@@@@" + e.message)
            }
        }
        //m.select(0);
        return listView
    }

    fun search(keywords: Array<String>, selected: Boolean) {

        //Result
        var map: HashMap<String, String> = HashMap()

        try {
            val sb = StringBuilder()


            for (word in keywords) {
                if (!word.isEmpty()) {
                    val searchResultList = grabbSearch(word)

                    if (searchResultList != null) {
                        //prettyPrint(searchResultList.iterator(), word.getKey());

                        for (result in searchResultList) {
                            //@@@@@ grabbMetaTags(result, keywords, sb);
                            val resourceId = result.id

                            // Confirm that the result represents a video. Otherwise, the
                            // item will not contain a video ID.
                            if (resourceId.kind == "youtube#video") {
                                val videoId = resourceId.videoId
                                val channelId = result.snippet.channelId
                                map[channelId] = videoId
                                println("----> $videoId\t$channelId")
                            }
                        }
                    }
                }
            }

            val u0 = String(Character.toChars(0x2705))
            val text = sb.toString()

            val numb = 500

            val strings: MutableList<String> = ArrayList()
            var index = 0
            while (index < text.length) {
                strings.add(text.substring(index, Math.min((index + 500).toDouble(), text.length.toDouble()).toInt()))
                index += 500
            }
            for (string in strings) {
                println(string)
                println("\n\n")
            }

            //Set<String> uniqueWords = new HashSet<String>(/*Arrays.asList(array)*/array);
            //System.out.println("### " + uniqueWords);

            //Map<String, String> myMap = new HashMap<String, String>();
            //System.out.println(new PrettyPrintingMap<String, Integer>(tagsMap));
            val log = StringBuilder()

            for ((channel, videoId) in map) {
                log.append("https://www.youtube.com/watch?v=").append(videoId).append("\n")

                if (selected) {
                    makeNewComments(channel, videoId, youtube!!)
                }
            }

            //        }

            //System.out.println("❤️❤️");
            println(log.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun grabbSearch(keyword: String): List<SearchResult> {
        //All data result


        val result: MutableList<SearchResult> = ArrayList()


        try {
        } catch (e: Exception) {
//
//            try {
//               file= new File(
//                        //"../resources/"
//                        "D:\\android\\ANDROID_TUTORIAL\\GUI_GENERATOR_2\\code_generator\\src\\main\\resources\\"
//                                + Config.PROPERTIES_FILENAME);
//               file.createNewFile();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            System.err.println(("There was an error reading " + file!!.absolutePath + ": " + e.cause
//                        + " : " + e.message)
//            )
        }

        try {
            // Prompt the user to enter a query term.
            //String queryTerm = "Ава";//getInputQuery();

            //youtube = makeYoutube(config_position);

            val search = youtube!!.search().list("id,snippet")

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            val apiKey = properties!!.getProperty("youtube.apikey")
            search.setKey(apiKey)
            search.setQ(keyword)

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video")

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields(
                "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/channelId)" +
                        ",pageInfo,nextPageToken,prevPageToken"
            )

            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED)
            var nextToken = ""
            while (nextToken != null) {
                if (!nextToken.isEmpty()) {
                    search.setPageToken(nextToken)
                }
                val response = search.execute()
                val pageInfo = response.pageInfo

                result.addAll(response.items)
                nextToken = response.nextPageToken
            }
            return result
        } catch (e: GoogleJsonResponseException) {
            println(e.localizedMessage)

            //System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
            //        + e.getDetails().getMessage());
        } catch (e: IOException) {
            System.err.println("There was an IO error: " + e.cause + " : " + e.message)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return emptyList()
    }





    companion object {
        private const val NUMBER_OF_VIDEOS_RETURNED: Long = 50

        fun main(args: Array<String>) {
            launch(*args)
        }

        var buggyLogger: Logger = Logger.getLogger(ChannelScrapperImpl::class.java.name)

        init {
            buggyLogger.level = Level.SEVERE
        }


        var youtube: YouTube? = null
        private const val VIDEO_FILE_FORMAT = "video/*"

        //List<Video> uploaded_video_arr;
        private const val DONOR_CHANNEL_PLAYLIST = "PLv6zdiyWRSORYWSVmvfFH0wwm9J23hZIM"





        //List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
        //BroadCastApi
        val scopes: List<String?> = Lists.newArrayList(
            "https://www.googleapis.com/auth/youtube.readonly",
            "https://www.googleapis.com/auth/youtube",
            "https://www.googleapis.com/auth/youtube.force-ssl"

        )
    }
}
