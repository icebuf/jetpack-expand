package com.icebuf.jetpackex.sample.fragment;

import androidx.databinding.library.baseAdapters.BR;

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
