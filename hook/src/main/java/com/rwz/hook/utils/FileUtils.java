package com.rwz.hook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.rwz.hook.core.Constance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtils {

    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE" };

    private static final String TAG = "FileUtils";
    private static final String FILE_DIR = "/storage/emulated/0/rwz/";

    //检测是否有写的权限
    public static boolean hasWritePermissions(Context context) {
        int permission = ActivityCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean requestWritePermissions(Activity activity) {
        try {
            boolean result = hasWritePermissions(activity);
            if (!result) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, Constance.REQUEST_EXTERNAL_STORAGE);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readText(String fileName) {
        String pathName = FILE_DIR + fileName;
        File file = new File(pathName);
        if (!file.exists()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            FileReader reader = new FileReader(pathName);
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void writeText(char[] args, String fileName) {
        if (args == null)
            return;
        String pathName = FILE_DIR + fileName;
//        String pathName = "/mnt/shared/Other/" + fileName;
        File file = new File(pathName);
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            LogUtil.d(TAG, "mkdirs = " + mkdirs);
        } else if (file.exists()) {
            int index = pathName.lastIndexOf("/");
            pathName = pathName.substring(0, index + 1) + "repeat_" + pathName.substring(index + 1);
        }
        LogUtil.d(TAG, "outputString = " + pathName, args.length);
        Writer writer = null;
        try {
            writer = new FileWriter(pathName);
            writer.write(args);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeText(String content, String fileName, boolean isCover) {
        if (content == null || content.length() == 0) {
            LogUtil.e("outputString content is null ", "fileName = " + fileName);
            return;
        }
        String pathName = FILE_DIR + fileName;
//        String pathName = "/mnt/shared/Other/" + fileName;
        File file = new File(pathName);
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            LogUtil.d(TAG, "mkdirs = " + mkdirs);
        } else if (file.exists()) {
            if (!isCover) {
                int index = pathName.lastIndexOf("/");
                pathName = pathName.substring(0, index + 1) + "repeat_" + pathName.substring(index + 1);
            } else {
                file.delete();
            }
        }
        LogUtil.d(TAG, "outputString = " + pathName, content.length());
        Writer writer = null;
        try {
            writer = new FileWriter(pathName);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
