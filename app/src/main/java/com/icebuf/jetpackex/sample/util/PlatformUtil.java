package com.icebuf.jetpackex.sample.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/28
 * E-mail：bflyff@hotmail.com
 */
public class PlatformUtil {

    @Product(value = {
            @LaunchInfo(
                    pkg = "com.huawei.systemmanager",
                    activity = ".startupmgr.ui.StartupNormalAppListActivity"
            ),
            @LaunchInfo(
                    pkg = "com.huawei.systemmanager",
                    activity = ".optimize.bootstart.BootStartActivity"
            )
    },names = {"honor"})
    public static final String HUAWEI = "huawei";

    @Product({@LaunchInfo(
            pkg = "com.miui.securitycenter",
            activity = "com.miui.permcenter.autostart.AutoStartManagementActivity"
    )})
    public static final String XIAOMI = "xiaomi";

    @Product({
            @LaunchInfo(pkg = "com.coloros.phonemanager"),
            @LaunchInfo(pkg = "com.oppo.safe"),
            @LaunchInfo(pkg = "com.coloros.oppoguardelf"),
            @LaunchInfo(pkg = "com.coloros.safecenter"),
    })
    public static final String OPPO = "oppo";

    @Product({@LaunchInfo(pkg = "com.iqoo.secure")})
    public static final String VIVO = "vivo";

    @Product({@LaunchInfo(pkg = "com.meizu.safe")})
    public static final String MEIZU = "meizu";

    @Product({
            @LaunchInfo(pkg = "com.samsung.android.sm_cn"),
            @LaunchInfo(pkg = "com.samsung.android.sm"),
    })
    public static final String SAMSUNG = "samsung";

    @Product({@LaunchInfo(
            pkg = "com.letv.android.letvsafe",
            activity = "com.letv.android.letvsafe.AutobootManageActivity"
    )})
    public static final String LE_TV = "letv";

    @Product({@LaunchInfo(pkg = "com.smartisanos.security")})
    public static final String SMARTISAN = "smartisan";

    public static final String UNKNOWN = "unknown";


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Product {
        LaunchInfo[] value();
        String[] names() default {};
    }

    @Target(ElementType.ANNOTATION_TYPE)
    public @interface LaunchInfo {
        String pkg();
        String activity() default "";
    }

    private static Set<Class<?>> sLoadClassSet;

    private static HashMap<String, List<LaunchInfo>> sProductMap;

    private static List<LaunchInfo> sLocalInfos;

    public static void registerClass(Class<?> clazz) {
        if(sLoadClassSet == null) {
            sLoadClassSet = new HashSet<>();
        }
        if(sLoadClassSet.add(clazz)) {
            loadLaunchInfosFromClass(clazz);
        }
    }

    private static Map<String, List<LaunchInfo>> getSupportLaunchInfos() {
        if(sProductMap == null) {
            registerClass(PlatformUtil.class);
        }
        return sProductMap;
    }

    private static void loadLaunchInfosFromClass(Class<?> clazz) {
        if(sProductMap == null) {
            sProductMap = new HashMap<>();;
        }
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Product product = field.getAnnotation(Product.class);
            if(product == null) {
                continue;
            }
            String productName = getStaticField(field);
            if(TextUtils.isEmpty(productName)) {
                continue;
            }
            List<LaunchInfo> loadInfos = new ArrayList<>(Arrays.asList(product.value()));
            List<LaunchInfo> infos = sProductMap.get(productName);
            if(infos != null) {
                infos.addAll(loadInfos);
            } else {
                infos = loadInfos;
                sProductMap.put(productName, infos);
            }
            Log.e("TAG", Arrays.toString(infos.toArray()));
            for (String name : product.names()) {
                sProductMap.put(name, infos);
            }
        }
    }

    private static <T> T getStaticField(Field field) {
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static List<LaunchInfo> getLaunchInfos() {
        if(sLocalInfos == null) {
            sLocalInfos = getSupportLaunchInfos().get(getProduct());
        }
        return sLocalInfos;
    }

    private static String getProduct() {
        if(Build.BRAND == null) {
            return UNKNOWN;
        }
        return Build.BRAND.toLowerCase();
    }


    public static void requestAutoLaunch(Context context) {
        List<LaunchInfo> launchInfos = getLaunchInfos();
        if(launchInfos == null || launchInfos.isEmpty()) {
            Log.e("TAG", "un support this product");
            return;
        }
        for (LaunchInfo info : launchInfos) {
            boolean ret = false;
            if(TextUtils.isEmpty(info.activity())) {
                try {
                    startLaunchActivity(context, info.pkg());
                    ret = true;
                } catch (Exception ignore) {
                }
            } else {
                try {
                    String activityName = info.activity().trim();
                    if(activityName.startsWith(".")) {
                        activityName += info.pkg();
                    }
                    startActivity(context, info.pkg(), activityName);
                    ret = true;
                } catch (Exception ignore) {
                }
            }
            if(ret) {
                break;
            }
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    public static void startLaunchActivity(@NonNull Context context,
                                           @NonNull String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     * @param context the context
     * @param packageName 指定应用包名
     * @param activityName 指定应用的Activity
     */
    public static void startActivity(@NonNull Context context,
                                    @NonNull String packageName,
                                    @NonNull String activityName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
