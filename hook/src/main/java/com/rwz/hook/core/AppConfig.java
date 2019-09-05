package com.rwz.hook.core;

/**
 * date： 2019/9/4 16:45
 * author： rwz
 * description：
 **/
public class AppConfig {

    //应用名
    private String appName;
    //包名
    private String packageName;
    //application类名
    private String applicationClassName;

    public AppConfig(String appName, String applicationClassName) {
        this.appName = appName;
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
}
