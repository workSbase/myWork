package com.lenovo.main.TaskInterfaceInfo;

import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.PeopleFace;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;

public class PeopleFaceInfo implements PeopleFace {

	@Override
	public void MStartMoveRecognizeTask(int nID) {
		// TODO Auto-generated method stub
		try {
			//BaseService.LeDarwinService.StartMoveRecognizeTask(nID);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void MExitMoveRecognzieTask() {
		// TODO Auto-generated method stub
		try {
			//BaseService.LeDarwinService.ExitMoveRecognzieTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
