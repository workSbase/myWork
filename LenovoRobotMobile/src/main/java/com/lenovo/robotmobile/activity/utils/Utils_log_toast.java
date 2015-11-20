package com.lenovo.robotmobile.activity.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/20.
 * ����һ��log �� Toast �İ�����
 * ������ʾ�����е�log �� Toast
 */
public class Utils_log_toast {
    private final static boolean ISSHOWLOG = true;
    private final static boolean ISSHOWTOAST = true;

    /**
     * ��ӡ I log
     *
     * @param TAG     ����Ǳ�־
     * @param content �������ʾ������
     */
    public static void i(String TAG, String content) {
        if (ISSHOWLOG)
            Log.i(TAG, content);
    }

    /**
     * ��ӡ E ��log
     *
     * @param Tag     ����Ǳ�־
     * @param content ���������
     */
    public static void e(String Tag, String content) {
        if (ISSHOWLOG)
            Log.e(Tag, content);
    }

    /**
     * ��ʾToast
     *
     * @param context �����Ļ���
     * @param content Ҫ��ʾ������
     * @param time    ��ʾ��ʱ��
     */
    public static void MToast(Context context, String content, int time) {
        if (ISSHOWTOAST)
            Toast.makeText(context, content, time);
    }
}
