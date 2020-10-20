package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.util.IceUtil;
import com.icebuf.jetpackex.viewmodel.ResultObserver;
import com.icebuf.testcase.ItemGroup;
import com.icebuf.testcase.ResItem;

import dagger.hilt.android.AndroidEntryPoint;

@ItemGroup("testcase")
@ResItem(name = R.string.test_name_recycle_view,
        description = R.string.test_desc_recycle_view,
        destination = R.id.action_homeFragment_to_recycleFragment)
@AndroidEntryPoint
public class RecycleFragment extends DBFragment<RecycleViewModel> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        RecyclerView recyclerView = view.findViewById(R.id.rv_news);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        SwipeRefreshLayout layout = view.findViewById(R.id.swl_layout);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getViewModel().requestTopNewsList(20);
            }
        });
        getViewModel().getTopNewsCount().observe(getViewLifecycleOwner(), new ResultObserver<Integer>() {

            @Override
            protected void onLoading(Integer data) {
                super.onLoading(data);
                IceUtil.showToast(requireContext(), R.string.loading_top_news);
            }

            @Override
            protected boolean onSuccess(Integer data) {
                Log.e(TAG, "onSuccess():: " + data);
                if(data == null || data == 0) {
                    IceUtil.showToast(requireContext(), R.string.already_up_to_date);
                } else {
                    IceUtil.showToast(requireContext(), getString(R.string.update_news_count, data));
                }
                layout.setRefreshing(false);
                return true;
            }

            @Override
            protected void onError(Integer data, String message) {
                super.onError(data, message);
                IceUtil.showToast(requireContext(), message);
                layout.setRefreshing(false);
            }
        });
        if(getViewModel().getTopNews().isEmpty()) {
            getViewModel().requestTopNewsList(20);
        }

    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<RecycleViewModel> getVMClass() {
        return RecycleViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycle;
    }
}
