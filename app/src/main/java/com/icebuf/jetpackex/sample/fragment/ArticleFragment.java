package com.icebuf.jetpackex.sample.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.databinding.DBFragment;
import com.icebuf.jetpackex.sample.R;
import com.icebuf.jetpackex.sample.constants.ViewModelConstants;
import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.util.ActionBarUtil;
import com.icebuf.jetpackex.sample.util.StatusBarUtil;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/29
 * E-mail：bflyff@hotmail.com
 */
@AndroidEntryPoint
public class ArticleFragment extends DBFragment<ArticleViewModel> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView textView = (TextView) view.findViewById(R.id.tv_describe);
//        Context context = view.getContext();
        Bundle bundle = getArguments();
        if(bundle == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
        String articleId = bundle.getString(ViewModelConstants.KEY_ID);
        if(TextUtils.isEmpty(articleId)) {
            Navigation.findNavController(view).popBackStack();
            return;
        }
        getViewModel().loadArticle(articleId);
        getViewModel().getArticle().observe(getViewLifecycleOwner(), new Observer<GankArticleEntity>() {
            @Override
            public void onChanged(GankArticleEntity entity) {
                if(entity != null) {
                    if(TextUtils.isEmpty(entity.getContent())) {
                        getViewModel().setContent(entity.getMarkdown());
                    } else {
                        getViewModel().setContent(entity.getContent());
                    }
                } else {
                    getViewModel().setContent(null);
                }
            }
        });
    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    protected Class<ArticleViewModel> getVMClass() {
        return ArticleViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article;
    }

    private int[] mPaddingData = new int[4];

    private Drawable mBackground;

    @Override
    public void onStart() {
        super.onStart();

        StatusBarUtil.setTranslucentStatus(requireActivity());
        StatusBarUtil.setStatusBarDarkTheme(requireActivity(), true);
        ActionBarUtil.with(requireActivity()).hide();

        View view = requireActivity().findViewById(R.id.fl_main);
        applyRootPadding(view);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                applyRootBackground(view);
            }
        }, 250);

//        DrawerArrowDrawable drawable = new DrawerArrowDrawable(requireContext());
//        drawable.setColor(Color.WHITE);
//        drawable.setProgress(1f);
//        ActionBarUtil.setHomeAsUpIndicator(requireActivity(), drawable);
//        ActionBarUtil.setCustomView(requireActivity(), R.layout.view_login_join_title);
//        StatusBarUtil.setStatusBarDarkTheme(requireActivity(), false);
//        if(mBackground == null) {
//            mBackground = getResources().getDrawable(R.drawable.v2_bg_login_join, null);
//        }
//        View view = requireActivity().findViewById(R.id.fl_root);
//        mBackupBackground = view.getBackground();
//        view.setBackground(mBackground);
    }

    private void applyRootBackground(View view) {
        mBackground = view.getBackground();
        view.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.white, null));
    }

    private void applyRootPadding(View view) {
        mPaddingData[0] = view.getPaddingStart();
        mPaddingData[1] = view.getPaddingTop();
        mPaddingData[2] = view.getPaddingEnd();
        mPaddingData[3] = view.getPaddingBottom();
        int statusHeight = StatusBarUtil.getStatusBarHeight(view.getContext());
        view.setPadding(mPaddingData[0], statusHeight, mPaddingData[2], mPaddingData[3]);
    }

    private void restoreRootPadding(View view) {
        view.setPadding(mPaddingData[0], mPaddingData[1],
                mPaddingData[2], mPaddingData[3]);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void restoreRootBackground(View view) {
        view.setBackground(mBackground);
    }

    @Override
    public void onStop() {
        super.onStop();

        ActionBarUtil.with(requireActivity()).show();

        View view = requireActivity().findViewById(R.id.fl_main);
        restoreRootPadding(view);
        restoreRootBackground(view);
//        View view = requireActivity().findViewById(R.id.fl_root);
//        view.setBackground(mBackupBackground);
//        ActionBarUtil.setDisplayShowCustomEnabled(requireActivity(), false);
    }

}
