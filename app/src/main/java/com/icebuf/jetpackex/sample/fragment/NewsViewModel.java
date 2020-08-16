package com.icebuf.jetpackex.sample.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> newsUrl = new MutableLiveData<>();

    public void setNewsUrl(String url) {
        if(!url.equals(newsUrl.getValue())) {
            newsUrl.setValue(url);
        }
    }

    public LiveData<String> getNewsUrl() {
        return newsUrl;
    }

}
