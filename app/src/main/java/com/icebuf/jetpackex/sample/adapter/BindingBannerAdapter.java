package com.icebuf.jetpackex.sample.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.icebuf.jetpackex.databinding.RecyclerViewAdapter;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.util.ReflectUtil;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/25
 * E-mail：bflyff@hotmail.com
 */
public class BindingBannerAdapter<T, VH extends BindingBannerAdapter.BindingHolder<T>>
        extends BaseBannerAdapter<T, VH> {

    private RecyclerViewAdapter mAdapter;

    public BindingBannerAdapter() {
        mAdapter = new RecyclerViewAdapter(mList);
    }

    @Override
    protected void onBind(VH holder, T data, int position, int pageSize) {
        mAdapter.onBindViewHolder(holder.mHolder, position);
    }

    @Override
    public VH createViewHolder(@NonNull ViewGroup parent, View itemView, int viewType) {
        return (VH) new BindingHolder<>(mAdapter.onCreateViewHolder(parent, viewType));
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner2;
    }

    @Override
    protected int getViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    public static class BindingHolder<T> extends BaseViewHolder<T> {

        RecyclerViewAdapter.BindingHolder mHolder;

        public BindingHolder(@NonNull RecyclerViewAdapter.BindingHolder holder) {
            super(holder.itemView);
            mHolder = holder;
        }

        @Override
        public void bindData(T data, int position, int pageSize) {

        }
    }

    @BindingAdapter(value = "adapterDataSet")
    public static <T, VH extends BaseViewHolder<T>>
    void setAdapterDataSet(BannerViewPager<T, VH> view, List<T> data) {
        BaseBannerAdapter<?, ?> adapter = view.getAdapter();
        if(adapter instanceof BindingBannerAdapter) {
            view.refreshData(data);
        } else if(adapter == null) {
            view.setAdapter((BaseBannerAdapter<T, VH>) new BindingBannerAdapter<>());
            view.create(data);
        }
    }

    @BindingAdapter(value = {"onPageClick", "pageAttrClick"}, requireAll = false)
    public static void setOnPageClick(BannerViewPager<?, ?> view, final OnPageClickListener listener,
                                        final InverseBindingListener attrChange) {
        if (attrChange == null) {
            BannerViewPager.OnPageClickListener clickListener = new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    listener.onPageClick(view, position);
                }
            };
            BaseBannerAdapter<?,?> adapter = view.getAdapter();
            if(adapter == null) {
                view.setOnPageClickListener(clickListener);
            } else {
                ReflectUtil.setSuperField(adapter, "mPageClickListener", clickListener);
            }
        } else {
            BannerViewPager.OnPageClickListener clickListener = new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    if (listener != null) {
                        listener.onPageClick(view, position);
                    }
                    attrChange.onChange();
                }
            };
            BaseBannerAdapter<?,?> adapter = view.getAdapter();
            if(adapter == null) {
                view.setOnPageClickListener(clickListener);
            } else {
                ReflectUtil.setSuperField(adapter, "mPageClickListener", clickListener);
            }
        }
    }

    public interface OnPageClickListener {

        void onPageClick(BannerViewPager<?, ?> viewPager, int position);
    }
}
