package com.unicool.aidlcallback.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by iCooL on 2017/5/5.
 * 建立服务端，要实现回调，需要新建RemoteCallbackList对象(一个存储回调对象的列表)，通过类似发送广播的形式来实现回调
 */

public class MyService extends Service {

    private static final String TAG = "aidlcallback";

    final RemoteCallbackList<ITaskCallback> mRcbList = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        Log.d(TAG, "MyService onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyService onStartCommand: startId=" + startId);
//        callbackClient(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind: ");
//        callbackClient(-1);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "MyService onRebind: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "MyService onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyService onDestroy: ");
        mRcbList.kill();
    }

    ///

    private void callbackClient(int val) {
        final int N = mRcbList.beginBroadcast();
        Log.d(TAG, "RemoteCallbackList N: " + N);
        for (int i = 0; i < N; i++) {
            try {
                mRcbList.getBroadcastItem(i).actionPerformed(val);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
                Log.d(TAG, "RemoteCallbackList i: " + i);
                Log.e(TAG, "RemoteCallbackList RemoteException: " + e.getMessage());
            }
        }
        mRcbList.finishBroadcast();
    }

    private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub() {
        @Override
        public boolean isTaskRunning() {
            Log.d(TAG, "ITaskBinder isTaskRunning: ");
            return false;
        }

        @Override
        public void stopRunningTask() {
            Log.d(TAG, "ITaskBinder stopRunningTask: ");
            stopSelf(); //对应startId，默认值-1
        }

        @Override
        public void registerCallback(ITaskCallback cb) {
            Log.d(TAG, "ITaskBinder registerCallback: " + (cb != null));
            if (cb == null) return;
            mRcbList.register(cb);

            // TODO: 2017/5/5 先注册，后使用 
            callbackClient(new Random().nextInt());
        }

        @Override
        public void unregisterCallback(ITaskCallback cb) {
            Log.d(TAG, "ITaskBinder unregisterCallback: " + (cb != null));
            if (cb != null) {
                mRcbList.unregister(cb);
            }
        }
    };
}
