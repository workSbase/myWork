package com.lenovo.robotmobile.activity.net;

import android.content.Context;

import com.lenovo.robotmobile.activity.minterface.ServerInterface;
import com.lenovo.robotmobile.activity.utils.Utils_log_toast;

/**
 * Created by Administrator on 2015/11/20.
 * 用来连接服务器的一个类,这个类的主要的工作,就是连接服务器
 * 向服务器发送心跳帧,
 * 这个是一个,单例模式
 */
public class ConnectServer implements ServerInterface {
    //先创建对象,防止了多线程的情况下产生错误
    public final static ConnectServer CONNECTSERVER = new ConnectServer();
    private Context context;

    private ConnectServer() {
    }

    public static ConnectServer getConnectserverInfo() {
        return CONNECTSERVER;
    }

    @Override
    public void startConnectServer(String IP, int PORT) {
        connectServer(IP, PORT);
    }

    @Override
    public void closeConnectServer() {

    }

    @Override
    public boolean isConnectServer() {
        return false;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    private void connectServer(String ip, int port) {
        //判断当前的 ip 和 port 是否有效
        boolean isTrue = ipAndPort(ip, port);
        //判断一下当前的网络环境是否是可用的
        boolean isNetWork = isNetWork();
        //如果以上两个条件同时满足的话,才开始连接服务器
        if (isTrue && isNetWork) {
            //这个地方表示是,现在可以正常工作的
        } else if (!isTrue) {
            Utils_log_toast.MToast(context, "请检查ip输入的格式", 0);
        } else {
            Utils_log_toast.MToast(context, "当前网络环境不可用", 0);
        }
    }

    private boolean isNetWork() {
        return false;
    }

    private boolean ipAndPort(String ip, int port) {
        if (ip != null)
            return true;
        return false;
    }
}
