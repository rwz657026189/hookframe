package com.rwz.hook.inf;

/**
 * date： 2019/9/16 16:41
 * author： rwz
 * description：
 **/
public interface OnResponseListener {

    /**
     * 收到服务器发来的消息
     */
    void onResponseFromServer(int code, String message);

}
