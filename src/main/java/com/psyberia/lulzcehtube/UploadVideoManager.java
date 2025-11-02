//package com.psyberia.lulzcehtube;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.googleapis.media.MediaHttpUploader;
//import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
//import com.google.api.client.http.InputStreamContent;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.Lists;
//import com.google.api.services.samples.Config;
//import com.google.api.services.youtube.YouTube;
//import com.google.api.services.youtube.model.Video;
//import com.google.api.services.youtube.model.VideoSnippet;
//import com.google.api.services.youtube.model.VideoStatus;
//
//import java.io.*;
//import java.security.GeneralSecurityException;
//import java.util.Arrays;
//import java.util.Collection;
//
//public class UploadVideoManager implements MediaHttpUploaderProgressListener {
//
//    private static UploadVideoManager instance;
//
//    public static UploadVideoManager newInstance(Callback callback) {
//        if (instance == null) {
//            instance = new UploadVideoManager(callback);
//        }
//        return instance;
//    }
//
//    private final Callback callback;
//    YouTube youTube;
//
//    private UploadVideoManager(Callback callback) {
//        this.callback = callback;
//        this.youTube = getService();
//    }
//
//    @Override
//    public void progressChanged(MediaHttpUploader uploader) {
//        switch (uploader.getUploadState()) {
//            case INITIATION_STARTED:
//                System.out.println("Initiation Started");
//                break;
//            case INITIATION_COMPLETE:
//                System.out.println("Initiation Completed");
//                break;
//            case MEDIA_IN_PROGRESS:
//                System.out.println("Upload in progress");
//                //----->    error..
//                // System.out.println("Upload percentage: " + uploader.getProgress());
//                //getNumBytesUploaded()
//                break;
//            case MEDIA_COMPLETE:
//                System.out.println("Upload Completed!");
//                callback.uploadCompleted();
//                break;
//            case NOT_STARTED:
//                System.out.println("Upload Not Started!");
//                break;
//        }
//    }
//
//    public interface Callback {
//        String getVideoFilePath();
//
//        String getVideoFileName();
//
//        void uploadCompleted();
//    }
//
//    private static final String CLIENT_SECRETS = "client_secret.json";
//
//
//    private static final Collection<String> SCOPES = Arrays.asList(
//            "https://www.googleapis.com/auth/youtube",
//            "https://www.googleapis.com/auth/youtube.force-ssl",
//            "https://www.googleapis.com/auth/youtubepartner",
//            "https://www.googleapis.com/auth/youtube.upload" //uploader
//    );
//
//
//    private static final String APPLICATION_NAME = "API code samples";
//    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//
//    /**
//     * Create an authorized Credential object.
//     *
//     * @return an authorized Credential object.
//     * @throws IOException
//     */
//    public Credential authorize(final NetHttpTransport httpTransport) throws IOException {
//        // Load client secrets.
//        //InputStream stream = ApiExample.class.getResourceAsStream(CLIENT_SECRETS);
//        //real
//        //InputStream stream = this.getClass().getResourceAsStream(VIDEO_FILES_PATH + video_filename);
//
//
//        Reader clientSecretReader = new InputStreamReader(Config.openStream());
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
//
//        //FileInputStream stream = new FileInputStream(fileString);
//        //GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(stream));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
//                        .build();
//        Credential credential =
//                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//        return credential;
//    }
//
//
//    public YouTube getService() {
//        try {
//            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            Credential credential = authorize(httpTransport);
//            return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static void main(String[] args) {
//        //000
//    }
//
//    public Video upload(VideoSnippet snippet) {
//
//
//        // Define the Video object, which will be uploaded as the request body.
//        Video videoObjectDefiningMetadata = new Video();
////        VideoSnippet snippet = new VideoSnippet();
////        snippet.setCategoryId("22");
////        snippet.setDescription("Description of uploaded videoObjectDefiningMetadata.");
////        snippet.setTitle("Test videoObjectDefiningMetadata upload.");
//        videoObjectDefiningMetadata.setSnippet(snippet);
//
//        // Add the status object property to the Video object.
//        VideoStatus status = new VideoStatus();
//        status.setPrivacyStatus("public"); //"unlisted" and "private."
//        videoObjectDefiningMetadata.setStatus(status);
//
//        try {
//            // TODO: For this request to work, you must replace "YOUR_FILE"
//            //       with a pointer to the actual file you are uploading.
//            //       The maximum file size for this operation is 137438953472.
//            String mediaFile = new File(callback.getVideoFilePath(), callback.getVideoFileName()).getAbsolutePath();
//            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(mediaFile));
////            InputStreamContent mediaContent =
////                    new InputStreamContent("application/octet-stream",
////                            );
//            InputStreamContent mediaContent = new InputStreamContent("video/*", stream);
//            mediaContent.setLength(mediaFile.length());
//
//            YouTube.Videos.Insert request = youTube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
//            MediaHttpUploader uploader = request.getMediaHttpUploader();
//            // Indicate whether direct media upload is enabled. A value of
//            // "True" indicates that direct media upload is enabled and that
//            // the entire media content will be uploaded in a single request.
//            // A value of "False," which is the default, indicates that the
//            // request will use the resumable media upload protocol, which
//            // supports the ability to resume an upload operation after a
//            // network interruption or other transmission failure, saving
//            // time and bandwidth in the event of network failures.
//            uploader.setDirectUploadEnabled(false);
//            uploader.setProgressListener(this);
//            Video response = request.execute();
//            //System.out.println(response);
//            System.out.println("https://www.youtube.com/watch?v=" + response.getId());
//            return response;
//        } catch (Exception e) {
//            handleException(e);
//        }
//        return null;
//    }
//
//    private void handleException(Exception e) {
//        if (e instanceof GoogleJsonResponseException) {
//            GoogleJsonResponseException t = (GoogleJsonResponseException) e;
//            System.err.println((t.getClass().getSimpleName() + "\t" + t.getDetails().getCode()
//                    + " :: " + t.getDetails().getMessage()));
//        } else {
//            System.err.println((e.getClass().getSimpleName() + "::" + e.getMessage()));
//        }
//    }
//
//}
