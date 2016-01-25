package cn.com.lenovo.speechservice.utils;

import android.util.Log;

/**
 * 管理Log打印的类
 * @author kongqw
 *
 */
public class Loger {
	/**
	 * 打印VERBOSE级别的LOG
	 * @param TAG
	 * @param msg
	 */
	public static void v(String TAG, String msg) {
		if (Constant.LOG_VERBOSE_FLAG < Constant.LOG_LEVEL_FLAG) {
			Log.v(TAG, msg);
		}
	}

	/**
	 * 打印DEBUG级别的LOG
	 * @param TAG
	 * @param msg
	 */
	public static void d(String TAG, String msg) {
		if (Constant.LOG_DEBUG_FLAG < Constant.LOG_LEVEL_FLAG) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * 打印INFO级别的LOG
	 * @param TAG
	 * @param msg
	 */
	public static void i(String TAG, String msg) {
		if (Constant.LOG_INFO_FLAG < Constant.LOG_LEVEL_FLAG) {
			Log.i(TAG, msg);
		}
	}

	/**
	 * 打印WARN级别的LOG
	 * @param TAG
	 * @param msg
	 */
	public static void w(String TAG, String msg) {
		if (Constant.LOG_WARN_FLAG < Constant.LOG_LEVEL_FLAG) {
			Log.w(TAG, msg);
		}
	}

	/**
	 * 打印ERROR级别的LOG
	 * @param TAG
	 * @param msg
	 */
	public static void e(String TAG, String msg) {
		if (Constant.LOG_ERROR_FLAG < Constant.LOG_LEVEL_FLAG) {
			Log.e(TAG, msg);
		}
	}
}
