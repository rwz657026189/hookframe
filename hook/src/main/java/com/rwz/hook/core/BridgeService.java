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
import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class BridgeService extends Service{

    private static final String TAG = "BridgeService";

    private static final int MAX_LOG_TEMP = 10000;
    private static MessengerHandler mHandle = new MessengerHandler();
    private Messenger mMsg = new Messenger(mHandle);
    //消息拦截器，只允许设置一个
    private static MsgInterceptor sMsgInterceptor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMsg.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelp.setContext(this);
    }

    /**
     * 设置消息拦截器
     */
    public static void setMsgInterceptor(MsgInterceptor sMsgInterceptor) {
        BridgeService.sMsgInterceptor = sMsgInterceptor;
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
                    case Constance.CLIENT_JOIN: //客户端注册
                        mClientMes = msg.replyTo;
                        outputLog("客户端注册成功");
                        break;
                    case Constance.TARGET_JOIN: //目标注册
                        putTargetMessenger(msg);
                        outputLog("目标注册成功");
                        break;
                    case Constance.LOG: //输出日志
                        outputLog(msg.getData().getString(Constance.KEY_MSG));
                        break;
                    default:
                        sendMsg(msg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void outputLog(String msg) throws RemoteException{
            if(!Constance.showLog)
                return;
            LogUtil.d("MessengerHandler" + " outputLog：" + msg);
            logData.append(Utils.getTime()).append(msg).append("\n");
            if (logData.length() > MAX_LOG_TEMP) {
                logData.delete(0, logData.length() - MAX_LOG_TEMP);
            }
            if (mClientMes != null) {
                Message obtain = Message.obtain(null, Constance.LOG);
                Bundle bundle = new Bundle();
                bundle.putString(Constance.KEY_MSG, logData.toString());
                obtain.setData(bundle);
                mClientMes.send(obtain);
            } else {
                LogUtil.e(TAG, "mClientMes = null");
            }
        }

        public void sendMsg(Message msg) throws RemoteException {
            boolean isFromTarget = msg.getData().getBoolean(Constance.KEY_FROM_TARGET);
            Message obtain = Message.obtain(null, msg.what);
            obtain.copyFrom(msg);
            if (isFromTarget) {
                if (mClientMes != null)
                    mClientMes.send(obtain);
                else
                    LogUtil.d("MessengerHandler" + " sendMsg：客户端未注册");
            } else {
                Messenger targetMessenger = getTargetMessenger(msg);
                if (targetMessenger != null)
                    targetMessenger.send(obtain);
                else
                    LogUtil.d("MessengerHandler" + " sendMsg：目标app未注册");
            }
        }
    }


}
