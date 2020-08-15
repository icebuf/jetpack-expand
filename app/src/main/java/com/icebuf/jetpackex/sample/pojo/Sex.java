package com.icebuf.jetpackex.sample.pojo;

import androidx.databinding.BindingConversion;

public enum Sex {
    MAN("男"),
    FEMALE("女");

    private String value;

    Sex(String str) {
        value = str;
    }

    public String getValue() {
        return value;
    }

    @BindingConversion
    public static String sex2String(Sex sex) {
        return sex == null ? "" : sex.getValue();
    }
}
