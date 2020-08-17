package com.icebuf.jetpackex.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/7/10
 * E-mail：bflyff@hotmail.com
 */
public class IceUtil {

    private static WeakReference<Toast> sGlobalToast;

    private static volatile Handler sUIHandler;

    /**
     * 设置
     * @param activity
     * @param enable
     */
    public static void setDisplayHomeAsUp(Activity activity, boolean enable) {
        if(activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(enable);
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(enable);
            }
        }
    }

    public static void setActionBarEnable(Activity activity, boolean enable) {
        if(activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if (actionBar != null) {
                if(enable) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if(enable) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
            }
        }
    }

    public static void showToast(Context context, @StringRes int resId) {
        showToast(context, resId, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, @StringRes int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    /**
     *
     * @param context 上下文
     * @param msg 消息内容
     */
    @AnyThread
    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 适用于当应用已经显示出一个吐司，且距离消失还有一段的时间，此时应用又放出一个吐司
     * 这时我们希望最新的吐司能够立即替换掉原来的吐司内容，并且从替换时起，然后再一整个
     * 预设的显示时长再消失，如果再消失前该方法又被调用，则重复整个过程
     * @param duration 显示时长
     * @see Toast#LENGTH_SHORT 显示较短时间的吐司，大约为4000ms
     * @see Toast#LENGTH_LONG 显示较长时间的吐司，大约为7000ms
     * @param context 上下文
     * @param msg 消息内容
     */
    @AnyThread
    public static void showToast(Context context, String msg, int duration) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            if(sGlobalToast != null && sGlobalToast.get() != null) {
                sGlobalToast.get().cancel();
            }
            Toast toast = Toast.makeText(context.getApplicationContext(), msg, duration);
            toast.show();
            sGlobalToast = new WeakReference<>(toast);
        } else {
            post(() -> showToast(context, msg, duration));
        }
    }

    public static void post(Runnable runnable) {
        if(sUIHandler == null) {
            sUIHandler = new Handler(Looper.getMainLooper());
        }
        sUIHandler.post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delay) {
        if(sUIHandler == null) {
            sUIHandler = new Handler(Looper.getMainLooper());
        }
        sUIHandler.postDelayed(runnable, delay);
    }

    public static void hideInputMethod(Context context, IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }


    public static void setActionBarOnLifecycle(Fragment fragment, boolean show) {
        setActionBarOnLifecycle(fragment.getLifecycle(), fragment.requireActivity(), show);
    }

    public static void setActionBarOnLifecycle(Lifecycle lifecycle, Activity activity, boolean show) {
        if(lifecycle.getCurrentState() != Lifecycle.State.CREATED) {
            return;
        }
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if(event == Lifecycle.Event.ON_START) {
                    setActionBarEnable(activity, show);
                } else if(event == Lifecycle.Event.ON_STOP) {
                    setActionBarEnable(activity, !show);
                } else if(event == Lifecycle.Event.ON_DESTROY) {
                    source.getLifecycle().removeObserver(this);
                }
            }
        });
    }


}
