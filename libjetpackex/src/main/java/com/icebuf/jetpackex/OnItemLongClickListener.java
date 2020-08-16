package com.icebuf.jetpackex;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public interface OnItemLongClickListener {

    boolean onItemLongClick(RecyclerView.Adapter<?> adapter, View v, int position);
}
