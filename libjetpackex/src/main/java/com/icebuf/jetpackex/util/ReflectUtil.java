package com.icebuf.jetpackex.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/11
 * E-mail：bflyff@hotmail.com
 */
public class ReflectUtil {

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClazz) {
        T annotation = clazz.getAnnotation(annotationClazz);
        if(annotation == null) {
            Class<?> superClazz = clazz.getSuperclass();
            if(superClazz != null) {
                annotation = getAnnotation(superClazz, annotationClazz);
            }
        }
        return annotation;
    }

    public static void setSuperField(Object object, String fieldName, Object fieldValue) {
        Field field = getSuperField(object.getClass(), fieldName);
        if(field != null) {
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                field.set(object, fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Field getSuperField(Class<?> clazz, String fieldName) {
        if(clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getSuperField(clazz.getSuperclass(), fieldName);
        }
    }

}
