package com.lenovo.main.TaskInterfaceInfo;

import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.BackToDockTaskInterface;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
import android.os.SystemClock;

//import android.util.Slog;

/**
 * 回充电站任务
 * 
 * @author Administrator
 * 
 */
public class BackToDockTaskInfo implements BackToDockTaskInterface {

	private boolean isStart;
	private Thread thread = null;
	private MBackToDockTaskThread backToDockTaskThread = null;

	@Override
	public int BackToDock() {
		// TODO Auto-generated method stub
		// if (thread == null && backToDockTaskThread == null) {
		// isStart = true;
		// backToDockTaskThread = new MBackToDockTaskThread();
		// thread = new Thread(backToDockTaskThread);
		// thread.start();
		// }
		try {
			//return BaseService.LeDarwinService.BackToDock();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getRobotTaskId() {
		try {
			//return BaseService.LeDarwinService.getRobotTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return 0;
	}

	class MBackToDockTaskThread implements Runnable {
		@Override
		public void run() {
			while (isStart) {
				SystemClock.sleep(50);
				int robotTaskId = getRobotTaskId();
				if (robotTaskId == 7) {
					try {
						//BaseService.LeDarwinService.BackToDock();
					} catch (Exception e) {
						// TODO: handle exception
					}

					closeThread();
				}
			}
		}
	}

	// 关闭线程
	private void closeThread() {
		if (thread != null && backToDockTaskThread != null) {
			isStart = false;

			thread = null;
			backToDockTaskThread = null;
		}
	}
}
