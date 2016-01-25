package com.lenovo.main.TaskInterfaceInfo;

import android.content.Context;
import android.os.SystemClock;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.AnotherInterface;
import com.lenovo.main.util.SendBroadCastTools;

import android.util.Log;
//import android.util.Slog;
import android.widget.Toast;
import android.os.Handler;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;

public class AnotherInfo implements AnotherInterface {

	public static final String TAG = "LenovoRobotService";
	public boolean isTouch;
	private Context context;

	private Thread thread = null;
	private MTouchSensorStautsThread touchSensorStautsThread = null;

	public AnotherInfo(Context context) {
		this.context = context;
	}

	private int countGo = 0;
	private int countB = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int flag = msg.what;
			switch (flag) {
			case 1:
				break;
			case 2:
				if (countGo++ > 3) {
					countGo = 0;
					BaseService.compound
							.speaking("挡着我的去路了,各位帮帮忙啦", true, false);
				}
				break;
			case 3:

				BaseService.compound.speaking("撞得我好疼呀,下次注意点", true, false);

				break;
			}
		};
	};

	@Override
	public int getRobotBatteryCap() {
		try {
			// return BaseService.LeDarwinService.getRobotBatteryCap();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getTouchSensorStauts() {
		// try {
		// if (BaseService.LeDarwinService != null) {
		//
		// return BaseService.LeDarwinService.getTouchSensorStauts();
		// } else {
		// Log.i("TOUCH", "绑定失败");
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		return 0;
	}

	public int getObstacleStatus() {
		try {
			// return BaseService.LeDarwinService.GetObstacleStatus();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int serLEDOn() {
		try {
			// return BaseService.LeDarwinService.setLEDOn();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int setLEDOff() {
		try {
			// return BaseService.LeDarwinService.setLEDOff();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int startGetTouchResult() {
		if (thread == null && touchSensorStautsThread == null) {
			isTouch = true;
			touchSensorStautsThread = new MTouchSensorStautsThread();
			thread = new Thread(touchSensorStautsThread);
			thread.start();
		}
		return 0;
	}

	@Override
	public void closeGetTouchResult() {
		// TODO Auto-generated method stub
		if (thread != null && touchSensorStautsThread != null) {
			isTouch = false;
			thread = null;
			touchSensorStautsThread = null;
		}
	}

	class MTouchSensorStautsThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isTouch) {
				SystemClock.sleep(1000);
				try {
					int touchSensorStauts = getTouchSensorStauts();

					if (touchSensorStauts == 1 || touchSensorStauts == 2
							|| touchSensorStauts == 3 || touchSensorStauts == 4) {
						if (context != null) {
							SendBroadCastTools.myBroadCast(context,
									"cn.com.lenovo.homepager", "face",
									touchSensorStauts, true);
						}
					}

					int obstacleStatus = getObstacleStatus();
					if (obstacleStatus == 1 || obstacleStatus == 2
							|| obstacleStatus == 3) {
						handler.sendEmptyMessage(obstacleStatus);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				// Slog.i(TAG, "获取 机器人的触摸值 的线程在工作 MTouchSensorStautsThread");
			}
		}
	}
}
