package com.rwz.hook.core.websocket;

import com.rwz.hook.inf.IPostEvent;
import com.rwz.hook.inf.OnResponseListener;
import com.rwz.hook.utils.LogUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 *  参考： https://blog.csdn.net/sbsujjbcy/article/details/52839540
 *  WebSocket 6455协议原文：https://tools.ietf.org/html/rfc6455#section-10
 *  onClose() code : 403, 需要在头信息添加Origin字段：值为客户端ip, 详见协议内容
 */
public class CommWebSocketClient extends WebSocketClient{

    private static final String TAG = "CommWebSocketClient";

    private IPostEvent<Boolean> mConnResultEvent;
    private OnResponseListener mResponseListener;

    public CommWebSocketClient(URI serverUri, Map headers) {
        super(serverUri, new Draft_6455(), headers, 5000);
    }

    public void setConnResultEvent(IPostEvent<Boolean> mConnResultEvent) {
        this.mConnResultEvent = mConnResultEvent;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        short httpStatus = handshakedata.getHttpStatus();
        LogUtil.d(TAG, "onOpen" , "status = " + httpStatus, "msg = " + handshakedata.getHttpStatusMessage());
        if (mConnResultEvent != null) {
            mConnResultEvent.onResult(httpStatus == 101);
        }
    }

    @Override
    public void onMessage(String message) {
        LogUtil.d(TAG, "onMessage", message);
        int code = getCode(message);
        if(mResponseListener != null)
            mResponseListener.onResponseFromServer(code, message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
    }

    private int getCode(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            return jsonObject.getInt("Code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogUtil.d(TAG, "onClose", "code = " + code, "reason = " + reason, "remote = " + remote);
        //1000 为正常关闭连接
        if (mConnResultEvent != null && code != 1000) {
            mConnResultEvent.onResult(false);
        }
    }

    @Override
    public void onError(Exception ex) {
        LogUtil.d(TAG, "onError", ex == null ? "" : ex.getMessage());
        if (ex != null) {
            ex.printStackTrace();
        }
        if (mConnResultEvent != null) {
            mConnResultEvent.onResult(false);
        }
    }

    public void setResponseListener(OnResponseListener responseListener) {
        mResponseListener = responseListener;
    }
}
