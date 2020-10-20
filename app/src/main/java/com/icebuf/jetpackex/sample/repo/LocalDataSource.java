package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;
import com.icebuf.jetpackex.sample.repo.db.GankArticleDao;
import com.icebuf.jetpackex.sample.repo.db.GankDatabase;
import com.icebuf.jetpackex.sample.repo.db.NewsDatabase;
import com.icebuf.jetpackex.sample.repo.db.TopNewsDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/17
 * E-mail：bflyff@hotmail.com
 */
public class LocalDataSource extends DataSource{

    TopNewsDao mTopNewsDao;

    GankArticleDao mArticleDao;

    @Inject
    public LocalDataSource(NewsDatabase database, GankDatabase gankDatabase) {
        mTopNewsDao = database.topNewsDao();
        mArticleDao = gankDatabase.articleDao();
    }

    public Observable<List<TianNewsEntity>> getTopNews(int num) {
        return mTopNewsDao.getTopNews(num);
    }

    public Completable putNews(List<TianNewsEntity> beans) {
        return mTopNewsDao.insertNews(beans);
    }

    public void deleteAll() {
        mTopNewsDao.deleteAllNews();
    }

    public Observable<GankArticleEntity> getArticleDetails(String id) {
        return mArticleDao.findById(id);
    }

    public Observable<List<GankArticleEntity>> getArticleDetails2(String id) {
        return mArticleDao.findById2(id);
    }

    public Completable putArticle(GankArticleEntity article) {
        return subAndObs(mArticleDao.insert(article));
    }
}
