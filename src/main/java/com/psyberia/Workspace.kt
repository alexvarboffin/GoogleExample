package com.psyberia

import java.io.File

object Workspace {

    const val PROJECT_CHRISTIAN_QUOTES = "D:\\walhalla\\QUOTES\\002_ChristianQuotes\\ChristianQuotes"
    const val PROJECT_CHRISTIAN_QUOTES_EN = "D:\\walhalla\\QUOTES\\002_ChristianQuotes\\ChristianQuotesEn"
    const val PROJECT_ISLAMICQUOTES: String = "D:\\walhalla\\QUOTES\\01_QuotesPhrases\\islamicquotes"

//    sealed class Database {
//        object M
//    }

    //================================
    val WORKSPACE = File("G:\\WORKSPACE")
    val database = File(WORKSPACE, "ffmpeg\\database")
    val storage = File("D:\\Temp")

    //================================

    var source: File = File(WORKSPACE, "sources")
    var templates: File = File(WORKSPACE, "/mp4/templates")
    var split_tmp: File = File(WORKSPACE, "tmp_splitter")


}