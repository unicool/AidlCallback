// ITaskCallback.aidl
package com.unicool.aidlcallback.server;

// Declare any non-default types here with import statements

interface ITaskCallback {
    /**
     * 用于存放要回调的client端的方法
     * -1：绑定方式启动的服务
     */
    void actionPerformed(int actionId);
}
