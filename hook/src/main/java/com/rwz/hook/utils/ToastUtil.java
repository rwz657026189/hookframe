package com.rwz.hook.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.rwz.hook.core.app.ContextHelp;


/**
 * Created by rwz on 2017/3/9.
 */

public class ToastUtil {

    private static ToastUtil INSTANCE;
    private Toast mToast;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ToastUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ToastUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void showShortSingle(final String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (isMainThread()) {
            getToast().setText(string);
            mToast.show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getToast().setText(string);
                    //已在主线程中，可以更新UI
                    mToast.show();
                }
            });
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private Toast getToast() {
        if (mToast == null) {
            mToast = Toast.makeText(ContextHelp.getInstance(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    public static void showShort(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, false);
        } else {
            showTextOnBackgroundThread(text, false);
        }
    }

    public static void showLong(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, true);
        } else {
            showTextOnBackgroundThread(text, true);
        }
    }



    private static void showTextOnBackgroundThread(final String text, final boolean isLong) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                showText(text, isLong);
            }
        });
    }

    private static void showText(String text, boolean isLong) {
        if (isLong) {
            Toast.makeText(ContextHelp.getInstance(), text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ContextHelp.getInstance(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
