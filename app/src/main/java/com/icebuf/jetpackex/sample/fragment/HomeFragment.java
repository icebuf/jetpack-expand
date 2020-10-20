package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends DBFragment<HomeViewModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ActionBarUtil.setHomeAsUpIndicator();
    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<HomeViewModel> getVMClass() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
}