package com.lenovo.main.util;

import android.content.Context;
import android.content.Intent;

public class SendBroadCastTools {
    /**
     * @param context           上下文
     * @param action            广播接受者的action
     * @param userFriendContent 用户好友的字符串
     * @param flag              是否将用户的好友发送出去
     */
    public static void myBroadCast(Context context, String action, String key,
                                   String userFriendContent, boolean flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (flag) {
            intent.putExtra(key, userFriendContent);
        }
        context.sendBroadcast(intent);
    }

    /**
     * @param context     上下文环境
     * @param action      发送的action
     * @param key         保存的key
     * @param booleanFlag boolean 类型的值
     * @param flag        是否要有保存的值
     */
    public static void myBroadCast(Context context, String action, String key,
                                   boolean booleanFlag, boolean flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (flag) {
            intent.putExtra(key, booleanFlag);
        }
        context.sendBroadcast(intent);
    }

    /**
     * @param context 上下文环境
     * @param action  发送的action
     * @param key     保存的key
     * @param intFlag int 类型的值
     * @param flag    是否要有保存的值
     */
    public static void myBroadCast(Context context, String action, String key,
                                   int intFlag, boolean flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (flag) {
            intent.putExtra(key, intFlag);
        }
        context.sendBroadcast(intent);
    }
}
