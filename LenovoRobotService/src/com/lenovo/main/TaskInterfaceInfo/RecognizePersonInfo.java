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
import com.google.gson.Gson;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.RecognizePersonInterface;
import com.lenovo.main.bin.PeopleListBin;
import com.lenovo.main.bin.PeopleListBin.PeopleList;
import com.lenovo.main.util.SpeechCompound;
import com.lenovo.main.util.StreamTools;

//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
//import android.util.Slog;
import android.widget.Toast;

/**
 * 人脸识别任务
 * 
 * @author Administrator
 * 
 */
public class RecognizePersonInfo implements RecognizePersonInterface {
	public static final String TAG = "LenovoRobotService";
	// private boolean isRecognize;
	private Context context;
	private SpeechCompound compound;
	private Gson gson;
	private Map<Integer, String> peopleMap = BaseService.peopleMap;

	// private Thread thread = null;
	// private MRecognizeThread recognizeThread = null;
	// 存放人的 ID 值
	private ArrayList<Integer> recognizeResultList = null;
	// 存放每一个人的 姓名
	private ArrayList<String> peopleNameList = null;

	private String result = "";

	private MDetectDistanceFeedbackThread detectDistanceFeedbackThread2;
	private MRecognizeThread recognizeThread;

	private String regex = ".*[a-zA-z]+.*";

	public RecognizePersonInfo(Context context, SpeechCompound compound,
			Gson gson) {
		this.gson = gson;
		this.context = context;
		this.compound = compound;

		if (recognizeResultList == null && peopleNameList == null) {
			recognizeResultList = new ArrayList<Integer>();
			peopleNameList = new ArrayList<String>();
		}

		// loadPersonMap();

		detectDistanceFeedbackThread2 = new MDetectDistanceFeedbackThread();
		detectDistanceFeedbackThread2.start();

		recognizeThread = new MRecognizeThread();
		recognizeThread.start();
	}

	boolean isResult = false;
	private int count = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// if (msg.what == 1) {
			// Toast.makeText(context, "人脸识别结果 : ---------------" + result,
			// Toast.LENGTH_SHORT).show();
			//
			// closeRecognizeThread();
			//
			// peopleNameList.clear();
			//
			// for (Integer i : recognizeResultList) {
			// if (i != 20) {
			// String string = peopleMap.get(i);
			// if (string != null) {
			// peopleNameList.add(string);
			// }
			// }
			// }
			// if (peopleNameList.size() > 0) {
			// compound.speaking(peopleNameList);
			// } else {
			// compound.speaking("尊贵的客人您好，欢迎光临，首次相见，请多关照", true, false);
			// }
			// }

			if (msg.what == 1) {
				// Slog.i(TAG, "RecognizeThread has result111111111!!!!!!!!");
				resultChuLi();

			} else if (msg.what == 2) {
				// Slog.i(TAG, "RecognizeThread has result22222222!!!!!!!!");
				int flag = (Integer) msg.obj;
				if (flag == 1) {
					// Slog.i(TAG, "RecognizeThread has result33333!!!!!!!!");
					compound.speaking("太近了", true, false);
				} else if (flag == 2) {

					// if (count++ > 3) {
					// count = 0;
					// // 2,开启线程,循环检测结果.
					// closeDetectDistanceFeedbackThread();
					// Slog.i(TAG, "RecognizeThread has result4444444!!!!!!!!");
					startRecognizeThread();
					// }

				} else if (flag == 3) {
					// Slog.i(TAG, "RecognizeThread has result555555!!!!!!!!");
					compound.speaking("太远了", true, false);
				}
			}
		}
	};
	private int noPeopleCount = 0;
	private String names = "";

	// private boolean isEnglish;

	private void resultChuLi() {
		if (!result.equals("err") && !result.equals("")) {

			Toast.makeText(context, "人脸识别结果 : ---------------" + result,
					Toast.LENGTH_SHORT).show();

			int length = result.length();
			recognizeResultList.clear();
			peopleNameList.clear();

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
						compound.speaking(names
								+ "Nice to meet you have a an nice day", false,
								false);
					} else {
						compound.speaking(names + "你们好我是达尔文,见到你们我很开心 ", true,
								false);
					}
				} else if (peopleNameList.size() == 1) {

					Matcher matcher = Pattern.compile(regex).matcher(names);
					if (matcher.matches()) {
						compound.speaking(names
								+ ",Nice to meet you have a an nice day",
								false, false);
					} else {
						compound.speaking(names + "你好我是达尔文,见到你我很开心", true,
								false);
					}
				}
			} else {
				if (peopleNameList.size() > 1) {
					Matcher matcher = Pattern.compile(regex).matcher(names);

					if (matcher.matches()) {
						compound.speaking(
								names
										+ "Nice to meet you have a an nice day People who don't know",
								false, false);
					} else {
						compound.speaking(names + "你们好我是达尔文,见到你们我很开心 中间有 "
								+ noPeopleCount + "个客人我不认识,更不知道名字叫什么", true,
								false);
					}
				} else if (peopleNameList.size() == 1) {
					Matcher matcher = Pattern.compile(regex).matcher(names);
					if (matcher.matches()) {
						compound.speaking(
								names
										+ "Nice to meet you have a an nice day People who don't know",
								false, false);
					} else {
						compound.speaking(names + "你好我是达尔文,见到你我很开心 中间有"
								+ noPeopleCount + "个人我不认识,更不知道名字叫什么", true,
								false);
					}
				} else {
					compound.speaking("人我都不认识,更不知道名字叫什么", true, false);
				}
			}

			noPeopleCount = 0;
			names = "";

		}
	}

	@Override
	public int StartRecognizePerson() {
		try {
			// 1,调用底层开始检测人脸
			// BaseService.LeDarwinService.StartRecognizePersonTask();
			startDetectDistanceFeedbackThread();
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	// private Thread threadDetect = null;
	// private MDetectDistanceFeedbackThread detectDistanceFeedbackThread =
	// null;

	private void startDetectDistanceFeedbackThread() {
		// isDetect = true;
		// if (threadDetect == null && detectDistanceFeedbackThread == null) {
		// detectDistanceFeedbackThread = new MDetectDistanceFeedbackThread();
		// threadDetect = new Thread(detectDistanceFeedbackThread);
		// threadDetect.start();
		// }
		synchronized (detectDistanceFeedbackThread2) {
			Toast.makeText(context, "测量距离被唤醒 人脸识别", Toast.LENGTH_SHORT).show();
			detectDistanceFeedbackThread2.notify();
		}
	}

	// private void closeDetectDistanceFeedbackThread() {
	// if (threadDetect != null && detectDistanceFeedbackThread != null) {
	// isDetect = false;
	// detectDistanceFeedbackThread = null;
	// threadDetect = null;
	// }
	// }

	@Override
	public int ExitRecognizePerson() {
		// TODO Auto-generated method stub
		try {
			// return BaseService.LeDarwinService.ExitRecognizePersonTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public String RecognizeResult() {
		try {
			// return BaseService.LeDarwinService.RecognizeResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public int getRobotTaskId() {
		try {
			// return BaseService.LeDarwinService.getRobotTask();
		} catch (Exception e) {
		}
		return 0;
	}

	class MDetectDistanceFeedbackThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					synchronized (RecognizePersonInfo.this.detectDistanceFeedbackThread2) {
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				int detectDistanceFeedback = getDetectDistanceFeedback();

				// Slog.i(TAG, "MDetectDistanceFeedbackThread  Ignore!!!!!!!!"
				// + detectDistanceFeedback);
				while (true) {
					SystemClock.sleep(50);
					// if (getRobotTaskId() == 2) {
					detectDistanceFeedback = getDetectDistanceFeedback();

					Message msg = new Message();
					msg.what = 2;
					msg.obj = detectDistanceFeedback;

					// Slog.i(TAG, "MDetectDistanceFeedbackThread !!!!!!!!"
					// + detectDistanceFeedback);
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
			}
			// }
		}
	}

	class MRecognizeThread extends Thread {
		@Override
		public void run() {

			while (true) {
				try {
					synchronized (RecognizePersonInfo.this.recognizeThread) {
						// Slog.i(TAG, "人脸识别线程正正在工作");
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				boolean isRecognize = false;
				getRobotTaskId();
				SystemClock.sleep(50);
				while (true) {
					int nRtn = getRobotTaskId();
					// Slog.i(TAG, "人脸识别线程正在工作  MRecognizeThread");
					if (getRobotTaskId() != 2 && isRecognize) {
						// Slog.i(TAG, "quit  MRecognizeThread !!!!!!!!");
						break;
					} else if (getRobotTaskId() == 2) {
						isRecognize = true;
						String recognizeResult = RecognizeResult();
						if (recognizeResult != null
								&& !recognizeResult.equals("0")) {

							RecognizePersonInfo.this.result = recognizeResult;
							// Slog.i(TAG, "人脸识别线程正在工作  MRecognizeThread"
							// + recognizeResult);

							handler.sendEmptyMessage(1);

							break;
						}
					}
					SystemClock.sleep(50);
				}
			}
		}
	}

	/**
	 * 开启识别线程
	 */
	private void startRecognizeThread() {
		// Slog.i(TAG, "RecognizeThread has resultAAAAAAAA!!!!!!!!");
		synchronized (recognizeThread) {
			// Slog.i(TAG, "RecognizeThread has resultBBBBBBBBBBBB!!!!!!!!");
			recognizeThread.notify();
		}
	}

	/**
	 * 关闭线程
	 * 
	 * private void closeRecognizeThread() { if (thread != null &&
	 * recognizeThread != null) { isRecognize = false; thread = null;
	 * recognizeThread = null;
	 * 
	 * // ExitRecognizePerson(); } }
	 */
	private int getDetectDistanceFeedback() {
		try {
			// return BaseService.LeDarwinService.getDetectDistanceFeedback();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
}
