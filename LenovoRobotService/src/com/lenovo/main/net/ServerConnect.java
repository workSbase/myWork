package com.lenovo.main.net;

import android.content.Context;


import android.widget.Toast;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.ServerInterface.ServerPackgerChange;
import com.lenovo.main.util.PublicData;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.TransportStatus;

/**
 * 处理服务器发送过来的数据包
 */

public class ServerConnect extends BaseConnect implements ServerPackgerChange {

	public static String str;

	public ServerConnect(Context context) {
		super(context);
		setServerPackgerChangeInfo(this);
	}

	private int taskControlFlag = 0;

	@Override
	public void setServerResponsePackger(MoPacket pack) {
		/**
		 * 在这个地方就可以直接使用 pack 把服务器连接和回调的方法和对结果具体操作的方法进行隔离
		 * 不变的放在父类中完成,变换的以接口的方式回调出来进行处理,使变化的不会影响到不变的
		 */
		int source = pack.getInt32();
		int target = pack.getInt32();
		int framesNumber = pack.getInt32();
		if (framesNumber != 17 && framesNumber != 18) {
			taskControlFlag = 0;
		}
		if ((source == 11 && target == 1) || (source == 21 && target == 2)
				|| (source == 0 && target == 1) || (source == 0 && target == 2)) {
			switch (framesNumber) {
			case 1:
				break;
			case 2:
				String string = moPacket.getString();
				if (string != null) {
					if (string.equals("11")) {
						myToast.showToast(0, "中文好友在线");
					} else if (string.equals("21")) {
						myToast.showToast(0, "英文文好友在线");
					}
				} else {
					myToast.showToast(0, "当前没有好友在线");
				}
				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:
				if (taskControlFlag == 0) {
					BaseService.remoteControlInterface.StartRemoteCtrlTask();
				}
				taskControlFlag = -1;
				// 运动控制,前进后退
				int controlFlagNumber = moPacket.getInt32();

				BaseService.remoteControlInterface
						.setRemoteCtrlMove(controlFlagNumber);
				break;
			case 18:
				if (taskControlFlag == 0) {
					BaseService.remoteControlInterface.StartRemoteCtrlTask();
				}
				taskControlFlag = -1;
				// 头部运动控制
				int frames_18 = moPacket.getInt32();
				Toast.makeText(context, "头部控制命令收到了", Toast.LENGTH_SHORT).show();
				switch (frames_18) {
				case 0:
					BaseService.remoteControlInterface.setHeadLookUp();
					break;
				case 1:
					BaseService.remoteControlInterface.setHeadBow();
					break;
				case 2:
					BaseService.remoteControlInterface.setLifterUp();
					break;
				case 3:
					BaseService.remoteControlInterface.setLifterDown();
					break;
				}
				break;
			case 19:

				break;
			case 20:
				// 定点任务
				int poseFlag = pack.getInt32();
				Toast.makeText(context, "标志位 ++" + poseFlag, Toast.LENGTH_SHORT)
						.show();
				float licationX = (float) pack.getDouble();
				float licationY = (float) pack.getDouble();
				float licationYaw = (float) pack.getDouble();

				if (poseFlag == 1) {

					Toast.makeText(context, "标志位 ++" + poseFlag,
							Toast.LENGTH_SHORT).show();
				}
				BaseService.locationForProjectionInterface
						.StartLocationForProjection(licationX, licationY,
								licationYaw, 0, 0, true, false, poseFlag);
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:
				// 手机通知机器人通话类型和被呼叫人。int videoType , int personId
				int videoCurrentType = moPacket.getInt32();

				switch (videoCurrentType) {
				case 0:
					// 表示正常的视频通话,只有是正常的视频通话才是可以获取到正常的人的ID
					// int personId = moPacket.getInt32();
					((BaseService) context).startCamareActivity(1);
					break;
				case 1:
					// 表示视频监控
					((BaseService) context).startCamareActivity(2);
					break;
				case 2:
					break;
				}

				break;
			case 26:

				break;
			case 27:
				// 机器人端发送图片到手机端
				break;
			case 28:
				// 家电标号
				moPacket.getInt32();
				int i = moPacket.getInt32();
				if (i == 0) {
					// 关闭
				} else {
				}
				break;
			case 29:

				break;
			case 30:
				// 每一张幻灯片的 ID
				break;
			case 31:

				break;
			case 32:
				// ①当手机端上传任务的时候,需要开启下面的任务开始找人,告诉家人有人来吃饭
				moPacket.getInt32();

				String frames_32 = moPacket.getString();
				// if (frames_32.contains("客人")) {

				Toast.makeText(context, frames_32, Toast.LENGTH_SHORT).show();
				BaseService.answerPhoneInterface.startSpeech(
						"客人应该马上就到了我去门口迎接一下", true, false);

				BaseService.taskInterface.StartRecognizePerson();

				// } else {
				// BaseService.answerPhoneInterface.startSpeech("收到提醒任务",
				// true, false);
				// }
				break;
			case 33:
				// 实时测血压帧:服务器发送提醒机器人测血压0,1,33,string7
				myToast.showToast(0, "测量血压");
				break;
			case 34:

				break;
			case 35:
				BaseService.answerPhoneInterface.isWait(true);

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:
				// 获取机器人的电量
				int robotBatteryCap = BaseService.anotherInterface
						.getRobotBatteryCap();
				if (mt != null) {
					if (PublicData.getUserFlag(context) == 0) {
						sendEnergy(1, 11, 38, robotBatteryCap);
					} else if (PublicData.getUserFlag(context) == 1) {
						sendEnergy(1, 11, 38, robotBatteryCap);
					}
				}
				break;
			case 39:
				int frames_39 = moPacket.getInt32();
				switch (frames_39) {
				case 0:
					BaseService.danceTaskInterface.StartDance();
					break;
				case 1:
					BaseService.danceTaskInterface.ExitDance();
					break;
				case 2:
					BaseService.backToDockTaskInterface.BackToDock();
					break;
				}
				break;
			case 40:
				int createMapFlag = pack.getInt32();
				if (createMapFlag == 0) {
					Toast.makeText(context, "机器人开始建图", Toast.LENGTH_SHORT)
							.show();
					// serviceHelpClassInfo.startCreateMap();
				} else if (createMapFlag == 1) {
					Toast.makeText(context, "机器人关闭建图", Toast.LENGTH_SHORT)
							.show();
					// serviceHelpClassInfo.stopCreateMap();
				}
				break;
			case 42:
				/*
				 * int int32 = pack.getInt32();
				 * 
				 * str = pack.getString();
				 */

				// BaseService.taskInterface.StartReminderTask();

				// if (int32 == 1) {
				// synchronized (ServerConnect.this.myReminderTask) {
				// myReminderTask.notify();
				// }
				// }

				break;
			case 43:
				break;
			}
		}
	}

	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";
	public final static String ROOM_NUMBER = "1";

	private void sendEnergy(int sou, int de, int fram, int robotBatteryCap) {
		// TODO Auto-generated method stub
		if (mt.getStatus() == TransportStatus.CONNECTED) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(sou);
			moPacket.pushInt32(de);
			moPacket.pushInt32(fram);
			moPacket.pushInt32(robotBatteryCap);

			mt.sendPacket(moPacket);

			moPacket = null;
		}
	}

	/*
	 * class MyReminderTask extends Thread {
	 * 
	 * @Override public void run() { while (true) { synchronized
	 * (ServerConnect.this.myReminderTask) { try { wait(); } catch
	 * (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } try { sleep(20000);
	 * mHandler.sendEmptyMessage(1); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } SystemClock.sleep(50);
	 * } } }
	 */
}
