package com.rwz.app;

import android.util.Log;

import com.rwz.hook.core.hook.TestHookManager;
import com.rwz.hook.core.AppConfig;
import com.rwz.hook.core.HookHelp;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * date： 2019/9/5 10:12
 * author： rwz
 * description：
 **/
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "MainHook";

    public MainHook() {
        //测试，不区分应用
        HookHelp.register(new AppConfig(), TestHookManager.class);
        //只测试某个应用
//        HookHelp.register(new AppConfig("简书", "com.jianshu.haruki"), TestHookManager.class);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d(TAG, "handleLoadPackage: PACKAGE_NAME = " + lpparam.packageName + ", processName = " + lpparam.processName);
        HookHelp.handleLoadPackage(lpparam);
    }

}
