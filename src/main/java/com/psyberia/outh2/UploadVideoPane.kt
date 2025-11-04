package com.psyberia.outh2

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState
import com.google.api.client.http.InputStreamContent
import com.google.api.services.CFG
import com.google.api.services.samples.Config

import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import com.psyberia.Workspace
import com.psyberia.Workspace.PROJECT_CHRISTIAN_QUOTES_EN
import com.psyberia.Workspace.PROJECT_ISLAMICQUOTES
import com.psyberia.Workspace.storage
import com.psyberia.lulzcehtube.project.ChannelScrapperImpl
import com.psyberia.outh2.YoutubeUtils.generateDescriptionFromTemplate
import com.psyberia.utils.limitTagsTo500Chars
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class UploadVideoPane(private val scrapper: ChannelScrapperImpl) : BorderPane() {


    private var youtube0: YouTube? = null

    init {
        val button = Button("Auth single Account")
        button.onAction = EventHandler { event: ActionEvent? ->
            uploadVideo()
        }


        val button1 = Button("Upload Video...")
        button1.onAction = EventHandler { event: ActionEvent? ->
            val m0 = CfgObj(
                //261,
                PROJECT_CHRISTIAN_QUOTES_EN + "\\src\\main\\assets\\titles.txt",
                PROJECT_CHRISTIAN_QUOTES_EN + "\\src\\main\\assets\\tags.txt",
                Workspace.templates.absolutePath,
                File(Workspace.storage, "alexvarboffin@gmail.com\\MP4").absolutePath,
                sql = File(Workspace.database, "christian_en.sqlite").absolutePath
            )
            uploadVideo(m0, Config.cfgs[5])
            val muslimConf = CfgObj(
                //87,
                PROJECT_ISLAMICQUOTES + "\\src\\main\\assets\\titles.txt",
                PROJECT_ISLAMICQUOTES + "\\src\\main\\assets\\tags.txt",

                "G:\\WORKSPACE\\mp4\\muslim",
                File(Workspace.storage, "alexvarboffin@gmail.com\\MP4_islamicquotes").absolutePath,
                "C:\\Users\\combo\\Desktop\\ffmpeg\\database\\islamicquotes.sqlite"
            )
            uploadVideo(muslimConf, Config.cfgs[4])


            uploadVideo(m0, Config.cfgs[6]) //danilabobrov82@gmail.com = Likee
            uploadVideo(muslimConf, Config.cfgs[7]) //danilabobrov82@gmail.com = Inspiring Islamic Quotes
            println("=================================================")
        }
        center = button
        bottom = button1
    }


    private fun uploadVideo(obj: CfgObj, cfg: CFG) {
        println(cfg.toString())
        try {
            val storage = File(obj.outFolder)
            val list = storage.listFiles { pathname: File -> pathname.name.endsWith(".mp4") }


            if (list!!.size == 0) {
                println("STORAGE IS EMPTY: ${storage.absolutePath}")
                return
            }

            val file = list[0]


            var title = TextUtilz.extractTextBetween(file.name).replace("__", "/")

            //            String simpleTitle = YoutubeUtils.generateTitle(file).split("\\.")[0];
//            System.out.println(simpleTitle);
            if (title.length > 100) {
                title = title.replace("video.", "video")
            }
            if (title.length > 100) {
                title = title.replace("#motivational", "#quotes")
            }
            if (title.length > 100) {
                title = title.replace("#quotes", "")
            }
            if (title.length > 100) {
                title = title.replace("#viral", "")
            }
            if (title.length > 100) {
                title = title.replace("#shorts", "")
            }
            if (title.length > 100) {
                title = title.replace("Motivational status video", "")
            }

            if (title.length > 100) {
                title = title.replace(" / ", ": ").trim { it <= ' ' }
            }

            println("[] $title")

            // Информация о видео (заголовок, описание и теги)
            val snippet = VideoSnippet()
            //snippet.setTitle("Test Upload via Java on " + Calendar.getInstance().getTime());
            snippet.setTitle(title)

            //List<String> tags0 = List.of("test", "example", "java", "YouTube Data API V3", "erase me");

            //ЛИМИТ ВСЕХ ТЕГОВ 500 символов, как быть
            val tmp = TextUtilz.readAllLines(obj.tags)
            val tags0: MutableList<String> = ArrayList()
            for (tag in tmp) {
                tags0.add(tag.replace("#", ""))
            }

            val description = """
                ${generateDescriptionFromTemplate(tmp, title)}
                ${Calendar.getInstance().time}
                """.trimIndent()

            val t0 = limitTagsTo500Chars(tags0)
            snippet.setTags(t0)
            snippet.setDescription(description)

            //System.out.println(title + " | " + t0 + " | " + description);
            // Авторизация через OAuth2
            val credential = Auth.authorize2(
                SCOPES,
                cfg.credentialDataStoreName + "amyrate",
                Config.OAUTH_CRE,
                cfg.client_secret
            )

            // Создание объекта YouTube для отправки запросов к API
            val youtube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName("youtube-cmdline-uploadvideo-sample")
                .build()

            // Метаданные видео
            val videoMetadata = Video()

            // Статус видео: "public", "private", "unlisted"
            val status = VideoStatus()
            status.setPrivacyStatus("public")
            videoMetadata.setStatus(status)
            videoMetadata.setSnippet(snippet)

            // Загружаемое видео
            val mediaContent = InputStreamContent(VIDEO_FILE_FORMAT, FileInputStream(file.absolutePath))

            // Вставка видео
            val videoInsert = youtube.videos().insert("snippet,statistics,status", videoMetadata, mediaContent)
            send(videoInsert, file, cfg)
        } catch (e: GoogleJsonResponseException) {
            println("Throwable: " + e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            println("Throwable: " + e.message)
            e.printStackTrace()
        } catch (t: Throwable) {
            println("Throwable: " + t.message)
            t.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun send(videoInsert: YouTube.Videos.Insert, file: File, cfg: CFG) {
        // Настройка загрузчика
        val uploader = videoInsert.mediaHttpUploader
        uploader.setDirectUploadEnabled(false)

        uploader.setProgressListener { uploader1: MediaHttpUploader ->
            when (uploader1.uploadState) {
                UploadState.INITIATION_STARTED -> println("Initiation Started")
                UploadState.INITIATION_COMPLETE -> println("Initiation Completed")
                UploadState.MEDIA_IN_PROGRESS -> println("Upload in progress: " + uploader1.progress * 100 + "%")
                UploadState.MEDIA_COMPLETE -> {
                    println("Upload Completed!..." + file.absolutePath)
                    val removed = file.delete()
                    println("!!! file removed !!! $removed")
                }

                UploadState.NOT_STARTED -> println("Upload Not Started!")
            }
        }

        // Загрузка видео через API
        val returnedVideo = videoInsert.execute()

        // Вывод информации о загруженном видео
        println("\n================== Returned Video ==================")
        println("@@@@@@@$cfg")
        println("  - https://www.youtube.com/watch?v=${returnedVideo.id}")
        println(returnedVideo.snippet.title)
        println("  - Tags: " + returnedVideo.snippet.tags)
        println("[" + returnedVideo.status.privacyStatus + "]  - View Count: [${returnedVideo.statistics.viewCount}]")
    }


    private fun uploadVideo() {
        try {
            val storage = File(storage, "alexvarboffin@gmail.com\\MP4")
            val list = storage.listFiles { pathname: File -> pathname.name.endsWith(".mp4") }


            if (list!!.isEmpty()) {
                println("STORAGE IS EMPTY")
                return
            }

            val file = list[0]


            var title = TextUtilz.extractTextBetween(file.name)
                .replace("__", "/")

            if (title.length > 100) {
                title = title.replace("video.", "video")
            }
            if (title.length > 100) {
                title = title.replace("#motivational", "#quotes")
            }
            if (title.length > 100) {
                title = title.replace("#quotes", "")
            }
            if (title.length > 100) {
                title = title.replace("#viral", "")
            }
            if (title.length > 100) {
                title = title.replace("#shorts", "")
            }
            if (title.length > 100) {
                title = title.replace("Motivational status video", "")
            }

            if (title.length > 100) {
                title = title.replace(" / ", ": ").trim { it <= ' ' }
            }


            //String simpleTitle = YoutubeUtils.generateTitle(file).split("\\.")[0];


            // Информация о видео (заголовок, описание и теги)
            val snippet = VideoSnippet()
            //snippet.setTitle("Test Upload via Java on " + Calendar.getInstance().getTime());
            snippet.setTitle(title)

            //List<String> tags0 = List.of("test", "example", "java", "YouTube Data API V3", "erase me");

            //ЛИМИТ ВСЕХ ТЕГОВ 500 символов, как быть
            val tags =
                TextUtilz.readAllLines( //"D:\\walhalla\\TTDwn\\AndroidStudioSourceCode\\app\\src\\main\\assets\\tags.txt"
                    "$PROJECT_CHRISTIAN_QUOTES_EN\\src\\main\\assets\\tags.txt"

                )
//            List<String> tags0 = new ArrayList<>();
//            for (String tag : tags) {
//                tags0.add(tag.replace("#", ""));
//            }
            val description = generateDescriptionFromTemplate(tags, title)
            val tags0 = limitTagsTo500Chars(tags)

            snippet.setTags(tags0)
            snippet.setDescription(description)

            //System.out.println(title + " | " + tags0 + " | " + description);
            // Авторизация через OAuth2
            val credential = Auth.authorize2(SCOPES, scrapper.cfg.credentialDataStoreName + "amyrate", Config.OAUTH_CRE, scrapper.cfg.client_secret
            )

            // Создание объекта YouTube для отправки запросов к API
            if (youtube0 == null) {
                youtube0 = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName("youtube-cmdline-uploadvideo-sample")
                    .build()
            }

            // Метаданные видео
            val videoMetadata = Video()

            // Статус видео: "public", "private", "unlisted"
            val status = VideoStatus()
            status.setPrivacyStatus("public")
            videoMetadata.setStatus(status)
            videoMetadata.setSnippet(snippet)

            // Загружаемое видео
            val mediaContent = InputStreamContent(
                VIDEO_FILE_FORMAT,
                FileInputStream(file.absolutePath)
            )

            // Вставка видео
            val videoInsert = youtube0!!.videos().insert("snippet,statistics,status", videoMetadata, mediaContent)
            send(videoInsert, file, scrapper.cfg)
        } catch (e: GoogleJsonResponseException) {
            println(
                ("GoogleJsonResponseException code: " + e.details.code + " : "
                        + e.details.message)
            )
            e.printStackTrace()
        } catch (t: Throwable) {
            println("Throwable: " + t.message)
            t.printStackTrace()
        }
    }

    companion object {
        private const val VIDEO_FILE_FORMAT = "video/*"


        val SCOPES: List<String> = listOf("https://www.googleapis.com/auth/youtube.upload")

    }
}
