package com.lenovo.main.TaskInterfaceInfo;

import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.AnswerPhoneInterface;
import com.lenovo.main.activity.FindingPeopleActivity;
import com.lenovo.main.util.CloseSpeechUtils;
import com.lenovo.main.util.PublicData;
import com.lenovo.main.util.SendBroadCastTools;
import com.lenovo.main.util.SpeechCompound;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;
import com.lenovo.mi.plato.comm.TransportStatus;
import android.os.SystemClock;

//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
import android.util.Log;

//import android.util.Slog;

/**
 * 电话任务接口的实现类
 * 
 * @author Administrator
 * 
 */

public class AnswerPhoneInfo implements AnswerPhoneInterface,
		OnCompletionListener {
	public static final String TAG = "LenovoRobotService";
	/**
	 * 上下文环境
	 */
	private Context context;
	/**
	 * 语音合成类
	 */
	private SpeechCompound compound;
	/*
	 * 是否开始找人
	 */
	// private boolean isFindingPeople;
	private MoTransport myMt;

	// private Thread thread = null;
	// private FindingPeopleThread findingPeopleThread = null;

	private MediaPlayer mediaPlayer = null;
	public static int personId_;

	// private Thread thread_music = null;
	// private MyMusicThread musicThread = null;

	// 让其线程等待的判断条件
	public boolean isWait = false;

	int count = 0;
	private FindingPeopleThread findingPeopleThread;

	public AnswerPhoneInfo(Context context, SpeechCompound compound,
			MoTransport myMt) {
		this.myMt = myMt;
		this.context = context;
		this.compound = compound;

		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, R.raw.aesthete);

			mediaPlayer.setOnCompletionListener(this);
		}

		findingPeopleThread = new FindingPeopleThread();
		findingPeopleThread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int msgWhat = msg.what;
			// 获取当前机器人的 任务 ID 值
			switch (msgWhat) {
			case 1:
				// 1,表示正在找人
				if (PublicData.getUserFlag(context) == 0) {
					drawinSendInstructToPhone(1, 11, msgWhat);
				} else {
					drawinSendInstructToPhone(2, 21, msgWhat);
				}

				break;
			case 2:

				// 已经找到人了,关闭找人界面
				finishFindPeopleActivity();

				// 停止播报找人的声音
				stopSpeaking();
				// 关闭录音的广播
				CloseSpeechUtils.setCloseSpeechFlag(context, 1);

				if (PublicData.getUserFlag(context) == 0) {
					drawinSendInstructToPhone(1, 11, msgWhat);
				} else if (PublicData.getUserFlag(context) == 1) {
					startSpeech("Finally found you hurry answered the phon",
							false, true);
					drawinSendInstructToPhone(2, 21, msgWhat);
				}
				break;
			case 10:
				// 音乐铃声播放完成,播放有人打电话找您
				if (PublicData.getUserFlag(context) == 0) {
					BaseService.answerPhoneInterface.startSpeech(
							BaseService.peopleMap.get(personId_) + "有您的电话请接听",
							true, true);
				} else if (PublicData.getUserFlag(context) == 0) {
					BaseService.answerPhoneInterface.startSpeech(
							"hava you a call", false, false);
				}
				break;
			case 11:
				// 循环播放音乐 4 次
				startMusic();
				break;
			case 5:

				BaseService.answerPhoneInterface.startSpeech("没有找到人,请帮忙接听一下好吗",
						true, false);
				break;
			default:
				if (getRobotTaskId() == -1) {
					finishFindPeopleActivity();

					if (PublicData.getUserFlag(context) == 0) {

						drawinSendInstructToPhone(1, 11, msgWhat);

					} else {

						drawinSendInstructToPhone(2, 21, msgWhat);
					}
					stopSpeaking();
				}
				break;
			}
		}
	};

	/**
	 * 人的ID 一个是想说的内容
	 */
	@Override
	public int StartVideoCallTask(int personId) {
		personId_ = personId;
		try {
			// 开启找人任务
			// BaseService.LeDarwinService.StartVideoCallTask(personId);
			// 调用开始找人
			startFindingPeople(personId);
			/**
			 * 开启铃声
			 */
			startMusic();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 开始播放音乐
	 */
	private void startMusic() {
		// TODO Auto-generated method stub
		try {
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭音乐线程
	 * 
	 */
	class MyMusicThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mediaPlayer.start();
		}
	}

	private void startFindingPeople(int personId) {
		try {
			startFindingActivity();

			synchronized (AnswerPhoneInfo.this.findingPeopleThread) {
				findingPeopleThread.notify();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public int ExitVideoCallTask() {
		try {
			// return BaseService.LeDarwinService.ExitVideoCallTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int PhoneConnected() {
		try {
			// return BaseService.LeDarwinService.PhoneConnected();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int PhoneDisconnected() {
		try {
			// return BaseService.LeDarwinService.PhoneDisconnected();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int getFindPeopleStatus() {
		try {
			// return BaseService.LeDarwinService.getFindPeopleStatus();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	/*
	 * 该方法在外部要被使用,所以定义在接口
	 * 
	 * @Override public void closeFindingThread() { // TODO Auto-generated
	 * method stub if (thread != null && findingPeopleThread != null) {
	 * isFindingPeople = false; thread = null; findingPeopleThread = null;
	 * 
	 * // finishFindPeopleActivity(); } }
	 */

	/**
	 * 把找人的结果发送给手机端
	 * 
	 * @param SourceID
	 * @param DestinyID
	 * @param instructFlag
	 */
	@Override
	public void drawinSendInstructToPhone(int SourceID, int DestinyID,
			int instructFlag) {
		if (myMt != null && myMt.getStatus() == TransportStatus.CONNECTED) {
			MoPacket pack = new MoPacket();
			pack.pushInt32(SourceID);
			pack.pushInt32(DestinyID);
			pack.pushInt32(26);
			pack.pushInt32(instructFlag);
			myMt.sendPacket(pack);
			pack = null;
		}
	}

	class FindingPeopleThread extends Thread {
		@Override
		public void run() {

			// TODO Auto-generated method stub
			while (true) {
				try {
					synchronized (AnswerPhoneInfo.this.findingPeopleThread) {
						wait();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				boolean isFindingPeopleTask = false;

				int findPeopleStatus = getFindPeopleStatus();

				while (true) {
					SystemClock.sleep(50);

					if (getRobotTaskId() != 3 && isFindingPeopleTask) {
						break;
					} else if (getRobotTaskId() == 3) {
						if (isWait) {
							break;
						} else {
							isFindingPeopleTask = true;

							findPeopleStatus = getFindPeopleStatus();

							if (findPeopleStatus == 2 || findPeopleStatus == 5) {

								handler.sendEmptyMessage(findPeopleStatus);

								break;
							}
						}
					}
				}
			}
		}
	}

	private int getRobotTaskId() {
		try {
			// return BaseService.LeDarwinService.getRobotTask();
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 开启找人界面
	 */
	private void startFindingActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, FindingPeopleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 关闭找人界面
	 */
	protected void finishFindPeopleActivity() {
		if (context != null) {
			SendBroadCastTools.myBroadCast(context,
					"finishspeachpopleactivity", null, null, false);
		}
	}

	/**
	 * 停止播报
	 */
	@Override
	public void stopSpeaking() {
		compound.stopSpeaking();
	}

	// @Override
	// public void setIsFindingPeople(boolean flag) {
	// // TODO Auto-generated method stub
	// // this.isFindingPeople = flag;
	// }

	@Override
	public void startSpeech(String content, boolean internationalFlag,
			boolean whileFlag) {

		compound.speaking(content, internationalFlag, whileFlag);
	}

	/**
	 * 音乐播放完成的回调
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		count++;
		if (count < 2) {
			handler.sendEmptyMessage(11);
		} else {
			count = 0;
			handler.sendEmptyMessage(10);
		}
	}

	@Override
	public void isWait(boolean isWait) {
		this.isWait = isWait;
	}
}
