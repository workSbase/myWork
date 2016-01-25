package com.lenovo.main.TaskInterfaceInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class ReminderTaskInfo_new implements ReminderTaskInterface {

	private Context context;
	private SpeechCompound speechCompound;
	private Map<Integer, String> peopleMap = BaseService.peopleMap;
	private Gson gson;
	// 存放人的 ID 值
	private ArrayList<Integer> recognizeResultList = null;
	// 存放每一个人的 姓名
	private ArrayList<String> peopleNameList = null;

	private Thread thread_height = null;
	private GetPeopleThread getHeightThread = null;

	private Thread thread_face = null;
	private MyPeopleFaceThread faceThread = null;
	/*
	 * 开始获取人的身高
	 */
	private boolean isStart;
	public boolean isStartPeopleFace;

	private String result = "";

	private String regex = ".*[a-zA-z]+.*";

	private int noPeopleCount = 0;
	private String names = "";
	public boolean isStartDetectDistanceFeedback = true;
	private My_Thread my_Thread;
	private Thread myThread = null;
	private int count = 0;

	public ReminderTaskInfo_new(Context context, SpeechCompound speechCompound,
			Gson gson) {
		this.context = context;
		this.speechCompound = speechCompound;
		this.gson = gson;
		if (recognizeResultList == null && peopleNameList == null) {
			recognizeResultList = new ArrayList<Integer>();
			peopleNameList = new ArrayList<String>();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {

				closeHeightThread();
				if (ServerConnect.str != null) {

					speechCompound.speaking(ServerConnect.str + "提前把饭菜给准备好哦",
							true, false);

				} else {
					speechCompound.speaking("有客人要来吃饭提前准备好饭菜", true, false);
				}

			} else if (msg.what == 2) {

				closePeopleFcaeThread();
				Toast.makeText(context, "人脸识别线程已经关闭" + isStartPeopleFace,
						Toast.LENGTH_SHORT).show();

				recognizeResultList.clear();
				peopleNameList.clear();

				resultChuLi();

			} else if (msg.what == 3) {
				closeHeightThread();
				speechCompound.speaking("今天怎么家里没有人啊,还有客人要来呢", true, false);

			} else if (msg.what == 4) {

				int flag = (Integer) msg.obj;

				if (flag == 1) {
					BaseService.compound.speaking("太近了", true, false);
				} else if (flag == 2) {

					if (count++ >= 1) {
						count = 0;
						closeMy_Thread();
						startPeopleFaceThread();
					}
				} else if (flag == 3) {
					BaseService.compound.speaking("太远了", true, false);
				}
			} else if (msg.what == 5) {
				// Toast.makeText(context, "开启线程的标志 " + isStartPeopleFace,
				// Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public int StartReminderTask() {
		if (getRobotTaskId() != 11) {
			try {
				//BaseService.LeDarwinService.StartReminderTaskInAdvance();
				// startHeightThread();
				startHeightThread();

				return 0;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}

	private void startMy_Thread() {
		if (my_Thread == null && myThread == null) {
			isStartDetectDistanceFeedback = true;
			my_Thread = new My_Thread();
			myThread = new Thread(my_Thread);

			myThread.start();
		}
	}

	private void closeMy_Thread() {
		if (my_Thread != null && myThread != null) {
			isStartDetectDistanceFeedback = false;
			my_Thread = null;
			myThread = null;
		}
	}

	protected void resultChuLi() {

		Toast.makeText(context, "识别到的结果是   : " + result, Toast.LENGTH_SHORT)
				.show();

		if (!result.equals("err") && !result.equals("")) {

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

					Matcher matcher = Pattern.compile(regex).matcher(names);

					if (matcher.matches()) {
						BaseService.compound.speaking(names
								+ "Nice to meet you have  an nice day", false,
								false);
					} else {
						BaseService.compound.speaking(names + "你们好,欢迎光临 ",
								true, false);
					}
				} else if (peopleNameList.size() == 1) {

					Matcher matcher = Pattern.compile(regex).matcher(names);
					if (matcher.matches()) {
						BaseService.compound.speaking(names
								+ ",Nice to meet you have an nice day", false,
								false);
					} else {
						BaseService.compound.speaking(names + "你好,欢迎光临", true,
								false);
					}
				}
			} else {
				if (peopleNameList.size() > 1) {
					Matcher matcher = Pattern.compile(regex).matcher(names);

					if (matcher.matches()) {
						BaseService.compound
								.speaking(
										names
												+ "Nice to meet you have a an nice day People who don't know",
										false, false);
					} else {
						BaseService.compound.speaking(names + "你们好,欢迎光临有 "
								+ noPeopleCount + "个客人我不认识", true, false);
					}
				} else if (peopleNameList.size() == 1) {
					Matcher matcher = Pattern.compile(regex).matcher(names);
					if (matcher.matches()) {
						BaseService.compound
								.speaking(
										names
												+ "Nice to meet you have a an nice day People who don't know",
										false, false);
					} else {
						BaseService.compound.speaking(names + "你好,欢迎光临有"
								+ noPeopleCount + "个客人我不认识", true, false);
					}
				} else {
					BaseService.compound.speaking("尊贵的客人您好,初次见面请多关照", true,
							false);
				}
			}

		} else {

			Toast.makeText(context, "识别到的结果是   : " + result, Toast.LENGTH_SHORT)
					.show();
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
		if (getRobotTaskId() == 11) {
			try {
				// BaseService.LeDarwinService.StartRecognizePerson_Reminder();
				//BaseService.LeDarwinService.StartReminder();
				startMy_Thread();

				return 0;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}

	/**
	 * 人脸识别
	 */
	private void startPeopleFaceThread() {
		if (thread_face == null && faceThread == null) {
			isStartPeopleFace = true;
			faceThread = new MyPeopleFaceThread();
			thread_face = new Thread(faceThread);
			thread_face.start();
		}
	}

	/**
	 * 关闭人脸识别线程
	 */
	private void closePeopleFcaeThread() {
		if (thread_face == null && faceThread == null) {
			isStartPeopleFace = false;
			thread_face = null;

			faceThread = null;
		}
	}

	/**
	 * 检测身高
	 */
	private void startHeightThread() {
		// TODO Auto-generated method stub
		if (thread_height == null && getHeightThread == null) {
			isStart = true;
			getHeightThread = new GetPeopleThread();
			thread_height = new Thread(getHeightThread);
			thread_height.start();
		}
	}

	private void closeHeightThread() {
		if (thread_height != null && getHeightThread != null) {

			isStart = false;
			thread_height = null;
			getHeightThread = null;
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
		try {
			// Toast.makeText(context, "开始第二次人脸识别", Toast.LENGTH_SHORT).show();
			//BaseService.LeDarwinService.StartRecognizePerson_Reminder();
			startMy_Thread();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 检测身高线程
	 * 
	 * @author rgj
	 * 
	 */
	class GetPeopleThread implements Runnable {
		boolean isReminderTask = false;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isStart) {
				SystemClock.sleep(50);
				if (getRobotTaskId() == 11) {
					isReminderTask = true;
					int findPeopleStatus = getFindPeopleStatus();
					if (findPeopleStatus == 2) {
						isReminderTask = false;
						handler.sendEmptyMessage(1);
					} else if (findPeopleStatus == 5) {
						isReminderTask = false;
						handler.sendEmptyMessage(3);
					}
				} else {
					if (isReminderTask) {
						isStart = false;
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
	class MyPeopleFaceThread implements Runnable {

		

		@Override
		public void run() {
			boolean isPeopleFace = false;
			handler.sendEmptyMessage(5);

			while (isStartPeopleFace) {

				if (getRobotTaskId() == 11) {
					isPeopleFace = true;
					String recognizeResult = RecognizeResult();

					//Slog.i("DEMO4", "DEMO 1111111111111111111111 ......"
							//+ recognizeResult);

					if (recognizeResult != null && !recognizeResult.equals("0")) {
						int length = recognizeResult.length();

						ReminderTaskInfo_new.this.result = recognizeResult;

						//Slog.i("DEMO4", "DEMO 22222222222222222222222 ......"
							//	+ recognizeResult);

//						isPeopleFace = false;
						handler.sendEmptyMessage(2);
					}


				} else {
					if (isPeopleFace) {
						isStartPeopleFace = false;
					}
				}
				SystemClock.sleep(50);
			}
		}
	}

	class My_Thread implements Runnable {

		@Override
		public void run() {
			boolean isStart = false;

			int detectDistanceFeedback = getDetectDistanceFeedback();

			while (isStartDetectDistanceFeedback) {

				if (getRobotTaskId() == 11) {

					isStart = true;

					SystemClock.sleep(50);

					detectDistanceFeedback = getDetectDistanceFeedback();

					Message msg = new Message();
					msg.what = 4;
					msg.obj = detectDistanceFeedback;
					handler.sendMessage(msg);
				} else {
					if (isStart) {
						isStartDetectDistanceFeedback = false;
					}
				}
			}
		}
	}
}
