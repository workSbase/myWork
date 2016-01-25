package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.MIService.BaseService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 测量 身高的时候,会发出这样一条广播来的,和人脸识别的时候
 * 
 * @author Administrator
 * 
 */
public class MeasureBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "测量的广播接收到了", Toast.LENGTH_SHORT).show();

		startAccept(context, intent);
	}

	private void startAccept(Context context, Intent intent) {
		int robotTaskId = BaseService.detectPersonInterface.getRobotTaskId();
		int intExtra = intent.getIntExtra("type", -1);
		if (intExtra != -1) {
			if (intExtra == 2 || intExtra == 0) {
				// 测量身高
				BaseService.detectPersonInterface.StartDetectPersonInfo();
			} else if (intExtra == 3 || intExtra == 1) {
				Toast.makeText(context, "当前机器人的任务ID " + robotTaskId,
						Toast.LENGTH_SHORT).show();
				if (robotTaskId == 11) {
					BaseService.taskInterface.startReminder_Recognize();
				} else {
					// 人脸识别
					BaseService.recognizePersonInterface.StartRecognizePerson();
				}
			}
		}
	}
}