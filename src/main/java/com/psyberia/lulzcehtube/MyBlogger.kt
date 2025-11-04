package com.psyberia.lulzcehtube

import com.google.api.services.blogger.Blogger
import com.google.api.services.blogger.model.Post
import com.google.api.services.blogger.model.Post.Images
import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.samples.youtube.cmdline.Auth.authorize
import com.google.api.services.samples.youtube.cmdline.data.Tools.print
import com.google.common.collect.Lists
import java.io.IOException

/**
 * Created by combo on 03.12.2016.
 */

class MyBlogger(post: Post, id: String) {
    init {
        content = post
        blogIDs.add(id)
    }

    @Throws(IOException::class)
    fun execute0() {
        //auth

        val credential = authorize(scopes, "blogger")
        // This object is used to make YouTube Data API requests.
        blogger = Blogger.Builder(
            Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential
        ).setApplicationName(
            "youtube-cmdline-myrate-sample"
        ).build()
        blogger?.let {
            for (blogid in blogIDs) { //<--------------------------------------

                print("Selected blog: $blogid")

                val postsInsertAction = it.posts().insert(blogid, content)
                postsInsertAction.setFields("author/displayName,content,published,title,url")

                val post = postsInsertAction.execute() //<-----------------------------------------------------------------

                println("Title: " + post.title)
                println("Author: " + post.author.displayName)
                println("Published: " + post.published)
                println("URL: " + post.url)
                println("Content: " + post.content)
            }


            //https://www.googleapis.com/blogger/v3/blogs/blogId
            //GET https://www.googleapis.com/blogger/v3/blogs/2399953?key=YOUR-API-KEY
//https://www.googleapis.com/blogger/v3/blogs/byurl?url=blog-url

            //https://www.googleapis.com/blogger/v3/blogs/byurl?url=http://code.blogger.com/

            //Retrieving a user's blogs
            val list = it.blogs()
            val bu = it.users() //list.get("1")
        }
    }

    companion object {
        //https://developers.google.com/apis-explorer/?hl=ru#p/blogger/v3/
        private var blogger: Blogger? = null
        private var content = Post()

        private val blogIDs = ArrayList<String>()

        private val scopes: List<String> = Lists.newArrayList( //Manage your Blogger account
            "https://www.googleapis.com/auth/blogger",  //View your Blogger account
            "https://www.googleapis.com/auth/blogger.readonly"
        )


        fun generateContent(): Post {
            //String accessToken = credential.getAccessToken();
            //System.out.println(accessToken);

            content.setTitle("19, no brains, right three days ago washed... accident Mon Sep 12 22:10:...")
            content.setContent(
                "" +
                        "" +
                        "<iframe width=\"480\" height=\"270\" src=\"https://www.youtube.com/embed/SkI0NVj_1l4\" frameborder=\"0\" allowFullScreen=\"\"></iframe>" +
                        ""
            )


            //https://www.youtube.com/watch?v=SkI0NVj_1l4
            val author = Post.Author()
            author.setDisplayName("][asper")

            content.setAuthor(author)
            content.setCustomMetaData("00000000000000000000000000000,888888888888")


            //java.util.List<Images> images
            val img1 = Images()
            img1.setUrl("http://d.stockcharts.com/img/articles/2014/12/14181400432571017853286.png")

            val img2 = Images()
            img2.setUrl("http://n-imagecache.aldenhosting.com/JAVA-tutorial/figures/uiswing/events/TableListSelectionDemo.gif")


            val images: MutableList<Images> = ArrayList()
            images.add(img1)
            images.add(img2)
            content.setImages(images)


            val labels: MutableList<String> = ArrayList()

            //Ярлыки
            labels.add("bbbbbbbbbbbbbbbb")
            labels.add("cccccccccccccccccccccccc")
            content.setLabels(labels)

            return content
        }

        @JvmStatic
        fun main(args: Array<String>) {
            generateContent()
            try {
                //new MyBlogger(content, GRABBERZ_BLOG_ID).execute();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
