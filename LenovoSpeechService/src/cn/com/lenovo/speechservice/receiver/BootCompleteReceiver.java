package cn.com.lenovo.speechservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * 开机自动启动的广播
 * @author Administrator
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//		// 获取到包的管理者
		//		PackageManager packageManager = context.getPackageManager();
		//		// 获取到打开一个应用程序的意图
		//		Intent intentForPackage = packageManager.getLaunchIntentForPackage("cn.com.lenovo.speechservice");
		//		if (null != intentForPackage) {
		//			// 打开程序
		//			context.startActivity(intentForPackage);
		//		}
	}
}
