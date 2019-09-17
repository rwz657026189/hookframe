package com.rwz.hook.core;

import android.support.annotation.Nullable;

/**
 * date： 2019/9/4 16:45
 * author： rwz
 * description：
 **/
public class AppConfig {

    //应用名，可省
    private String appName;
    //包名
    private String packageName;

    public AppConfig() {
        this.packageName = Constance.ALL_PACKAGE_NAME;
    }

    public AppConfig(@Nullable String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", PACKAGE_NAME='" + packageName + '\'' +
                '}';
    }
}
