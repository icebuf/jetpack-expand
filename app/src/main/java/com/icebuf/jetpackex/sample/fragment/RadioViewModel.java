package com.icebuf.jetpackex.sample.fragment;

import android.util.Log;
import android.view.View;

import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
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

    private ObservableField<String> xiaomingSex = new ObservableField<>();

    private MutableLiveData<Sex> sexMutableLiveData = new MutableLiveData<>();

    public ObservableInt getBackgroundColor() {
        return backgroundColor;
    }

    public void onColorChanged(Object value) {
        backgroundColor.set((Integer) value);
        Log.e("TAG", "onColorChanged():: " + value);
    }

    public ObservableField<String> getSex() {
        return xiaomingSex;
    }

    public MutableLiveData<Sex> getSexMutableLiveData() {
        return sexMutableLiveData;
    }

    public void onSexChanged(View view, Object value) {
        Sex sex = (Sex) value;
        xiaomingSex.set(view.getResources().getString(sex.getValue()));
        Log.e("TAG", "onSexChanged():: " + value);
    }

    @BindingConversion
    public static <T> T sex2String(ObservableField<T> observableField) {
        return observableField.get();
    }
}
