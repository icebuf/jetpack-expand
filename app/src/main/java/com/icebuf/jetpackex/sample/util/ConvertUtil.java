package com.icebuf.jetpackex.sample.util;

import android.text.TextUtils;

public class ConvertUtil {


    public static String http2HttpsUrl(String url) {
        if(TextUtils.isEmpty(url)) {
            return url;
        }
        if(url.startsWith("http://")) {
            return url.replace("http://", "https://");
        }
        return url;
    }

}
