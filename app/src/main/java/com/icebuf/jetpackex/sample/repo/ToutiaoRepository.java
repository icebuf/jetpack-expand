package com.icebuf.jetpackex.sample.repo;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.icebuf.jetpackex.sample.pojo.NewsBean;

import java.util.List;

public class ToutiaoRepository {

    private ToutiaoDataSource mToutiaoDataSource;

    private MutableLiveData<Integer> mTopNewsCount = new MutableLiveData<>();

    private ObservableList<NewsBean> mNewsList = new ObservableArrayList<>();

    private static volatile ToutiaoRepository sInstance;

    private ToutiaoRepository(ToutiaoDataSource dataSource) {
        mToutiaoDataSource = dataSource;
    }

    public static ToutiaoRepository getInstance(ToutiaoDataSource dataSource) {
        if(sInstance == null) {
            sInstance = new ToutiaoRepository(dataSource);
        }
        return sInstance;
    }

    public ObservableList<NewsBean> getTopNews() {
        return mNewsList;
    }

    public void updateTopNews(int num) {
        mToutiaoDataSource.updateTopNews(num)
                .subscribe(toutiaoResponse -> {
                    if (toutiaoResponse.getCode() == 200) {
                        updateTopNews(mNewsList, toutiaoResponse.getNewslist());
                    }
                }, throwable -> System.out.println(throwable.getMessage())).isDisposed();
    }

    private void updateTopNews(List<NewsBean> current, List<NewsBean> newslist) {
        if(current.isEmpty()) {
            current.addAll(newslist);
            mTopNewsCount.setValue(newslist.size());
            mTopNewsCount.setValue(null);
        } else {
            NewsBean firstNews = current.get(0);
            int index = 0;
            for (NewsBean bean : newslist) {
                if(bean.equals(firstNews)) {
                    index = newslist.indexOf(bean);
                    break;
                }
            }
            if(index > 0) {
                current.addAll(0, newslist.subList(0, index));
            }
            mTopNewsCount.setValue(index);
            mTopNewsCount.setValue(null);
        }
    }

    public LiveData<Integer> getTopNewsCount() {
        return mTopNewsCount;
    }
}
