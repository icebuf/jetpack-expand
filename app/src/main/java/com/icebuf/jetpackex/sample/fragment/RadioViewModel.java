package com.icebuf.jetpackex.sample.fragment;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.icebuf.jetpackex.sample.pojo.Sex;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class RadioViewModel extends ViewModel {

    public ObservableInt backgroundColor = new ObservableInt();

    private ObservableField<Sex> sex = new ObservableField<>();

    public ObservableInt getBackgroundColor() {
        return backgroundColor;
    }

    public void onColorChanged(Object value) {
        backgroundColor.set((Integer) value);
    }

    public ObservableField<Sex> getSex() {
        return sex;
    }

    public void onSexChanged(Object value) {
        sex.set((Sex) value);
        Log.e("TAG", "onSexChanged():: " + value);
    }
}
