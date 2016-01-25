package com.lenovo.main.util;

import android.content.Context;

/**
 * 一些公共数据的存放类
 * 
 * @author Administrator
 * 
 */
public class PublicData {
	// 机器人 端的 IP
	public static final String IP = "10.100.8.82";
	// public static final String IP = "192.168.1.6";
	// public static final String IP = "192.168.1.3";
	// 机器人 端的端口号
	public static final int PORT = 9014;
	public static final boolean TOASTFLAG = true;
	/**
	 * -1,表示当前什么任务都没有 0,表示当前是打电话任务 1,定点任务 2,表示遥控任务
	 */
	public static int TASKFLAG = -1;

	/**
	 * 判断联系人的类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getUserFlag(Context context) {
		SharedPreferencesHelpClass sharedPreferencesHelpClassInfor = SharedPreferencesHelpClass
				.getSharedPreferencesHelpClassInfor();
		return sharedPreferencesHelpClassInfor.getUserFlag(context);
	}

}
