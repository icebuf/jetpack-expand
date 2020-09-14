package com.icebuf.jetpackex.sample.hilt;

import android.content.Context;

import com.icebuf.jetpackex.sample.repo.LocalDataSource;
import com.icebuf.jetpackex.sample.repo.TianDataSource;
import com.icebuf.jetpackex.sample.repo.TianRepository;
import com.icebuf.jetpackex.sample.repo.db.NewsDatabase;
import com.icebuf.jetpackex.sample.rest.TianApi;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/10
 * E-mail：bflyff@hotmail.com
 */
@Module
@InstallIn({ActivityComponent.class, FragmentComponent.class})
public abstract class TianModule {

    @Provides
    public static NewsDatabase provideNewsDatabase(@ApplicationContext Context context) {
        return NewsDatabase.getInstance(context);
    }

    @Provides
    public static LocalDataSource provideLocalDataSource(NewsDatabase database) {
        return new LocalDataSource(database.topNewsDao());
    }

    @Provides
    public static Retrofit provideRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(TianApi.BASE_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    @Provides
    public static TianDataSource provideTianDataSource(Retrofit retrofit) {
        return new TianDataSource(retrofit);
    }

    @Provides
    public static TianRepository provideTianRepo(LocalDataSource localDataSource,
                                                 TianDataSource tianDataSource) {
        return TianRepository.getInstance(tianDataSource, localDataSource);
    }

}
