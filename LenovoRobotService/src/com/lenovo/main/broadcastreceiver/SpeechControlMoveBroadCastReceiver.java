package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.MIService.BaseService;
//import com.lenovo.main.MIService.ServiceHelpClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 语音控制机器人前进后退,左转右转
 * 
 * @author Administrator
 * 
 */
public class SpeechControlMoveBroadCastReceiver extends BroadcastReceiver {

	// private ServiceHelpClass serviceHelpClassInfo;

	@Override
	public void onReceive(Context context, Intent intent) {
		// serviceHelpClassInfo = ServiceHelpClass.getServiceHelpClassInfo();
		// 开始执行
		startExecute(context, intent);

	}

	private void startExecute(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String moveFlag = intent.getStringExtra("MOVE");
		String goFlag = intent.getStringExtra("GO");
		String openFlag = intent.getStringExtra("OPEN");
		String closeFlag = intent.getStringExtra("CLOSE");

		if (moveFlag != null) {
			int robotTaskId = BaseService.remoteControlInterface
					.getRobotTaskId();
			// int robotTaskId = serviceHelpClassInfo.getRobotTaskid();

			Toast.makeText(context, "当前机器人任务 ID : " + robotTaskId,
					Toast.LENGTH_SHORT).show();
			if (robotTaskId != 4) {
				// 开始控制任务.
				BaseService.remoteControlInterface.StartRemoteCtrlTask();
				speechMo(moveFlag);
			} else {
				speechMo(moveFlag);
			}

		} else if (goFlag != null) {
			String[] split = goFlag.split(",");

			float pointX = Float.parseFloat(split[0]);
			float pointY = Float.parseFloat(split[1]);

			// serviceHelpClassInfo.startLocationForProjection(pointX, pointY,
			// 0,
			// 0, 3);
			BaseService.locationForProjectionInterface
					.StartLocationForProjection(pointX, pointY, 0, 0, 0, false,
							false,0);
		} else if (openFlag != null) {
			// serviceHelpClassInfo.openLight(1);
		} else if (closeFlag != null) {
			// serviceHelpClassInfo.openLight(0);
		}
	}

	private void speechMo(String moveFlag) {
		if (moveFlag.equals("0")) {
			// 前进
			// serviceHelpClassInfo.setRemoteCtrlMove(0);
			BaseService.remoteControlInterface.setRemoteCtrlMove(0);
		} else if (moveFlag.equals("1")) {
			// 后退
			// serviceHelpClassInfo.setRemoteCtrlMove(1);
			BaseService.remoteControlInterface.setRemoteCtrlMove(1);
		} else if (moveFlag.equals("2")) {
			// 左转
			// serviceHelpClassInfo.setRemoteCtrlMove(2);
			BaseService.remoteControlInterface.setRemoteCtrlMove(2);
		} else if (moveFlag.equals("3")) {
			// 右转
			// serviceHelpClassInfo.setRemoteCtrlMove(3);
			BaseService.remoteControlInterface.setRemoteCtrlMove(3);
		} else if (moveFlag.equals("4")) {
			// 停止
		} else if (moveFlag.equals("5")) {
			// 开始跟人
			// serviceHelpClassInfo.exitRemoteCtrl();
			// serviceHelpClassInfo.startFollow();
			BaseService.followMeTaskInterface.StartFollowMeTask();
		} else if (moveFlag.equals("6")) {
			// 结束跟人
			// serviceHelpClassInfo.exitRemoteCtrl();
			// serviceHelpClassInfo.exitFollow();
			BaseService.followMeTaskInterface.ExitFollowMeTask();
		}
	}
}
