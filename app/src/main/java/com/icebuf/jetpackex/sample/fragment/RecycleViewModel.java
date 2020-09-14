package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableList;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;
import com.icebuf.jetpackex.sample.repo.TianRepository;
import com.icebuf.jetpackex.viewmodel.Result;


public class RecycleViewModel extends ViewModel {


    TianRepository repository;

    @ViewModelInject
    public RecycleViewModel(TianRepository repo) {
        repository = repo;
    }

    /**
     * 获取头条新闻列表
     * @return 返回的ObservableList可以与RecyclerView绑定，当ObservableList数据改变
     * 的时候，自动通知RecyclerView刷新对应的条目
     */
    public ObservableList<TianNewsEntity> getTopNews() {
        return repository.getTopNews();
    }

    /**
     * 获取新闻获取状态的结果
     * @return 可以对应为其他UI更新获取新闻过程中的信息
     */
    public LiveData<Result<Integer>> getTopNewsCount() {
        return repository.getTopNewsCount();
    }

    public void requestTopNewsList(int num) {
        repository.requestTopNewsList(num);
    }

    public void onNewsItemClick(View view, int position) {
        String newsUrl = getTopNews().get(position).getUrl();
        Bundle bundle = new Bundle();
        bundle.putString("url", newsUrl);
        Navigation.findNavController(view).navigate(R.id.action_recycleFragment_to_newsFragment, bundle);
    }

}
