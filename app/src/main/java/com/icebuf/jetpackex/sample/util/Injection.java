package com.icebuf.jetpackex.sample.util;

import android.content.Context;

import com.icebuf.jetpackex.sample.repo.LocalDataSource;
import com.icebuf.jetpackex.sample.repo.ToutiaoDataSource;
import com.icebuf.jetpackex.sample.repo.ToutiaoRepository;
import com.icebuf.jetpackex.sample.repo.db.NewsDatabase;
import com.icebuf.jetpackex.sample.rest.ToutiaoApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static ToutiaoRepository provideToutiaoRepo(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ToutiaoApi.BASE_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
        ToutiaoDataSource dataSource = new ToutiaoDataSource(retrofit);
        NewsDatabase database = NewsDatabase.getInstance(context);
        LocalDataSource localDataSource = new LocalDataSource(database.topNewsDao());
        return ToutiaoRepository.getInstance(dataSource, localDataSource);
    }


}
