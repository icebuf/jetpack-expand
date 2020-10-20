package com.icebuf.jetpackex.sample.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            setTranslucentStatus(activity);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
            systemBarTintManager.setStatusBarTintEnabled(true);//显示状态栏
            systemBarTintManager.setStatusBarTintColor(colorId);//设置状态栏颜色
        }
    }

    /**
     * 设置状态栏透明
     * @param activity the activity
     */
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }


    /**
     * Adjust view layout based on system windows such as the status bar.
     * @see android.R.attr#fitsSystemWindows
     * @param activity the activity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setRootViewFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        ViewGroup winContent = activity.findViewById(android.R.id.content);
        if (winContent.getChildCount() > 0) {
            ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
            if (rootView != null) {
                rootView.setFitsSystemWindows(fitSystemWindows);
            }
        }
    }

    /**
     * 设置支持的状态栏深色浅色模式
     * @param activity the activity
     * @param dark 是否为dark模式
     * @return true为设置成功
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean setSupportSystemUiDark(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSystemUiDark(activity, dark);
            return true;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CustomSystemUi custom = getCustomSystemUi();
            if(custom != null) {
                return custom.setDark(activity, dark);
            }
        }
        return false;
    }

    private static CustomSystemUi sCustomUi;

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

    public static CustomSystemUi getCustomSystemUi() {
        if(sCustomUi == null) {
            sCustomUi = getCustomSystemUi0();
        }
        return sCustomUi;
    }

    private static CustomSystemUi getCustomSystemUi0() {
        String value = SystemPropUtil.get(KEY_VERSION_MIUI);
        if(!TextUtils.isEmpty(value)) {
            return new MiuiSystemUi();
        }
        if(Build.DISPLAY.toLowerCase().contains("flyme")) {
            return new FlymeSystemUi();
        }
        return new UnknownSystemUi();
    }

    public interface CustomSystemUi {

        boolean setDark(Activity activity, boolean dark);
    }

    static class UnknownSystemUi implements CustomSystemUi {

        @Override
        public boolean setDark(Activity activity, boolean dark) {
            //TODO
            return false;
        }
    }

    static class MiuiSystemUi implements CustomSystemUi {

        @Override
        public boolean setDark(Activity activity, boolean dark) {
            Window window = activity.getWindow();
            Class<?> clazz = window.getClass();
            try {
                @SuppressLint("PrivateApi")
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkMode = field.getInt(layoutParams);
                Method method = clazz.getDeclaredMethod("setExtraFlags", int.class, int.class);
                if(!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(window, dark ? darkMode : 0, darkMode);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class FlymeSystemUi implements CustomSystemUi {

        @Override
        public boolean setDark(Activity activity, boolean dark) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Class<?> clazz = WindowManager.LayoutParams.class;
            try {
                Field darkFlag = clazz.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = clazz.getDeclaredField("meizuFlags");
                if(!darkFlag.isAccessible()) {
                    darkFlag.setAccessible(true);
                }
                if(!meizuFlags.isAccessible()) {
                    meizuFlags.setAccessible(true);
                }
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 设置状态栏深色浅色模式 仅支持6.0以上版本
     * @param activity the activity
     * @param dark 是否为dark模式
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setSystemUiDark(Activity activity, boolean dark) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null) {
            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (decorView.getSystemUiVisibility() != vis) {
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    /**
     * 获取系统资源中定义的状态栏高度
     * @param context the context
     * @return the status bar height with px
     */
    public static int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 获取当前activity窗口的状态栏高度
     * @param activity 当前显示的activity
     * @return the status bar height with px
     */
    public static int getCurrentStatusBarHeight(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return dm.heightPixels - frame.height();
    }

}
