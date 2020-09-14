package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.TianHotVideoEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsResponse;
import com.icebuf.jetpackex.sample.rest.TianNewsApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TianDataSource {

    private Retrofit mRetrofit;

    private TianNewsApi mNewsApi;

    public TianDataSource(Retrofit retrofit) {
        mRetrofit = retrofit;
        mNewsApi = mRetrofit.create(TianNewsApi.class);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Observable<TianNewsResponse<TianNewsEntity>> getTopNews(int num) {
        //对 发送请求 进行封装
        return mNewsApi.getTopNews(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TianNewsResponse<TianHotVideoEntity>> getHotVideos() {
        //对 发送请求 进行封装
        return mNewsApi.getHotVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
