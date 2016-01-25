package com.lenovo.lenovorobot_new.miinterfaceinfo;

import android.content.Context;
import android.os.ILenovoDarwin;
import android.os.RemoteException;
import android.os.SystemClock;

import com.lenovo.lenovorobot_new.miinterface.RecognizeInterface;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

/**
 * 人脸识别用到的主要的类
 * 
 * @author Administrator
 * 
 */
public class PersonFaceRecognizeInfo implements RecognizeInterface {

	public static final String TAG = null;
	private Context context;
	private ILenovoDarwin leDarwinService;
	private String result = "";

	public PersonFaceRecognizeInfo(Context context,
			ILenovoDarwin leDarwinService) {
		log_Toast = new Log_Toast(context);
		this.context = context;
		this.leDarwinService = leDarwinService;
	}

	@Override
	public void mstartRecognize() {
		try {
			leDarwinService.StartRecognizePerson();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getRecognzieResult() {
		try {
			return leDarwinService.RecognizeResult();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void mExitRecognizePersonTask() {
		// TODO Auto-generated method stub
		try {
			leDarwinService.ExitRecognizePerson();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getRobotTaskId() {
		try {
			return leDarwinService.getRobotTask();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// 计数器,校验人站的位置是否是正确的,校验三次
	private int count = 0;
	private Log_Toast log_Toast;

	class PersonFaceRecognizeThread extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized (context) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 远近距离的检测
				while (getRobotTaskId() == 2) {
					if (getRobotTaskId() != 2) {
						break;
					}
					int detecDistanceFeedBack = getDetecDistanceFeedBack();
					if (detecDistanceFeedBack == 2) {
						log_Toast.i(TAG, "距离刚刚好了");
						count += 1;
						if (count >= 3) {
							count = 0;
							break;
						}
					} else if (detecDistanceFeedBack == 1) {
						log_Toast.i(TAG, "太近了");
					} else if (detecDistanceFeedBack == 3) {
						log_Toast.i(TAG, "太远了");
					}
					SystemClock.sleep(50);
				}
				// 获取识别结果
				while (getRobotTaskId() == 2) {
					SystemClock.sleep(50);

					if (getRobotTaskId() != 2) {
						break;
					}
					// 把操作字符串的部分都放在线程中完成这样的话可以提高效率,只要把最后的结果给到主线程就行,减轻主线程的负担
					String recognzieResult = getRecognzieResult();
					if (recognzieResult != null && !recognzieResult.equals("0")) {
						// 得到的是人的ID值,把字符串进行切割
						String[] split = recognzieResult.split(",");
						for (String stID : split) {
							// 用这个人的id到人的集合中去匹配,如果有就把这个人给拿出来,拼接在result后面
							int id = Integer.parseInt(stID);
						}
					}
				}
			}
		}

	}

	@Override
	public int getDetecDistanceFeedBack() {
		try {
			return leDarwinService.getDetectDistanceFeedback();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}
