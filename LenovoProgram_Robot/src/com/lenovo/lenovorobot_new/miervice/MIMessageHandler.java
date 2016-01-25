package com.lenovo.lenovorobot_new.miervice;

import android.content.Context;
import android.os.Handler;
import android.os.ILenovoDarwin;
import android.os.Message;

import com.lenovo.lenovorobot_new.miinterfaceinfo.MIClass;

/**
 * 用来和底层交互的 handler,所有和底层相关的调用全部放在该类中完成
 * 
 * @author Administrator
 * 
 */
public class MIMessageHandler extends Handler {

	private MIClass miClass;

	public MIMessageHandler(Context context, ILenovoDarwin LeDarwinService) {
		miClass = new MIClass(LeDarwinService);

	}

	// ②通过handler的标示进行调用
	@Override
	public void handleMessage(Message msg) {
		int key = msg.what;
		int peopleId = (Integer) msg.obj;
		switch (key) {
		case 1:
			// 开启找人
			// anwerPhoneInterface.startFindingPersonTask(peopleId);
			break;
		case 2:
			// anwerPhoneInterface.exitFindingPersonTask();
			break;
		case 3:
			// 遥控
			break;
		case 4:
			// 定点
			break;
		case 5:
			// 测量身高
			break;
		case 6:
			// 识别人脸
			break;
		case 7:
			// 伺服
			break;
		case 8:
			// 跳舞
			break;
		case 9:
			// 结束跳舞
			break;
		}
	}
}
