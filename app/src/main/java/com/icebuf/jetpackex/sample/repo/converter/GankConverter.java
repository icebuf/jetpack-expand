package com.icebuf.jetpackex.sample.repo.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/30
 * E-mail：bflyff@hotmail.com
 */
public class GankConverter {

    @TypeConverter
    public static String list2String(List<String> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public static List<String> string2List(String value) {
        return (List<String>) new Gson().fromJson(value, List.class);
    }
}
