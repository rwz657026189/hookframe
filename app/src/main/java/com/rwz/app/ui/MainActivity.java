package com.rwz.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.rwz.app.R;
import com.rwz.hook.core.Constance;
import com.rwz.hook.core.app.ClientManager;
import com.rwz.hook.core.app.ReceivedListener;
import com.rwz.hook.core.websocket.WebSocketManager;
import com.rwz.hook.utils.LogUtil;
import com.rwz.hook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReceivedListener{

    private ClientManager mManager;
    private AppCompatTextView mContentView;
    private GridView mGridView;
    private List<Pair<String, View.OnClickListener>> mData;
    private SimpleAdapter mAdapter;
    private EditText mInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.content);
        mGridView = findViewById(R.id.grid_view);
        mInputView = findViewById(R.id.input);
        mData = new ArrayList<>();
        mAdapter = new SimpleAdapter(this, mData);
        mGridView.setAdapter(mAdapter);
        mManager = new ClientManager(this)
                .setMsgInterceptor(msg -> false) //拦截消息
                .setResponseListener((code, message) -> {//接受到服务器消息
                    //...
                })
                .connService(this, false);
        addFunction();
        //每次构建后，重启手机
        Utils.checkVersionCode(this);
    }

    private void addFunction() {
        mAdapter.addData("连接服务器", v -> mManager.sendMessage(Constance.CODE_CONN_SERVER, null));
        mAdapter.addData("断开服务器", v -> WebSocketManager.getInstance().closeConn());
        mAdapter.addData("复制日志", v -> Utils.copyText(this, mContentView.getText()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.destroy(this);
    }

    @Override
    public void onReceivedEvent(int code, String packageName, Bundle data) {
        if (code == Constance.CODE_LOG) {
            mContentView.setText(data.getString(Constance.KEY_LOG));
        }
    }

    private String print(String text) {
        LogUtil.d("MainActivity" + " print：" + text);
        return text;
    }

    private String show(String text) {
        LogUtil.d("MainActivity" + " print：" + text);
        return text;
    }

}
