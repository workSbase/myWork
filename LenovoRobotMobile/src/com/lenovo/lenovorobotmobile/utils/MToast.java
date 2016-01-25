package com.lenovo.lenovorobotmobile.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 自己封装的 Toast
 * 
 * @author Administrator
 * 
 */
public class MToast {
	public static void showToast(Context context, String content, int valueInt) {
		if (Constant.value) {
			Toast.makeText(context, content, valueInt).show();
		}
	}
}
