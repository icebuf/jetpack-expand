package com.icebuf.jetpackex.sample.pojo;

import androidx.databinding.library.baseAdapters.BR;

import com.icebuf.jetpackex.databinding.RecyclerViewItem;
import com.icebuf.jetpackex.sample.R;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
@RecyclerViewItem(layoutId = R.layout.item_test, variableId = BR.item)
public class TestItem {

    private int index;

    private String name;

    private String description;

    private int destination;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getDestination() {
        return destination;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
