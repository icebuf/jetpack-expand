package com.icebuf.jetpackex.sample.repo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;

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
public interface GankArticleDao {

    @Query("SELECT * FROM GankArticleEntity WHERE _id = :id")
    Observable<GankArticleEntity> findById(String id);

    @Query("SELECT * FROM GankArticleEntity WHERE _id = :id")
    Observable<List<GankArticleEntity>> findById2(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(GankArticleEntity news);

    @Query("DELETE FROM GankArticleEntity")
    void clear();
}
