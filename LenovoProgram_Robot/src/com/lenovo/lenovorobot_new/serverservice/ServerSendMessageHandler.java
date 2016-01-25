package com.lenovo.lenovorobot_new.serverservice;

import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;

import android.os.Handler;
import android.os.Message;

/**
 * 用来处理发送服务器数据的,使用的时候,只需要将要发送的数据包传递过来就行了
 * 
 * @author Administrator
 * 
 */
public class ServerSendMessageHandler extends Handler {
	private MoPacket pack;
	private ServerSendThread serverSendThread;
	private final int SENDMSG = 1;

	public ServerSendMessageHandler(MoTransport moTransport) {
		serverSendThread = new ServerSendThread(moTransport);
	}

	@Override
	public void handleMessage(Message msg) {
		int key = msg.what;
		switch (key) {
		case SENDMSG:
			this.pack = (MoPacket) msg.obj;
			synchronized (serverSendThread) {
				notify();
			}
			break;
		}
	}

	class ServerSendThread extends Thread {
		private MoTransport moTransport;

		public ServerSendThread(MoTransport moTransport) {
			this.moTransport = moTransport;
		}

		@Override
		public void run() {
			while (true) {
				synchronized (ServerSendMessageHandler.this.serverSendThread) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (moTransport != null && pack != null) {

					moTransport.sendPacket(ServerSendMessageHandler.this.pack);

					pack = null;
				}
			}
		}
	}
}
