package com.rwz.hook.core.app;

import android.content.Context;

public class ContextHelp {

    private static Context instance;

    public static void setContext(Context context) {
        if(context != null)
            ContextHelp.instance = context.getApplicationContext();
    }

    public static Context getInstance() {
        return instance;
    }
}
