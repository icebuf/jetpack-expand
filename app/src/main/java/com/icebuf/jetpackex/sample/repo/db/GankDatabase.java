package com.icebuf.jetpackex.sample.repo.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/17
 * E-mail：bflyff@hotmail.com
 */
@Database(entities = {GankArticleEntity.class}, version = 1, exportSchema = false)
public abstract class GankDatabase extends RoomDatabase {

    private static volatile GankDatabase INSTANCE;

    public static GankDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (GankDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GankDatabase.class, "gank.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GankArticleDao articleDao();
}
