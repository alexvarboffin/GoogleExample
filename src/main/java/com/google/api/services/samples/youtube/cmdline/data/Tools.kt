package com.google.api.services.samples.youtube.cmdline.data

/**
 * Created by combo on 05.12.2016.
 */
object Tools {
    @JvmStatic
    fun print(s: String) {
        println("### $s ###")
    }


    //"Please go to http://www.stackoverflow.com";
    @JvmStatic
    fun fixUrl(originalString: String): String {
        return originalString.replace("https://.+?(com|net|org)/{0,1}".toRegex(), "<a href=\"$0\">$0</a>")
    }
}
