package com.rwz.hook.core.websocket;

import com.google.gson.Gson;
import com.rwz.hook.core.Constance;
import com.rwz.hook.core.app.ContextHelp;
import com.rwz.hook.entity.LogEntity;
import com.rwz.hook.inf.IPostEvent;
import com.rwz.hook.inf.OnResponseListener;
import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.Utils;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */
public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    static final int port = Constance.SERVER_PORT;
    //    private static final String SERVER_URL = "ws://localhost:8080/";
    private static final String SERVER_URL = Constance.SERVER_URL;
    private CommWebSocketClient mWebClient;
    private static WebSocketManager instance;
    private final Executor executorService = Executors.newSingleThreadExecutor();
    //设备唯一序列号
    private final String IMEI;

    public static WebSocketManager getInstance() {
        if(instance == null)
            synchronized (WebSocketClient.class) {
                if(instance == null)
                    instance = new WebSocketManager();
            }
        return instance;
    }

    private WebSocketManager() {
        IMEI = Utils.getImei(ContextHelp.getInstance());
    }

    public void startConn() {
        if (mWebClient == null) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    HashMap header = new HashMap();
                    header.put("Origin", "http://127.0.0.1:" + port);
                    mWebClient = new CommWebSocketClient(URI.create(SERVER_URL), header);
                    if(mWebClient != null)
                        mWebClient.setResponseListener(mResponseListener);
                    mWebClient.setConnResultEvent(mConnResultEvent);
                    mWebClient.connect();
                }
            });
        }
    }

    IPostEvent<Boolean> mConnResultEvent = new IPostEvent<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            if (result) {
                mReconnectCount = 0;
                sendLog("连接成功！");
            } else if(mWebClient != null){
                mWebClient.setConnResultEvent(null);
                closeConn();
                tryConn();
            }
        }
    };

    public void closeConn() {
        if (mWebClient != null) {
            if (!mWebClient.isClosed())
                mWebClient.close();
            mWebClient.setConnResultEvent(null);
            mWebClient.setResponseListener(null);
        }
        mWebClient = null;
        LogUtil.d(TAG, "已关闭连接");
    }

    private int mReconnectCount;

    //-1表示无限重连
    private int MAX_RECONNECT_COUNT  = -1;
    private void tryConn() {
        if (mReconnectCount <= MAX_RECONNECT_COUNT || MAX_RECONNECT_COUNT == -1) {
            mReconnectCount++;
            LogUtil.d(TAG, "重试次数：" + mReconnectCount);
            startConn();
        } else {
            LogUtil.e(TAG, "已达到重试次数上限：" + mReconnectCount);
        }
    }

    private OnResponseListener mResponseListener;

    /**
     * 设置服务器发来的消息监听
     */
    public WebSocketManager setResponseListener(OnResponseListener responseListener) {
        mResponseListener = responseListener;
        if(mWebClient != null)
            mWebClient.setResponseListener(responseListener);
        return this;
    }

    //*************************  发送消息  *************************//
    public void sendMsg(String msg) {
        boolean isOpen = mWebClient != null && mWebClient.isOpen();
        LogUtil.d(TAG, "sendMsg", "isOpen = " + isOpen);
        if (isOpen) {
            mWebClient.send(msg);
        }
    }

    public void sendMsg(Object obj) {
        String json = new Gson().toJson(obj);
        String newJson = Utils.injectParams(json, "IMEI", IMEI);
        sendMsg(newJson);
    }

    /** 发送正常的消息日志 **/
    public void sendLog(String msg) {
        sendMsg(LogEntity.create(msg));
    }

    /** 发送异常的消息日志 **/
    public void sendErrorLog(String msg) {
        sendMsg(LogEntity.createError(msg));
    }

}
