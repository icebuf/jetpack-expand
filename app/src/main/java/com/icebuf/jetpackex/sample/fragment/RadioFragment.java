package com.icebuf.jetpackex.sample.fragment;

import androidx.databinding.library.baseAdapters.BR;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.testcase.ItemGroup;
import com.icebuf.testcase.ResItem;


/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
@ItemGroup("testcase")
@ResItem(name = R.string.test_name_radio_group,
        description = R.string.test_desc_radio_group,
        destination = R.id.action_homeFragment_to_radioFragment)
public class RadioFragment extends DBFragment<RadioViewModel> {

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<RadioViewModel> getVMClass() {
        return RadioViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_radio;
    }
}
