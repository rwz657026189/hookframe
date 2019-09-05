package com.rwz.hook.core.app;

import android.os.Bundle;

/**
 * date： 2019/9/5 15:14
 * author： rwz
 * description：
 **/
public interface ReceivedListener {

    void onReceivedEvent(int code, String packageName, Bundle data);

}
