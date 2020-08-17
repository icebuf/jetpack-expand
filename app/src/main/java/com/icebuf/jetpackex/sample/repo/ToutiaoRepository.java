package com.icebuf.jetpackex.sample.repo;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.icebuf.jetpackex.sample.pojo.NewsBean;
import com.icebuf.jetpackex.sample.util.ConvertUtil;
import com.icebuf.jetpackex.viewmodel.Result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Repository 存储全局数据，生命周期大于Activity
 */
public class ToutiaoRepository {

    private static final String TAG = ToutiaoRepository.class.getSimpleName();

    private ToutiaoDataSource mToutiaoDataSource;

    private LocalDataSource mLocalDataSource;

    private MutableLiveData<Result<Integer>> mTopNewsCount = new MutableLiveData<>();

    private ObservableList<NewsBean> mNewsList = new ObservableArrayList<>();

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private static volatile ToutiaoRepository sInstance;

    private ToutiaoRepository(ToutiaoDataSource dataSource, LocalDataSource localDataSource) {
        mToutiaoDataSource = dataSource;
        mLocalDataSource = localDataSource;
    }

    public static ToutiaoRepository getInstance(ToutiaoDataSource dataSource, LocalDataSource localDataSource) {
        if(sInstance == null) {
            sInstance = new ToutiaoRepository(dataSource, localDataSource);
        }
        return sInstance;
    }

    public ObservableList<NewsBean> getTopNews() {
        return mNewsList;
    }

    public void requestTopNewsList(int num) {
        Result<?> result = mTopNewsCount.getValue();
        if(result != null && !result.isInvalid()
                && result.status() == Result.Status.LOADING) {
            return;
        }
        mTopNewsCount.setValue(Result.loading());
        if(mNewsList.isEmpty()) {
            loadLocalNews(num);
        } else {
            loadRemoteNews(num);
        }
    }

    private void loadRemoteNews(int num) {
        Log.w(TAG, "load form remote: " + num);
        mDisposable.add(mToutiaoDataSource.updateTopNews(num)
                .subscribe(toutiaoResponse -> {
                    if (toutiaoResponse.getCode() == 200) {
                        updateTopNews(toutiaoResponse.getNewslist());
                    } else {
                        mTopNewsCount.setValue(Result.error(toutiaoResponse.getMsg()));
                    }
                }, throwable -> mTopNewsCount.setValue(Result.error(throwable.getMessage()))));
    }

    private void loadLocalNews(int num) {
        Log.w(TAG, "load form local: " + num);
        mDisposable.add(mLocalDataSource.getTopNews(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsBean>>() {
                    @Override
                    public void accept(List<NewsBean> beans) throws Exception {
                        if(beans == null || beans.isEmpty()) {
                            //本地没有数据
                            loadRemoteNews(num);
                            return;
                        }
                        //按发布时间排序
                        Collections.sort(beans, new Comparator<NewsBean>() {
                            @Override
                            public int compare(NewsBean o1, NewsBean o2) {
                                return o1.getCtime().compareTo(o2.getCtime());
                            }
                        });
                        mNewsList.addAll(beans);
                        Log.w(TAG, "loaded local news: " + beans.size());
                        NewsBean topNews = beans.get(0);
                        long date = ConvertUtil.date2Long("yyyy-MM-dd H:m:s", topNews.getCtime());
                        if(System.currentTimeMillis() / 1000 - date > TimeUnit.MINUTES.toSeconds(30)) {
                            //有足够的数据但是过期了
                            loadRemoteNews(num);
                        } else if(mNewsList.size() < num) {
                            //本地有数据但是不够要刷新的
                            loadRemoteNews(num - mNewsList.size());
                        }
                    }
                }));
    }

    private void updateTopNews(List<NewsBean> newslist) {
        if(mNewsList.isEmpty()) {
            Log.w(TAG, "loaded remote news: " + newslist.size());
            mNewsList.addAll(newslist);
            mDisposable.add(mLocalDataSource.putNews(newslist)
                    .subscribeOn(Schedulers.io())
                    .subscribe());
            mTopNewsCount.setValue(Result.success(newslist.size()));
        } else {
            NewsBean firstNews = mNewsList.get(0);
            int index = 0;
            for (NewsBean bean : newslist) {
                if(bean.equals(firstNews)) {
                    index = newslist.indexOf(bean);
                    break;
                }
            }
            if(index > 0) {
                List<NewsBean> sub = newslist.subList(0, index);
                mNewsList.addAll(0, sub);
                mDisposable.add(mLocalDataSource.putNews(newslist)
                        .subscribeOn(Schedulers.io())
                        .subscribe());
                Log.w(TAG, "loaded remote news: " + sub.size());
            }
            mTopNewsCount.setValue(Result.success(index));
        }
    }

    public LiveData<Result<Integer>> getTopNewsCount() {
        return mTopNewsCount;
    }
}
