package com.icebuf.jetpackex.databinding;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    public static RecyclerView.OnScrollListener getInstance() {
        return new RecyclerScrollListener(null);
    }

    public RecyclerScrollListener(RecyclerView view) {

    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        Log.e("TAG", "onScrollStateChanged():: newState = " + newState);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.e("TAG", "onScrolled():: dy = " + dy);
    }
}
