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
package com.google.api.services.samples.youtube.cmdline.data

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.blogger.model.Post.Images
import com.google.api.services.samples.youtube.cmdline.data.MyVideos.getAllVideo0
import com.google.api.services.samples.youtube.cmdline.data.Tools.fixUrl
import com.google.api.services.samples.youtube.cmdline.data.Tools.print
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ChannelListResponse
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.PlaylistItemListResponse
import com.google.api.services.youtube.model.ThumbnailDetails
import com.google.api.services.youtube.model.VideoSnippet
import com.google.common.collect.Lists
import java.io.IOException

/**
 * Print a list of videos uploaded to the authenticated user's YouTube channel.
 *
 * @author Jeremy Walker
 */
object MyUploads {
    private var content: com.google.api.services.blogger.model.Post? = null

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private val youtube: YouTube? = null

    /**
     * Authorize the user, call the youtube.channels.list method to retrieve
     * the playlist ID for the list of videos uploaded to the user's channel,
     * and then call the youtube.playlistItems.list method to retrieve the
     * list of videos in that playlist.
     *
     * @param args command line args (not used).
     */
    var counter: Int = 0

    @JvmStatic
    fun main(args: Array<String>) {
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.

        val scopes: List<String> = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly")

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

            val channelRequest: com.google.api.services.youtube.YouTube.Channels.List =
                youtube.channels().list("contentDetails")


            channelRequest.setMine(true)


            channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo")
            val channelResult: ChannelListResponse = channelRequest.execute()

            val channelsList: List<com.google.api.services.youtube.model.Channel> = channelResult.getItems()


            if (channelsList != null) {
                // The user's default channel is the first item in the list.
                // Extract the playlist ID for the channel's videos from the
                // API response.
                val uploadPlaylistId: String = channelsList[0].getContentDetails().getRelatedPlaylists().getUploads()


                println("#####$uploadPlaylistId#####")


                // Define a list to store items in the list of uploaded videos.
                val playlistItemList: MutableList<PlaylistItem> = ArrayList<PlaylistItem>()


                //https://developers.google.com/youtube/v3/docs/playlistItems
                // Retrieve the playlist of the channel's uploaded videos.
                val playlistItemRequest: com.google.api.services.youtube.YouTube.PlaylistItems.List =
                    youtube.playlistItems().list("id,contentDetails,snippet")
                playlistItemRequest.setPlaylistId(uploadPlaylistId)


                // Only retrieve data used in this application, thereby making
                // the application more efficient. See:
                // https://developers.google.com/youtube/v3/getting-started#partial
                playlistItemRequest.setFields(
                    "items(contentDetails/videoId" +
                            ",snippet/title,snippet/publishedAt" +
                            ",snippet/description" +  //dont have -> ",snippet/tags" +
                            ",etag,status" +
                            ",snippet/thumbnails" +
                            "),nextPageToken,pageInfo"
                )


                //dont work >playlistItemRequest.setPart("snippet");//<-------------- &part=snippet
                var nextToken = ""

                // Call the API one or more times to retrieve all items in the
                // list. As long as the API response returns a nextPageToken,
                // there are still more items to retrieve.
                do {
                    playlistItemRequest.setPageToken(nextToken)
                    val playlistItemResult: PlaylistItemListResponse = playlistItemRequest.execute()

                    playlistItemList.addAll(playlistItemResult.getItems())

                    nextToken = playlistItemResult.getNextPageToken()
                } while (nextToken != null)


                // Prints information about the results.
                prettyPrint(playlistItemList.size, playlistItemList.iterator())
            }
        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
            System.err.println(
                ("There was a service error: " + e.details.code + " : "
                        + e.details.message)
            )
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    /*
     * Print information about all of the items in the playlist.
     *
     * @param size size of list
     *
     * @param iterator of Playlist Items from uploaded Playlist
     */
    @Throws(IOException::class)
    private fun prettyPrint(size: Int, playlistEntries: Iterator<PlaylistItem>) {
        println("=============================================================")
        println("\t\tTotal Videos Uploaded: $size")
        println("=============================================================\n")

        while (playlistEntries.hasNext()) {
            val playlistItem: PlaylistItem = playlistEntries.next()


            print((counter++).toString() + "\n")


            //System.out.println("RAW DATA: "+playlistItem);


            //================================================================
            val id: String = playlistItem.getContentDetails().getVideoId()
            val title: String = playlistItem.getSnippet().getTitle()
            var description: String = playlistItem.getSnippet().getDescription().replace("\n".toRegex(), "<br/>")
            description = fixUrl(description)

            //================================================================
            print(" Video id: $id")
            print(" Video name  = $title")

            //System.out.println(" video id    = " + playlistItem.getSnippet().getT);
            print(" upload date = " + playlistItem.getSnippet().getPublishedAt())
            print(" description: = $description")


            //Thumbinails: = {"default":{"height":90,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/default.jpg","width":120},
            // "high":{"height":360,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/hqdefault.jpg","width":480},
            // "medium":{"height":180,"url":"https://i.ytimg.com/vi/exJvh5u_t_s/mqdefault.jpg","width":320}}
            val thubnails: ThumbnailDetails = playlistItem.getSnippet().getThumbnails()

            if (null != thubnails) {
                println(" Thumbinails: = " + thubnails.getHigh().getUrl()) //.getMaxres().getUrl()
            }


            print(" E-tag = " + playlistItem.getEtag())
            print(" Status = " + playlistItem.getStatus())

            print("\n-------------------------------------------------------------\n")

            val info: VideoSnippet = getAllVideo0(id).getItems().get(0).getSnippet()


            // List<Video> lists = getAllPlaylist();

            //  for (Video current: lists) {
            //      current.getSnippet();
            //  }


            //  print(info.toString());


//----------------------------------------------------------------------------------------------------
            content = com.google.api.services.blogger.model.Post()
            content.setTitle(title)
            content.setContent(
                ("""<div style="text-align: center;">
<br /></div>
<div style="text-align: center;">
<iframe allowfullscreen="true" frameborder="0" height="470" src="https://www.youtube.com/embed/$id" width="100%"></iframe></div>
<div style="text-align: left;">
<br /></div>
<div style="text-align: left;">
$description</div>""")
            )


            //https://www.youtube.com/watch?v=SkI0NVj_1l4
            val author: com.google.api.services.blogger.model.Post.Author =
                com.google.api.services.blogger.model.Post.Author()
            author.setDisplayName("][asper")

            content.setAuthor(author)
            content.setCustomMetaData("00000000000000000000000000000,888888888888")


            val images: MutableList<Images> = ArrayList<Images>()


            //java.util.List<Images> images
            val img1: Images = Images()
            img1.setUrl("http://d.stockcharts.com/img/articles/2014/12/14181400432571017853286.png")

            val img2: Images = Images()
            if (null != thubnails) {
                img2.setUrl(thubnails.getHigh().getUrl())
                images.add(img2)
            }

            images.add(img1)
            content.setImages(images)


            //List<String> labels = new ArrayList<>();

            //Ярлыки
            //labels.add("bbbbbbbbbbbbbbbb");
            //labels.add("cccccccccccccccccccccccc");
            content.setLabels(info.getTags())


            //-------------------------------------------------------------------------------------------
            //      new MyBlogger(content, GRABBERZ_BLOG_ID).execute();
        }
    }
}
