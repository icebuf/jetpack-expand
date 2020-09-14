package com.icebuf.jetpackex.sample.repo.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/17
 * E-mail：bflyff@hotmail.com
 */
@Database(entities = {TianNewsEntity.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase  extends RoomDatabase {

    private static NewsDatabase INSTANCE;

    public static NewsDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsDatabase.class, "top_news.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TopNewsDao topNewsDao();
}
