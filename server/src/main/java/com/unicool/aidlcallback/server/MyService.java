package com.unicool.aidlcallback.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by iCooL on 2017/5/5.
 * 建立服务端，要实现回调，需要新建RemoteCallbackList对象(一个存储回调对象的列表)，通过类似发送广播的形式来实现回调
 */

public class MyService extends Service {

    private static final String TAG = "aidlcallback";

    final RemoteCallbackList<ITaskCallback> mCallbacks = new RemoteCallbackList<ITaskCallback>();

    @Override
    public void onCreate() {
        Log.d(TAG, "MyService onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyService onStartCommand: startId=" + startId);
        callback(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind: ");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ///

    void callback(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).actionPerformed(val);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
    }

    private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub() {
        @Override
        public boolean isTaskRunning() {
            return false;
        }

        @Override
        public void stopRunningTask() {

        }

        @Override
        public void registerCallback(ITaskCallback cb) {
            if (cb != null) {
                mCallbacks.register(cb);
            }
        }

        @Override
        public void unregisterCallback(ITaskCallback cb) {
            if (cb != null) {
                mCallbacks.unregister(cb);
            }
        }
    };
}
