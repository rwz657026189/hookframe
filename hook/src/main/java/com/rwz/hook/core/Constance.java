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
    //是否显示日志
    boolean showLog = true;
    //最大支持线程数
    int MAX_THREAD = 64;
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
    String KEY_MSG = "msg";
    String KEY_TARGET_PACKAGE_NAME = "KEY_TARGET_PACKAGE_NAME";
    String KEY_FROM_TARGET = "FROM_TARGET";
    //连接服务器
    String KEY_CONN_SERVER = "KEY_CONN_SERVER";

}
