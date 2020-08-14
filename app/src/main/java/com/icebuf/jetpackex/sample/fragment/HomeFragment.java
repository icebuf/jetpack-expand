package com.icebuf.jetpackex.sample.fragment;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends DBFragment<HomeViewModel> {

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<HomeViewModel> getVMClass() {
        return HomeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getVMFactory() {
        return new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
}