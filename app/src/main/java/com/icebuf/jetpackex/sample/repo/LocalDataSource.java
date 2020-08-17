package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.NewsBean;
import com.icebuf.jetpackex.sample.repo.db.TopNewsDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/17
 * E-mail：bflyff@hotmail.com
 */
public class LocalDataSource {

    private TopNewsDao mTopNewsDao;

    public LocalDataSource(TopNewsDao dao) {
        mTopNewsDao = dao;
    }

    public Observable<List<NewsBean>> getTopNews(int num) {
        return mTopNewsDao.getTopNews(num);
    }

    public Completable putNews(List<NewsBean> beans) {
        return mTopNewsDao.insertNews(beans);
    }

    public void deleteAll() {
        mTopNewsDao.deleteAllUsers();
    }
}
