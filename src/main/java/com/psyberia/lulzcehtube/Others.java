package com.psyberia.lulzcehtube;

import java.io.UnsupportedEncodingException;

public class Others {

    private static String encodeValue(String value) throws UnsupportedEncodingException {
//        try {
//            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex.getCause());
//        }
        String utf8String = "";
//        try {
//            utf8String = new String(value.getBytes("windows-1251"), "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

        byte[] cp1251s = value.getBytes("cp1251");
        utf8String = new String(cp1251s, "UTF8");

        return utf8String;
    }
}
