package com.rwz.hook.core.app;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.rwz.hook.core.BridgeService;
import com.rwz.hook.core.Constance;
import com.rwz.hook.core.MsgInterceptor;
import com.rwz.hook.inf.OnResponseListener;
import com.rwz.hook.utils.LogUtil;

/**
 * date： 2019/9/5 14:00
 * author： rwz
 * description：
 **/
public class ClientManager {

    private static final String TAG = "ClientManager";

    private ReceivedListener mReceivedListener;
    private static Messenger mMessenger;

    public ClientManager(ReceivedListener listener) {
        mReceivedListener = listener;
    }


    public ClientManager connService(Context context, boolean isConnServer) {
        Intent service = new Intent();
        service.setClassName(context.getPackageName(), BridgeService.class.getName());
        service.putExtra(Constance.KEY_CONN_SERVER, isConnServer);
        context.bindService(service, conn, Service.BIND_AUTO_CREATE);
        return this;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(TAG, "onServiceConnected");
            mMessenger = new Messenger(service);
            sendMessage(Constance.CODE_CLIENT_JOIN, null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    //消息：目标app -> 客户端
    //注意：若只需hook一个app，可省bundle，否则必须包含包名参数Constance.KEY_TARGET_PACKAGE_NAME
    public void sendMessage(int code, @Nullable Bundle bundle) {
        Message message = Message.obtain(null, code);
        message.replyTo = reply;
        if(bundle == null)
            bundle = new Bundle();
        bundle.putBoolean(Constance.KEY_FROM_TARGET, false);
        message.setData(bundle);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Messenger reply = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (mReceivedListener != null) {
                mReceivedListener.onReceivedEvent(msg.what, bundle.getString(Constance.KEY_TARGET_PACKAGE_NAME), bundle);
            }
            return false;
        }
    }));

    public void destroy(Context context) {
        mReceivedListener = null;
        context.unbindService(conn);
        setMsgInterceptor(null);
        setResponseListener(null);
    }

    /**
     * 设置消息拦截器, 页面关闭后默认会释放，若要保持后台开启，请在application中调用BridgeService.setMsgInterceptor()
     */
    public ClientManager setMsgInterceptor(MsgInterceptor sMsgInterceptor) {
        BridgeService.setMsgInterceptor(sMsgInterceptor);
        return this;
    }

    /**
     * 监听服务器消息，页面关闭后默认会释放，若要保持后台开启，请在application中调用BridgeService.setResponseListener()
     */
    public ClientManager setResponseListener(OnResponseListener responseListener){
        BridgeService.setResponseListener(responseListener);
        return this;
    }

}
