package com.rwz.hook.core.hook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.ReflectUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * date： 2019/9/5 10:07
 * author： rwz
 * description：调试专用，输出当前应用activity名、application、包名、入参、成员变量
 **/
public class TestHookManager extends BaseHookManager {

    @Override
    protected void onHookSuccess() throws Throwable{
        //测试用
        hookOnResume();
    }

    @Override
    protected void onReceivedEvent(int code, Bundle bundle) throws Throwable{
    }

    private void hookOnResume() throws Exception{
        XposedHelpers.findAndHookMethod("android.app.Activity",
                mClassLoader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object thisObject = param.thisObject;
                Object vars = ReflectUtil.getAllFiled(thisObject, "");
                String app = "";
                String packageName = "";
                String turnArgs = "";
                if (thisObject instanceof Context) {
                    Context context = (Context) thisObject;
                    app = context.getApplicationContext() + "";
                    packageName = context.getPackageName();
                    if(context instanceof Activity)
                        turnArgs = ((Activity) context).getIntent().getExtras() + "";
                }
                String activityName = thisObject + "";
                LogUtil.d("TestHookManager" + " onResume：--------------------------------------------------------------------");
                LogUtil.d("TestHookManager" + " onResume：application = " + app);
                LogUtil.d("TestHookManager" + " onResume：PACKAGE_NAME = " + packageName);
                LogUtil.d("TestHookManager" + " onResume：activity = " + activityName);
                LogUtil.d("TestHookManager" + " onResume：turnArgs = " + turnArgs);
                LogUtil.d("TestHookManager" + " onResume：vars = " + vars);
                showLog("application = " + app + "\n"
                        + "packageName = " + packageName + "\n"
                        + "activity = " + activityName + "\n"
                        + "跳转参数 = " + turnArgs + "\n"
                        + "成员变量 = " + vars);
            }
        });
    }

}
