package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableList;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.constants.ViewModelConstants;
import com.icebuf.jetpackex.sample.pojo.GankBannerEntity;
import com.icebuf.jetpackex.sample.pojo.GankDataEntity;
import com.icebuf.jetpackex.sample.repo.GankRepository;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */

public class GankViewModel extends ViewModel {

    GankRepository repository;

    @ViewModelInject
    public GankViewModel(GankRepository repo) {
        repository = repo;
    }

    public void updateBanners() {
        repository.updateBanners();
    }

    public void updateData(String category, String type, int page, int count) {
        repository.updateData(category, type, page, count);
    }

    public ObservableList<GankBannerEntity> getBannerList() {
        return repository.getBannerList();
    }

    public ObservableList<GankDataEntity> getDataList() {
        return repository.getDataList();
    }


    public void onPageClick(View view, int position) {
        String url = getBannerList().get(position).getUrl();
        Bundle bundle = new Bundle();
        bundle.putString(ViewModelConstants.KEY_URL, url);
        Navigation.findNavController(view)
                .navigate(R.id.action_global_webFragment, bundle);
    }

    public void onItemClick(View view, int position) {
        GankDataEntity data = getDataList().get(position);
        String id = data.get_id();
        Bundle bundle = new Bundle();
        bundle.putString(ViewModelConstants.KEY_ID, id);
        Navigation.findNavController(view)
                .navigate(R.id.action_global_articleFragment, bundle);
    }

    public void onCategoryChanged(View view, Object value) {
        String category = (String) value;
        if(TextUtils.isEmpty(category)) {
            category = "All";
        }
    }
}
