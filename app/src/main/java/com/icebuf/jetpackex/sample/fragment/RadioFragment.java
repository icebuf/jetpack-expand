package com.icebuf.jetpackex.sample.fragment;

import androidx.databinding.library.baseAdapters.BR;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
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
