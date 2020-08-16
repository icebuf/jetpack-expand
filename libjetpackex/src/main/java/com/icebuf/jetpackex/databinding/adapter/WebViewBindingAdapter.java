package com.icebuf.jetpackex.databinding.adapter;

import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

@BindingMethods({
        @BindingMethod(type = WebView.class, attribute = "loadUrl", method = "loadUrl"),
        @BindingMethod(type = WebView.class, attribute = "webViewClient", method = "setWebViewClient")
})
//@InverseBindingMethods(
//        @InverseBindingMethod(type = WebView.class, attribute = "settings", method = "getSettings")
//)
public class WebViewBindingAdapter {

    @BindingAdapter(value = {"settings", "settingsAttrChanged"}, requireAll = false)
    public static void setSettings(WebView view, WebSettingListener listener, InverseBindingListener attrChange) {
        if (attrChange == null) {
            if (listener != null) {
                listener.onSetting(view, view.getSettings());
            }
        } else {
            if (listener != null) {
                listener.onSetting(view, view.getSettings());
            }
            attrChange.onChange();
        }
    }

    @InverseBindingAdapter(attribute = "settings", event = "settingsAttrChanged")
    public static WebSettings getSettings(WebView view) {
        return view.getSettings();
    }

    public interface WebSettingListener {

        void onSetting(WebView webView, WebSettings settings);
    }
}
