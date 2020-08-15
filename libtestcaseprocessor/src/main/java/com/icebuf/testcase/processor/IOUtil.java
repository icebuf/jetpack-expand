package com.icebuf.testcase.processor;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

    public static void close(Closeable ... closeables) {
        if(closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
