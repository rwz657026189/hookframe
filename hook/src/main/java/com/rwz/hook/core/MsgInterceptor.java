package com.rwz.hook.core;

import android.os.Message;

/**
 * date： 2019/9/12 15:45
 * author： rwz
 * description：消息拦截器
 **/
public interface MsgInterceptor {

    /**
     * @param msg 收到双向的消息
     * @return true: 拦截， false：不拦截
     */
    boolean onReceivedMessage(Message msg);

}
