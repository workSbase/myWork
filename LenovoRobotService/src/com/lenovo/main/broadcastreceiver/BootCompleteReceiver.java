package com.lenovo.main.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	// 获取到类名
	// private final String TAG = "BootCompleteReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// // 监听手机启动
		// Toast.makeText(context, "开机启动的广播", Toast.LENGTH_SHORT).show();
		// Intent intent_service = new Intent(context, MyService.class);
		// context.startService(intent_service);

		// 获取到包的管理者
		PackageManager packageManager = context.getPackageManager();
		// 获取到打开一个应用程序的意图
		Intent intentForPackage = packageManager
				.getLaunchIntentForPackage("com.lenovo.lenovoRobotService");
		if (null != intentForPackage) {
			// 打开程序
			context.startActivity(intentForPackage);
		}
	}
}
