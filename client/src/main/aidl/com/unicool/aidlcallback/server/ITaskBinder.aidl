// ITaskBinder.aidl
package com.unicool.aidlcallback.server;

import com.unicool.aidlcallback.server.ITaskCallback;

// Declare any non-default types here with import statements

interface ITaskBinder {
    /**
     * 用于存放供给client端调用的方法
     */
    boolean isTaskRunning();
    void stopRunningTask();
    void registerCallback(ITaskCallback cb);
    void unregisterCallback(ITaskCallback cb);
}
