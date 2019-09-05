package com.rwz.hook.core;

import android.text.TextUtils;

import com.rwz.hook.core.hook.IHookManager;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * date： 2019/9/5 10:39
 * author： rwz
 * description：
 **/
public class HookHelp {

    private static final Map<String, Class<? extends IHookManager>> mHookManagerClass = new HashMap<>();
    private static boolean isInit;

    private HookHelp() {
    }

    public static void register(String packageName, Class<? extends IHookManager> manager){
        if(isInit)
            return;
        if(TextUtils.isEmpty(packageName) || mHookManagerClass.containsKey(packageName))
            return;
        mHookManagerClass.put(packageName, manager);
    }

    public static boolean isHook(String packageName, String processName) {
        return TextUtils.equals(packageName, processName) && mHookManagerClass.containsKey(packageName);
    }

    public static void handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam) {
        isInit = true;
        Class<? extends IHookManager> cls = mHookManagerClass.get(packageName);
        if(cls == null)
            return;
        try {
            IHookManager hookManager = cls.newInstance();
            AppConfig appConfig = hookManager.getAppConfig();
            if(appConfig != null)
                appConfig.setPackageName(packageName);
            hookManager.init(lpparam.classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
