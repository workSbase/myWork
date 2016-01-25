package com.lenovo.lenovorobot_new.miinterfaceinfo;

import android.content.Context;
import android.content.Intent;
import android.os.ILenovoDarwin;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;

import com.lenovo.lenovorobot_new.activity.FindingPeopleActivity;
import com.lenovo.lenovorobot_new.miinterface.AnwerPhoneInterface;
import com.lenovo.lenovorobot_new.serverservice.ServerService;
import com.lenovo.mi.plato.comm.MoPacket;

public class AnwerPhoneInterfaceInfo implements AnwerPhoneInterface {
	private Context context;
	private FindPersonThread findPersonThread;
	private ILenovoDarwin leDarwinService;
	// 找人结果的控制条件
	public boolean isFindingPeople = false;

	public AnwerPhoneInterfaceInfo(Context context,
			ILenovoDarwin leDarwinService) {
		this.context = context;
		this.leDarwinService = leDarwinService;
	}

	@Override
	public void startFindingPersonTask(int personId) {
		try {
			leDarwinService.StartAnswerPhone(personId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// 开启找人界面
		startFindingPeopleActivity();

		if (findPersonThread == null) {
			findPersonThread = new FindPersonThread();
		}
		synchronized (findPersonThread) {
			notify();
		}
	}

	private void startFindingPeopleActivity() {
		// 开启找人界面
		Intent intent = new Intent(context, FindingPeopleActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void finishFindingPeopleActivity() {
		// 关闭找人界面
		FindingPeopleActivity.handler.sendEmptyMessage(1);
	}

	@Override
	public void exitFindingPersonTask() {
		isFindingPeople = true;
		try {
			leDarwinService.ExitAnswerPhone();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getFindingPersonStatus() {
		try {
			return leDarwinService.getFindPeopleStatus();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	class FindPersonThread extends Thread {
		private Message msg;

		@Override
		public void run() {
			msg = new Message();
			msg.what = 1;
			while (true) {
				synchronized (findPersonThread) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (getRobotCourentTask() == 3) {
						if (getRobotCourentTask() != 3) {
							break;
						}
						if (isFindingPeople) {
							break;
						}
						switch (getFindingPersonStatus()) {
						case 1:
							// 表示正在找人
							break;
						case 2:
							// 告诉手机端已经找到人了
							exitFindingPersonTask();
							finishFindingPeopleActivity();
							break;
						case 5:
							// 告诉手机端找人出现错误了
							exitFindingPersonTask();
							finishFindingPeopleActivity();
							break;
						}
						sendPackage(msg);
						SystemClock.sleep(50);
					}
				}
			}
		}
	}

	private void sendPackage(Message msg) {
		// 封装成数据包
		MoPacket moPacket = new MoPacket();
		moPacket.pushInt32(1);
		moPacket.pushInt32(11);
		moPacket.pushInt32(26);
		moPacket.pushInt32(getFindingPersonStatus());
		ServerService.serverSendMessageHandler.sendMessage(msg);
		moPacket = null;
	}

	@Override
	public int getRobotCourentTask() {
		try {
			return leDarwinService.getRobotTask();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
