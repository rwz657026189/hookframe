package com.rwz.app.hook;

import android.content.Context;
import android.os.Bundle;

import com.rwz.app.MsgCode;
import com.rwz.hook.core.hook.BaseHookManager;
import com.rwz.hook.utils.LogUtil;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * date： 2019/9/5 10:07
 * author： rwz
 * description：
 * version: v4.7.1
 **/
public class JSHookManager extends BaseHookManager {

    //参数
    private Object mArgs;
    //成员变量
    private String mVars;
    //application
    private String app;
    //activity
    private String activityName;

    @Override
    protected void onHookSuccess() throws Throwable{
        //测试用
        hookActivity();
    }

    @Override
    protected void onReceivedEvent(int code, Bundle bundle) throws Throwable{
        LogUtil.d("JSHookManager" + " onReceivedEvent：" + code + "," + bundle);
        if (code == MsgCode.GET_ACTIVITY_INFO) {
            Bundle data = new Bundle();
            data.putString(MsgCode.KEY_ACTIVITY_INFO, "args:" +  mArgs + "\n vars:" + mVars + "\n app:" + app + "\n activityName:" + activityName);
            sendMessage(MsgCode.RETURN_ACTIVITY_INFO, data);
        }
    }

    private void hookActivity() throws Exception{
        XposedHelpers.findAndHookMethod("android.support.v7.app.AppCompatActivity",
                mClassLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                mArgs = param.args[0];
                Object thisObject = param.thisObject;
                Field[] fields = thisObject.getClass().getDeclaredFields();
                StringBuilder sb = new StringBuilder();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object object = field.get(thisObject);
                        sb.append(field.getName()).append(" = ").append(object).append("\n");
                    }
                }
                mVars = sb.toString();
                if (thisObject instanceof Context) {
                    app = ((Context) thisObject).getApplicationContext() + "";
                }
                activityName = thisObject + "";
            }
        });
    }

}
