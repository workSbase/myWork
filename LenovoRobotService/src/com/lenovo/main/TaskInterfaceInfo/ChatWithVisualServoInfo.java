package com.lenovo.main.TaskInterfaceInfo;

import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.ChatWithVisualServoInterface;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
//import android.util.Slog;

public class ChatWithVisualServoInfo implements ChatWithVisualServoInterface {

	public ChatWithVisualServoInfo() {
	}

	@Override
	public int StartChatWithVisualServoTask() {
		if (getRobotTaskId() != 10) {
			try {
				// BaseService.LeDarwinService.StartChatWithVisualServoTask();

				return 0;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}

	@Override
	public int ExitChatWithVisualServoTask() {
		try {
			// BaseService.LeDarwinService.ExitChatWithVisualServoTask();

			return 0;
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
