package com.rwz.app.hook;

import android.os.Bundle;

import com.rwz.hook.core.AppConfig;
import com.rwz.hook.core.hook.BaseHookManager;
import com.rwz.hook.utils.LogUtil;

/**
 * date： 2019/9/5 10:07
 * author： rwz
 * description：
 **/
public class TestHookManager extends BaseHookManager {

    @Override
    protected void onHookSuccess() {

    }

    @Override
    protected void onReceivedEvent(int code, Bundle bundle) {
        LogUtil.d("TestHookManager" + " onReceivedEvent：" + code + "," + bundle);
    }

    @Override
    public AppConfig getAppConfig() {
        return new AppConfig("xf", "com.foundersc.app.xf.XfApplication");
    }

}
