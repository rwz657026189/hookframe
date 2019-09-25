package com.rwz.hook.core.hook;

/**
 * date： 2019/9/24 16:16
 * author： rwz
 * description：
 **/
public class SimpleMethodHook extends CommMethodHook{

    public SimpleMethodHook() {
    }

    public SimpleMethodHook(boolean showLog) {
        super(showLog);
    }

    @Override
    protected void onCallMethod(MethodHookParam param) throws Throwable {

    }
}
