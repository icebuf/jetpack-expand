package com.icebuf.jetpackex.sample.fragment;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class RadioViewModel extends ViewModel {


    public ObservableInt backgroundColor = new ObservableInt();


    public ObservableInt getBackgroundColor() {
        return backgroundColor;
    }

    public void onColorChanged(Object value) {
        backgroundColor.set((Integer) value);
    }
}
