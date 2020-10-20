package com.icebuf.jetpackex.sample.hilt;

import com.icebuf.jetpackex.sample.hilt.qualifier.IOExecutor;
import com.icebuf.jetpackex.sample.hilt.qualifier.LoadExecutor;
import com.icebuf.jetpackex.sample.hilt.qualifier.NetExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import io.reactivex.internal.schedulers.RxThreadFactory;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/22
 * E-mail：bflyff@hotmail.com
 *
 * 提供整个应用用到的线程资源
 */
@Module
@InstallIn(ApplicationComponent.class)
public class ExecutorModule {

    @IOExecutor
    @Singleton
    @Provides
    public static ExecutorService providerIO() {
        return Executors.newSingleThreadExecutor();
    }

    @NetExecutor
    @Singleton
    @Provides
    public static ExecutorService providerNet() {
        return Executors.newFixedThreadPool(4, new RxThreadFactory("Net"));
    }

    @LoadExecutor
    @Singleton
    @Provides
    public static ExecutorService providerLoad() {
        return Executors.newFixedThreadPool(4);
    }
}
