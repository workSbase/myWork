package com.lenovo.main.MIService;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lenovo.main.activityVideo.TesterApplication;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.TransportStatus;

/**
 * 现在该类中主要完成的就是,用户的登录,和绑定底层的 MIService
 */
public class MyService extends BaseService {

	public int key;
	public static Handler myServiceHandler;

	@Override
	public void initData() {
		/**
		 * 这个handler是给用户切换的时候使用的,还有就是YY演示的一些广播也是在这里面处理 ,这是一个 静态的
		 * handler,目的就是为了别人能够拿到,该handler发消息过来,为什么要在service中使用这样一种机制呢
		 * 是因为这里面涉及到有约登录的操作,如果是放在广播中来完成的话,如果该广播的生命周期都结束了,用户还是没有登录成功
		 * 那么这个时候,就会抱错,广播中不能做耗时操作,所以要放在service中来完成,使用静态的目的是为了能够直接调用
		 */
		myServiceHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int key = msg.what;
				if (key == -1) {
					// Bundle bundle = msg.getData();
					// String name = bundle.getString("userName");
					// String passWold = bundle.getString("passWorld");
					// int userNumber = bundle.getInt("userFlag_numebr");
					// if (getBoolean(name, passWold, userNumber)) {
					// loginServer(userNumber, name, passWold);
					// }
				} else {
					// 把消息发送给 handler的帮助类来完成消息的处理
					BaseService.handlerMassageUtils.setMassage(key);
				}
			}
		};
		loginServer();
		initAgoraVideo();
	}

	// 初始化 视频对象,设置账号和用户名
	private void initAgoraVideo() {
		((TesterApplication) getApplication()).setUserInformation(
				"6d10928c053d480988df9e8ee811671f", "zxc");
	}

	/**
	 * 发送心跳帧给服务器告诉服务器我现在是在线的
	 * 
	 * @param i
	 * @param userName
	 * @param passWorld
	 */
	public void loginServer() {
		if (myMt != null && myMt.getStatus() == TransportStatus.CONNECTED) {
			MoPacket pack = new MoPacket();
			pack.pushInt32(1);
			pack.pushInt32(0);
			pack.pushInt32(7);
			pack.pushInt32(0);
			myMt.sendPacket(pack);
			pack = null;
		} else {
			Toast.makeText(this, "心跳帧发送失败", Toast.LENGTH_SHORT).show();
		}
	}
}