package com.icebuf.jetpackex.sample.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mUrl = new MutableLiveData<>();

    public void setUrl(String url) {
        if(!url.equals(mUrl.getValue())) {
            mUrl.setValue(url);
        }
    }

    public LiveData<String> getUrl() {
        return mUrl;
    }

}
