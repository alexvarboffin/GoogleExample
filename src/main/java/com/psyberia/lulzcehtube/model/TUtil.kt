package com.psyberia.lulzcehtube.model

import org.jsoup.Jsoup
import java.io.IOException

object TUtil {
    var rrr: String = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:28.0) Gecko/20100101 Firefox/28.0"
    const val TIMEOUT: Int = 15 * 1000

    fun getKey(contentURL: String): String {
        var key: String = ""
        try {
            val headers: MutableMap<String, String> = HashMap()
            headers["Accept"] =
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
            headers["Accept-Encoding"] = "gzip, deflate, br"
            headers["Accept-Language"] = "en-US,en;q=0.9"
            headers["Range"] = "bytes=0-200000"
            headers["User-Agent"] = rrr
            val doc = Jsoup.connect(contentURL)
                .timeout(TIMEOUT) //.userAgent(rrr)
                .ignoreContentType(true) //.headers(headers)
                .followRedirects(true)
                .get()
            val data = doc.toString()
            val tmp = data.split("vid:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //Log.d(TAG, "getKey: " + data);
            if (tmp.size > 1) {
                key = tmp[1].split("%".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
            }
            return key
        } catch (e: IOException) {
            //DLog.handleException(e);
        }
        return key
    }
}
