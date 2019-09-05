package com.rwz.hook.core;

public interface Constance {

    //当前应用包名
    String packageName = "com.rwz.app";

    boolean isDebug = true;
    boolean showLog = true;
    //最大支持线程数
    int MAX_THREAD = 64;

    //*******************  客户端相应  *******************//

    int CLIENT_JOIN = 1;
    int TARGET_JOIN = 2;
    int LOG = 5;

    //正常日志
    int LOG_NORMAL = 100;
    //异常日志
    int LOG_ERROR = 400;

    //*******************  消息键值  *******************//
    String KEY_MSG = "msg";
    String KEY_TARGET_PACKAGE_NAME = "KEY_TARGET_PACKAGE_NAME";
    String KEY_FROM_TARGET = "FROM_TARGET";

}
