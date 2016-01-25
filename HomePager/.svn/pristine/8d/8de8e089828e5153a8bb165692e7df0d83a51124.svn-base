package cn.com.lenovo.homepager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	// 获取到类名
	private final String TAG = this.getClass().getName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "HomePager OnStart", Toast.LENGTH_SHORT).show();
		// // 获取到包的管理者
		PackageManager packageManager = context.getPackageManager();
		// 获取到打开一个应用程序的意图
		Intent intentForPackage = packageManager
				.getLaunchIntentForPackage("cn.com.lenovo.homepager");
		if (null != intentForPackage) {
			// 打开程序
			context.startActivity(intentForPackage);
		}
	}
}
