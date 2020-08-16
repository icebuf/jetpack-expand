package com.icebuf.jetpackex.sample.pojo;

import android.content.Context;

import androidx.annotation.StringRes;

import com.icebuf.jetpackex.sample.R;

public enum Sex {
    MAN(R.string.sex_man),
    FEMALE(R.string.sex_female);

    @StringRes
    private int value;

    Sex(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getValueString(Context context) {
        return context.getString(value);
    }
}
