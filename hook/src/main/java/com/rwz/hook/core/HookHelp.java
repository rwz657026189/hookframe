package com.rwz.hook.core;

import android.text.TextUtils;

import com.rwz.hook.core.hook.IHookManager;
import com.rwz.hook.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * date： 2019/9/5 10:39
 * author： rwz
 * description：
 **/
public class HookHelp {

    private static final Map<AppConfig, Class<? extends IHookManager>> mHookManagerMap = new HashMap<>();
    private static boolean isInit;

    private HookHelp() {
    }

    public static void register(AppConfig appConfig, Class<? extends IHookManager> manager){
        if(isInit)
            return;
        if(appConfig == null || mHookManagerMap.containsKey(appConfig))
            return;
        mHookManagerMap.put(appConfig, manager);
    }

    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (TextUtils.equals(lpparam.packageName, lpparam.processName)) {
            AppConfig appConfig = HookHelp.getAppConfig(lpparam.packageName);
            if(appConfig != null)
                handleLoadPackage(appConfig, lpparam);
            //针对需要hook所有应用的情况
            AppConfig config = HookHelp.getAppConfig(Constance.ALL_PACKAGE_NAME);
            if(config != null)
                handleLoadPackage(config, lpparam);
        }
    }

    private static void handleLoadPackage(AppConfig appConfig, XC_LoadPackage.LoadPackageParam lpparam) {
        if(appConfig == null)
            return;
        isInit = true;
        Class<? extends IHookManager> cls = mHookManagerMap.get(appConfig);
        if(cls == null)
            return;
        try {
            IHookManager hookManager = cls.newInstance();
            LogUtil.d("HookHelp" + " handleLoadPackage：" + appConfig, appConfig);
            hookManager.setAppConfig(appConfig);
            hookManager.init(lpparam.classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppConfig getAppConfig(String packageName) {
        if(packageName == null)
            return null;
        Set<AppConfig> keySet = mHookManagerMap.keySet();
        if(keySet == null)
            return null;
        for (AppConfig config : keySet) {
            if(TextUtils.equals(packageName, config.getPackageName()))
                return config;
        }
        return null;
    }

}
