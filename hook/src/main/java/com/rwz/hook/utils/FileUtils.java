package com.rwz.hook.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void outputString(char[] args, String fileName) {
        if (args == null)
            return;
        String pathName = "/storage/emulated/0/rwz/" + fileName;
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

    public static void outputString(String args, String fileName) {
        if (args == null || args.length() == 0) {
            LogUtil.e("outputString content is null ", "fileName = " + fileName);
            return;
        }
        String pathName = "/storage/emulated/0/rwz/" + fileName;
//        String pathName = "/mnt/shared/Other/" + fileName;
        //1011934301,436747842

        File file = new File(pathName);
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            LogUtil.d(TAG, "mkdirs = " + mkdirs);
        } else if (file.exists()) {
            int index = pathName.lastIndexOf("/");
            pathName = pathName.substring(0, index + 1) + "repeat_" + pathName.substring(index + 1);
        }
        LogUtil.d(TAG, "outputString = " + pathName, args.length());
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

}
