package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.constants.ViewModelConstants;
import com.icebuf.jetpackex.webkit.ProgressWebChromeClient;

public class WebFragment extends DBFragment<NewsViewModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
        String url = bundle.getString(ViewModelConstants.KEY_URL, "");
        if(TextUtils.isEmpty(url)) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
//        getBinding().setVariable(BR.client, new DefaultWebViewClient());
        getBinding().setVariable(BR.chromeClient, new ProgressWebChromeClient());
        getViewModel().setUrl(url);
    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<NewsViewModel> getVMClass() {
        return NewsViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
    }
}
