package com.psyberia.lulzcehtube

import java.util.*

object Utils {
    @JvmStatic
    fun generateMessage(): String {
        val aa = arrayOf<String?>(
            "Tik Tok|TikT❤️k|TikTok|TikT0k|TikT❤️k|TikT0k",
            "Musically",
            "Facebook",
            "Instagram",
            "Likee or Like"
        )
        for (i in aa.indices) {
            if (aa[i]!!.contains("|")) {
                val aaa = aa[i]!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val strList = Arrays.asList(*aaa)
                Collections.shuffle(strList)
                val firstName = strList.toTypedArray<String?>()[0]
                aa[i] = firstName
            }
        }

        val aaa: MutableList<String?> = ArrayList(aa.size)
        aaa.addAll(Arrays.asList(*aa))

        Collections.shuffle(aaa)

        val m123 = arrayOf(
            arrayOf(
                //                        "DOWNLOAD",
                //                        "Download",
                //                        "Download",
                //                        "Download...,"
                "\uD83D\uDD25\uD83D\uDD25",
                "\uD83D\uDC4D\uD83D\uDC4D",
                "\uD83D\uDC97\uD83D\uDC97",
                "\uD83D\uDE10\uD83D\uDE10"
            ),
            arrayOf(
                "Best Ways to Download @@@ without Watermark",
                "Best Ways to Download @@@ without Watermark 2❤️2❤️",
                "How to Download @@@ Videos Without Watermark",
                "@@@ Video Without Watermark 2020 ANDROID",
                "Any video in @@@ without a watermark 2❤️2❤️",
                "@@@ Video Without Watermark 2❤️2❤️ Android",
                "Any video in @@@ without a watermark",
                "@@@ Vide0 Without Watermark 2o2o ANDROID",
                "@@@ Vide0 Without Watermark 2❤️2❤️ ANDROID",
                "Any video in @@@ without a Watermark",
            ),
            arrayOf(
                "https://www.youtube.com/watch?v=8YtJXUKPQYc" //likee youtube
                //"https://www.youtube.com/watch?v=edT7x28YnLQ"
                //                                "https://ttvloader.web.app/",
                //                                "https://play.google.com/store/apps/details?id=com.walhalla.ttloader#"

            ),
            arrayOf(
                "\uD83D\uDD25\uD83D\uDD25",
                "\uD83D\uDC4D\uD83D\uDC4D",
                "\uD83D\uDC97\uD83D\uDC97",
                "\uD83D\uDE10\uD83D\uDE10",
                "\uD83D\uDE0D\uD83D\uDE0D"
            )
        )

        //        Download: market://details?id=com.walhalla.ttloader
//        Download: https://play.google.com/store/apps/details?id=com.walhalla.ttloader
        val r = Random()
        //            int i = r.nextInt(1);
//            for (int i1 = 0; i1 < 100; i1++) {
//        for (String s : aaa) {
//
//        }
        val join = java.lang.String.join(", ", aaa)

        val s1 = m123[0][r.nextInt(m123[0].size)]
        val s2 = m123[1][r.nextInt(m123[1].size)].replace("@@@", join)
        val url = m123[2][r.nextInt(m123[2].size)]
        val s4 = m123[3][r.nextInt(m123[3].size)]

        //System.out.println(message);
        return """$s1 $s2  $s4
$url#${System.currentTimeMillis()}"""
    }
}
