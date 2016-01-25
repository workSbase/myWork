package com.lenovo.main.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 这是 SharedPreferences 的一个帮助类,这个类是一个单例模式的
 * 
 * @author Administrator
 * 
 */
public class SharedPreferencesHelpClass {
	private SharedPreferencesHelpClass() {
	}

	public static final SharedPreferencesHelpClass SHARED_PREFERENCES_HELP_CLASS = new SharedPreferencesHelpClass();

	public static SharedPreferencesHelpClass getSharedPreferencesHelpClassInfor() {
		return SHARED_PREFERENCES_HELP_CLASS;
	}

	/**
	 * 判断用户是哪一个角色
	 * 
	 * @param context
	 * @return
	 */
	public int getUserFlag(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"userChangeCinfig", Context.MODE_PRIVATE);
		String stringUserFlag = preferences.getString("userFlag", "");

		return panDuanUserFlag(stringUserFlag);
	}

	private int panDuanUserFlag(String stringUserFlag) {
		if (!stringUserFlag.equals("")) {
			if (stringUserFlag.equals("CHINA")) {
				return 0;
			} else if (stringUserFlag.equals("ENGLISH")) {
				return 1;
			}
		}
		return -1;
	}
}
