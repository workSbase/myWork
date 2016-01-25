package com.lenovo.main.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.MyService;
import com.lenovo.main.activity.MainActivity;

public class ShowTitleBar {
	private Context context;

	public ShowTitleBar(Context context) {
		this.context = context;
	}

	public void showBar() {

		Notification notification = new Notification(R.drawable.ic_launcher1,
				"VideoManageService", System.currentTimeMillis());
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, "VideoService", "is running",
				pendingIntent);
		((MyService) context).startForeground(1, notification);
	}
}
