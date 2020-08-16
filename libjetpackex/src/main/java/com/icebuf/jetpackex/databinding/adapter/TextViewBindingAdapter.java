package com.icebuf.jetpackex.databinding.adapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

public class TextViewBindingAdapter {

    @BindingAdapter(value = {"android:text", "textFormat"})
    public static <T> void setText(TextView textView, LiveData<T> liveData, String format) {
        T value = liveData.getValue();
        String textValue = value == null ? "" : value.toString();
        textView.setText(format == null ? textValue : String.format(format, textValue));
    }

    @BindingAdapter(value = {"android:text", "textFormat"}, requireAll = false)
    public static <T> void setText(TextView textView, ObservableField<T> field, String format) {
        T value = field.get();
        String textValue = value == null ? "" : value.toString();
        textView.setText(format == null ? textValue : String.format(format, textValue));
    }
}
