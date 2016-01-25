package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.MIService.BaseService;
//import com.lenovo.main.MIService.ServiceHelpClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 在视频播放结束的时候,把投影关闭和关闭任务
 * 
 * @author Administrator
 */
public class CloseProjectBroadCastReceiver extends BroadcastReceiver {

	// private ServiceHelpClass serviceHelpClassInfo;

	@Override
	public void onReceive(Context context, Intent intent) {
		// serviceHelpClassInfo = ServiceHelpClass.getServiceHelpClassInfo();
		// serviceHelpClassInfo.closeProjector();
		BaseService.projectorInterface.CloseProjector();
	}
}
