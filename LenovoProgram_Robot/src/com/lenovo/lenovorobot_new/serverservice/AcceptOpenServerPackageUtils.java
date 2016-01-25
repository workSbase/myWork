package com.lenovo.lenovorobot_new.serverservice;

import android.content.Context;
import android.os.Message;

import com.lenovo.lenovorobot_new.miervice.MIService;
import com.lenovo.lenovorobot_new.utils.Log_Toast;
import com.lenovo.mi.plato.comm.MoPacket;

/**
 * 用来处理服务器,发送过来的数据包
 * 
 * @author Administrator
 * 
 */
public class AcceptOpenServerPackageUtils {
	private static final String TAG = "AcceptOpenServerPackageUtils";
	private Log_Toast log_Toast;

	public AcceptOpenServerPackageUtils(Context context) {
		log_Toast = new Log_Toast(context);
	}

	/**
	 * 成功拿到服务器传递过来的数据包,这个是拆包函数
	 * 
	 * @param pack
	 */
	public void openPackage(MoPacket pack) {
		int source = pack.getInt32();
		int target = pack.getInt32();
		int framesNumber = pack.getInt32();
		log_Toast.i(TAG, "source " + source + " " + target + "  "
				+ framesNumber);
		if ((source == 11 && target == 1) || (source == 21 && target == 2)
				|| (source == 0 && target == 1) || (source == 0 && target == 2)) {
			switch (framesNumber) {
			case 1:
				break;
			case 2:
				String string = pack.getString();
				if (string != null) {
					if (string.equals("11")) {
						log_Toast.i(TAG, "中文好友在线");
					} else if (string.equals("21")) {
						log_Toast.i(TAG, "英文文好友在线");
					}
				} else {
					log_Toast.i(TAG, "当前没有好友在线");
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
				// 运动控制,前进后退
				break;
			case 18:
				// 头部控制
				break;
			case 19:
				// 手机端控制投影打开和关闭
				break;
			case 20:
				// 手机设定机器人导航目标点
				break;
			case 21:
				// 手机设定机器人导航起点。
				break;
			case 22:
				// 机器人上传自己的位置。
				break;
			case 23:

				break;
			case 24:

				break;
			case 25:
				// 手机通知机器人通话类型和被呼叫人 0/1/2，0/1/2
				// 判断当前的呼叫类型是视频通话还是视频监控
				int int32 = pack.getInt32();
				switch (int32) {
				case 0:
					// 表示视频监控
					break;
				case 1:
					// 视频通话
					int peopleId = pack.getInt32();
					Message msg = new Message();
					msg.what = 1;
					msg.obj = peopleId;
					MIService.MIMESSAGEHANDLER.sendMessage(msg);
					
					break;
				case 2:

					break;
				}
				break;
			case 26:

				break;
			case 27:
				// 手机通知机器人拍照。
				break;
			case 28:
				// 家电实时控制帧：手机文字控制界面输入，通过服务器转发到机器人执行
				break;
			case 29:

				break;
			case 30:
				// 手机发送图片id到机器人
				break;
			case 31:
				// 发送投影地点
				break;
			case 32:
				// 实时提醒帧:服务器发送提醒命令给机器人
				break;
			case 33:
				// 实时测血压帧:服务器发送提醒机器人测血压
				break;
			case 34:

				break;
			case 35:
				// 手机通知取消找人帧:手机发送通知机器人取消找人
				break;
			case 36:

				break;
			case 37:

				break;
			case 38:
				// 手机发送,机器人收到后返回电量
				break;
			case 39:
				// 手机发送,机器人收到后控制跳舞和回充电站 0/1/2
				break;
			case 40:
				break;
			case 42:
				break;
			case 43:
				break;
			}
		}
	}
}
