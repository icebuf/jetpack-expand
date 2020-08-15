package com.icebuf.jetpackex.databinding.adapter;

import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

public class TextViewBindingAdapter {

    @BindingAdapter(value = {"android:text", "textFormat"}, requireAll = false)
    public static <T> void setText(TextView textView, ObservableField<T> observable, int format) {
        T value = observable.get();
        String textValue = value == null ? "" : value.toString();
        textView.setText(format <= 0 ? textValue : textView.getResources().getString(format, textValue));
    }

    @BindingAdapter(value = {"android:text", "textFormat"})
    public static <T> void setText(TextView textView, ObservableField<T> observable, String format) {
        T value = observable.get();
        String textValue = value == null ? "" : value.toString();
        textView.setText(format == null ? textValue : String.format(format, textValue));
    }
}
