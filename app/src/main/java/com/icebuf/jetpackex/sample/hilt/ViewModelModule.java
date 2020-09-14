package com.icebuf.jetpackex.sample.hilt;

import androidx.lifecycle.ViewModel;

import com.icebuf.jetpackex.sample.fragment.RecycleViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/10
 * E-mail：bflyff@hotmail.com
 */
@Module
@InstallIn({ActivityComponent.class, FragmentComponent.class})
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ClassKey(RecycleViewModel.class)
    public abstract ViewModel bindActivityViewModel(RecycleViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(RecycleViewModel.class)
    public abstract ViewModel bindFragmentViewModel(RecycleViewModel viewModel);

}
