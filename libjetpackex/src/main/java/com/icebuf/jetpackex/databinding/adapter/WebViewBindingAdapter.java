package com.icebuf.jetpackex.databinding.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.icebuf.jetpackex.util.ReflectUtil;
import com.icebuf.jetpackex.webkit.ProgressWebChromeClient;

@BindingMethods({
        @BindingMethod(type = WebView.class, attribute = "loadUrl", method = "loadUrl"),
        @BindingMethod(type = WebView.class, attribute = "webViewClient", method = "setWebViewClient"),
        @BindingMethod(type = WebView.class, attribute = "webChromeClient", method = "setWebChromeClient")
})
//@InverseBindingMethods(
//        @InverseBindingMethod(type = WebView.class, attribute = "settings", method = "getSettings")
//)
public class WebViewBindingAdapter {

    @BindingConversion
    public static WebChromeClient convert2WebChromeClient(Class<? extends WebChromeClient> clazz) {
        return ReflectUtil.newInstanceSafe(WebChromeClient.class, clazz);
    }

    @BindingConversion
    public static WebChromeClient convert2WebChromeClient(String clazzName) {
        return ReflectUtil.newInstanceSafe(WebChromeClient.class, clazzName);
    }

    @BindingConversion
    public static WebViewClient convert2WebViewClient(Class<? extends WebViewClient> clazz) {
        return ReflectUtil.newInstanceSafe(WebViewClient.class, clazz);
    }

    @BindingConversion
    public static WebViewClient convert2WebViewClient(String clazzName) {
        return ReflectUtil.newInstanceSafe(WebChromeClient.class, clazzName);
    }

    @BindingAdapter(value = "webProgress")
    public static void setWebProgress(WebView webView, @IdRes int progressId) {
        ViewGroup parent = (ViewGroup) webView.getParent();
        View view = parent.findViewById(progressId);
        if(view instanceof ProgressBar) {
            webView.setWebChromeClient(new ProgressWebChromeClient((ProgressBar) view));
        }
    }

    @BindingAdapter(value = {"loadData", "dataMimeType", "dataEncoding"}, requireAll = false)
    public static void loadData(WebView webView, String data, String mimeType, String encoding) {
        mimeType = TextUtils.isEmpty(mimeType) ? "text/html" : mimeType;
        encoding = TextUtils.isEmpty(encoding) ? "utf-8" : encoding;
        webView.loadData(data, mimeType, encoding);
    }

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
