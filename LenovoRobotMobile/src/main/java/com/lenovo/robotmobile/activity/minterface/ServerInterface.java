package com.lenovo.robotmobile.activity.minterface;

import android.content.Context;

/**
 * Created by Administrator on 2015/11/20.
 * 这个接口做出来主要的目的是为了,提供给其他人的,用来连接服务器
 * 用来和服务器断开连接
 * 用来判断当前是否和服务器还有连接.
 * 主要的目的是为了面向接口编程
 */
public interface ServerInterface {
    /**
     * 开始连接服务器
     *
     * @param IP   服务器的 ip 地址
     * @param PORT 服务器的端口号
     */
    public void startConnectServer(String IP, int PORT);

    /**
     * 用来和服务器之间断开连接
     */
    public void closeConnectServer();

    /**
     * 判断当前是否和服务器之间还有联系
     *
     * @return
     */
    public boolean isConnectServer();

    /**
     * 初始化上下文环境
     */
    public void setContext(Context context);
}
