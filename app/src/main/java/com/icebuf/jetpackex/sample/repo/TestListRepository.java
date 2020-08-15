package com.icebuf.jetpackex.sample.repo;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.icebuf.jetpackex.sample.pojo.TestItem;
import com.icebuf.testcase.ItemEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class TestListRepository {

    private Map<String, ObservableList<TestItem>> testItemsMap;

    private static volatile TestListRepository sInstance;

    private TestListRepository() {}


    public static TestListRepository getInstance() {
        if(sInstance == null) {
            sInstance = new TestListRepository();
        }
        return sInstance;
    }

    public ObservableList<TestItem> getTestList(Context context, String group) {
        if(testItemsMap == null) {
            testItemsMap = new HashMap<>();
        }
        loadTestItems(context, group);
        return testItemsMap.get(group);
    }

    private void loadTestItems(Context context, String group) {
        ObservableList<TestItem> testItems = testItemsMap.get(group);
        if(testItems != null) {
            return;
        }
        testItems = new ObservableArrayList<>();
        testItemsMap.put(group, testItems);
        AssetManager assetManager = context.getAssets();
        try {
            if(group.toLowerCase().lastIndexOf(".json") < 0) {
                group += ".json";
            }
            InputStream is = assetManager.open(group);
            JsonArray array = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonArray();
            Gson gson = new Gson();
            for (JsonElement element : array) {
                ItemEntity entity = gson.fromJson(element, ItemEntity.class);
                TestItem testItem = getTestItem(context, entity);
                testItems.add(testItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assetManager.close();
        Collections.sort(testItems, new Comparator<TestItem>() {
            @Override
            public int compare(TestItem o1, TestItem o2) {
                int val = o1.getIndex() - o2.getIndex();
                if(val != 0) {
                    return val;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private TestItem getTestItem(Context context, ItemEntity entity) {
        TestItem testItem = new TestItem();
        switch (entity.getType()) {
            case DEFAULT:
                testItem.setName(entity.getName());
                testItem.setDescription(entity.getDescription());
                break;
            case RES:
                testItem.setName(context.getString(entity.getNameId()));
                testItem.setDescription(context.getString(entity.getDescriptionId()));
                break;
        }
        testItem.setIndex(entity.getIndex());
        testItem.setDestination(entity.getDestination());
        return testItem;
    }

}
