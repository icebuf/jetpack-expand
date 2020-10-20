package com.icebuf.jetpackex.sample.util;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("PrivateApi")
public class SystemPropUtil {

    private static Class<?> sSystemPropClazz;

    private static Method sGetPropMethod;

    private static Method sSetPropMethod;

    static {
        try {
            sSystemPropClazz = Class.forName("android.os.SystemProperties");
            sGetPropMethod = sSystemPropClazz.getMethod("get", String.class, String.class);
            sSetPropMethod = sSystemPropClazz.getMethod("set", String.class, String.class);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        String value = defaultValue;
        if(sSystemPropClazz != null && sGetPropMethod != null) {
            try {
                value = (String) sGetPropMethod.invoke(sSystemPropClazz, key, defaultValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 设置一个系统变量
     * 需要系统权限和签名 <code>android:sharedUserId="android.uid.system"</code>
     * @param key 变量名
     * @param value 变量值
     */
    public static void set(String key, String value) {
        if(sSystemPropClazz != null && sSetPropMethod != null) {
            try {
                sSetPropMethod.invoke(sSystemPropClazz, key, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
