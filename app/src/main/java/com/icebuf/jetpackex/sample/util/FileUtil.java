package com.icebuf.jetpackex.sample.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.FileUriExposedException;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type File explorer utils.
 */
public class FileUtil {

    /**
     * The constant TAG.
     */
    public static final String TAG = FileUtil.class.getSimpleName();

    public static String SYSTEM_PROVIDER = "com.android.settings.files";

    public static String USER_PROVIDER = null;

    /**
     * The constant MIME_APK.
     */
    public static final String MIME_APK = "application/vnd.android.package-archive";

    /**
     * The constant REQUEST_CODE_INSTALL_APK.
     */
    public static final int REQUEST_CODE_INSTALL_APK = 1001;

    /**
     * The constant DIR_RULE.
     */
    public static final String DIR_RULE = "\\/:*?\"<>|";

    /**
     * 调用系统功能打开文件
     * 对8.0系统做了兼容，需要请求安装应用对权限才能继续安装应用
     * 处理了以下异常到抛出
     *
     * @param context 上下文
     * @param file    要打开的文件
     * @throws Exception the exception
     * @see FileUriExposedException
     * @see ActivityNotFoundException
     * @see Manifest.permission#REQUEST_INSTALL_PACKAGES
     */
    public static void openFile(Context context, File file) throws Exception {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri contentUri;
        //7.0之后对系统不能直接暴露文件URL给其他应用
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
           intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

           int uid = PackageUtil.getAppUid(context);

           String userProvider = TextUtils.isEmpty(USER_PROVIDER) ?
                    context.getPackageName() + ".fileprovider" : USER_PROVIDER;
           String authority = uid == Process.SYSTEM_UID ? SYSTEM_PROVIDER : userProvider;

           Log.i(TAG, "package uid：" + uid + " authority: " + authority);
           contentUri = FileProvider.getUriForFile(context, authority, file);

        }else {
            contentUri = Uri.fromFile(file);
        }

        String type = getMimeTypeFromUrl("file://" + file.getPath());
        if(type == null) {
            throw new NullPointerException("mime type is null!");
        }
        intent.setDataAndType(contentUri, type);

        //8.0需要请求安装权限
        if (type.equals(MIME_APK) && !checkInstallPermission(context)) {
            Log.e(TAG, "open file " + file.getPath() + " need install permission");
        }
        //防止定制或裁剪过的系统找不到对于Activity
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, 0);

        if (resInfoList.size() <= 0) {
            Log.e(TAG, "not found any activity can resolve the intent of file : " + file.getPath());
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }
        }
        context.startActivity(intent);
    }

    /**
     * 检查安装权限
     * @param context the context
     * @see Manifest.permission#REQUEST_INSTALL_PACKAGES
     * @see Manifest.permission#INSTALL_PACKAGES
     * @return true为通过
     */
    private static boolean checkInstallPermission(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.REQUEST_INSTALL_PACKAGES)
                    == PackageManager.PERMISSION_DENIED) {

                if(context instanceof Activity) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                            REQUEST_CODE_INSTALL_APK);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PackageManager pm = context.getPackageManager();
                    return pm.canRequestPackageInstalls();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文件MIME类型
     *
     * @param url 文件URL
     * @return 不能识别时返回null mime type from url
     */
    public static String getMimeTypeFromUrl(String url) {
        String type = null;
        //使用系统API，获取URL路径中文件的后缀名（扩展名）
        String extension = getExtensionFromUrl(url);
        if (!TextUtils.isEmpty(extension)) {
            //使用系统API，获取MimeTypeMap的单例实例，然后调用其内部方法获取文件后缀名（扩展名）所对应的MIME类型
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        Log.i(TAG, "MIME type：" + (type == null ? "null" : type));
        return type;
    }

    /**
     * 由于系统自带API{@link MimeTypeMap#getFileExtensionFromUrl}不一定能正常取得文件
     * 扩展名，比如app-debug.apk可以获取到，但是app-debug 2.apk却不能，因此采用传统方式
     * 再次获取一次
     *
     * @param url 文件URL
     * @return 文件拓展名 ，当为文件夹时返回空字符串
     */
    public static String getExtensionFromUrl(String url){
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension.equals("")){
            int start = url.lastIndexOf(".");
            if(start < 0)
                return "";
            extension = url.substring(start + 1);
        }
        return extension;
    }

    /**
     * Get extension string.
     *
     * @param path the path
     * @return the string
     */
    public static String getExtension(String path){
        return getExtensionFromUrl("file://" + path);
    }

    public static String normalize(String s) {
        return s;
    }

    /**
     * Save bitmap 2 jpeg.
     *
     * @param bitmap  the bitmap
     * @param path    the path
     * @param quality the quality
     * @throws IOException the io exception
     */
    public static void saveBitmap2Jpeg(Bitmap bitmap, String path, int quality)
            throws IOException {
        saveBitmap2Jpeg(bitmap,new File(path), quality);
    }

    /**
     * Save bitmap 2 jpeg string.
     *
     * @param bitmap  the bitmap
     * @param path    the path
     * @param name    the name
     * @param quality the quality
     * @return the string
     * @throws IOException the io exception
     */
    public static String saveBitmap2Jpeg(Bitmap bitmap, String path, String name, int quality)
            throws IOException {
        return saveBitmap2Image(bitmap, Bitmap.CompressFormat.JPEG, path, name, quality);
    }

    /**
     * Save bitmap 2 png string.
     *
     * @param bitmap the bitmap
     * @param path   the path
     * @param name   the name
     * @return the string
     * @throws IOException the io exception
     */
    public static String saveBitmap2Png(Bitmap bitmap, String path, String name)
            throws IOException {
        return saveBitmap2Image(bitmap, Bitmap.CompressFormat.PNG, path, name, 100);
    }

    /**
     * Save bitmap 2 image string.
     *
     * @param bitmap  the bitmap
     * @param format  the format
     * @param path    the path
     * @param name    the name
     * @param quality the quality
     * @return the string
     * @throws IOException the io exception
     */
    public static String saveBitmap2Image(Bitmap bitmap, Bitmap.CompressFormat format,
                                        String path, String name, int quality) throws IOException {
        //转换小写
        String fileName = name.toLowerCase();;
        //文件名补全
        switch (format){
            case PNG:
                fileName = fileName.lastIndexOf(".png") > 0 ? name : name + ".png";
                break;
            case JPEG:
                String reg = ".+(.JPEG|.jpeg|.JPG|.jpg)$";
                if(!Pattern.matches(reg, fileName)) {
                    fileName = name + ".jpg";
                }
                break;
            case WEBP:
                fileName = fileName.lastIndexOf(".webp") > 0 ? name : name + ".webp";
                break;
            default:
                throw new IOException("Unsupported compress format!");
        }
        File file = new File(path, fileName);
        saveBitmap2Image(bitmap, format, new File(path, fileName),quality);
        return file.getPath();
    }

    /**
     * Save bitmap 2 jpeg.
     *
     * @param bitmap  the bitmap
     * @param file    the file
     * @param quality the quality
     * @throws IOException the io exception
     */
    public static void saveBitmap2Jpeg(Bitmap bitmap, File file, int quality)
            throws IOException {
        saveBitmap2Image(bitmap, Bitmap.CompressFormat.JPEG, file, quality);
    }

    /**
     * Save bitmap 2 png.
     *
     * @param bitmap the bitmap
     * @param file   the file
     * @throws IOException the io exception
     */
    public static void saveBitmap2Png(Bitmap bitmap, File file) throws IOException {
        saveBitmap2Image(bitmap, Bitmap.CompressFormat.PNG, file, 100);
    }

    /**
     * Save bitmap 2 image.
     *
     * @param bitmap  the bitmap
     * @param format  the format
     * @param file    the file
     * @param quality the quality
     * @throws IOException the io exception
     */
    public static void saveBitmap2Image(Bitmap bitmap, Bitmap.CompressFormat format,
                                        File file, int quality) throws IOException {
        File parent = file.getParentFile();
        if(parent != null && !parent.exists()) {
            if(!parent.mkdirs()){
                throw new IOException("directory " + parent.getPath() + " create failed!");
            }
        }
        if(file.isDirectory()) {
            if(!file.delete()) {
                throw new IOException("directory " + file.getPath() + " delete failed!");
            }
        }
        if(!file.exists() && !file.createNewFile()) {
            throw new IOException("file " + file.getPath() + " create failed!");
        }
        FileOutputStream out = new FileOutputStream(file);
        if (bitmap.compress(format, quality, out)) {
            out.flush();
            out.close();
            Log.i(TAG, "write file " + file.getName() + " length "
                    + readableFileSize(file.length()));
            return;
        }
        out.flush();
        out.close();
        throw new IOException("file " + file.getPath()
                + " compressed to the specified stream failed!");
    }

    /**
     * Readable file size string.
     *
     * @param size the size
     * @return the string
     */
    public static String readableFileSize(long size) {
        if(size <= 0) return "0B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * New save name string.
     *
     * @param path the path
     * @return the string
     */
    public static String newFileName(String path) {
        return newFileName(path, "yyyy-MM-dd_HH_mm");
    }

    /**
     * New save name string.
     *
     * @param path    the path
     * @param pattern the pattern
     * @return the string
     */
    public static String newFileName(String path, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        File file = new File(path);
        String[] files = file.list();
        int i = 1;
        String name = date + "";
        if(files == null)
            return name;
        boolean contains = true;
        while (contains){
            contains = false;
            for (String str : files){
                if(str.contains(name)){
                    name = date + "_" + i++ ;
                    contains = true;
                    break;
                }
            }
        }
        return name;
    }

    /**
     * Verify file file.
     *
     * @param path the path
     * @return the file
     */
    public static File verifyFile(String path) {
        File file = new File(path);
        if(file.exists()) {
            return file;
        }
        if(file.mkdirs()) {
            return file;
        }
        return null;
    }


    /**
     * Copy file using channel.
     *
     * @param source the source
     * @param dest   the dest
     * @throws IOException the io exception
     */
    public static void copyFileWithChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {

            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    /**
     * The constant NO_ERROR.
     */
    public static final int NO_ERROR = 0;

    /**
     * The constant ERROR_CODE_SRC_EXISTS.
     */
    public static final int ERROR_CODE_SRC_EXISTS = -1;

    /**
     * The constant ERROR_CODE_DST_EXISTS.
     */
    public static final int ERROR_CODE_DST_EXISTS = -2;

    /**
     * The constant ERROR_CODE_SRC_NOT_EXISTS.
     */
    public static final int ERROR_CODE_SRC_NOT_EXISTS = -3;

    /**
     * The constant ERROR_CODE_DST_NOT_EXISTS.
     */
    public static final int ERROR_CODE_DST_NOT_EXISTS = -4;

    /**
     * The constant ERROR_CODE_MKDIR.
     */
    public static final int ERROR_CODE_MKDIR = -5;

    /**
     * The constant ERROR_CODE_CREATE.
     */
    public static final int ERROR_CODE_CREATE = -6;

    /**
     * The constant ERROR_CODE_DELETE.
     */
    public static final int ERROR_CODE_DELETE = -7;

    /**
     * The constant ERROR_CODE_READ.
     */
    public static final int ERROR_CODE_READ = -8;

    /**
     * The constant ERROR_CODE_WRITE.
     */
    public static final int ERROR_CODE_WRITE = -9;

    /**
     * The constant ERROR_CODE_IO.
     */
    public static final int ERROR_CODE_IO = -10;

    /**
     * The constant ERROR_CODE_RENAME.
     */
    public static final int ERROR_CODE_RENAME = -11;

    /**
     * The constant ERROR_CODE_FILE_NAME_EMPTY.
     */
    public static final int ERROR_CODE_FILE_NAME_EMPTY = -12;

    /**
     * The constant ERROR_CODE_FILE_NAME_START.
     */
    public static final int ERROR_CODE_FILE_NAME_START = -13;

    /**
     * The constant ERROR_CODE_FILE_NAME_RULE.
     */
    public static final int ERROR_CODE_FILE_NAME_RULE = -14;

    /**
     * Get error tag string.
     *
     * @param context   the context
     * @param file      the file
     * @param errorFlag the error flag
     * @return the string
     */
    public static String getErrorTag(Context context, File file, int errorFlag){
        String fileName = file == null ? "" : file.getName();
        return getErrorTag(context,fileName,errorFlag);
    }

    /**
     * Get error tag string.
     *
     * @param context   the context
     * @param fileName  the file name
     * @param errorFlag the error flag
     * @return the string
     */
    public static String getErrorTag(Context context, String fileName, int errorFlag){
        String tag = "";
//        switch (errorFlag){
//            case ERROR_CODE_SRC_EXISTS:
//                tag = context.getString(R.string.src_file_has_exist,fileName);
//                break;
//            case ERROR_CODE_DST_EXISTS:
//                tag = context.getString(R.string.dst_file_has_exist,fileName);
//                break;
//            case ERROR_CODE_SRC_NOT_EXISTS:
//                tag = context.getString(R.string.src_file_has_not_exist,fileName);
//                break;
//            case ERROR_CODE_DST_NOT_EXISTS:
//                tag = context.getString(R.string.dst_file_has_not_exist,fileName);
//                break;
//            case ERROR_CODE_MKDIR:
//                tag = context.getString(R.string.error_mkdir,fileName);
//                break;
//            case ERROR_CODE_CREATE:
//                tag = context.getString(R.string.error_create_file,fileName);
//                break;
//            case ERROR_CODE_DELETE:
//                tag = context.getString(R.string.error_delete_file,fileName);
//                break;
//            case ERROR_CODE_READ:
//                tag = context.getString(R.string.error_read_file,fileName);
//                break;
//            case ERROR_CODE_WRITE:
//                tag = context.getString(R.string.error_write_file,fileName);
//                break;
//            case ERROR_CODE_IO:
//                tag = context.getString(R.string.error_unknown_io);
//                break;
//            case ERROR_CODE_RENAME:
//                tag = context.getString(R.string.error_rename_file,fileName);
//                break;
//            case ERROR_CODE_FILE_NAME_EMPTY:
//                tag = context.getString(R.string.error_empty_name);
//                break;
//            case ERROR_CODE_FILE_NAME_START:
//                tag = context.getString(R.string.error_name_start);
//                break;
//            case ERROR_CODE_FILE_NAME_RULE:
//                tag = context.getString(R.string.error_name_char,DIR_RULE);
//                break;
//            default:
//                tag = context.getString(R.string.error_unknown_type,fileName);
//        }
        return tag;
    }

//    public static int mkdirIgnoreExists(@NonNull String path, @NonNull String name) {
//        int ret = mkdir(new File(path , name));
//        if(ret != ERROR_CODE_SRC_EXISTS)
//            return ret;
//
////        String extension = getExtension(path);
//        if(name.endsWith("()"))
//
//        return ret;
//    }

    /**
     * Mkdir int.
     *
     * @param path the path
     * @param name the name
     * @return the int
     */
    public static int mkdir(@NonNull String path, @NonNull String name) {
        return mkdir(new File(path , name));
    }

    /**
     * Mkdir int.
     *
     * @param file the file
     * @return the int
     */
    public static int mkdir(File file) {
        if(file.exists()) {
            return ERROR_CODE_SRC_EXISTS;
        }
        boolean result = file.mkdir();
        if(!result)
            return ERROR_CODE_MKDIR;
        return 0;
    }

    /**
     * Copy file to int.
     *
     * @param source   the source
     * @param destPath the dest path
     * @return the int
     */
    public static int copyFileTo(@NonNull File source, @NonNull String destPath) {
        if(!source.exists())
            return ERROR_CODE_SRC_NOT_EXISTS;
        if(!source.canRead())
            return ERROR_CODE_READ;

        File dest = new File(destPath);
        if(!dest.exists())
            return ERROR_CODE_DST_NOT_EXISTS;
        if(!dest.canWrite())
            return ERROR_CODE_WRITE;
        //目标文件名
        String destName = obtainFileName(destPath,source.getName());
        try {
            copyFileUsingStream(source,new File(dest,destName));
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR_CODE_IO;
        }
        return 0;
    }

    private static String obtainFileName(String destPath, String srcName) {
        //1.如果目标文件夹内不存在这个文件直接返回源文件名
        File file = new File(destPath,srcName);
        if(!file.exists()) {
            return srcName;
        }

        //2.创建正则表达式
        String extension = "." + getExtension(file.getPath());
        srcName = srcName.replace(extension,"");
        String regex = srcName + "的副本(( \\d+)*)" + extension + "$";
        Pattern pattern = Pattern.compile(regex);
        String newName = srcName + "的副本";
        //3.获取文件夹内所有文件名
        File parent = file.getParentFile();
        if(parent == null) {
            return srcName;
        }
        String[] names = parent.list();
        if(names == null) {
            return srcName;
        }
        Set<Integer> indexList = new TreeSet<>();
        for (String name : names){
            Matcher matcher = pattern.matcher(name);
            while (matcher.find()){
                Log.d(TAG,"name: " + name + " 匹配成功！");
                int i = matcher.groupCount();
                String str = matcher.group(i - 1);
                if(TextUtils.isEmpty(str)){
                    indexList.add(1);
                }else {
                    str = str.replace(" ","");
                    int value = Integer.parseInt(str);
                    indexList.add(value);
                }
            }
        }
        int index = 1;
        while (indexList.contains(index)){
            index ++;
        }
        if (index == 1) {
            return newName + extension;
        }
        return newName + " " + index + extension;
    }

    /**
     * Check file name int.
     *
     * @param name the name
     * @return the int
     */
    public static int checkFileName(@NonNull String name) {
        if(name.equals(""))
            return ERROR_CODE_FILE_NAME_EMPTY;

        if(name.startsWith(".") || name.startsWith(".."))
            return ERROR_CODE_FILE_NAME_START;


        for (int i = 0;i < DIR_RULE.length();i++) {
            String ch = DIR_RULE.charAt(i) + "";
            if(name.contains(ch))
                return ERROR_CODE_FILE_NAME_RULE;

        }
        return 0;
    }

    /**
     * The interface Copy listener.
     */
    public interface CopyListener{
        /**
         * On copy file.
         *
         * @param file the file
         */
        void onCopyFile(File file);
    }

    /**
     * Rename to int.
     *
     * @param file     the file
     * @param destPath the dest path
     * @return the int
     */
    public static int renameTo(File file, String destPath) {
        // Destination directory
        File dir = new File(destPath);

        if(!dir.exists())
            return ERROR_CODE_DST_EXISTS;

        if(!dir.canWrite())
            return ERROR_CODE_WRITE;

        String[] names = dir.list();
        if(names != null) {
            for (String name : names) {
                if (name.equals(file.getName()))
                    return ERROR_CODE_DST_EXISTS;
            }
        }

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        return success ? 0 : ERROR_CODE_RENAME;
    }

    /**
     * Copy dir to int.
     *
     * @param file     the file
     * @param destPath the dest path
     * @return the int
     */
    public static int copyDirTo(File file, String destPath){
        return copyDirTo(file,destPath,null);
    }

    /**
     * Copy dir to int.
     *
     * @param file     the file
     * @param destPath the dest path
     * @param listener the listener
     * @return the int
     */
    public static int copyDirTo(File file, String destPath, CopyListener listener) {
        if(file.isDirectory()){
            File dir = new File(destPath,file.getName());
            int result = mkdir(dir);
            if(result < 0){
                return result;
            }
            Log.e(TAG,"创建文件夹：" + dir.getPath() + " 成功！");
            File[] files = file.listFiles();
            if(files != null) {
                for (File child : files) {
                    result = copyDirTo(child, dir.getPath(), listener);
                    if (result < 0) {
                        return result;
                    }
                }
            }
            Log.e(TAG,"文件夹：" + file.getPath() + " 复制成功！");
            if(listener != null){
                listener.onCopyFile(file);
            }
        }else {
            int result = copyFileTo(file,destPath);
            if(result < 0){
                return result;
            }
            Log.e(TAG,"复制文件：" + file.getPath() + " 成功！");
            if(listener != null){
                listener.onCopyFile(file);
            }
        }
        return 0;
    }

    /**
     * Copy file using stream.
     *
     * @param source the source
     * @param dest   the dest
     * @throws IOException the io exception
     */
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if(is != null) {
                is.close();
            }
            if(os != null) {
                os.close();
            }
        }
    }

    /**
     * Delete files int.
     *
     * @param paths the paths
     * @return the int
     */
    public static int deleteFiles(List<String> paths) {
        if(paths == null)
            return 0;
        for (String path:paths){
            File file = new File(path);
            int ret = deleteFile(file);
            if(ret < 0)
                return ret;
        }
        return 0;
    }

    /**
     * Delete files int.
     *
     * @param paths the paths
     * @return the int
     */
    public static int deleteFiles(String... paths){
        if(paths == null || paths.length <= 0)
            return 0;

        for (String path:paths){
            File file = new File(path);
            int ret = deleteFile(file);
            if(ret < 0)
                return ret;
        }
        return 0;
    }

    /**
     * Delete file int.
     *
     * @param file the file
     * @return the int
     */
    public static int deleteFile(@NonNull File file){
        if(!file.exists())
            return 0;

        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File child:files){
                int ret = deleteFile(child);
                if(ret < 0)
                    return ret;
            }
        }

        if(file.delete())
            return 0;
        return ERROR_CODE_DELETE;
    }

    /**
     * 获取文件夹中的所有文件数
     *
     * @param file 输入文件或文件夹
     * @return 当输入参数为文件时返回1 ，表示它本身也是一个文件
     */
    public static int getFileCount(@NonNull File file) {
        int count = 1;
        File[] files = file.listFiles();
        for (File child:files){
            count++;
            if(child.isDirectory()){
                count += getFileCount(child);
            }
        }
        return count;
    }


    /**
     * The interface Search listener.
     */
    public interface SearchListener {

        /**
         * On searched.
         *
         * @param file the file
         */
        void onSearched(File file);

        /**
         * Is cancel boolean.
         *
         * @return the boolean
         */
        boolean isCancel();

    }

    /**
     * 纵向搜索符合条件的文件
     *
     * @param file       要搜索的文件目录
     * @param resultList 符合条件的结果保存的列表
     * @param fileFilter 文件筛选条件
     * @param listener   搜索监听器
     */
    public static void searchFile(@NonNull File file, @NonNull List<File> resultList,
                                  @NonNull FileFilter fileFilter,
                                  @Nullable SearchListener listener){
        if(!file.exists())
            return;

        if(file.isFile()) {
            //文件合格就发出回掉
            if(fileFilter.accept(file)){
                resultList.add(file);
                if(listener != null)
                    listener.onSearched(file);
                Log.e(TAG,"找到文件：" + file.getName());
            }
            return;
        }

        if(!file.canRead())
            return;

        //如果取消搜索则从当前递归位置一直返回
        if(listener != null){
            if(listener.isCancel())
                return;
        }

        //Log.e(TAG,"开始搜索文件夹：" + file.getPath());
        File[] files = file.listFiles();

        for (File child:files){
            searchFile(child,resultList,fileFilter,listener);
        }
    }

    /**
     * Search file 2.
     *
     * @param file       the file
     * @param resultList the result list
     * @param fileFilter the file filter
     * @param listener   the listener
     */
    public static void searchFile2(@NonNull File file, @NonNull List<File> resultList,
                                   @NonNull FileFilter fileFilter,
                                   @Nullable SearchListener listener){

        if(!file.exists())
            return;

        if(file.isFile()) {
            //文件合格就发出回掉
            if(fileFilter.accept(file)){
                if(listener != null)
                    listener.onSearched(file);
            }
            return;
        }

        if(!file.canRead())
            return;

        // 树中当前层节点的集合。
        ArrayList<File> currentLevelNodes = new ArrayList<>();
        currentLevelNodes.add(file);

        // 判断当前层是否有节点
        while(currentLevelNodes.size() > 0){
            // 下一层节点的集合。
            ArrayList<File> nextLevelNodes = new ArrayList<File>();
            // 找到树中所有的下一层节点，并把这些节点放到 nextLevelNodes 中。
            for (File f : currentLevelNodes) {
                // 如果符合过滤条件，就放到返回结果里面。
                if (fileFilter.accept(f)){
                    resultList.add(f);
                    if(listener != null){
                        listener.onSearched(f);
                    }
                }
                // 如果有子节点，就把子节点加入 nextLevelNodes
                if (hasChildren(f)) {
                    nextLevelNodes.addAll(Arrays.asList(f.listFiles()));
                }

                if(listener != null){
                    if(listener.isCancel()){
                        break;
                    }
                }
            }

            // 令当前层节点集合的引用指向下一层节点的集合。
            currentLevelNodes = nextLevelNodes;

            if(listener != null){
                if(listener.isCancel()){
                    break;
                }
            }
        }

//        resultList.trimToSize();
    }

    private static boolean hasChildren(final File f){
        boolean flag = false;
        File[] list = null;
        if (f.isDirectory()) {
            list = f.listFiles();
        }
        if (null != list && list.length > 0) {
            flag = true;
        }
        return flag;
    }

}
