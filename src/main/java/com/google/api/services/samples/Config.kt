package com.google.api.services.samples

import com.google.api.services.CFG
import com.google.common.collect.Lists
import java.io.File


public object Config {


    const val OAUTH_CRE: String = "G:\\STORAGE\\MyApp\\CRED\\cred\\"
    const val CRED_BASE: String = "G:\\STORAGE\\MyApp\\CRED\\clients"


    @kotlin.jvm.JvmField
    val cfgs: Array<CFG> = arrayOf(
        CFG("alexvarboffin", "tiktoktube2"),
        CFG("usercmm", "tiktoktube3"),
        CFG("userd", "tiktoktube4"),
        CFG("usere", "tiktoktube5"),


        CFG("userepmmmmm", "aa03022022"),  //alex muslim
        CFG("userepmmmmz", "iballwasrawt88"),  //alex christ
        //opt


        CFG("danilabobrov82", "iballwasrawt88"),  //danilabobrov82@gmail.com = Likee
        CFG("danilabobrov82_2", "iballwasrawt88"),  //danilabobrov82@gmail.com = Inspiring Islamic Quotes

        CFG("userabbcc", "tiktoktube-281514"),
    )



    //"D:\\android\\ANDROID_TUTORIAL\\GUI_GENERATOR_2\\code_generator\\src\\main\\resources\\"
    @kotlin.jvm.JvmField
    val scopes: List<String> = Lists.newArrayList(
        "https://www.googleapis.com/auth/youtube",
        "https://www.googleapis.com/auth/youtube.force-ssl",
        "https://www.googleapis.com/auth/youtubepartner",
        "https://www.googleapis.com/auth/youtube.upload" //uploader
    )
    /**
     * This is the directory that will be used under the auser's home directory where OAuth tokens will be stored.
     */
    //public static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
    //    public static InputStream stream0;
    //
    //    static {
    //        try {
    //            stream0 = new FileInputStream(
    //                    //                    "resources/"
    //                    BASE + "tiktoktube-281514\\youtube.properties");
    //
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    public static FileInputStream openStream() throws FileNotFoundException {
    //        //Auth.class.getResourceAsStream("/client_secrets.json")
    /**/        // Load client secrets. */ //        return new FileInputStream(
    //
    //                //        "C:\\ausers\\combo\\Desktop"
    //                //"C:\\ausers\\combo\\Desktop\\client_secrets.json"//""
    //        );//
    //    }
}
