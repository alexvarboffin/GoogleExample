package com.psyberia.outh2

import java.util.*

object YoutubeUtils {
    private const val yt_share_description = "%1\$s\n" +
            "\n" +
            "Download our \"Christian Quotes\" app\n" +
            "\uD83C\uDF1F \"Christian Quotes\" \uD83C\uDF1F\n" +
            "\uD83D\uDD17 Google Play: https://bit.ly/3SlsHsc\n" +
            "\n" +
            "%2\$s\n" +
            "\n" +
            "Your Queries:\n" +
            "%3\$s"

    //    public static String generateTitle(File file) {
    //        String simpleTitle = file.getName();
    //        String clearTmpl = simpleTitle
    //                .replace("__", "/");
    //        return clearTmpl.split("_")[1];
    //    }
    private fun format1(qq: List<String?>): String {
        val xx = java.lang.String.join(", ", qq).replace("#", "")
        val m1 = TextUtilz.divideString(xx, 10)
        return java.lang.String.join("\n", m1).trim { it <= ' ' } + "."
    }

    fun generateDescriptionFromTemplate(tags: List<String>, simpleTitle: String): String {
        val tagText = TextUtilz.selectAndShuffleTags(tags, 450)
        val mm = tagText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val qq = Arrays.asList(*mm)
        Collections.shuffle(qq)
        val xx = format1(qq)

        val m1 = TextUtilz.divideString(tagText, 10)

        val tagzz = java.lang.String.join("\n", m1)
        val txt = String.format(yt_share_description, simpleTitle, tagzz, xx)
        return txt
    }
}
