package com.icebuf.jetpackex.webkit;

import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/10/13
 * E-mail：bflyff@hotmail.com
 */
public class ProgressWebChromeClient extends WebChromeClient{

    public static final Class<? extends WebChromeClient> CLAZZ = ProgressWebChromeClient.class;

    public static final int MAX_VALUE = 100;

    private ProgressBar mProgressBar;

    public ProgressWebChromeClient() {

    }

    public ProgressWebChromeClient(ProgressBar progressBar) {
        setProgressBar(progressBar);
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if(mProgressBar == null) {
            return;
        }
        if(mProgressBar.getMax() != MAX_VALUE) {
            mProgressBar.setMax(MAX_VALUE);
        }
        if(newProgress == MAX_VALUE) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            if(mProgressBar.getVisibility() != View.VISIBLE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mProgressBar.setProgress(newProgress, true);
            } else {
                mProgressBar.setProgress(newProgress);
            }
        }
    }

}
