package com.lenovo.lenovorobotmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 自己封装的一个 Sharedpreferences
 * 
 * @author Administrator
 * 
 */
public class SharedpreferencesUilts {
	private Editor edit;
	private SharedPreferences sharedPreferences;

	/**
	 * 自己封装的一个 Sharedpreferences
	 * 
	 * 
	 */
	public SharedpreferencesUilts(Context context) {
		sharedPreferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		edit = sharedPreferences.edit();
	}

	/**
	 * 保存一个 String 一个value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveString(String key, String value) {
		return edit.putString(key, value).commit();
	}

	/**
	 * 通过 key 获取到相应的 boolean 值. true 表示的是 中文用户. false 表示的是英文用户
	 * 
	 * @param key
	 * @return
	 */
	public boolean getString(String key) {
		String str = sharedPreferences.getString(key, "");
		if (str != null && !str.equals("")) {
			if (str.equals("china")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * s 可以为null
	 * 
	 * @param key
	 * @param s
	 * @return
	 */
	public String getString(String key, String s) {
		String str = sharedPreferences.getString(key, "");
		if (str != null && !str.equals("")) {
			return str;
		}
		return null;
	}

	/**
	 * 保存一个 Int
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveInt(String key, int value) {
		return edit.putInt(key, value).commit();
	}

	/**
	 * 通过 key 获得保存一个int的值
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		int int1 = sharedPreferences.getInt(key, -1);
		if (int1 != -1) {
			return int1;
		}
		return -1;
	}

	/**
	 * 通过 key 保存一个 boolean
	 * 
	 * @param key
	 * @return
	 */
	public boolean saveBoolean(String key, boolean value) {
		return edit.putBoolean(key, value).commit();
	}

	/**
	 * 通过 key 获得 一个 boolean
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}

	public void removeData() {
		edit.clear().commit();
	}
}
