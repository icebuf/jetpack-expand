package com.icebuf.jetpackex.sample.repo;

import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.pojo.TestItem;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class TestListRepository {

    private ObservableArrayList<TestItem> testItems;

    private static volatile TestListRepository sInstance;

    private TestListRepository() {}


    public static TestListRepository getInstance() {
        if(sInstance == null) {
            sInstance = new TestListRepository();
        }
        return sInstance;
    }

    public ObservableList<TestItem> getTestList(Context context) {
        if(testItems == null) {
            testItems = new ObservableArrayList<>();
            loadTestItems(context, testItems);
        }
        return testItems;
    }

    private void loadTestItems(Context context, List<TestItem> testItems) {
        TestItem testItem = new TestItem();
        testItem.setName("RadioGroup");
        testItem.setDescription("RadioGroup ...");
        testItem.setDestination(R.id.action_homeFragment_to_radioFragment);
        testItems.add(testItem);
    }

}
