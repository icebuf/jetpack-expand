package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.ToutiaoResponse;
import com.icebuf.jetpackex.sample.rest.ToutiaoApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ToutiaoDataSource {

    private Retrofit mRetrofit;

    private ToutiaoApi mToutiaoApi;

    public ToutiaoDataSource(Retrofit retrofit) {
        mRetrofit = retrofit;
        mToutiaoApi = mRetrofit.create(ToutiaoApi.class);
    }

    public Observable<ToutiaoResponse> updateTopNews(int num) {
        //对 发送请求 进行封装
        return mToutiaoApi.getTopNews(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
