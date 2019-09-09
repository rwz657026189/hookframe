package com.rwz.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rwz.app.MsgCode;
import com.rwz.app.R;
import com.rwz.hook.core.app.ClientManager;
import com.rwz.hook.core.app.ReceivedListener;

public class MainActivity extends AppCompatActivity implements ReceivedListener {

    private ClientManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = new ClientManager(this);
        mManager.connService(this);
        findViewById(R.id.text).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(MsgCode.KEY_CODE, "000001.sz");
            mManager.sendMessage(MsgCode.REQUEST, bundle);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.destroy(this);
    }

    @Override
    public void onReceivedEvent(int code, String packageName, Bundle data) {

    }

    public static void main(String[] args) {

    }

}
