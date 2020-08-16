package com.icebuf.jetpackex.sample.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.DefaultWebViewClient;
import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;

public class NewsFragment extends DBFragment<NewsViewModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
        String url = bundle.getString("url", "");
        if(TextUtils.isEmpty(url)) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
        getBinding().setVariable(BR.client, new DefaultWebViewClient());
        getViewModel().setNewsUrl(url);
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
        return R.layout.fragment_news;
    }
}
