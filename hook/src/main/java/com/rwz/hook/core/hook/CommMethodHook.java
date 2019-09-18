package com.rwz.hook.core.hook;

import com.rwz.hook.core.Constance;
import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.ReflectUtil;

import de.robv.android.xposed.XC_MethodHook;

/**
 * date： 2019/9/18 11:09
 * author： rwz
 * description：
 **/
public abstract class CommMethodHook extends XC_MethodHook {

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        if (Constance.showLog) {
            String name = param.method.getName();
            Class<?> cls = param.method.getDeclaringClass();
            //打印相关参数
            LogUtil.d(cls + "#" + name + "()", ReflectUtil.getHookParamDetail(param));
            //打印堆栈信息
            LogUtil.stackTraces(name, 5, 4);
        }
        onCallMethod(param);
    }

    protected abstract void onCallMethod(MethodHookParam param) throws Throwable;

}
