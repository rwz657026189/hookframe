package com.rwz.hook.core;

import android.support.annotation.Nullable;

/**
 * date： 2019/9/4 16:45
 * author： rwz
 * description：
 **/
public class AppConfig {

    //应用名，保留字段，可省
    private String appName;
    //包名
    private String packageName;
    //application类名, 若未null，则不能进行应用间通信
    private String applicationClassName;

    public AppConfig(@Nullable String appName, String packageName, @Nullable String applicationClassName) {
        this.appName = appName;
        this.packageName = packageName;
        this.applicationClassName = applicationClassName;
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

    public String getApplicationClassName() {
        return applicationClassName;
    }


    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", applicationClassName='" + applicationClassName + '\'' +
                '}';
    }
}
