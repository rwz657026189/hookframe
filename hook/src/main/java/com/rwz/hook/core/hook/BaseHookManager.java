package com.rwz.hook.core.hook;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rwz.hook.core.AppConfig;
import com.rwz.hook.core.BridgeService;
import com.rwz.hook.core.Constance;
import com.rwz.hook.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * date： 2019/9/4 16:43
 * author： rwz
 * description：hook管理基类，主要建立了与客户端的通信
 * 注意：
 * 1.发送消息/接收消息的前提是必须配置application且连接到服务
 **/
public abstract class BaseHookManager implements IHookManager, ServiceConnection{

    private final static String SERVICE_CLASS_NAME = BridgeService.class.getName();
    protected Context mContext;
    protected ClassLoader mClassLoader;
    protected AppConfig mAppConfig;
    private Messenger mMessenger;

    @Override
    public void setAppConfig(@NonNull AppConfig appConfig) {
        mAppConfig = appConfig;
    }

    @Override
    public void init(ClassLoader classLoader) {
        boolean isHook = this.mClassLoader == null && classLoader != null;
        this.mClassLoader = classLoader;
        LogUtil.d("BaseHookManager" + " init：" + "isHook = " + isHook + ", classLoader = " + classLoader);
        if (isHook) {
            hookApp();
        }
    }

    protected void hookApp() {
        String appContext = "android.app.Application";
        XposedHelpers.findAndHookMethod(appContext, mClassLoader, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        mContext = (Context) param.args[0];
                        if(mContext != null)
                            mClassLoader = mContext.getClassLoader();
                        LogUtil.d("BaseHookManager" + " afterHookedMethod：success mContext = " + mContext);
                        connService(mContext);
                        onHookSuccess();
                    }
                });
    }

    protected void connService(Context context) {
        if(context == null)
            return;
        Intent intent = new Intent();
        intent.setClassName(Constance.PACKAGE_NAME, SERVICE_CLASS_NAME);
        context.bindService(intent, this, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LogUtil.d("BaseHookManager" + " onServiceConnected：");
        mMessenger = new Messenger(service);
        sendMessage(Constance.CODE_TARGET_JOIN, null);
    }

    //消息：目标app -> 客户端
    protected void sendMessage(int code, @Nullable Bundle bundle) {
        Message message = Message.obtain(null, code);
        message.replyTo = reply;
        if(bundle == null)
            bundle = new Bundle();
        bundle.putString(Constance.KEY_TARGET_PACKAGE_NAME, mAppConfig.getPackageName());
        if(Constance.showLog)
            bundle.putString(Constance.KEY_TARGET_APP_NAME, mAppConfig.getAppName());
        bundle.putBoolean(Constance.KEY_FROM_TARGET, true);
        message.setData(bundle);
        LogUtil.d("BaseHookManager" + " sendMessage：" + message.getData());
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //ui展示日志
    protected void showLog(String content) {
        if (Constance.showLog) {
            Bundle data = new Bundle();
            data.putString(Constance.KEY_LOG, content);
            sendMessage(Constance.CODE_LOG, data);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogUtil.d("BaseHookManager" + " onServiceDisconnected：");
    }

    //消息：客户端 -> 目标app
    final Messenger reply = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mExecutor.execute(new CommRunnable(msg.what, msg.getData()));
            return false;
        }
    }));

    private final Executor mExecutor =  Executors.newFixedThreadPool(Constance.MAX_THREAD);

    private class CommRunnable implements Runnable{
        private int code;
        private Bundle bundle;

        public CommRunnable(int code, Bundle bundle) {
            this.code = code;
            this.bundle = bundle;
        }

        @Override
        public void run() {
            LogUtil.d("BaseHookManager" + " onReceivedEvent：" + "code = " + code, "bundle = " + bundle);
            try {
                onReceivedEvent(code, bundle);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  hook成功, 可以hook对应的类
     */
    protected abstract void onHookSuccess() throws Throwable;

    //消息：客户端 -> 目标app
    protected abstract void onReceivedEvent(int code, Bundle bundle) throws Throwable;


    public void destroy() {
        if(mContext != null)
            mContext.unbindService(this);
    }

    public Object createObject(String className, Object... args) throws Exception{
        return XposedHelpers.newInstance(mClassLoader.loadClass(className), args);
    }

}
