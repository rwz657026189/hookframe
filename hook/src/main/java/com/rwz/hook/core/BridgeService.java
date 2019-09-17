package com.rwz.hook.core;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rwz.hook.core.app.ContextHelp;
import com.rwz.hook.core.websocket.WebSocketManager;
import com.rwz.hook.inf.OnResponseListener;
import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class BridgeService extends Service{

    private static final String TAG = "BridgeService";

    private static MessengerHandler mHandle = new MessengerHandler();
    private Messenger mMsg = new Messenger(mHandle);
    //消息拦截器，只允许设置一个
    private static MsgInterceptor sMsgInterceptor;
    private static String VERSION_CODE;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        boolean connServer = intent.getBooleanExtra(Constance.KEY_CONN_SERVER, false);
        if(connServer)
            WebSocketManager.getInstance().startConn();
        return mMsg.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelp.setContext(this);
        VERSION_CODE = "当前版本v" + Utils.getVersionCode(this) + "\n";
        mHandle.logData.append(VERSION_CODE);
    }

    /**
     * 设置消息拦截器
     */
    public static void setMsgInterceptor(MsgInterceptor sMsgInterceptor) {
        BridgeService.sMsgInterceptor = sMsgInterceptor;
    }

    /**
     * 监听服务器消息
     */
    public static void setResponseListener(OnResponseListener responseListener){
        WebSocketManager.getInstance().setResponseListener(responseListener);
    }

    private static class MessengerHandler extends Handler{

        private Messenger mClientMes;
        private Map<String, Messenger> mTargetMes = new HashMap<>();
        private final StringBuilder logData = new StringBuilder();

        private Messenger getTargetMessenger(Message msg) {
            if(msg == null)
                return null;
            String packageName = msg.getData().getString(Constance.KEY_TARGET_PACKAGE_NAME);
            LogUtil.d("MessengerHandler" + " getTargetMessenger：" + packageName + "," + mTargetMes);
            if (TextUtils.isEmpty(packageName) && mTargetMes.size() == 1)
                return mTargetMes.values().iterator().next();
            return packageName == null ? null : mTargetMes.get(packageName);
        }

        private void putTargetMessenger(Message msg) {
            if(msg == null)
                return;
            String packageName = msg.getData().getString(Constance.KEY_TARGET_PACKAGE_NAME);
            if (!TextUtils.isEmpty(packageName)) {
                mTargetMes.put(packageName, msg.replyTo);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (sMsgInterceptor != null && sMsgInterceptor.onReceivedMessage(msg))
                return;
            LogUtil.d(TAG, "handleMessage", "msg: what = " + msg.what, "data = " +  msg.getData());
            try {
                switch (msg.what) {
                    case Constance.CODE_CLIENT_JOIN: //客户端注册
                        mClientMes = msg.replyTo;
                        outputLog("客户端注册成功", true);
                        break;
                    case Constance.CODE_TARGET_JOIN: //目标注册
                        putTargetMessenger(msg);
                        outputLog(getPrefix(msg) + "注册成功", true);
                        break;
                    case Constance.CODE_CONN_SERVER://连接到服务器
                        WebSocketManager.getInstance().startConn();
                        break;
                    case Constance.CODE_LOG: //输出日志
                        outputLog(getPrefix(msg) + msg.getData().getString(Constance.KEY_LOG),false);
                        break;
                    default:
                        sendMsg(msg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getPrefix(Message msg) {
            String appName = msg.getData().getString(Constance.KEY_TARGET_APP_NAME);
            return (appName == null ? "目标app" : appName) + "：";
        }

        private void outputLog(String msg, boolean isForce) throws RemoteException{
            if(!Constance.showLog && !isForce)
                return;
            LogUtil.d("MessengerHandler" + " outputLog：" + msg);
            logData.append("▪ ").append(Utils.getTime()).append(msg).append("\n");
            int length = VERSION_CODE.length();
            if (logData.length() > Constance.MAX_LOG_TEMP + length) {
                logData.delete(length, logData.length() - Constance.MAX_LOG_TEMP);
            }
            if (mClientMes != null) {
                Message obtain = Message.obtain(null, Constance.CODE_LOG);
                Bundle bundle = new Bundle();
                String text = logData.toString();
                bundle.putString(Constance.KEY_LOG, text);
                obtain.setData(bundle);
                mClientMes.send(obtain);
            } else {
                LogUtil.e(TAG, "mClientMes = null");
            }
        }

        private void sendMsg(Message msg) throws RemoteException {
            boolean isFromTarget = msg.getData().getBoolean(Constance.KEY_FROM_TARGET);
            Message obtain = Message.obtain(null, msg.what);
            obtain.copyFrom(msg);
            if (isFromTarget) {
                if (mClientMes != null)
                    mClientMes.send(obtain);
                else
                    outputLog("客户端未注册", true);
            } else {
                Messenger targetMessenger = getTargetMessenger(msg);
                if (targetMessenger != null)
                    targetMessenger.send(obtain);
                else
                    outputLog(getPrefix(msg) + "未注册", true);
            }
        }
    }


}
