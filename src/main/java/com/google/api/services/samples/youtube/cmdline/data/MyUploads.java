/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.youtube.cmdline.data;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.collect.Lists;
import com.psyberia.lulzcehtube.MyBlogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.api.services.samples.youtube.cmdline.data.Tools.print;


/**
 * Print a list of videos uploaded to the authenticated user's YouTube channel.
 *
 * @author Jeremy Walker
 */
public class MyUploads {

    private static Post content;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Authorize the user, call the youtube.channels.list method to retrieve
     * the playlist ID for the list of videos uploaded to the user's channel,
     * and then call the youtube.playlistItems.list method to retrieve the
     * list of videos in that playlist.
     *
     * @param args command line args (not used).
     */

    static int counter = 0;

    public static void main(String[] args) {

        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");

        try {
            // Authorize the request.
//            Credential credential = Auth.authorize(scopes, "myuploads");
//
//            // This object is used to make YouTube Data API requests.
//            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
//                    "youtube-cmdline-myuploads-sample").build();

            // Call the API's channels.list method to retrieve the
            // resource that represents the authenticated user's channel.
            // In the API response, only include channel information needed for
            // this use case. The channel's contentDetails part contains
            // playlist IDs relevant to the channel, including the ID for the
            // list that contains videos uploaded to the channel.
            YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");


            channelRequest.setMine(true);


            channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
            ChannelListResponse channelResult = channelRequest.execute();

            List<Channel> channelsList = channelResult.getItems();


            if (channelsList != null) {
                // The user's default channel is the first item in the list.
                // Extract the playlist ID for the channel's videos from the
                // API response.
                String uploadPlaylistId = channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();


                System.out.println("#####" + uploadPlaylistId + "#####");


                // Define a list to store items in the list of uploaded videos.
                List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();


                //https://developers.google.com/youtube/v3/docs/playlistItems
                // Retrieve the playlist of the channel's uploaded videos.
                YouTube.PlaylistItems.List playlistItemRequest = youtube.playlistItems().list("id,contentDetails,snippet");
                playlistItemRequest.setPlaylistId(uploadPlaylistId);


                // Only retrieve data used in this application, thereby making
                // the application more efficient. See:
                // https://developers.google.com/youtube/v3/getting-started#partial
                playlistItemRequest.setFields(
                        "items(contentDetails/videoId" +
                                ",snippet/title,snippet/publishedAt" +
                                ",snippet/description" +
                                //dont have -> ",snippet/tags" +
                                ",etag,status" +
                                ",snippet/thumbnails" +
                                "),nextPageToken,pageInfo");


                //dont work >playlistItemRequest.setPart("snippet");//<-------------- &part=snippet

                String nextToken = "";

                // Call the API one or more times to retrieve all items in the
                // list. As long as the API response returns a nextPageToken,
                // there are still more items to retrieve.
                do {
                    playlistItemRequest.setPageToken(nextToken);
                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                    playlistItemList.addAll(playlistItemResult.getItems());

                    nextToken = playlistItemResult.getNextPageToken();
                } while (nextToken != null);


                // Prints information about the results.
                prettyPrint(playlistItemList.size(), playlistItemList.iterator());
            }

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Print information about all of the items in the playlist.
     *
     * @param size size of list
     *
     * @param iterator of Playlist Items from uploaded Playlist
     */
    private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) throws IOException {
        System.out.println("=============================================================");
        System.out.println("\t\tTotal Videos Uploaded: " + size);
        System.out.println("=============================================================\n");

        while (playlistEntries.hasNext()) {
            PlaylistItem playlistItem = playlistEntries.next();


            print(counter++ + "\n");

            //System.out.println("RAW DATA: "+playlistItem);


            //================================================================
            String id = playlistItem.getContentDetails().getVideoId();
            String title = playlistItem.getSnippet().getTitle();
            String description = playlistItem.getSnippet().getDescription().replaceAll("\n", "<br/>");
            description = Tools.fixUrl(description);
            //================================================================

            print(" Video id: " + id);
            print(" Video name  = " + title);

            //System.out.println(" video id    = " + playlistItem.getSnippet().getT);

            print(" upload date = " + playlistItem.getSnippet().getPublishedAt());
            print(" description: = " + description);


            //Thumbinails: = {"default":{"height":90,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/default.jpg","width":120},
            // "high":{"height":360,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/hqdefault.jpg","width":480},
            // "medium":{"height":180,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/mqdefault.jpg","width":320}}

            ThumbnailDetails thubnails = playlistItem.getSnippet().getThumbnails();

            if (null != thubnails) {
                System.out.println(" Thumbinails: = " + thubnails.getHigh().getUrl());//.getMaxres().getUrl()
            }


            print(" E-tag = " + playlistItem.getEtag());
            print(" Status = " + playlistItem.getStatus());

            print("\n-------------------------------------------------------------\n");

            VideoSnippet info = MyVideos.getAllVideo0(id).getItems().get(0).getSnippet();

            // List<Video> lists = getAllPlaylist();

            //  for (Video current: lists) {
            //      current.getSnippet();
            //  }


            //  print(info.toString());


//----------------------------------------------------------------------------------------------------
            content = new Post();
            content.setTitle(title);
            content.setContent("<div style=\"text-align: center;\">\n" +
                    "<br /></div>\n" +
                    "<div style=\"text-align: center;\">\n" +
                    "<iframe allowfullscreen=\"true\" frameborder=\"0\" height=\"470\" src=\"https://www.youtube.com/embed/"
                    + id
                    + "\" width=\"100%\"></iframe></div>\n" +
                    "<div style=\"text-align: left;\">\n" +
                    "<br /></div>\n" +
                    "<div style=\"text-align: left;\">\n" +
                    description + "</div>");


            //https://www.youtube.com/watch?v=SkI0NVj_1l4

            Post.Author author = new Post.Author();
            author.setDisplayName("][asper");

            content.setAuthor(author);
            content.setCustomMetaData("00000000000000000000000000000,888888888888");


            List<Post.Images> images = new ArrayList<Post.Images>();


            //java.util.List<Images> images
            Post.Images img1 = new Post.Images();
            img1.setUrl("http://d.stockcharts.com/img/articles/2014/12/14181400432571017853286.png");

            Post.Images img2 = new Post.Images();
            if (null != thubnails) {
                img2.setUrl(thubnails.getHigh().getUrl());
                images.add(img2);
            }

            images.add(img1);
            content.setImages(images);


            //List<String> labels = new ArrayList<>();

            //Ярлыки
            //labels.add("bbbbbbbbbbbbbbbb");
            //labels.add("cccccccccccccccccccccccc");
            content.setLabels(info.getTags());
            //-------------------------------------------------------------------------------------------
      //      new MyBlogger(content, GRABBERZ_BLOG_ID).execute();


        }
    }
}
