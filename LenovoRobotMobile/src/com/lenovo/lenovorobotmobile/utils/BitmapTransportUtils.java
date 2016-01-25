package com.lenovo.lenovorobotmobile.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import com.lenovo.lenovorobotmobile.MInterface.BitmapTransportInterface;
import com.lenovo.mi.plato.comm.ITransportCallback;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;
import com.lenovo.mi.plato.comm.TransportStatus;

public class BitmapTransportUtils implements ITransportCallback {

	private static final String TAG = "BitmapTransportUtils";
	private Context context;
	private MoTransport moTransport;
	private String ip;
	private int port;
	private BitmapTransportInterface transportInterface;
	private boolean ch_checkBox;
	private boolean en_checkBox;
	boolean isGetBitmap;
	private BitmapTransportThread bitmapTransportThread = null;
	private TransportThread transportThread = null;
	// 创建连接的标志位
	int flag = 0;
	private ServerUtils msgToServerHelp;
	// 线程池的使用
	private ExecutorService threadPool;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int i = msg.what;
			switch (i) {
			case 1:
				MToast.showToast(context, "9017 端口连接成功", 0);
				break;
			case 2:
				MToast.showToast(context, "9017 端口连接失败", 0);
				break;
			}
		};
	};

	public BitmapTransportUtils(Context context, String ip, int port,
			boolean ch_checkBox, boolean en_checkBox,
			ServerUtils msgToServerHelp) {
		this.msgToServerHelp = msgToServerHelp;
		this.ch_checkBox = ch_checkBox;
		this.en_checkBox = en_checkBox;
		this.context = context;
		this.ip = ip;
		this.port = port;
		moTransport = new MoTransport();
		// 创建一个线程池
		threadPool = Executors.newSingleThreadExecutor();
	}

	public void startTransport() {
		// isConnect = true;
		if (bitmapTransportThread == null) {

			BitmapTransportThread bitmapThread_1 = new BitmapTransportThread();
			BitmapTransportThread bitmapThread_2 = new BitmapTransportThread();

			threadPool.execute(bitmapThread_1);
			threadPool.execute(bitmapThread_2);

		}
	}

	private boolean transportIsConnect() {
		if (moTransport != null) {

			return moTransport.getStatus() == TransportStatus.CONNECTED;
		}
		return false;
	}

	public MoTransport getMoTransport() {
		if (moTransport != null) {
			return moTransport;
		}
		return null;
	}

	/**
	 * 关闭当前连接
	 */
	public void closeTransport() {
		if (moTransport != null) {
			moTransport.close();
			// isConnect = false;
			moTransport = null;
		}
		if (isGetBitmap) {
			isGetBitmap = false;
		}
		if (threadPool != null) {
			threadPool.shutdown();
			threadPool = null;
		}

		flag = 0;
	}

	public void startDrawBitmap() {
		if (transportThread == null) {
			isGetBitmap = true;
			Thread thread = new Thread(new TransportThread());
			thread.start();
		}
	}

	private void send_41() {
		if (ch_checkBox) {
			// 从服务器获取图片资源
			setData(11, 0, 41, 0);
		} else if (en_checkBox) {
			setData(21, 0, 41, 0);
		}
	}

	private void setData(int sourceID, int destinyID, int frameID, int flag) {
		if (transportIsConnect()) {

			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(sourceID);
			moPacket.pushInt32(destinyID);
			moPacket.pushInt32(frameID);
			moPacket.pushInt32(flag);

			moTransport.sendPacket(moPacket);

			moPacket = null;
		}
	}

	public void setBitmapTransportInterface(
			BitmapTransportInterface transportInterface) {
		this.transportInterface = transportInterface;
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

	@Override
	public void onPacket(MoTransport arg0, MoPacket arg1) {
		if (arg1 != null) {
			// TODO Auto-generated method stub
			arg1.getInt32();
			arg1.getInt32();
			if (arg1.getInt32() == 41) {
				byte[] byteArray = arg1.getByteArray();
				Log.i(TAG, "map size:" + byteArray.length);
				if (byteArray != null) {
					Bitmap map = BitmapFactory.decodeByteArray(byteArray, 0,
							byteArray.length);
					if (map != null && transportInterface != null) {
						transportInterface.setMopacket(map);
					}
				}
			}
		}
	}

	class TransportThread implements Runnable {
		@Override
		public void run() {
			while (isGetBitmap) {
				SystemClock.sleep(500);
				send_41();
			}
		}
	}

	public class BitmapTransportThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (flag == 0 && moTransport != null) {
				flag++;
				moTransport.setCallBack(BitmapTransportUtils.this);
				moTransport.connect(BitmapTransportUtils.this.ip,
						BitmapTransportUtils.this.port);
			}
			if (transportIsConnect()) {
				handler.sendEmptyMessage(1);
				while (transportIsConnect()) {
					moTransport.recvPacket();
					SystemClock.sleep(10);
				}
			} else if (flag != 0) {
				handler.sendEmptyMessage(2);
			}
		}
	}
}
