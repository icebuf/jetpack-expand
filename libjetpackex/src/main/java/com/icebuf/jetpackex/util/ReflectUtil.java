package com.icebuf.jetpackex.util;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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

    public static <T> T newInstance(String clazzName, Object ... objects) {
        try {
            return newInstance(Class.forName(clazzName), objects);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T newInstance(Class<?> clazz, Object ... objects) {
        if (clazz.isAssignableFrom(RecyclerView.OnScrollListener.class)) {
            try {
                if (objects == null || objects.length <= 0) {
                    return (T) clazz.newInstance();
                }
                List<Class<?>> clazzList = new ArrayList<>();
                for (Object object : objects) {
                    clazzList.add(object.getClass());
                }
                Constructor<?> constructor = clazz.getConstructor(clazzList.toArray(new Class[0]));
                return (T) constructor.newInstance(objects);
            } catch(NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new RuntimeException("bad class " + clazz.getName());
    }
}
