package com.rwz.hook.core.websocket;


import com.rwz.hook.core.Constance;
import com.rwz.hook.utils.LogUtil;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 仅供测试
 */
public class WebSocketServerManager {

    private static final String TAG = "WebSocketServerManager";
    private static WebSocketServerManager instance;
    private WebSocketServer webSocketServer;
    private WebSocket mWebSocketClient;
    static final int port = Constance.isDebug ? 9090 : 8080;

    public static WebSocketServerManager getInstance() {
        if(instance == null )
            synchronized (WebSocketServerManager.class) {
                if(instance == null)
                    instance = new WebSocketServerManager();
            }
        return instance;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void startServer() {
        try {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    webSocketServer = new WebSocketServer(new InetSocketAddress("localhost", port)) {
                        @Override
                        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                            LogUtil.d(TAG, "server onOpen");
                            mWebSocketClient = webSocket;
                        }

                        @Override
                        public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                            LogUtil.d(TAG,"server onClose:" + i + " " + s + " " + b);
                        }

                        @Override
                        public void onMessage(WebSocket webSocket, String s) {
                            LogUtil.d(TAG,"server onMessage:" + s);
                            webSocket.send("received : " + s);
                        }

                        @Override
                        public void onError(WebSocket webSocket, Exception e) {
                            LogUtil.d(TAG,"server onMessage:" + e);
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStart() {
                            LogUtil.d(TAG,"server onStart:");
                        }
                    };
                    webSocketServer.run();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if (mWebSocketClient != null && webSocketServer != null) {
            mWebSocketClient.send(msg);
        }
    }


}
