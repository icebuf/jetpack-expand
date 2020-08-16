package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.pojo.NewsBean;
import com.icebuf.jetpackex.sample.repo.ToutiaoRepository;
import com.icebuf.jetpackex.sample.util.Injection;

public class RecycleViewModel extends ViewModel {

    ToutiaoRepository repository;

    public RecycleViewModel() {
        repository = Injection.provideToutiaoRepo();
    }

    public ObservableList<NewsBean> getTopNews() {
        return repository.getTopNews();
    }

    public LiveData<Integer> getTopNewsCount() {
        return repository.getTopNewsCount();
    }

    public void onUpdateNewsList(int num) {
        repository.updateTopNews(num);
    }

    public void onNewsItemClick(View view, int position) {
        String newsUrl = getTopNews().get(position).getUrl();
        Bundle bundle = new Bundle();
        bundle.putString("url", newsUrl);
        Navigation.findNavController(view).navigate(R.id.action_recycleFragment_to_newsFragment, bundle);
    }

}
