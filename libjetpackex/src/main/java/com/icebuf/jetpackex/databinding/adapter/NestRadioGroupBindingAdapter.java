package com.icebuf.jetpackex.databinding.adapter;

import android.util.Log;
import android.util.SparseArray;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import com.icebuf.jetpackex.widget.NestedRadioGroup;

import java.util.Objects;

/**
 * The type Radio group binding adapter.
 *
 * @author IceTang
 * @version 1.0  Data: 2020/8/5 E-mail：bflyff@hotmail.com
 */
@InverseBindingMethods({
        @InverseBindingMethod(type = NestedRadioGroup.class, attribute = "android:checkedButton", method = "getCheckedId"),
})
public class NestRadioGroupBindingAdapter {

    private static final String TAG = NestRadioGroupBindingAdapter.class.getSimpleName();

    private static final int KEY_VALUE_MAP = -100;

    private static final int KEY_VALUE_CHECK = -101;

    /**
     * Sets checked value.
     *
     * @param <T>   the type parameter
     * @param group the group
     * @param value the value
     */
    @BindingAdapter(value = {"checkedValue"})
    public static <T> void setCheckedValue(NestedRadioGroup group, T value) {
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
        Log.w(TAG, "value " + value + " for radioButton id not found!");
    }

    private static <T> SparseArray<T>  getValueArray(NestedRadioGroup group) {
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
    @BindingAdapter(value = {"onValueChanged", "valueAttrChanged"}, requireAll = false)
    public static <T> void setListeners(NestedRadioGroup view, final OnValueChangedListener<T> listener,
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

    private static <T> void setOnValueChangeListener(NestedRadioGroup view, OnValueChangedListener<T> listener) {
        view.setOnCheckedChangeListener(new NestedRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroup group, int checkedId) {
                SparseArray<T> valueArray = getValueArray(group);
                if(listener != null) {
                    listener.onValueChanged(group, valueArray.get(checkedId));
                }
                group.setTag(KEY_VALUE_CHECK, false);
            }
        });
        SparseArray<T> valueArray = getValueArray(view);
        T value = valueArray.get(view.getCheckedId());
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
        void onValueChanged(NestedRadioGroup group, T value);
    }
}
