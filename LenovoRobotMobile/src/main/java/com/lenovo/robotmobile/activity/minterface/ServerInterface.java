package com.lenovo.robotmobile.activity.minterface;

/**
 * Created by Administrator on 2015/11/20.
 * ����ӿ���������Ҫ��Ŀ����Ϊ��,�ṩ�������˵�,�������ӷ�����
 * �����ͷ������Ͽ�����
 * �����жϵ�ǰ�Ƿ�ͷ�������������.
 * ��Ҫ��Ŀ����Ϊ������ӿڱ��
 */
public interface ServerInterface {
    /**
     * ��ʼ���ӷ�����
     *
     * @param IP   �������� ip ��ַ
     * @param PORT �������Ķ˿ں�
     */
    public void startConnectServer(String IP, int PORT);

    /**
     * �����ͷ�����֮��Ͽ�����
     */
    public void closeConnectServer();

    /**
     * �жϵ�ǰ�Ƿ�ͷ�����֮�仹����ϵ
     *
     * @return
     */
    public boolean isConnectServer();
}
