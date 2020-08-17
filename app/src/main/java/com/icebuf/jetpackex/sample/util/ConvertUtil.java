package com.icebuf.jetpackex.sample.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static long date2Long(String pattern, String text) {
//        DateTimeFormatter.ofPattern("").parse(date);
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            Date date = format.parse(text);
            if(date != null) {
                return date.getTime();
            }
        } catch (ParseException ignore) {
        }
        return 0;
    }

}
