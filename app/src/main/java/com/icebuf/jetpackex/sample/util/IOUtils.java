package com.icebuf.jetpackex.sample.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/5/15
 * E-mail：bflyff@hotmail.com
 * @function
 */
public class IOUtils {

    public static void closeQuietly(Closeable c) {
        try {
            if(c != null) {
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
