package com.lenovo.robotmobile.activity.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/20.
 * 这是一个log 和 Toast 的帮助类
 * 用来显示程序中的log 和 Toast
 */
public class Utils_log_toast {
    private final static boolean ISSHOWLOG = true;
    private final static boolean ISSHOWTOAST = true;

    /**
     * 打印 I log
     *
     * @param TAG     这个是标志
     * @param content 这个是显示的内容
     */
    public static void i(String TAG, String content) {
        if (ISSHOWLOG)
            Log.i(TAG, content);
    }

    /**
     * 打印 E 的log
     *
     * @param Tag     这个是标志
     * @param content 这个是内容
     */
    public static void e(String Tag, String content) {
        if (ISSHOWLOG)
            Log.e(Tag, content);
    }

    /**
     * 显示Toast
     *
     * @param context 上下文环境
     * @param content 要显示的内容
     * @param time    显示的时间
     */
    public static void MToast(Context context, String content, int time) {
        if (ISSHOWTOAST)
            Toast.makeText(context, content, time);
    }
}
