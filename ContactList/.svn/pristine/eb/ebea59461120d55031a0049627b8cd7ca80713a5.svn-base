package com.lenovo.contactslist.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 单例模式的一个类
 */
public class SharedPreferencesHelpClass {
	private SharedPreferencesHelpClass() {
	}

	public static final SharedPreferencesHelpClass SHARED_PREFERENCES_HELP_CLASS = new SharedPreferencesHelpClass();
	private SharedPreferences sharedPreferences;

	public static SharedPreferencesHelpClass getSharedPreferencesHelpClassInfor() {

		return SHARED_PREFERENCES_HELP_CLASS;
	}

	public int getUserFlag(Context context) {
		sharedPreferences = context.getSharedPreferences("userFlagConfig",
				Context.MODE_PRIVATE);
		String stringFlag = sharedPreferences.getString("userFlag", "");
		return panDuanUserFlag(stringFlag);
	}

	private int panDuanUserFlag(String stringFlag) {
		// TODO Auto-generated method stub
		if (!stringFlag.equals("")) {
			if (stringFlag.equals("CHINA")) {
				return 0;
			} else if (stringFlag.equals("ENGLISH")) {
				return 1;
			}
		}
		return -1;
	}
}
