package com.icebuf.jetpackex.databinding.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.ViewParent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Objects;

/**
 * The type Radio group binding adapter.
 *
 * @author IceTang
 * @version 1.0  Data: 2020/8/5 E-mail：bflyff@hotmail.com
 */
public class RadioGroupBindingAdapter {

    private static final int KEY_VALUE_MAP = -100;

    private static final int KEY_VALUE_CHECK = -101;

    /**
     * Sets value.
     *
     * @param <T>         多样化存储数据的类型
     * @param radioButton 绑定value的radioButton
     * @param value       该radioButton所表示的值
     */
    @BindingAdapter(value = {"android:value"})
    public static <T> void setValue(RadioButton radioButton, T value) {
        ViewParent parent = radioButton.getParent();
        if (parent instanceof RadioGroup) {
            RadioGroup group = (RadioGroup) parent;
            SparseArray<T> valueArray = getValueArray(group);
            valueArray.append(radioButton.getId(), value);

            boolean check = (boolean) group.getTag(KEY_VALUE_CHECK);
            if(check) {
                group.check(radioButton.getId());
            }
        }
    }


    /**
     * Sets checked value.
     *
     * @param <T>   the type parameter
     * @param group the group
     * @param value the value
     */
    @BindingAdapter(value = {"android:checkedValue"})
    public static <T> void setCheckedValue(RadioGroup group, T value) {
        if(value == null) {
            group.check(-1);
            return;
        }
        SparseArray<T> valueArray = getValueArray(group);
        /**
         * 这里不可以用<code>valueArray.indexOfValue(value);</code>
         * 由于该方法直接调用<code>==</code>判断对象是否相等
         * 但是大多数情况下set的对象和radioGroup中映射的对象并不是同一个对象
         * 应当使用equals判断
         */
        for (int i = 0; i < valueArray.size(); i++) {
            if(Objects.equals(valueArray.valueAt(i), value)) {
                int id = valueArray.keyAt(i);
                group.check(id);
                return;
            }
        }
        group.setTag(KEY_VALUE_CHECK, true);
        Log.e("TAG", "value " + value + " for radioButton id not found!");
    }

    private static <T> SparseArray<T>  getValueArray(RadioGroup group) {
        Object object = group.getTag(KEY_VALUE_MAP);
        if(object instanceof SparseArray) {
            return (SparseArray<T>) object;
        }
        SparseArray<T> valueArray = new SparseArray<>();
        group.setTag(KEY_VALUE_MAP, valueArray);
        return valueArray;
    }

    /**
     * Sets listeners.
     *
     * @param <T>        the type parameter
     * @param view       the view
     * @param listener   the listener
     * @param attrChange the attr change
     */
    @BindingAdapter(value = {"android:onValueChanged", "android:valueAttrChanged"}, requireAll = false)
    public static <T> void setListeners(RadioGroup view, final OnValueChangedListener<T> listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            setOnValueChangeListener(view, listener);
        } else {
            setOnValueChangeListener(view, (OnValueChangedListener<T>) (group, value) -> {
                if (listener != null) {
                    listener.onValueChanged(group, value);
                }
                attrChange.onChange();
            });
        }
    }

    private static <T> void setOnValueChangeListener(RadioGroup view, OnValueChangedListener<T> listener) {
        view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SparseArray<T> valueArray = getValueArray(group);
                if(listener != null) {
                    listener.onValueChanged(group, valueArray.get(checkedId));
                }
                group.setTag(KEY_VALUE_CHECK, false);
            }
        });
        SparseArray<T> valueArray = getValueArray(view);
        T value = valueArray.get(view.getCheckedRadioButtonId());
        if(value != null) {
            listener.onValueChanged(view, value);
        }
    }

    /**
     * The interface On value changed listener.
     *
     * @param <T> the type parameter
     */
    public interface OnValueChangedListener<T> {

        /**
         * On value changed.
         *
         * @param group the group
         * @param value the value
         */
        void onValueChanged(RadioGroup group, T value);
    }
}
