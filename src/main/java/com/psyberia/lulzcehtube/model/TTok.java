package com.psyberia.lulzcehtube.model;

import org.apache.http.util.TextUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TTok {


    private static String rrr = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:28.0) Gecko/20100101 Firefox/28.0";
    private static final int TIMEOUT = 90000;

    public static String main0(TikTokVideo video) throws IOException {
        TTResponse response = new TTResponse();
        Document doc = Jsoup.connect(
        //        "https://www.tiktok.com/@nadyadorofeeva/video/6803993696493686022"
                        video.getHref()
        )
                .timeout(TIMEOUT)
                .userAgent(rrr)
                .ignoreContentType(true)
                .get();


        //New features
        String resp = doc.toString();
        String[] check = resp.split("\"contentUrl\":\"");
        if (check.length > 1) {
            response.contentURL = check[1].split("\"")[0];
            String buff = resp.split("\"thumbnailUrl\":\\[\"")[1];
            response.thumb = buff.split("\"")[0];

            String aaaa = resp.split("\"url\":\"")[1];
            String ccc = aaaa.split("\"")[0];
            String hhh = ccc.split("@")[1];
            response.username = hhh.split("/")[0];
            response.videoKey = getKey(response.contentURL);

            System.out.println("@@@@@@@@@@@@@" + response.username);
            System.out.println("@@@@@@@@@@@@@" + response.thumb);
            System.out.println("@@@@@@@@@@@@@" + response.videoKey);


            response.title = doc.title();
            if (response.videoKey != null) {
                response.cleanVideo = "https://api2.musical.ly/aweme/v1/playwm/?video_id=" + response.videoKey;
            }

            try {
                //DLog.d(result.toString());
                //@@@String target = result.select("link[rel=\"canonical\"]").last().attr("href");
                String target = response.cleanVideo;

                if (!TextUtils.isEmpty(target)) {
                    //@@@target = target.split("video/")[1];
                    return target;
                } else {
                    System.out.println("err");
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("err");

            }
        }
        return "NONE";
    }

    private static String getKey(String contentURL) {
        String key = null;
        try {
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//            headers.put("Accept-Encoding", "gzip, deflate, br");
//            headers.put("Accept-Language", "en-US,en;q=0.9");
//            headers.put("Range", "bytes=0-200000");

//            Connection aa = Jsoup.connect(contentURL);
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                aa.header(entry.getKey(), entry.getValue());
//            }
            Document doc = Jsoup.connect(contentURL)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Range", "bytes=0-200000")
                    .header("User-Agent", rrr)
                    .timeout(TIMEOUT)
                    //.userAgent(rrr)
                    .ignoreContentType(true)
                    .followRedirects(true)
                    .get();
            String data = doc.toString();
            String[] tmp = data.split("vid:");
            //Log.d(TAG, "getKey: " + data);
            if (tmp.length > 1) {
                key = tmp[1].split("%")[0].trim();
            }
            return key;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static class TTResponse {

        public String contentURL;
        public String thumb;
        public String username;
        public String videoKey;
        public String cleanVideo;
        public String title;

        @Override
        public String toString() {
            return "TTResponse{" +
                    "contentURL='" + contentURL + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", username='" + username + '\'' +
                    ", videoKey='" + videoKey + '\'' +
                    ", cleanVideo='" + cleanVideo + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

}
