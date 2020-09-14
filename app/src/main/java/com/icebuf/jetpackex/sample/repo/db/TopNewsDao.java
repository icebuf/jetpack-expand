package com.icebuf.jetpackex.sample.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/17
 * E-mail：bflyff@hotmail.com
 */
@Dao
public interface TopNewsDao {

    @Query("SELECT * FROM TianNewsEntity LIMIT :num")
    Observable<List<TianNewsEntity>> getTopNews(int num);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNews(TianNewsEntity news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNews(List<TianNewsEntity> newsList);

    @Query("DELETE FROM TianNewsEntity")
    void deleteAllNews();
}
