package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.MIService.BaseService;
//import com.lenovo.main.MIService.ServiceHelpClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * homePager 表情变化的广播
 * 
 * @author Administrator
 */
public class FaceChangeBroadCastReceiver extends BroadcastReceiver {
	// private ServiceHelpClass serviceHelpClassInfo;

	@Override
	public void onReceive(Context context, Intent intent) {
		// serviceHelpClassInfo = ServiceHelpClass.getServiceHelpClassInfo();
		startGetThouchStat(intent);
	}

	private void startGetThouchStat(Intent intent) {
		if (BaseService.anotherInterface != null) {

			int faceFlag = intent.getIntExtra("state", -1);
			if (faceFlag != -1) {
				if (faceFlag == 1) {
					// serviceHelpClassInfo.getTouchSensorStauts();
					BaseService.anotherInterface.startGetTouchResult();
				} else if (faceFlag == 0) {
					// serviceHelpClassInfo.closeTouchThread();
					BaseService.anotherInterface.closeGetTouchResult();
				}
			}
		}
	}
}