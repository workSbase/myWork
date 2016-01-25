package com.lenovo.lenovorobotmobile.net;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.lenovo.lenovorobotmobile.MInterface.ServerPacketInterfaceObserver;
import com.lenovo.lenovorobotmobile.MInterface.SubjectServerConnectInterface;
import com.lenovo.lenovorobotmobile.utils.MToast;
import com.lenovo.lenovorobotmobile.utils.Yuv2Rgb;
import com.lenovo.mi.plato.comm.ITransportCallback;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;
import com.lenovo.mi.plato.comm.TransportStatus;

/**
 * 
 * 用来连接服务器的类,设计成一个单例模式,里面也是用到了内容观察者模式
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class ServerConnect implements ITransportCallback,
		SubjectServerConnectInterface {

	public static ServerConnect SERVER = new ServerConnect();
	private MoTransport transport;
	private Context context;
	// 这个地方在使用的时候有可能 还没有 context 这二个对象,有可能会报错
	private String ip;
	private int sport;
	private Thread thread;
	private Timer timer;
	private TimerTask timerTask;

	// 存放内容观察者的集合
	private ArrayList<ServerPacketInterfaceObserver> interfaceObserverLists = new ArrayList<ServerPacketInterfaceObserver>();

	private ServerConnect() {
	}

	public static ServerConnect getConnectServer() {

		return SERVER;
	}
	private int number = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int flag = msg.what;
			switch (flag) {
			case 0:
				MToast.showToast(context, "9015 端口连接成功", 0);
				break;
			case 1:
				MToast.showToast(context, "9015 端口连接失败", 0);
				break;
			case 2:
				MoPacket packet = (MoPacket) msg.obj;
				// 说明服务器给返回数据来了
				if (packet != null) {
					// 这是第一帧的数据.
					int int32_1 = packet.getInt32();
					// 这是第二帧的数据.
					int int32_2 = packet.getInt32();
					// 第三帧的数据.
					int int32_3 = packet.getInt32();
					// 表示是,服务器给我们返回的数据
					if (int32_1 == 0) {
						// 某帧是否运行成功帧
						if (int32_2 == 11 && int32_3 == 5) {
							int int32 = packet.getInt32();
							int isSucceed = packet.getInt32();
							if (isSucceed == 0) {
								MToast.showToast(context, int32 + " 运行失败", 0);
							} else {
								notifyObserverResultInteger(int32, 0);
							}
						} else if (int32_2 == 11 && int32_3 == 2) {
							// 好友是否在线帧
							String friendIsOnline = packet.getString();
							notifyObserverServerResult(friendIsOnline, 0);
						} else if (int32_2 == 11 && int32_3 == 12) {
							String robotState = packet.getString();
							notifyObserverServerResult(robotState, 1);
						} else if (int32_2 == 11 && int32_3 == 13) {
							String string = packet.getString();
							notifyObserverServerResult(string, 2);
						} else if (int32_2 == 11 && int32_3 == 15) {

							String time = packet.getString();
							notifyObserverServerResult(time, 3);
						} else if (int32_2 == 11 && int32_3 == 16
								&& packet.getInt32() == 3245) {

						} else if (int32_2 == 11 && int32_3 == 41) {
							// 图片信息
							byte[] byteArray = packet.getByteArray();

							// MToast.showToast(context, "china 开始绘制地图"
							// + byteArray.toString(), 0);

							// Bitmap rawByteArray2RGBABitmap2 = Yuv2Rgb
							// .rawByteArray2RGBABitmap2(byteArray, 640,
							// 480);
							// MToast.showToast(context, "china 开始绘制地图", 0);

							notifyObserverPictureByte(BitmapFactory
									.decodeByteArray(byteArray, 0,
											byteArray.length));
							
							number++;
							notifyObserverResultInteger(number, 5);
						} else if (int32_2 == 21 && int32_3 == 5) {
							// 表示帧号
							int int32 = packet.getInt32();
							int isSucceed = packet.getInt32();
							if (isSucceed == 0) {
								MToast.showToast(context, int32 + " 运行失败", 0);
							} else {
								notifyObserverResultInteger(int32, 0);
							}
						} else if (int32_2 == 21 && int32_3 == 2) {
							// 好友是否在线帧
							String friendIsOnline = packet.getString();
							notifyObserverServerResult(friendIsOnline, 0);
						} else if (int32_2 == 21 && int32_3 == 12) {
							// 机器人的当前状态
							String robotState = packet.getString();
							notifyObserverServerResult(robotState, 1);
						} else if (int32_2 == 21 && int32_3 == 13) {
							String string = packet.getString();
							// 机器人当前的监控报告
							notifyObserverServerResult(string, 2);
						} else if (int32_2 == 21 && int32_3 == 15) {
							String time = packet.getString();
							notifyObserverServerResult(time, 3);
						} else if (int32_2 == 21 && int32_3 == 16
								&& packet.getInt32() == 3245) {
						} else if (int32_2 == 21 && int32_3 == 41) {
							byte[] byteArray = packet.getByteArray();
							Bitmap rawByteArray2RGBABitmap2 = Yuv2Rgb
									.rawByteArray2RGBABitmap2(byteArray, 640,
											480);
							MToast.showToast(context, "English 开始绘制地图", 0);
							notifyObserverPictureByte(rawByteArray2RGBABitmap2);
						}
					} else if (int32_1 == 1) {
						// 从手机端转发过来的消息
						if (int32_2 == 11 && int32_3 == 29) {
							MToast.showToast(context, "照片消息来了", 0);
							byte[] byteArray = packet.getByteArray();
							Bitmap rawByteArray2RGBABitmap2 = Yuv2Rgb
									.rawByteArray2RGBABitmap2(byteArray, 640,
											480);
							notifyObserverPictureByte(rawByteArray2RGBABitmap2);
						} else if (int32_2 == 11 && int32_3 == 26) {
							// 是否找到人
							int int32_4 = packet.getInt32();
							notifyObserverResultInteger(int32_4, 0);

						} else if (int32_2 == 11 && int32_3 == 23) {

							double x = packet.getDouble();
							double y = packet.getDouble();
							double yaw = packet.getDouble();

							String result = x + "," + y + "," + yaw;
							if (result != null) {
								notifyObserverServerResult(result, 4);
							}
						} else if (int32_2 == 11 && int32_3 == 38) {
							int robotEnergy = packet.getInt32();
							notifyObserverResultInteger(robotEnergy, 1);
						}
					} else if (int32_1 == 2) {
						// 从手机端转发过来的消息
						if (int32_2 == 21 && int32_3 == 29) {
							MToast.showToast(context, "照片消息来了", 0);
							byte[] byteArray = packet.getByteArray();
							Bitmap rawByteArray2RGBABitmap2 = Yuv2Rgb
									.rawByteArray2RGBABitmap2(byteArray, 640,
											480);
							notifyObserverPictureByte(rawByteArray2RGBABitmap2);
						} else if (int32_2 == 21 && int32_3 == 26) {
							// 是否找到人
							int int32_4 = packet.getInt32();
							notifyObserverResultInteger(int32_4, 0);
						} else if (int32_2 == 21 && int32_3 == 23) {

							double x = packet.getDouble();
							double y = packet.getDouble();
							double yaw = packet.getDouble();

							String result = x + "," + y + "," + yaw;
							if (result != null) {
								notifyObserverServerResult(result, 4);
							}
						} else if (int32_2 == 21 && int32_3 == 38) {
							int robotEnergy = packet.getInt32();
							notifyObserverResultInteger(robotEnergy, 1);
						}
					}
				}
				break;
			}
		};
	};

	// 开始连接服务器
	public void startConnect(final String ip, final int sport) {
		this.ip = ip;
		this.sport = sport;
		transport = new MoTransport();
		// 定时器
		timer = new Timer();
		timerTask = new TimerTask() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (thread != null) {
					if (!getIsConnect() && isMyConnectThread) {
						// Log.i(TAG, "Service_ CH 定时器开始工作了");
						thread.interrupt();
						// 判断线程是否被终止掉
						if (thread.currentThread().isInterrupted()) {
							// 把上一个线程 置为 null
							thread = null;
							thread = new Thread(new MyConnectThread());
							thread.start();
						}
					}
				}
			}
		};
		thread = new Thread(new MyConnectThread());
		if (!thread.isAlive()) {

			thread.start();

			timer.schedule(timerTask, 0, 1000);
		}
	}

	private boolean isMyConnectThread = true;

	/**
	 * 注销线程
	 */
	public void stopThread(boolean flag) {
		if (thread != null) {
			thread.interrupt();
			// thread.stop();
		}
		isMyConnectThread = flag;
	}

	class MyConnectThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 设置回调
			transport.setCallBack(ServerConnect.this);
			// 连接服务器,9015 端口
			transport.connect(ip, sport);
			if (transport.getStatus() == TransportStatus.CONNECTED) {
				handler.sendEmptyMessage(0);
				while (transport.getStatus() == TransportStatus.CONNECTED) {
					if (isMyConnectThread) {
						// Log.i(TAG, "连接服务器开始工作");
						transport.recvPacket();
					}
					SystemClock.sleep(10);
				}
			} else {
				handler.sendEmptyMessage(1);
			}
		}
	}

	/**
	 * 得到传输对象
	 * 
	 * @return
	 */
	public MoTransport getMoTransport() {

		return transport;
	}

	/**
	 * 传递进来一个 上下文对象
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 判断连接是否存在,是否连接成功
	 * 
	 * @return
	 */
	public boolean getIsConnect() {

		if (transport != null
				&& transport.getStatus() == TransportStatus.CONNECTED) {
			return true;
		}
		return false;
	}

	@Override
	public void onConnected(MoTransport arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDestroy(MoTransport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnecte(MoTransport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetError(MoTransport arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 主要用到的就是这个方法,是用来接收服务器发送回来的 消息包
	 */
	@Override
	public void onPacket(MoTransport arg0, MoPacket pack) {
		if (pack != null) {
			// 现在把收到的包,直接放到,handler 中去做具体的相应逻辑的处理
			Message msg = new Message();
			msg.obj = pack;
			msg.what = 2;
			handler.sendMessage(msg);
		}
	}

	@Override
	public void registerObserver(ServerPacketInterfaceObserver interfaceObserver) {
		// TODO Auto-generated method stub
		if (interfaceObserver != null) {
			interfaceObserverLists.add(interfaceObserver);
		}
	}

	@Override
	public void removeObserver(ServerPacketInterfaceObserver interfaceObserver) {
		// TODO Auto-generated method stub
		if (interfaceObserver != null) {
			int indexOf = interfaceObserverLists.indexOf(interfaceObserver);
			interfaceObserverLists.remove(indexOf);
		}
	}

	@Override
	public void notifyObserverServerResult(String resut, int flag) {
		// TODO Auto-generated method stub
		for (ServerPacketInterfaceObserver interfaceObserver : interfaceObserverLists) {
			interfaceObserver.setServerResult(resut, flag);
		}
	}

	@Override
	public void notifyObserverPictureByte(Bitmap bitmap) {
		// TODO Auto-generated method stub
		for (ServerPacketInterfaceObserver interfaceObserver : interfaceObserverLists) {
			interfaceObserver.setPictureByte(bitmap);
		}
	}

	@Override
	public void notifyObserverResultInteger(int result, int flag) {
		// TODO Auto-generated method stub
		for (ServerPacketInterfaceObserver interfaceObserver : interfaceObserverLists) {
			interfaceObserver.setResultInteger(result, flag);
		}
	}
}
