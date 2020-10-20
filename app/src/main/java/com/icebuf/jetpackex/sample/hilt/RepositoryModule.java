package com.icebuf.jetpackex.sample.hilt;

import com.icebuf.jetpackex.sample.repo.GankDataSource;
import com.icebuf.jetpackex.sample.repo.GankRepository;
import com.icebuf.jetpackex.sample.repo.LocalDataSource;
import com.icebuf.jetpackex.sample.repo.TianDataSource;
import com.icebuf.jetpackex.sample.repo.TianRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/22
 * E-mail：bflyff@hotmail.com
 */
@Module
@InstallIn(ApplicationComponent.class)
public class RepositoryModule {

    @Provides
    public static TianRepository provideTianRepo(LocalDataSource local,
                                                 TianDataSource remote) {
        return TianRepository.getInstance(local, remote);
    }

    @Provides
    public static GankRepository provideGankRepo(LocalDataSource local,
                                                 GankDataSource remote) {
        return GankRepository.getInstance(local, remote);
    }
}
