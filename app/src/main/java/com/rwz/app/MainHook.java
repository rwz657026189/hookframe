package com.rwz.app;

import android.util.Log;

import com.rwz.app.hook.JSHookManager;
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
        HookHelp.register(new AppConfig("js", "com.jianshu.haruki",
                "com.tencent.tinker.loader.app.TinkerApplication"), JSHookManager.class);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d(TAG, "handleLoadPackage: packageName = " + lpparam.packageName + ", processName = " + lpparam.processName);
        HookHelp.handleLoadPackage(lpparam);
    }

}
