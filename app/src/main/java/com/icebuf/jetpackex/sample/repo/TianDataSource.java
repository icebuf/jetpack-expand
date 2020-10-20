package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.TianHotVideoEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsResponse;
import com.icebuf.jetpackex.sample.rest.TianNewsService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TianDataSource extends DataSource{

    @Inject
    TianNewsService mNewsService;

    @Inject
    public TianDataSource() {

    }

    public Observable<TianNewsResponse<TianNewsEntity>> getTopNews(int num) {
        //对 发送请求 进行封装
        return mNewsService.getTopNews(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TianNewsResponse<TianHotVideoEntity>> getHotVideos() {
        //对 发送请求 进行封装
        return mNewsService.getHotVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
