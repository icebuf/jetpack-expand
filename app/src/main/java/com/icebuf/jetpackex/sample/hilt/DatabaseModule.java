package com.icebuf.jetpackex.sample.hilt;

import android.content.Context;

import com.icebuf.jetpackex.sample.repo.db.GankDatabase;
import com.icebuf.jetpackex.sample.repo.db.NewsDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/22
 * E-mail：bflyff@hotmail.com
 */
@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public static NewsDatabase provideNewsDatabase(@ApplicationContext Context context) {
        return NewsDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    public static GankDatabase provideGankDatabase(@ApplicationContext Context context) {
        return GankDatabase.getInstance(context);
    }

}
