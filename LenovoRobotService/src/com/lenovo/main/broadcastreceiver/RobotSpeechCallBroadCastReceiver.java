package com.lenovo.main.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 机器人端语音打电话 广播.打电话给谁谁谁
 * 
 * @author Administrator
 */
public class RobotSpeechCallBroadCastReceiver extends BroadcastReceiver {
	public RobotSpeechCallBroadCastReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// String str = intent.getStringExtra("com.android.launcher2.Launcher");
		String str = intent.getStringExtra("CALL").trim();
		Toast.makeText(context, "我收到打电话广播了" + str, Toast.LENGTH_SHORT).show();
		if (str != null) {
			// SharedPreferences 目的就是为了打电话的时候. 让 cameraActivity
			// 在被点击的时候,会出现挂断电话的按钮
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_MULTI_PROCESS);
			sp.edit().putInt("value", 6).commit();
			sendBroadCast(context);

			if (!TextUtils.isEmpty(str) && (str.equals("1001&1002&1003"))) {
				Toast.makeText(context, "拨打电话" + str, Toast.LENGTH_SHORT)
						.show();
			}
			boolean name1 = intent.getBooleanExtra("endCall", false);
			// 执行 语音挂断
			if (name1) {

			}
		}
	}

	private void sendBroadCast(Context context) {
		Intent intent1 = new Intent();
		intent1.setAction("cn.com.lenovo.speechservice");
		intent1.putExtra("StopSpeech", true);
		context.sendBroadcast(intent1);
		Toast.makeText(context, "录音关闭,广播发送", Toast.LENGTH_SHORT).show();
	}
}
