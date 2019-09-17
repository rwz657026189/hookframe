package com.rwz.hook.core;

public interface Constance {

    //*******************  每个独立应用支持修改的参数  *******************//
    //▲必须修改为当前app包名
    String PACKAGE_NAME = "com.rwz.app";
    //端口
    int SERVER_PORT = 8080;
    //url
    String SERVER_URL = "ws://192.168.0.183:" + SERVER_PORT;
    //是否debug
    boolean isDebug = true;
    //UI页面是否显示日志(耗性能，建议仅供测试用)
    boolean showLog = true;
    //最大支持线程数
    int MAX_THREAD = 64;
    //日志最大缓存字符
    int MAX_LOG_TEMP = 10000;
    //*******************************************************************//


    //所有包名
    String ALL_PACKAGE_NAME = "ALL_PACKAGE_NAME";
    //360
    String APPLICATION_CLASS_360 = "com.stub.StubApp";

    //*******************  客户端相应  *******************//

    int CODE_CLIENT_JOIN = 1;
    int CODE_TARGET_JOIN = 2;
    int CODE_LOG = 3;
    //连接到服务器
    int CODE_CONN_SERVER = 4;

    //正常日志
    int LOG_NORMAL = 100;
    //异常日志
    int LOG_ERROR = 400;

    //*******************  消息键值  *******************//
    //日志
    String KEY_LOG = "KEY_LOG";
    //目标app包名
    String KEY_TARGET_PACKAGE_NAME = "KEY_TARGET_PACKAGE_NAME";
    //目标app
    String KEY_TARGET_APP_NAME = "KEY_TARGET_APP_NAME";
    //消息是否来自目标app
    String KEY_FROM_TARGET = "KEY_FROM_TARGET";
    //连接服务器
    String KEY_CONN_SERVER = "KEY_CONN_SERVER";

}
