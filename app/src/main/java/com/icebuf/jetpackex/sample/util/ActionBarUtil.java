package com.icebuf.jetpackex.sample.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.icebuf.jetpackex.sample.R;

import java.lang.reflect.Field;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/24
 * E-mail：bflyff@hotmail.com
 */
public class ActionBarUtil {

    private static final String TAG = ActionBarUtil.class.getSimpleName();

    public static final int KEY_ACTION_BAR_LISTENER = -10001;

    /**
     * 获取一个兼容App和Androidx的ActionBar
     * @param activity the activity
     * @return 一个兼容actionBar的实例
     */
    public static ActionBarCompat with(Activity activity) {
        if(activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if(actionBar != null) {
                return new SupportActionBarWrapper(actionBar);
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if(actionBar != null) {
                return new AppActionBarWrapper(actionBar);
            }
        }
        throw new RuntimeException("un support actionBar for " + activity.getClass().getName());
    }

    /**
     * 将actionBar的title设为水平居中
     * @param activity the activity
     */
    public static void setTitleInCenter(Activity activity, boolean enable) {
        View decorView = activity.getWindow().getDecorView();
        Toolbar actionBar = decorView.findViewById(R.id.action_bar);
        if(actionBar == null) {
            Log.w(TAG, "The activity of " + activity.getClass().getSimpleName()
                    + " is not support action bar!");
            return;
        }
        TextView titleView = getFieldValue(actionBar, "mTitleTextView");
        if(titleView == null) {
            Log.w(TAG, "The action bar of " + activity.getClass().getSimpleName()
                    + " was not found title text view!");
            return;
        }
        ViewTreeObserver.OnGlobalLayoutListener listener;
        Object obj = titleView.getTag(KEY_ACTION_BAR_LISTENER);
        if(obj instanceof ViewTreeObserver.OnGlobalLayoutListener) {
            listener = (ViewTreeObserver.OnGlobalLayoutListener) obj;
        } else {
            listener = new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    int centerX = actionBar.getWidth() / 2;
                    int titleCenterX = (titleView.getLeft() + titleView.getRight()) / 2;
                    //Log.e("TAG", "text = " + titleView.getText()
                    //       + " centerX = " + centerX
                    //       + " titleCenterX = " + titleCenterX
                    //       + " translationX = " + titleView.getTranslationX());
                    if (centerX != titleCenterX) {
                        titleView.setTranslationX(centerX - titleCenterX);
                    } else {
                        titleView.setTranslationX(0f);
                    }
                }
            };
        }
        if(enable) {
            titleView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
            titleView.setTag(KEY_ACTION_BAR_LISTENER, listener);
            activity.getApplication().registerActivityLifecycleCallbacks(new LifecycleHandler(activity));
            Log.d(TAG, "add listener of title in center!");
        } else {
            titleView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            titleView.setTag(KEY_ACTION_BAR_LISTENER, null);
            Log.d(TAG, "remove listener of title in center!");
        }
    }

    /**
     * 自动解除居中监听的绑定
     */
    static class LifecycleHandler implements
            Application.ActivityLifecycleCallbacks {

        private Activity mActivity;

        public LifecycleHandler(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            if(mActivity == activity) {
                setTitleInCenter(activity, false);
                mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
                mActivity = null;
            }
        }
    }

    private static <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ActionBarCompat {

        ActionBarCompat setCustomView(View view);

        ActionBarCompat setCustomView(View view, LayoutParams layoutParams);

        ActionBarCompat setCustomView(int id);

        ActionBarCompat setIcon(@DrawableRes int resId);

        ActionBarCompat setIcon(Drawable icon);

        ActionBarCompat setLogo(@DrawableRes int resId);

        ActionBarCompat setLogo(Drawable logo);

        ActionBarCompat setTitle(CharSequence title);

        ActionBarCompat setTitle(@StringRes int resId);

        ActionBarCompat setSubtitle(CharSequence subtitle);

        ActionBarCompat setSubtitle(int resId);

        ActionBarCompat setDisplayOptions(@ActionBar.DisplayOptions int options);

        ActionBarCompat setDisplayOptions(@ActionBar.DisplayOptions int options,
                                          @ActionBar.DisplayOptions int mask);

        ActionBarCompat setDisplayUseLogoEnabled(boolean useLogo);

        ActionBarCompat setDisplayShowHomeEnabled(boolean showHome);

        ActionBarCompat setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

        ActionBarCompat setDisplayShowTitleEnabled(boolean showTitle);

        ActionBarCompat setDisplayShowCustomEnabled(boolean showCustom);

        ActionBarCompat setBackgroundDrawable(@Nullable Drawable d);

        ActionBarCompat setStackedBackgroundDrawable(Drawable d);

        ActionBarCompat setSplitBackgroundDrawable(Drawable d);

        View getCustomView();

        CharSequence getTitle();

        CharSequence getSubtitle();

        @ActionBar.DisplayOptions
        int getDisplayOptions();

        int getHeight();

        ActionBarCompat show();

        ActionBarCompat hide();

        boolean isShowing();

        ActionBarCompat addOnMenuVisibilityListener(OnMenuVisibilityListener listener);

        ActionBarCompat removeOnMenuVisibilityListener(OnMenuVisibilityListener listener);

        ActionBarCompat setHomeButtonEnabled(boolean enabled);

        Context getThemedContext();

        ActionBarCompat setHomeAsUpIndicator(Drawable drawable);

        ActionBarCompat setHomeAsUpIndicator(@DrawableRes int resId);

        ActionBarCompat setHomeActionContentDescription(@Nullable CharSequence description);

        ActionBarCompat setHomeActionContentDescription(@StringRes int resId);

        ActionBarCompat setHideOnContentScrollEnabled(boolean hideOnContentScroll);

        boolean isHideOnContentScrollEnabled();

        int getHideOffset();

        ActionBarCompat setHideOffset(int offset);

        ActionBarCompat setElevation(float elevation);

        float getElevation();

        Object getmActionBar();
    }

    public interface OnMenuVisibilityListener extends ActionBar.OnMenuVisibilityListener,
            android.app.ActionBar.OnMenuVisibilityListener{

    }

    public static class LayoutParams {

        private ActionBar.LayoutParams mParams;

        private android.app.ActionBar.LayoutParams mAppParams;

        public LayoutParams(ActionBar.LayoutParams params) {
            this.mParams = params;
        }

        public LayoutParams(android.app.ActionBar.LayoutParams appParams) {
            this.mAppParams = appParams;
        }
    }

    static class SupportActionBarWrapper implements ActionBarCompat{

        private final ActionBar mActionBar;

        public SupportActionBarWrapper(ActionBar actionBar) {
            this.mActionBar = actionBar;
        }

        @Override
        public ActionBarCompat setHomeAsUpIndicator(Drawable drawable) {
            mActionBar.setHomeAsUpIndicator(drawable);
            return this;
        }

        @Override
        public ActionBarCompat setHomeAsUpIndicator(int resId) {
            mActionBar.setHomeAsUpIndicator(resId);
            return this;
        }

        @Override
        public ActionBarCompat setHomeActionContentDescription(@Nullable CharSequence description) {
            mActionBar.setHomeActionContentDescription(description);
            return this;
        }

        @Override
        public ActionBarCompat setHomeActionContentDescription(int resId) {
            mActionBar.setHomeActionContentDescription(resId);
            return this;
        }

        @Override
        public ActionBarCompat setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
            mActionBar.setHideOnContentScrollEnabled(hideOnContentScroll);
            return this;
        }

        @Override
        public boolean isHideOnContentScrollEnabled() {
            return mActionBar.isHideOnContentScrollEnabled();
        }

        @Override
        public int getHideOffset() {
            return mActionBar.getHideOffset();
        }

        @Override
        public ActionBarCompat setHideOffset(int offset) {
            mActionBar.setHideOffset(offset);
            return this;
        }

        @Override
        public ActionBarCompat setElevation(float elevation) {
            mActionBar.setElevation(elevation);
            return this;
        }

        @Override
        public float getElevation() {
            return mActionBar.getElevation();
        }

        @Override
        public Object getmActionBar() {
            return mActionBar;
        }

        @Override
        public ActionBarCompat setDisplayShowCustomEnabled(boolean enable) {
            mActionBar.setDisplayShowCustomEnabled(enable);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(View view) {
            mActionBar.setCustomView(view);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(View view, LayoutParams layoutParams) {
            mActionBar.setCustomView(view, layoutParams.mParams);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(int id) {
            mActionBar.setCustomView(id);
            return this;
        }

        @Override
        public ActionBarCompat setIcon(int resId) {
            mActionBar.setIcon(resId);
            return this;
        }

        @Override
        public ActionBarCompat setIcon(Drawable icon) {
            mActionBar.setIcon(icon);
            return this;
        }

        @Override
        public ActionBarCompat setLogo(int resId) {
            mActionBar.setLogo(resId);
            return this;
        }

        @Override
        public ActionBarCompat setLogo(Drawable logo) {
            mActionBar.setLogo(logo);
            return this;
        }

        @Override
        public ActionBarCompat setTitle(CharSequence title) {
            mActionBar.setTitle(title);
            return this;
        }

        @Override
        public ActionBarCompat setTitle(int resId) {
            mActionBar.setTitle(resId);
            return this;
        }

        @Override
        public ActionBarCompat setSubtitle(CharSequence subtitle) {
            mActionBar.setSubtitle(subtitle);
            return this;
        }

        @Override
        public ActionBarCompat setSubtitle(int resId) {
            mActionBar.setSubtitle(resId);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayOptions(int options) {
            mActionBar.setDisplayOptions(options);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayOptions(int options, int mask) {
            mActionBar.setDisplayOptions(options, mask);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayUseLogoEnabled(boolean useLogo) {
            mActionBar.setDisplayUseLogoEnabled(useLogo);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayShowHomeEnabled(boolean showHome) {
            mActionBar.setDisplayShowHomeEnabled(showHome);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayShowTitleEnabled(boolean showTitle) {
            mActionBar.setDisplayShowTitleEnabled(showTitle);
            return this;
        }

        @Override
        public ActionBarCompat setBackgroundDrawable(Drawable drawable) {
            mActionBar.setBackgroundDrawable(drawable);
            return this;
        }

        @Override
        public ActionBarCompat setStackedBackgroundDrawable(Drawable d) {
            mActionBar.setStackedBackgroundDrawable(d);
            return this;
        }

        @Override
        public ActionBarCompat setSplitBackgroundDrawable(Drawable d) {
            mActionBar.setSplitBackgroundDrawable(d);
            return this;
        }

        @Override
        public View getCustomView() {
            return mActionBar.getCustomView();
        }

        @Override
        public CharSequence getTitle() {
            return mActionBar.getTitle();
        }

        @Override
        public CharSequence getSubtitle() {
            return mActionBar.getSubtitle();
        }

        @Override
        public int getDisplayOptions() {
            return mActionBar.getDisplayOptions();
        }

        @Override
        public int getHeight() {
            return mActionBar.getHeight();
        }

        @Override
        public ActionBarCompat show() {
            mActionBar.show();
            return this;
        }

        @Override
        public ActionBarCompat hide() {
            mActionBar.hide();
            return this;
        }

        @Override
        public boolean isShowing() {
            return mActionBar.isShowing();
        }

        @Override
        public ActionBarCompat addOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            mActionBar.addOnMenuVisibilityListener(listener);
            return this;
        }

        @Override
        public ActionBarCompat removeOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            mActionBar.removeOnMenuVisibilityListener(listener);
            return this;
        }

        @Override
        public ActionBarCompat setHomeButtonEnabled(boolean enabled) {
            mActionBar.setHomeButtonEnabled(enabled);
            return this;
        }

        @Override
        public Context getThemedContext() {
            return mActionBar.getThemedContext();
        }

    }

    static class AppActionBarWrapper implements ActionBarCompat{

        private final android.app.ActionBar mActionBar;

        public AppActionBarWrapper(android.app.ActionBar actionBar) {
            this.mActionBar = actionBar;
        }

        @Override
        public ActionBarCompat setHomeAsUpIndicator(Drawable drawable) {
            mActionBar.setHomeAsUpIndicator(drawable);
            return this;
        }

        @Override
        public ActionBarCompat setHomeAsUpIndicator(int resId) {
            mActionBar.setHomeAsUpIndicator(resId);
            return this;
        }

        @Override
        public ActionBarCompat setHomeActionContentDescription(@Nullable CharSequence description) {
            mActionBar.setHomeActionContentDescription(description);
            return this;
        }

        @Override
        public ActionBarCompat setHomeActionContentDescription(int resId) {
            mActionBar.setHomeActionContentDescription(resId);
            return this;
        }

        @Override
        public ActionBarCompat setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
            mActionBar.setHideOnContentScrollEnabled(hideOnContentScroll);
            return this;
        }

        @Override
        public boolean isHideOnContentScrollEnabled() {
            return mActionBar.isHideOnContentScrollEnabled();
        }

        @Override
        public int getHideOffset() {
            return mActionBar.getHideOffset();
        }

        @Override
        public ActionBarCompat setHideOffset(int offset) {
            mActionBar.setHideOffset(offset);
            return this;
        }

        @Override
        public ActionBarCompat setElevation(float elevation) {
            mActionBar.setElevation(elevation);
            return this;
        }

        @Override
        public float getElevation() {
            return mActionBar.getElevation();
        }

        @Override
        public Object getmActionBar() {
            return mActionBar;
        }

        @Override
        public ActionBarCompat setDisplayShowCustomEnabled(boolean enable) {
            mActionBar.setDisplayShowCustomEnabled(enable);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(View view) {
            mActionBar.setCustomView(view);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(View view, LayoutParams layoutParams) {
            mActionBar.setCustomView(view, layoutParams.mAppParams);
            return this;
        }

        @Override
        public ActionBarCompat setCustomView(int id) {
            mActionBar.setCustomView(id);
            return this;
        }

        @Override
        public ActionBarCompat setIcon(int resId) {
            mActionBar.setIcon(resId);
            return this;
        }

        @Override
        public ActionBarCompat setIcon(Drawable icon) {
            mActionBar.setIcon(icon);
            return this;
        }

        @Override
        public ActionBarCompat setLogo(int resId) {
            mActionBar.setLogo(resId);
            return this;
        }

        @Override
        public ActionBarCompat setLogo(Drawable logo) {
            mActionBar.setLogo(logo);
            return this;
        }

        @Override
        public ActionBarCompat setTitle(CharSequence title) {
            mActionBar.setTitle(title);
            return this;
        }

        @Override
        public ActionBarCompat setTitle(int resId) {
            mActionBar.setTitle(resId);
            return this;
        }

        @Override
        public ActionBarCompat setSubtitle(CharSequence subtitle) {
            mActionBar.setSubtitle(subtitle);
            return this;
        }

        @Override
        public ActionBarCompat setSubtitle(int resId) {
            mActionBar.setSubtitle(resId);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayOptions(int options) {
            mActionBar.setDisplayOptions(options);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayOptions(int options, int mask) {
            mActionBar.setDisplayOptions(options, mask);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayUseLogoEnabled(boolean useLogo) {
            mActionBar.setDisplayUseLogoEnabled(useLogo);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayShowHomeEnabled(boolean showHome) {
            mActionBar.setDisplayShowHomeEnabled(showHome);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            return this;
        }

        @Override
        public ActionBarCompat setDisplayShowTitleEnabled(boolean showTitle) {
            mActionBar.setDisplayShowTitleEnabled(showTitle);
            return this;
        }

        @Override
        public ActionBarCompat setBackgroundDrawable(Drawable drawable) {
            mActionBar.setBackgroundDrawable(drawable);
            return this;
        }

        @Override
        public ActionBarCompat setStackedBackgroundDrawable(Drawable d) {
            mActionBar.setStackedBackgroundDrawable(d);
            return this;
        }

        @Override
        public ActionBarCompat setSplitBackgroundDrawable(Drawable d) {
            mActionBar.setSplitBackgroundDrawable(d);
            return this;
        }

        @Override
        public View getCustomView() {
            return mActionBar.getCustomView();
        }

        @Override
        public CharSequence getTitle() {
            return mActionBar.getTitle();
        }

        @Override
        public CharSequence getSubtitle() {
            return mActionBar.getSubtitle();
        }

        @Override
        public int getDisplayOptions() {
            return mActionBar.getDisplayOptions();
        }

        @Override
        public int getHeight() {
            return mActionBar.getHeight();
        }

        @Override
        public ActionBarCompat show() {
            mActionBar.show();
            return this;
        }

        @Override
        public ActionBarCompat hide() {
            mActionBar.hide();
            return this;
        }

        @Override
        public boolean isShowing() {
            return mActionBar.isShowing();
        }

        @Override
        public ActionBarCompat addOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            mActionBar.addOnMenuVisibilityListener(listener);
            return this;
        }

        @Override
        public ActionBarCompat removeOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            mActionBar.removeOnMenuVisibilityListener(listener);
            return this;
        }

        @Override
        public ActionBarCompat setHomeButtonEnabled(boolean enabled) {
            mActionBar.setHomeButtonEnabled(enabled);
            return this;
        }

        @Override
        public Context getThemedContext() {
            return mActionBar.getThemedContext();
        }

    }
}
