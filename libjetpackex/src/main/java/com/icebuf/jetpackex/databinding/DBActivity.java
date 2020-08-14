package com.icebuf.jetpackex.databinding;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/10
 * E-mail：bflyff@hotmail.com
 *
 * DataBinding的简单模版Activity
 */
public abstract class DBActivity<VM extends ViewModel> extends AppCompatActivity {

    private ViewDataBinding mBinding;
    private VM mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModel = new ViewModelProvider(this, getVMFactory()).get(getVMClass());
        mBinding.setVariable(getVariableId(), mViewModel);
        mBinding.setLifecycleOwner(this);
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }

    public VM getViewModel() {
        return mViewModel;
    }

    @NonNull
    protected abstract Class<VM> getVMClass();

    @NonNull
    protected ViewModelProvider.Factory getVMFactory() {
        return new ViewModelProvider.NewInstanceFactory();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @LayoutRes
    protected abstract int getVariableId();
}
