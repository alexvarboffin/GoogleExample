package com.psyberia.lulzcehtube.model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import com.google.api.services.CFG;
import com.google.api.services.samples.Config;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.collect.Lists;
import com.psyberia.lulzcehtube.YoutubeRate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by combo on 01.09.2016.
 */
public class XChannel {

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private YouTube youtube;
    private String channelId;
    private String videoId;
    private String text;

    public XChannel(String channelName) {
        channelId = channelName;
    }

    public void setVideoId(String var0) {
        videoId = var0;
    }

    public void setText(String var0) {
        text = var0;
    }

    public void setUploadPlaylistId(String var0) {
        uploadPlaylistId = var0;
    }


    //https://www.youtube.com/playlist?list=PLV4xApIh67zEnGib0U4YWNBoE2YTJKDf5
    // The user's default channel is the first item in the list.
    // Extract the playlist ID for the channel's videos from the
    // API response.


    static String uploadPlaylistId;

    //load in string
    //My chanel

    //String channel = "UCmM6mEXyj-T-7TXFu1DwCFw|WIQpM3u1sWA|Test messages";

    //public String[] getConfigs(){
    //    ArrayList channelList = new ArrayList();
    //    String[] tmp = channel.split("|");
    //}


    /*
     * Prompt the user to enter a channel ID. Then return the ID.
     */
    public String getChannelId() throws IOException {

        //System.out.print("Please enter a channel id: ");
        //BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        //channelId = bReader.readLine();


        //if (channelId.length() < 1) {
        // If nothing is entered, defaults to "YouTube For Developers."
        //    channelId = "UCtVd0c0tGXuTSbU5d8cSBUg";
        //}

        return channelId;
    }

    /*
     * Prompt the user to enter a video ID. Then return the ID.
     */
    public String getVideoId() throws IOException {

        //System.out.print("Please enter a video id: ");
        //BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        //videoId = bReader.readLine();

        return videoId;
    }

    /*
     * Prompt the user to enter text for a comment. Then return the text.
     */
    public String getText() throws IOException {

        //System.out.print("Please enter a comment text: ");
        //BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        //text = bReader.readLine();

        if (text.length() < 1) {
            // If nothing is entered, defaults to "YouTube For Developers."
            text = "YouTube For Developers.";
        }
        return text;
    }

    public String getLastVideoId() {
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");

        try {
//            // Authorize the request.
//            Credential credential = Auth.authorize(scopes, "myuploads");
//
//            // This object is used to make YouTube Data API requests.
//            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
//                    "youtube-cmdline-myuploads-sample").build();


            // Define a list to store items in the list of uploaded videos.
            List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

            // Retrieve the playlist of the channel's uploaded videos.
            YouTube.PlaylistItems.List playlistItemRequest
                    = youtube.playlistItems().list("id,contentDetails,snippet");


            //-          playlistItemRequest.setPlaylistId(uploadPlaylistId);
            //playlistItemRequest.setMaxResults((long) 3);


            // Only retrieve data used in this application, thereby making
            // the application more efficient. See:
            // https://developers.google.com/youtube/v3/getting-started#partial

            System.out.println(uploadPlaylistId);

            playlistItemRequest.setPlaylistId(uploadPlaylistId);
            playlistItemRequest.setFields(
                    "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");


            //One Query
            playlistItemRequest.setMaxResults(1L);
            PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
            playlistItemList.addAll(playlistItemResult.getItems());


            // Prints information about the results.
            prettyPrint(playlistItemList.size(), playlistItemList.iterator());

            return playlistItemList.get(0).getContentDetails().getVideoId();

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }


    public List<PlaylistItem> gettAllVideoFromPlaylist(String playlistId) {
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList(
                "https://www.googleapis.com/auth/youtube",
                "https://www.googleapis.com/auth/youtube.force-ssl",
                "https://www.googleapis.com/auth/youtubepartner",
                "https://www.googleapis.com/auth/youtube.upload",
                "https://www.googleapis.com/auth/youtube.readonly"
        );

        try {
            CFG cfg = Config.cfgs[1];
            Credential credential = Auth.authorize2(scopes,
                    cfg.getCredentialDataStoreName(),
                    System.getProperty("user.home") + "/" + "." + cfg.getCredentialDataStoreName(),
                    cfg.getClient_secret()
            );
            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample" + 1).build();
            // Authorize the request.
//            Credential credential = Auth.authorize(scopes, "myuploads");
//
//            // This object is used to make YouTube Data API requests.
//            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
//                    "youtube-cmdline-myuploads-sample").build();


            // Define a list to store items in the list of uploaded videos.
            List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

            // Retrieve the playlist of the channel's uploaded videos.
            YouTube.PlaylistItems.List playlistItemRequest
                    = youtube.playlistItems().list("id,contentDetails,snippet");

            playlistItemRequest.setPlaylistId(playlistId);
            //playlistItemRequest.setMaxResults((long) 3);


            // Only retrieve data used in this application, thereby making
            // the application more efficient. See:
            // https://developers.google.com/youtube/v3/getting-started#partial
            playlistItemRequest.setFields(
                    "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");


            //One Query
            //playlistItemRequest.setMaxResults(Long.valueOf(1));
            PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
            playlistItemList.addAll(playlistItemResult.getItems());


            // Prints information about the results.
            prettyPrint(playlistItemList.size(), playlistItemList.iterator());

            return playlistItemList;

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /*
     * Print information about all of the items in the playlist.
     *
     * @param size size of list
     *
     * @param iterator of Playlist Items from uploaded Playlist
     */
    private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
        System.out.println("=============================================================");
        System.out.println("\t\tTotal Videos Uploaded: " + size);
        System.out.println("=============================================================\n");

        while (playlistEntries.hasNext()) {
            PlaylistItem playlistItem = playlistEntries.next();
            System.out.println(" video name  = " + playlistItem.getSnippet().getTitle());
            System.out.println(" video id    = " + playlistItem.getContentDetails().getVideoId());
            System.out.println(" upload date = " + playlistItem.getSnippet().getPublishedAt());
            System.out.println("\n-------------------------------------------------------------\n");
        }
    }

    public void setComments() {
        //System.out.println(lastVideo[0] + ":" + tmp);

        // This OAuth 2.0 access scope allows for full read/write access to the
        // authenticated user's account and requires requests to use an SSL connection.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");

        try {
            // Authorize the request.
//            Credential credential = Auth.authorize(scopes, "commentthreads");
//
//            // This object is used to make YouTube Data API requests.
//            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
//                    .setApplicationName("youtube-cmdline-commentthreads-sample").build();


            //###########################################################


            // Prompt the user for the comment text.
            // Retrieve the text that the user is commenting.
            String text = getText();
            //System.out.println("You chose " + text + " to subscribe.");


            // Insert channel comment by omitting videoId.
            // Create a comment snippet with text.
            CommentSnippet commentSnippet = new CommentSnippet();
            commentSnippet.setTextOriginal(text);

            // Create a top-level comment with snippet.
            Comment topLevelComment = new Comment();
            topLevelComment.setSnippet(commentSnippet);

            // Create a comment thread snippet with channelId and top-level
            // comment.
            CommentThreadSnippet commentThreadSnippet = new CommentThreadSnippet();
            commentThreadSnippet.setChannelId(channelId);
            commentThreadSnippet.setTopLevelComment(topLevelComment);

            // Create a comment thread with snippet.
            CommentThread commentThread = new CommentThread();
            commentThread.setSnippet(commentThreadSnippet);

            // Call the YouTube Data API's commentThreads.insert method to
            // create a comment.
            CommentThread channelCommentInsertResponse = youtube.commentThreads()
                    .insert("snippet", commentThread).execute();


            // Print information from the API response.
            System.out.println("\n================== Created Channel Comment ==================\n");
            CommentSnippet snippet = channelCommentInsertResponse.getSnippet().getTopLevelComment()
                    .getSnippet();
            System.out.println("  - Author: " + snippet.getAuthorDisplayName());
            System.out.println("  - Comment: " + snippet.getTextDisplay());
            System.out
                    .println("\n-------------------------------------------------------------\n");


            // Insert video comment
            commentThreadSnippet.setVideoId(videoId);


            //Узнаем количество комментариев под видео
            // Call the YouTube Data API's commentThreads.list method to
            // retrieve video comment threads.
            CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads()
                    .list("snippet").setVideoId(videoId).setTextFormat("plainText").execute();
            List<CommentThread> videoComments = videoCommentsListResponse.getItems();
            int commCount = videoComments.size();
            System.out.println("TEST VIDEO COMMENT THREAD SIZE: " + commCount);


            if (commCount < 10) {
                //    for (int i = 0; i < (10 - commCount); i++) {
                // Call the YouTube Data API's commentThreads.insert method to
                // create a comment.
                CommentThread videoCommentInsertResponse = youtube.commentThreads()
                        .insert("snippet", commentThread).execute();

                // Print information from the API response.
                System.out.println("\n================== Created Video Comment ==================\n");
                snippet = videoCommentInsertResponse.getSnippet().getTopLevelComment().getSnippet();
                System.out.println("  - Author: " + snippet.getAuthorDisplayName());
                System.out.println("  - Comment: " + snippet.getTextDisplay());
                System.out.println("\n-------------------------------------------------------------\n");
                //    }
            } else System.out.println("Слишком много комментариев...");

        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode()
                    + " : " + e.getDetails().getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public void setLikes() throws IOException {
        YoutubeRate rates = new YoutubeRate();
        rates.rate(getVideoId(), "like");
    }
}
