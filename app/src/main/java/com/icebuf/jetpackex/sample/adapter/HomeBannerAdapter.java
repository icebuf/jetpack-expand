package com.icebuf.jetpackex.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.pojo.GankBannerEntity;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/25
 * E-mail：bflyff@hotmail.com
 */
public class HomeBannerAdapter extends BaseBannerAdapter<GankBannerEntity, HomeBannerAdapter.BannerHolder> {


    public HomeBannerAdapter(ObservableList<GankBannerEntity> bannerList) {
        mList = bannerList;
        bannerList.addOnListChangedCallback(new ObservableListCallback<>(this));
    }

    public HomeBannerAdapter() {

    }

    @Override
    protected void onBind(BannerHolder holder, GankBannerEntity data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }


    @Override
    public BannerHolder createViewHolder(@NonNull ViewGroup parent, View itemView, int viewType) {
        return new BannerHolder(itemView);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner2;
    }

    public static class BannerHolder extends BaseViewHolder<GankBannerEntity> {

        private TextView titleView;

        private ImageView imageView;

        public BannerHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.tv_describe);
            imageView = itemView.findViewById(R.id.iv_image);
        }

        @Override
        public void bindData(GankBannerEntity data, int position, int pageSize) {
            titleView.setText(data.getTitle());
            Glide.with(imageView)
                    .load(data.getImage())
                    .placeholder(R.drawable.bg_image_error)
                    .into(imageView);
        }
    }

    static class ObservableListCallback<T> extends
            ObservableList.OnListChangedCallback<ObservableList<T>> {

        private RecyclerView.Adapter<BannerHolder> mAdapter;

        public ObservableListCallback(RecyclerView.Adapter<BannerHolder> adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender,
                                        int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender,
                                     int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemRangeChanged(fromPosition, itemCount);
            mAdapter.notifyItemRangeChanged(toPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public static class ObservableListCallback2<T> extends
            ObservableList.OnListChangedCallback<ObservableList<T>> {

        private BannerViewPager<T, ?> mViewPager;

        public ObservableListCallback2(BannerViewPager<T, ?> viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            mViewPager.refreshData(sender);
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mViewPager.refreshData(sender);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender,
                                        int positionStart, int itemCount) {
            mViewPager.refreshData(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender,
                                     int fromPosition, int toPosition, int itemCount) {
            mViewPager.refreshData(sender);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mViewPager.refreshData(sender);
        }
    }
}
