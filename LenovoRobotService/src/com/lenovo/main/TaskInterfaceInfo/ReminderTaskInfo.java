package com.lenovo.main.TaskInterfaceInfo;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;
import com.google.gson.Gson;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.ReminderTaskInterface;
import com.lenovo.main.bin.PeopleListBin;
import com.lenovo.main.bin.PeopleListBin.PeopleList;
import com.lenovo.main.net.ServerConnect;
import com.lenovo.main.util.SpeechCompound;
import com.lenovo.main.util.StreamTools;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
//import android.util.Slog;

public class ReminderTaskInfo implements ReminderTaskInterface {

	String TAG = "ReminderTaskInfo";

	private Context context;
	private SpeechCompound speechCompound;
	private Map<Integer, String> peopleMap = BaseService.peopleMap;
	private Gson gson;
	// 存放人的 ID 值
	private ArrayList<Integer> recognizeResultList = null;
	// 存放每一个人的 姓名
	private ArrayList<String> peopleNameList = null;

	/*
	 * 开始获取人的身高
	 */
	private boolean isStart;
	// public boolean isStartPeopleFace;

	private String result = "";

	private int noPeopleCount = 0;
	private String names = "";
	// 人脸识别的线程
	private MyPeopleFaceThread faceThread;
	// 找人结果的线程
	private GetPeopleThread getPeopleThread;

	public ReminderTaskInfo(Context context, SpeechCompound speechCompound,
			Gson gson) {
		this.context = context;
		this.speechCompound = speechCompound;
		this.gson = gson;
		if (recognizeResultList == null && peopleNameList == null) {
			recognizeResultList = new ArrayList<Integer>();
			peopleNameList = new ArrayList<String>();
		}
		// loadPersonMap();

		faceThread = new MyPeopleFaceThread();
		faceThread.start();

		getPeopleThread = new GetPeopleThread();
		getPeopleThread.start();

		// detectDistanceFeedbackThread = new MyDetectDistanceFeedbackThread();
		// detectDistanceFeedbackThread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {

				speechCompound.speaking(ServerConnect.str, true, false);

			} else if (msg.what == 2) {

				// closePeopleFcaeThread();
				recognizeResultList.clear();
				peopleNameList.clear();

				resultChuLi();

			} else if (msg.what == 3) {
				// closeHeightThread();
				speechCompound.speaking("今天怎么家里没有人啊,还有客人要来呢", true, false);

			} else if (msg.what == 4) {
				int flag = (Integer) msg.obj;
				if (flag == 1) {
					BaseService.compound.speaking("您离的太近了,远一点", true, false);
				} else if (flag == 2) {

					synchronized (getPeopleThread) {
						getPeopleThread.notify();
					}

				} else if (flag == 3) {
					BaseService.compound.speaking("您离的太远了,靠我近点", true, false);
				}
			}
		};
	};

	// private MyDetectDistanceFeedbackThread detectDistanceFeedbackThread;

	@Override
	public int StartReminderTask() {
		if (getRobotTaskId() != 11) {
			try {
			//	BaseService.LeDarwinService.StartReminderTaskInAdvance();

				startHeightThread();
				return 0;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}

	protected void resultChuLi() {
		int length = result.length();
		if (length == 1) {
			recognizeResultList.add(Integer.parseInt(result));
		} else {
			String[] split = result.split(",");
			for (String str : split) {
				int parseInt = Integer.parseInt(str);

				recognizeResultList.add(parseInt);

			}
		}
		if (recognizeResultList.size() > 0) {
			for (Integer i : recognizeResultList) {
				String string = peopleMap.get(i);
				if (string == null) {
					noPeopleCount++;
				} else {
					peopleNameList.add(string);
				}
			}
		}
		for (String name : peopleNameList) {
			names += name;
		}
		if (noPeopleCount == 0) {
			if (peopleNameList.size() > 1) {
				speechCompound.speaking(names + "你们好我是达尔文,见到你们我很开心 ", true,
						false);
			} else if (peopleNameList.size() == 1) {
				speechCompound.speaking(names + "你好我是达尔文,见到你我很开心", true, false);
			}
		} else {
			if (peopleNameList.size() > 1) {
				speechCompound.speaking(names + "你们好我是达尔文,见到你们我很开心 中间有 "
						+ noPeopleCount + "个人我不认识,更不知道名字叫什么", true, false);
			} else if (peopleNameList.size() == 1) {
				speechCompound.speaking(names + "你好我是达尔文,见到你我很开心 中间有"
						+ noPeopleCount + "个人我不认识,更不知道名字叫什么", true, false);
			} else {
				speechCompound.speaking("人我都不认识,更不知道名字叫什么", true, false);
			}
		}
		noPeopleCount = 0;
		names = "";

	}

	@Override
	public int ExitReminderTask() {
		try {
			//BaseService.LeDarwinService.ExitReminderTask();
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getFindPeopleStatus() {
		try {
			//return BaseService.LeDarwinService.getFindPeopleStatus();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int StartRecognizePerson() {
		// if (getRobotTaskId() == 11) {
		Toast.makeText(context, "当前机器人 任务 ID ..." + getRobotTaskId(),
				Toast.LENGTH_LONG).show();
		try {
			// BaseService.LeDarwinService.StartRecognizePerson_Reminder();
			Toast.makeText(context, "reminder 开始调用方法111111111",
					Toast.LENGTH_LONG).show();
			//BaseService.LeDarwinService.StartReminder();
			Toast.makeText(context, "reminder 方法已经调用过22222222222222",
					Toast.LENGTH_LONG).show();
			startPeopleFaceThread();
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		// }
		return 0;
	}

	/**
	 * 人脸识别
	 */
	private void startPeopleFaceThread() {
		// 激活正在wait()中的线程,这个是人脸识别
		// Slog.i(TAG, "StartRecognizePersonBBBBBB");
		// synchronized (detectDistanceFeedbackThread) {
		// Slog.i(TAG, "StartRecognizePersonCCCCC");
		// detectDistanceFeedbackThread.notify();
		// }
	}

	/**
	 * 检测身高
	 */
	private void startHeightThread() {
		// 激活正在wait() 中的线程,这个是获取是否找到人的线程
		synchronized (getPeopleThread) {
			getPeopleThread.notify();
		}

	}

	@Override
	public int ExitRecognizePerson() {
		try {
			//return BaseService.LeDarwinService.ExitRecognizePerson_Reminder();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public String RecognizeResult() {
		try {
			//return BaseService.LeDarwinService.RecognizeResult();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public int getRobotTaskId() {
		try {
			//return BaseService.LeDarwinService.getRobotTask();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 检测身高线程
	 * 
	 * @author rgj
	 * 
	 */
	class GetPeopleThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					synchronized (ReminderTaskInfo.this.getPeopleThread) {
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				boolean isReminderTask = false;
				int findPeopleStatus = getFindPeopleStatus();
				while (true) {

					SystemClock.sleep(50);
					if (getRobotTaskId() != 11) {
						break;
					} else {
						findPeopleStatus = getFindPeopleStatus();

						//Slog.i(TAG, "GetPeopleThread 1111111111.........."
								//+ findPeopleStatus);

						if (findPeopleStatus == 2) {
							//Slog.i(TAG,
									//"GetPeopleThread 22222222222 .................. "
										//	+ findPeopleStatus);
							handler.sendEmptyMessage(1);

							break;

						} else if (findPeopleStatus == 5) {
							//Slog.i(TAG,
									//"GetPeopleThread 22222222222 .................. "
										//	+ findPeopleStatus);
							handler.sendEmptyMessage(3);

							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 识别人脸线程
	 * 
	 * @author rgj
	 * 
	 */
	class MyPeopleFaceThread extends Thread {
		@Override
		public void run() {
			//Slog.i(TAG, "StartRecognizePersonEEEE");
			while (true) {
				try {
					//Slog.i(TAG, "StartRecognizePersonFFFFFF");
					synchronized (ReminderTaskInfo.this.faceThread) {
						//Slog.i(TAG, "StartRecognizePersonGGGGGGG");
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//Slog.i(TAG, "StartRecognizePersonHHHHHHH");
				while (true) {
				//	Slog.i(TAG, "StartRecognizePersonJJJJJJJJ");
					if (getRobotTaskId() != 11) {
						//Slog.i(TAG, "StartRecognizePersonKKKKKK");
						break;
					} else {
						//Slog.i(TAG, "StartRecognizePersonLLLLLLL");
						String recognizeResult = RecognizeResult();
						if (recognizeResult != null
								&& !recognizeResult.equals("0")) {
							//Slog.i(TAG, "StartRecognizePersonMMMMMM");
							int length = recognizeResult.length();

							ReminderTaskInfo.this.result = recognizeResult;

							handler.sendEmptyMessage(2);
							//Slog.i(TAG, "StartRecognizePersonNNNNNN");
							break;
						}
					}
					SystemClock.sleep(50);
				}
			}
		}
	}

	/**
	 * 距离检测的线程
	 * 
	 * @author rgj
	 * 
	 * 
	 *         class MyDetectDistanceFeedbackThread extends Thread {
	 * @Override public void run() { while (true) { try { synchronized
	 *           (ReminderTaskInfo.this.detectDistanceFeedbackThread) { wait();
	 *           } } catch (Exception e) { // TODO: handle exception } int
	 *           detectDistanceFeedback = getDetectDistanceFeedback(); while
	 *           (true) { // int nCount = 0; SystemClock.sleep(50);
	 *           detectDistanceFeedback = getDetectDistanceFeedback();
	 * 
	 *           Message msg = new Message(); msg.what = 4; msg.obj =
	 *           detectDistanceFeedback;
	 * 
	 *           if (detectDistanceFeedback != 2) {
	 * 
	 *           handler.sendMessage(msg);
	 * 
	 *           } else {
	 * 
	 *           handler.sendMessage(msg);
	 * 
	 *           break; }
	 * 
	 *           } } } }
	 */
	private int getDetectDistanceFeedback() {
		try {
			//return BaseService.LeDarwinService.getDetectDistanceFeedback();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public void startReminder_Recognize() {
		// TODO Auto-generated method stub
		try {
			//BaseService.LeDarwinService.StartRecognizePerson_Reminder();
			// startMy_Thread();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
