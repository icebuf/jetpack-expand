package com.icebuf.jetpackex.sample.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Ice Nation
 * 日期：2019/2/13 10:35
 * 邮箱：bflyff@hotmail.com
 */
public class StorageUtil {

    private static final String TAG = StorageUtil.class.getSimpleName();

    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().getPath();


    public static File getDiskCacheDir(Context context) {
        File cacheFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheFile = context.getExternalCacheDir();
        } else {
            cacheFile = context.getCacheDir();
        }
        return cacheFile;
    }

    /**
     * 获取外部插入的U盘数量
     * @param context the context
     * @return U盘数量
     */
    public static int getUsbDiskCount(Context context) {
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);

        List<StorageVolume> volumeList = getStorageVolumes(storageManager);
        int count = 0;
        for (StorageVolume volume : volumeList) {
            boolean isMounted = Environment.MEDIA_MOUNTED.equals(StorageVolumeCompat.getState(volume));
            //要判断的是当前已经挂载并且可移除的存储设备
            count += isMounted && StorageVolumeCompat.isRemovable(volume) ? 1 : 0;
        }
        return count;
    }

    /**
     * 获取最后一个插入的U盘
     * @param context the context
     * @return U盘所挂载的文件目录
     */
    @Nullable
    public static File getLastUsbDisk(Context context) {
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);

        List<StorageVolume> volumeList = getStorageVolumes(storageManager);

        for (int i = volumeList.size() - 1; i >= 0;  i-- ) {
            StorageVolume volume = volumeList.get(i);
            boolean isMounted = Environment.MEDIA_MOUNTED.equals(StorageVolumeCompat.getState(volume));
            //要判断的是当前已经挂载并且可移除的存储设备
            if(isMounted && StorageVolumeCompat.isRemovable(volume)) {
                StorageVolumeCompat xVolume = new StorageVolumeCompat(volume, null);
                return xVolume.getPathFile();
            }
        }
        return null;
    }

    @NonNull
    public static List<StorageVolume> getStorageVolumes(StorageManager storageManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //API24以后新增的公开访问方法
            return storageManager.getStorageVolumes();
        }
        List<StorageVolume> volumeList = new ArrayList<>();
        try {
            Class<?> clazz = Class.forName("android.os.storage.StorageManager");
            @SuppressLint("DiscouragedPrivateApi")
            Method method = clazz.getDeclaredMethod("getVolumeList");
            StorageVolume[] volumes =  (StorageVolume[])method.invoke(storageManager);
            if(volumes != null) {
                volumeList.addAll(Arrays.asList(volumes));
            }
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return volumeList;
    }

    public static List<StorageVolumeCompat> getXStorageVolumes(Context context) {
        StorageManager sm = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        if(sm == null) {
            return null;
        }
        List<StorageVolume> volumeList = getStorageVolumes(sm);
        List<?> volumeInfos = getVolumeInfos(sm);
        List<StorageVolumeCompat> xVolumeList = new ArrayList<>();
        for (StorageVolume volume : volumeList) {
            xVolumeList.add(new StorageVolumeCompat(volume, volumeInfos));
        }
        return xVolumeList;
    }

    @Nullable
    private static List<?> getVolumeInfos(StorageManager sm) {
        try {
            @SuppressLint("PrivateApi")
            Method getVolumes = sm.getClass().getMethod("getVolumes");
            return (List<?>) getVolumes.invoke(sm);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class StorageVolumeCompat {

        private static Field mIdField;
        private static Field mDescriptionField;
        private static Field mInternalPathField;
        private static Field mPathField;
        private static Field mPrimaryField;
        private static Field mRemovableField;
        private static Field mEmulatedField;
        private static Field mFsUuidField;
        private static Field mStateField;

        private static Field mVIPathField;
        private static Field mVIFsTypeField;
        private static Field mVIFsLabelField;
        private static Field mVIFsUuidField;
        private static Field mVIInternalPathField;

        static {
            try {
                Class<?> clazz = Class.forName("android.os.storage.StorageVolume");

                mPathField = getField(clazz, "mPath");
                mStateField = getField(clazz, "mState");
                mPrimaryField = getField(clazz, "mPrimary");
                mRemovableField = getField(clazz, "mRemovable");
                mEmulatedField = getField(clazz,"mEmulated");
                mFsUuidField = getField(clazz, "mFsUuid");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    mInternalPathField = getField(clazz, "mInternalPath");
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                    mIdField = getField(clazz, "mId");
                    mFsUuidField = getField(clazz, "mFsUuid");
                    mDescriptionField = getField(clazz, "mDescription");
                }

                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                    mDescriptionField = getField(clazz, "mUserLabel");
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    @SuppressLint("PrivateApi")
                    Class<?> infoClazz = Class.forName("android.os.storage.VolumeInfo");
                    mVIPathField = getField(infoClazz, "path");
                    mVIFsLabelField = getField(infoClazz, "fsLabel");
                    mVIFsTypeField = getField(infoClazz, "fsType");
                    mVIFsUuidField = getField(infoClazz, "fsUuid");
                    mVIInternalPathField = getField(infoClazz, "internalPath");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private static Field getField(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "" +  e.getMessage());
                return null;
            }
        }

        //API 23+
        private String mId;
        //API 23+ API 28-
        //private int mStorageId;

        private File mPath;
        //API 28+
        private File mInternalPath;
        //API 23+
        private String mDescription;
        private boolean mPrimary;
        private boolean mRemovable;
        private boolean mEmulated;
        //API 28-
        //private final long mMtpReserveSize;
        //private boolean mAllowMassStorage;
        //private long mMaxFileSize;
        //private UserHandle mOwner;
        private String mFsUuid;
        private String mState;

        //form android.os.storage.VolumeInfo
        private String fsType;
        private String fsUuid;
        private String fsLabel;

        public StorageVolumeCompat(StorageVolume volume, List<?> volumeInfos) {
            mId = getObject(volume, mIdField);
            mPath = getObject(volume, mPathField);
            mDescription = getObject(volume, mDescriptionField);
            mInternalPath = getInternalPath(volume, volumeInfos);
            mPrimary = isPrimary(volume);
            mRemovable = isRemovable(volume);
            mEmulated = isEmulated(volume);
            mFsUuid = getUuid(volume);
            mState = getState(volume);

            fsType = getObject(volumeInfos, mVIFsTypeField);
            fsLabel = getObject(volumeInfos, mVIFsLabelField);
            fsUuid = getObject(volumeInfos, mVIFsUuidField);
        }

        private File getInternalPath(StorageVolume volume, List<?> volumeInfos) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                return getObject(volume, mInternalPathField);
            }
            String path = getObject(volumeInfos, mVIInternalPathField);
            if(TextUtils.isEmpty(path)) {
                return null;
            }
            return new File(path);
        }

        private <T> T getObject(List<?> volumeInfos, Field field) {
            if(volumeInfos == null || field == null){
                return null;
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                for (Object obj : volumeInfos) {
                    try {
                        String path = (String) mVIPathField.get(obj);
                        if(mPath.getPath().equals(path)) {
                            field.setAccessible(true);
                            return (T) field.get(obj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        public String getId() {
            return mId;
        }

        public File getPathFile() {
            return mPath;
        }

        public File getInternalPath() {
            return mInternalPath;
        }

        public String getUserLabel() {
            return mDescription;
        }

        public boolean isPrimary() {
            return mPrimary;
        }

        public boolean isEmulated() {
            return mEmulated;
        }

        public boolean isRemovable() {
            return mRemovable;
        }

        public String getUuid() {
            return mFsUuid;
        }

        public String getState() {
            return mState;
        }

        public String getFsLabel() {
            return fsLabel;
        }

        public String getFsType() {
            return fsType;
        }

        public String getFsUuid() {
            return fsUuid;
        }

        public static String getState(StorageVolume volume) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return volume.getState();
            }
            return getObject(volume, mStateField);
        }

        public static String getUuid(StorageVolume volume) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return volume.getUuid();
            }
            return getObject(volume, mFsUuidField);
        }

        public static boolean isEmulated(StorageVolume volume) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return volume.isEmulated();
            }
            return getBoolean(volume, mEmulatedField);
        }

        public static boolean isRemovable(StorageVolume volume) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return volume.isRemovable();
            }
            return getBoolean(volume, mRemovableField);
        }

        public static boolean isPrimary(StorageVolume volume) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return volume.isPrimary();
            }
            return getBoolean(volume, mPrimaryField);
        }

        private static boolean getBoolean(StorageVolume volume, Field field) {
            if(field == null) {
                return false;
            }
            field.setAccessible(true);
            try {
                return field.getBoolean(volume);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

        private static <T> T getObject(StorageVolume volume, Field field) {
            if(field == null) {
                return null;
            }
            field.setAccessible(true);
            try {
                return (T) field.get(volume);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
