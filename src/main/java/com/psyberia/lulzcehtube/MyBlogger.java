package com.psyberia.lulzcehtube;

/**
 * Created by combo on 03.12.2016.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.api.services.samples.youtube.cmdline.data.Tools.print;

public class MyBlogger {
    //https://developers.google.com/apis-explorer/?hl=ru#p/blogger/v3/

    private static Blogger blogger;
    private static Post content = new Post();

    private static ArrayList<String> blogIDs = new ArrayList<>();

    private static List<String> scopes = Lists.newArrayList(
            //Manage your Blogger account
            "https://www.googleapis.com/auth/blogger",
            //View your Blogger account
            "https://www.googleapis.com/auth/blogger.readonly"
    );


    public MyBlogger(Post post, String id) {
        content = post;
        blogIDs.add(id);
    }

    public void execute() throws IOException {

        //auth
        Credential credential = Auth.authorize(scopes, "blogger");
        // This object is used to make YouTube Data API requests.
        blogger = new Blogger.Builder(
                Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "youtube-cmdline-myrate-sample").build();


        for (String blogid : blogIDs) {//<--------------------------------------

            print("Selected blog: " + blogid);

            Blogger.Posts.Insert postsInsertAction = blogger.posts().insert(blogid, content);
            postsInsertAction.setFields("author/displayName,content,published,title,url");

            Post post = postsInsertAction.execute();//<-----------------------------------------------------------------

            System.out.println("Title: " + post.getTitle());
            System.out.println("Author: " + post.getAuthor().getDisplayName());
            System.out.println("Published: " + post.getPublished());
            System.out.println("URL: " + post.getUrl());
            System.out.println("Content: " + post.getContent());
        }


        //https://www.googleapis.com/blogger/v3/blogs/blogId
        //GET https://www.googleapis.com/blogger/v3/blogs/2399953?key=YOUR-API-KEY
//https://www.googleapis.com/blogger/v3/blogs/byurl?url=blog-url

        //https://www.googleapis.com/blogger/v3/blogs/byurl?url=http://code.blogger.com/

        //Retrieving a user's blogs

        Blogger.Blogs list;
        list = blogger.blogs();
        Blogger.Users bu = blogger.users();//list.get("1")
    }

    public static Post generateContent() {
        //String accessToken = credential.getAccessToken();
        //System.out.println(accessToken);

        content.setTitle("19, no brains, right three days ago washed... accident Mon Sep 12 22:10:...");
        content.setContent("" +
                "" +
                "<iframe width=\"480\" height=\"270\" src=\"https://www.youtube.com/embed/SkI0NVj_1l4\" frameborder=\"0\" allowFullScreen=\"\"></iframe>" +
                "");


        //https://www.youtube.com/watch?v=SkI0NVj_1l4

        Post.Author author = new Post.Author();
        author.setDisplayName("][asper");

        content.setAuthor(author);
        content.setCustomMetaData("00000000000000000000000000000,888888888888");


        //java.util.List<Images> images
        Post.Images img1 = new Post.Images();
        img1.setUrl("http://d.stockcharts.com/img/articles/2014/12/14181400432571017853286.png");

        Post.Images img2 = new Post.Images();
        img2.setUrl("http://n-imagecache.aldenhosting.com/JAVA-tutorial/figures/uiswing/events/TableListSelectionDemo.gif");


        List<Post.Images> images = new ArrayList<Post.Images>();
        images.add(img1);
        images.add(img2);
        content.setImages(images);


        List<String> labels = new ArrayList<>();

        //Ярлыки
        labels.add("bbbbbbbbbbbbbbbb");
        labels.add("cccccccccccccccccccccccc");
        content.setLabels(labels);

        return content;
    }

    public static void main(String[] args) {
        generateContent();
        try {
            //new MyBlogger(content, GRABBERZ_BLOG_ID).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
