package com.icebuf.jetpackex.databinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/12
 * E-mail：bflyff@hotmail.com
 */
public abstract class DBFragment<VM extends ViewModel> extends Fragment {

    private ViewDataBinding mBinding;

    protected VM mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(getVMClass());
        mBinding.setVariable(getVariableId(), mViewModel);
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }

    public VM getViewModel() {
        return mViewModel;
    }

//    public <T extends ViewModel> T getActivityViewModel(Class<T> clazz) {
//        return new ViewModelProvider(requireActivity(), null).get(clazz);
//    }

    protected abstract int getVariableId();

    protected abstract Class<VM> getVMClass();

    @LayoutRes
    protected abstract int getLayoutId();

}
