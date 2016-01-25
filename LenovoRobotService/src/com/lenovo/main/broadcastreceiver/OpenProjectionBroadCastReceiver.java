package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.MIService.BaseService;
//import com.lenovo.main.MIService.ServiceHelpClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 打开投影的广播,机器人端长安视频播放.发出的广播会被,该广播接收者接收到
 * 
 * @author Administrator
 * 
 */
public class OpenProjectionBroadCastReceiver extends BroadcastReceiver {

	// private ServiceHelpClass serviceHelpClassInfo;

	@Override
	public void onReceive(Context context, Intent intent) {
		// serviceHelpClassInfo = ServiceHelpClass.getServiceHelpClassInfo();
		startTasck();
	}

	private void startTasck() {
		// TODO Auto-generated method stub
		// serviceHelpClassInfo.startLocationForProjection(6.0f, 0.0f, -1.75f,
		// 0,
		// 2);
		// serviceHelpClassInfo.setisNavigationStatus(true);
		// serviceHelpClassInfo.getNavigationStatus();

		BaseService.locationForProjectionInterface.StartLocationForProjection(
				6.0f, 0.0f, -1.75f, 1, 0, false, true,0);
	}
}
