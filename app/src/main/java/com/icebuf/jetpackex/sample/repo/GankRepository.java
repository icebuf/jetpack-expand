package com.icebuf.jetpackex.sample.repo;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.pojo.GankBannerEntity;
import com.icebuf.jetpackex.sample.pojo.GankDataEntity;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 *
 * 负责处理请求逻辑和保存数据
 */
public class GankRepository {

    private volatile static GankRepository sInstance;

    private GankDataSource mGankDataSource;

    private LocalDataSource mLocalDataSource;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private ObservableList<GankBannerEntity> mBannerList = new ObservableArrayList<>();

    private ObservableList<GankDataEntity> mDataList = new ObservableArrayList<>();

    private GankRepository(LocalDataSource local, GankDataSource remote) {
        mLocalDataSource = local;
        mGankDataSource = remote;
    }

    public static GankRepository getInstance(LocalDataSource local, GankDataSource remote) {
        if(sInstance == null) {
            synchronized (GankRepository.class) {
                if(sInstance == null) {
                    sInstance = new GankRepository(local, remote);
                }
            }
        }
        return sInstance;
    }

    public void updateBanners() {
        mDisposable.add(mGankDataSource.getBanners()
                .subscribe(gankResponse -> {
                    if (gankResponse.getStatus() == 100) {
                        mBannerList.clear();
                        mBannerList.addAll(gankResponse.getData());
                    }
                }));
    }


    public void updateData(String category, String type, int page, int count) {
        mDisposable.add(mGankDataSource.getData(category, type, page, count)
                .subscribe(gankResponse -> {
                    if (gankResponse.getStatus() == 100) {
                        mDataList.clear();
                        mDataList.addAll(gankResponse.getData());
//                        Log.e("TAG", Arrays.toString(mDataList.toArray()));
                    }
                }));
    }

    public ObservableList<GankBannerEntity> getBannerList() {
        return mBannerList;
    }

    public ObservableList<GankDataEntity> getDataList() {
        return mDataList;
    }

    public void loadArticle(String id, MutableLiveData<GankArticleEntity> article) {
        loadArticleFromLocal(id, article, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                loadArticleFromRemote(id, article);
            }
        });
    }

    public void loadArticleFromLocal(String id, MutableLiveData<GankArticleEntity> liveData,
                                     Consumer<Throwable> consumer) {
        mDisposable.add(mLocalDataSource.getArticleDetails2(id)
                .subscribe(new Consumer<List<GankArticleEntity>>() {
                    @Override
                    public void accept(List<GankArticleEntity> entities) throws Exception {
                        if (entities == null || entities.isEmpty()) {
                            consumer.accept(new Exception());
                            return;
                        }
                        liveData.setValue(entities.get(0));
                    }
                }, consumer));
    }

    private void loadArticleFromRemote(String id, MutableLiveData<GankArticleEntity> liveData) {
        mDisposable.add(mGankDataSource.getArticleDetails(id)
                .subscribe(gankResponse -> {
                    if (gankResponse.getStatus() == 100) {
                        GankArticleEntity article = gankResponse.getData();
                        if(article != null) {
                            liveData.setValue(article);
                            mDisposable.add(mLocalDataSource
                                    .putArticle(article).subscribe());
                        }
                    }
                }));
    }

}
