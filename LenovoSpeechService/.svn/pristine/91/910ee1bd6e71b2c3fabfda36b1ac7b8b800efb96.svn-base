package cn.com.lenovo.speechservice.engine;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 管理任务栈的类
 * @author kongqw
 *
 */
public class Tasker {
	// 上下文
	private Context mContext;
	// 程序名
	private String mAppName;
	// 程序包名
	private String mPackageName;
	// Activity管理者
	private static ActivityManager mActivityManager;
	// 包管理者
	private PackageManager mPackageManager;

	/**
	 * 构造方法
	 * @param context
	 */
	public Tasker(Context context) {
		this.mContext = context;
		if (null == mActivityManager)
			mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/**
	 * 获取收当前正在运行的程序包名
	 * @return
	 */
	public String getRunningTask() {
		// 从运行的任务栈中取得屏幕当前正在运行的程序
		List<RunningTaskInfo> runningTasks = mActivityManager.getRunningTasks(1);
		// 遍历获取包名
		for (RunningTaskInfo runningTask : runningTasks) {
			mPackageName = runningTask.baseActivity.getPackageName();
		}
		return mPackageName;
	}

	/**
	 * 获取收当前正在运行的程序名
	 * @return
	 */
	public String getRunningAppName() {
		// 取得包名
		mPackageName = getRunningTask();
		// 取得包管理者
		mPackageManager = mContext.getPackageManager();
		try {
			// 通过包名取得应用名
			mAppName = mPackageManager.getApplicationInfo(mPackageName, PackageManager.GET_META_DATA)
					.loadLabel(mPackageManager).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return mAppName;
	}

	/**
	 * 获取正在运行的Activity
	 * @return
	 */
	public String getRunningActivity() {
		return mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName().toString();
	}
}
