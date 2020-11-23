package com.icebuf.jetpackex;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * OnItemFocusListener
 * <p>
 * E-mail：bflyff@hotmail.com
 *
 * @author tangjie
 * @version 1.0 on 2020/8/7
 */
public interface OnItemFocusListener {

    /**
     * 列表的Item焦点变换时调用.
     *
     * @param adapter RecyclerView适配器
     * @param itemView item view
     * @param position 焦点发生变化的item在列表中的位置
     * @param hasFocus 焦点变化后的值
     */
    void onItemFocusChanged(RecyclerView.Adapter<?> adapter, View itemView,
                            int position, boolean hasFocus);
}
