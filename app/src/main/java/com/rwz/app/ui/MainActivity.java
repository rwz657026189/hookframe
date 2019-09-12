package com.rwz.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.rwz.app.MsgCode;
import com.rwz.app.R;
import com.rwz.hook.core.app.ClientManager;
import com.rwz.hook.core.app.ReceivedListener;
import com.rwz.hook.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements ReceivedListener {

    private ClientManager mManager;
    private AppCompatTextView mMContextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = new ClientManager(this);
        mManager.connService(this);
        findViewById(R.id.text).setOnClickListener(v -> {
            mManager.sendMessage(MsgCode.GET_ACTIVITY_INFO, null);
        });
        mMContextView = findViewById(R.id.content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.destroy(this);
    }

    @Override
    public void onReceivedEvent(int code, String packageName, Bundle data) {
        if (code == MsgCode.RETURN_ACTIVITY_INFO) {
            String text = data.getString(MsgCode.KEY_ACTIVITY_INFO);
            LogUtil.d("MainActivity" + " onReceivedEvent：" + text);
            mMContextView.setText(text);
        }
    }

    public static void main(String[] args) {

    }

}
