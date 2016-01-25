package com.lenovo.main.TaskInterfaceInfo;

import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.RemoteControlInterface;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
//import android.util.Slog;

/**
 * 遥控任务
 * 
 * @author Administrator
 * 
 */
public class RemoteControlInfo implements RemoteControlInterface {
	private static final String TAG = "LenovoRobotService";

	public RemoteControlInfo() {
	}

	@Override
	public int StartRemoteCtrlTask() {
		try {

			// return BaseService.LeDarwinService.StartRemoteCtrlTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int ExitRemoteCtrl() {
		try {
			// return BaseService.LeDarwinService.ExitRemoteCtrl();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setRemoteCtrlMove(int moveFlag) {
		try {
			// 1,判断机器人的ID值是不是当前的
			// Slog.i(TAG, "机器人当前任务 ID 值   : ......." + getRobotTaskId());
			if (getRobotTaskId() == 4) {
				// 2,把方向设置给底层
				// return
				// BaseService.LeDarwinService.setRemoteCtrlMove(moveFlag);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setLifterUp() {
		try {
			// return BaseService.LeDarwinService.setLifterUp();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setLifterDown() {
		try {
			// return BaseService.LeDarwinService.setLifterDown();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setHeadLookUp() {
		try {
			// return BaseService.LeDarwinService.setHeadLookUp();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setHeadBow() {
		try {
			// return BaseService.LeDarwinService.setHeadBow();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getRobotTaskId() {
		try {
			// return BaseService.LeDarwinService.getRobotTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
}
