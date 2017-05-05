// ITaskCallback.aidl
package com.unicool.aidlcallback.server;

// Declare any non-default types here with import statements

interface ITaskCallback {
    /**
     * 用于存放要回调client端的方法
     */
    void actionPerformed(int actionId);
}
