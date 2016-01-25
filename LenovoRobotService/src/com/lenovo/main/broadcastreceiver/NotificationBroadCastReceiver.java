package com.lenovo.main.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lenovo.lenovoRobotService.R;

/**
 * 创建未接来电的,通知消息
 * 
 * @author Administrator
 */
public class NotificationBroadCastReceiver extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// 获得一个状态栏通知管理器
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 指定弹出简要信息的图标和内容, 弹出的时间为: 现在
		Notification notification = new Notification(R.drawable.a1,
				"You have had news of the call..", System.currentTimeMillis());

		notification.setLatestEventInfo(context, "Tom",
				"Just to give you a call", null);

		// 设置通知的类型为: 前台服务, 特点, 不可以被关闭掉

		// 指定弹出通知时: 有声音, 震动, 亮灯
		notification.defaults = Notification.DEFAULT_ALL;

		nm.notify(88, notification); // 弹出一个通知

	}

}
