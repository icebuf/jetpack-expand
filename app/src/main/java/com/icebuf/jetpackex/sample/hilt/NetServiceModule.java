package com.icebuf.jetpackex.sample.hilt;

import com.icebuf.jetpackex.sample.BuildConfig;
import com.icebuf.jetpackex.sample.hilt.qualifier.NetExecutor;
import com.icebuf.jetpackex.sample.rest.GankService;
import com.icebuf.jetpackex.sample.rest.TianNewsService;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
@Module
@InstallIn(ApplicationComponent.class)
public class NetServiceModule {


    @Provides
    public static OkHttpClient provideOkHttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        //add log record
        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            LoggingInterceptor interceptor = new LoggingInterceptor.Builder()
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build();
            builder.addInterceptor(interceptor);
        }
        //添加认证authIntercepter,验证调用者身份信息
//        builder.addInterceptor(chain -> addAuthIntercepter(chain));
        return builder.build();
    }

    /**
     *
     * @param client hilt提供的OkHttpClient
     * @see #provideOkHttp()
     * @param executor hilt提供的executor
     * @see ExecutorModule#providerIO() io线程池
     * @see ExecutorModule#providerLoad() 加载线程池
     * @see ExecutorModule#providerNet() 网络请求线程池
     * @return 配置了一部分的Retrofit.Builder
     */
    @Provides
    public static Retrofit.Builder provideRetrofitBuilder(OkHttpClient client, @NetExecutor ExecutorService executor) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(executor);
    }

    @Provides
    public static TianNewsService provideTianService(Retrofit.Builder builder) {
        return builder.baseUrl(TianNewsService.BASE_URL)
                .build()
                .create(TianNewsService.class);
    }

    @Provides
    public static GankService provideGankService(Retrofit.Builder builder) {
        return builder.baseUrl(GankService.BASE_URL)
                .build()
                .create(GankService.class);
    }
}
