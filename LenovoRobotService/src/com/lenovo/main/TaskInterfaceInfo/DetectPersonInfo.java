package com.lenovo.main.TaskInterfaceInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.DetectPersonInterface;
import com.lenovo.main.util.PublicData;
import com.lenovo.main.util.SpeechCompound;

//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
import android.util.Log;
//import android.util.Slog;
import android.widget.Toast;

/**
 * 测量身高任务
 * 
 * @author Administrator
 * 
 */
public class DetectPersonInfo implements DetectPersonInterface {
	public static final String TAG = "LenovoRobotService";
	// boolean isHaveHeight;
	// private Thread thread = null;
	// private MDetectPersonHeight detectPersonHeightThread = null;
	private SpeechCompound compound;
	private Context context;
	private MDetectDistanceFeedback detectDistanceFeedback;
	private MDetectPersonHeight detectPersonHeight;

	public DetectPersonInfo(Context context, SpeechCompound compound) {
		this.context = context;
		this.compound = compound;

		detectDistanceFeedback = new MDetectDistanceFeedback();
		detectDistanceFeedback.start();

		detectPersonHeight = new MDetectPersonHeight();
		detectPersonHeight.start();
	}

	private int count = 0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			if (key == 1) {
				// closeDetectThread();

				float height = (Float) msg.obj;
				if (PublicData.getUserFlag(context) == 0) {
					Toast.makeText(context, "测量到身高了", Toast.LENGTH_SHORT)
							.show();
					if (height > 165.0f) {
						compound.speaking("你的身高是 " + height + "厘米,哇你好高啊", true,
								false);
					} else {
						compound.speaking("你的身高是 " + height + "厘米", true, false);
					}
				} else if (PublicData.getUserFlag(context) == 1) {
					if (height > 165.0f) {
						compound.speaking("you are " + height
								+ " centimeter tall you are very tall", false,
								false);
					} else {
						compound.speaking("you are " + height + " centimeter",
								false, false);
					}
				}
			} else if (key == 2) {
				int flag = (Integer) msg.obj;
				switch (flag) {
				case 1:
					compound.speaking("太近了", true, false);
					break;
				case 2:
					startDetectThread();
					break;
				case 3:
					compound.speaking("太远了", true, false);
					break;
				}
			}
		};
	};

	@Override
	public int StartDetectPersonInfo() {
		// try {
		// // 1,开始执行任务
		// if (BaseService.LeDarwinService != null) {
		//
		// BaseService.LeDarwinService.StartDetectPersonInfoTask();
		//
		// // 测量距离
		// startDetectDistanceFeedback();
		//
		// } else {
		// Log.i("DetectPersonInfo", "绑定失败");
		// }
		//
		// // return 0;
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		return 0;
	}

	private void startDetectThread() {
		// TODO Auto-generated method stub
		// if (thread == null && detectPersonHeightThread == null) {
		// isHaveHeight = true;
		// detectPersonHeightThread = new MDetectPersonHeight();
		// thread = new Thread(detectPersonHeightThread);
		// thread.start();
		// }
		synchronized (detectPersonHeight) {
			detectPersonHeight.notify();
		}
	}

	// private void closeDetectThread() {
	// if (thread != null && detectPersonHeightThread != null) {
	// isHaveHeight = false;
	// thread = null;
	// detectPersonHeightThread = null;
	//
	// // ExitDetectPersonInfo();
	// }
	// }

	// private Thread threadDetectDistanceFeedback = null;
	// private MDetectDistanceFeedback detectDistanceFeedback = null;

	private void startDetectDistanceFeedback() {
		// isStart = true;
		// if (threadDetectDistanceFeedback == null
		// && detectDistanceFeedback == null) {
		// detectDistanceFeedback = new MDetectDistanceFeedback();
		// threadDetectDistanceFeedback = new Thread(detectDistanceFeedback);
		// threadDetectDistanceFeedback.start();
		// }
		synchronized (detectDistanceFeedback) {
			detectDistanceFeedback.notify();
		}
	}

	// private void closeDetectDistanceFeedback() {
	// if (threadDetectDistanceFeedback != null
	// && detectDistanceFeedback != null) {
	// isStart = false;
	// threadDetectDistanceFeedback = null;
	// detectDistanceFeedback = null;
	// }
	// }

	@Override
	public int ExitDetectPersonInfo() {
		// TODO Auto-generated method stub
		try {
			// return BaseService.LeDarwinService.ExitDetectPersonInfoTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public float getPeopleheight() {
		try {
			// return BaseService.LeDarwinService.getPeopleheight();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0.0f;
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

	// private boolean isStart = true;

	class MDetectDistanceFeedback extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (true) {
				try {
					synchronized (this) {
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				int detectDistanceFeedback = getDetectDistanceFeedback();
				while (true) {
					SystemClock.sleep(50);

					detectDistanceFeedback = getDetectDistanceFeedback();

					Message msg = new Message();
					msg.what = 2;
					msg.obj = detectDistanceFeedback;

					if (detectDistanceFeedback != 2) {
						handler.sendMessage(msg);

					} else {
						if (count++ >= 1) {
							count = 0;
							handler.sendMessage(msg);
							break;
						}
					}
				}
				Log.i(TAG, "MDetectDistanceFeedback 333333");
			}

		}
	}

	class MDetectPersonHeight extends Thread {
		@Override
		public void run() {

			while (true) {
				Log.i(TAG, "MDetectPersonHeight 0000000");
				try {
					Log.i(TAG, "MDetectPersonHeight 11111111");
					synchronized (DetectPersonInfo.this.detectPersonHeight) {
						Log.i(TAG, "MDetectPersonHeight 22222222222222222");
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				boolean isDetectHeight = false;
				while (true) {
					SystemClock.sleep(50);
					// if (getRobotTaskId() == 1) {
					// detectHeight = true;
					// float peopleheight = getPeopleheight();
					// if (peopleheight > 0.0f) {
					// Message msg = new Message();
					// msg.obj = peopleheight;
					// msg.what = 1;
					// // 说明是有身高的.
					// handler.sendMessage(msg);
					// }
					// } else {
					// if (detectHeight) {
					// closeDetectThread();
					// }
					// }
					// Log.i(TAG, "MDetectPersonHeight AAAAAAA");

					if (getRobotTaskId() != 1 && isDetectHeight) {

						break;
					} else if (getRobotTaskId() == 1) {
						isDetectHeight = true;
						float peopleheight = getPeopleheight();
						if (peopleheight > 0.0f) {
							Message msg = new Message();
							msg.obj = peopleheight;
							msg.what = 1;
							// 说明是有身高的.
							handler.sendMessage(msg);

							Log.i(TAG, "MDetectPersonHeight CCCCCC");
							break;
						}
					}
				}
				Log.i(TAG, "MDetectPersonHeight 2222222");
			}
		}
	}

	private int getDetectDistanceFeedback() {
		try {
			// return BaseService.LeDarwinService.getDetectDistanceFeedback();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
}
