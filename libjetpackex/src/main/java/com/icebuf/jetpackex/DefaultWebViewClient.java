package com.icebuf.jetpackex;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DefaultWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WebView.HitTestResult hit = view.getHitTestResult();
        //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
        if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
            //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
            Log.e("重定向", "重定向: " + hit.getType() + " && EXTRA（）" + hit.getExtra() + "------");
            Log.e("重定向", "GetURL: " + view.getUrl() + "\n" +"getOriginalUrl()"+ view.getOriginalUrl());
            Log.d("重定向", "URL: " + url);
        }
        if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
            view.loadUrl(url);
            return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走

        } else { //加载的url是自定义协议地址
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public void onSetting(WebSettings settings) {
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(false);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(false);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setBlockNetworkImage(false);
    }
}
