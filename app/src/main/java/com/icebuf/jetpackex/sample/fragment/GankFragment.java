package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.testcase.ItemGroup;
import com.icebuf.testcase.ResItem;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
@ItemGroup("testcase")
@ResItem(name = R.string.test_name_gank_api,
        description = R.string.test_desc_gank_api,
        destination = R.id.action_homeFragment_to_gankFragment)
@AndroidEntryPoint
public class GankFragment extends DBFragment<GankViewModel> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().updateBanners();
        getViewModel().updateData("Article", "All", 1, 20);

//        BannerViewPager<GankBannerEntity, HomeBannerAdapter.BannerHolder> viewPager = view.findViewById(R.id.view_banner);
//        viewPager.setAdapter(new HomeBannerAdapter());
//        viewPager.create();
//        getViewModel().getBannerList().addOnListChangedCallback(
//                new HomeBannerAdapter.ObservableListCallback2<>(viewPager));

    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<GankViewModel> getVMClass() {
        return GankViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }
}
