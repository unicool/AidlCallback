package com.unicool.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.unicool.aidlcallback.server.ITaskBinder;
import com.unicool.aidlcallback.server.ITaskCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "aidlcallback";
    private Button btnBind;
    private Button btnUnbind;
    private Button btnStart;
    private Button btnStop;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);


        btnBind = (Button) findViewById(R.id.btn_bind);
        btnUnbind = (Button) findViewById(R.id.btn_unbind);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);

        btnBind.setText("Bind Service");
        btnBind.setEnabled(true);
        btnUnbind.setText("Unbind Service");
        btnUnbind.setEnabled(false);

        btnBind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBindClick();
            }
        });

        btnUnbind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onUnbindClick();
            }
        });
    }

    private void onBindClick() {
        printf("send intent & connection to bind");
        Bundle args = new Bundle();
        Intent intent = new Intent("com.unicool.aidlcallback.server.MyService");
        intent.putExtras(args);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        startService(intent);
        btnBind.setEnabled(false);
        btnUnbind.setEnabled(true);
    }

    private void onUnbindClick() {
        printf("send connection to unbind");
        unbindService(mConnection);
//        stopService(new Intent("com.unicool.aidlcallback.server.MyService"));
        btnBind.setEnabled(true);
        btnUnbind.setEnabled(false);
    }

    private void printf(String str) {
        Log.d(TAG, "### Client---" + str);
    }

    private ITaskBinder mService;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ITaskBinder.Stub.asInterface(service);
            try {
                // TODO: 2017/5/5 调用远程服务的方法 
                mService.registerCallback(mCallback);

                // TODO: 2017/5/5 本客户端方法被服务调用 X 启动/绑定的时候就被调用了
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private ITaskCallback mCallback = new ITaskCallback.Stub() {

        public void actionPerformed(int id) {
            printf("client：callback by server id=" + id);
        }
    };

}
