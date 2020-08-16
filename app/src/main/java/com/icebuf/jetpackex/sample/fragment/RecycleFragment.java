package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.testcase.ItemGroup;
import com.icebuf.testcase.ResItem;

@ItemGroup("testcase")
@ResItem(name = R.string.test_name_recycle_view,
        description = R.string.test_desc_recycle_view,
        destination = R.id.action_homeFragment_to_recycleFragment)
public class RecycleFragment extends DBFragment<RecycleViewModel> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        RecyclerView recyclerView = view.findViewById(R.id.rv_news);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"

        getViewModel().getTopNewsCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer == null) {
                    return;
                }
                if(integer == 0) {
                    Toast.makeText(requireContext(), R.string.already_up_to_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(requireContext(),
                        getString(R.string.update_news_count, integer), Toast.LENGTH_SHORT).show();
            }
        });
        if(getViewModel().getTopNews().isEmpty()) {
            getViewModel().onUpdateNewsList(20);
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
