package com.lenovo.robotmobile.activity.net;

import android.content.Context;

import com.lenovo.robotmobile.activity.minterface.ServerInterface;
import com.lenovo.robotmobile.activity.utils.Utils_log_toast;

/**
 * Created by Administrator on 2015/11/20.
 * �������ӷ�������һ����,��������Ҫ�Ĺ���,�������ӷ�����
 * ���������������֡,
 * �����һ��,����ģʽ
 */
public class ConnectServer implements ServerInterface {
    //�ȴ�������,��ֹ�˶��̵߳�����²�������
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
        //�жϵ�ǰ�� ip �� port �Ƿ���Ч
        boolean isTrue = ipAndPort(ip, port);
        //�ж�һ�µ�ǰ�����绷���Ƿ��ǿ��õ�
        boolean isNetWork = isNetWork();
        //���������������ͬʱ����Ļ�,�ſ�ʼ���ӷ�����
        if (isTrue && isNetWork) {
            //����ط���ʾ��,���ڿ�������������
        } else if (!isTrue) {
            Utils_log_toast.MToast(context, "����ip����ĸ�ʽ", 0);
        } else {
            Utils_log_toast.MToast(context, "��ǰ���绷��������", 0);
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
