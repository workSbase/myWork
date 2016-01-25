package com.lenovo.lenovorobotmobile.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.MInterface.BitmapTransportInterface;
import com.lenovo.lenovorobotmobile.MInterface.ControlMoveInterface;
import com.lenovo.lenovorobotmobile.utils.BitmapTransportUtils;
import com.lenovo.lenovorobotmobile.view.ControlMoveView;
import com.lenovo.lenovorobotmobile.view.ControlMoveView.MoveControlLister;
import com.lenovo.lenovorobotmobile.view.CreateUpdataMapView;

public class CreateMap extends BaseActivity implements MoveControlLister,
		ControlMoveInterface, BitmapTransportInterface {

	private ControlMoveView mControlMoveView;
	private int moveFlag;
	private static final String TAG = "CreateMap";

	private boolean isSendControlFlag = true;
	private ControlMoveFlagThread controlMoveFlagThread;
	private CreateUpdataMapView mCreateUpdataMapView;

	private BitmapTransportUtils bitmapTransportUtils;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int i = msg.what;
			switch (i) {
			case 1:
				Bitmap map = (Bitmap) msg.obj;
				if (map != null) {
					// 把图片设置给显示的View
					mCreateUpdataMapView.setBitmap(map);
				}
				break;
			}
		};
	};

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.createmapview, null);
		setContentView(view);

		mControlMoveView = (ControlMoveView) findViewById(R.id.createMapGather);

		mControlMoveView.setControlBitmapLister(this);
		mControlMoveView.setControlMoveInterface(this);

		// 显示创建的地图
		mCreateUpdataMapView = (CreateUpdataMapView) findViewById(R.id.mCreateUpdataMapView);
		mCreateUpdataMapView.setBackgroundColor(Color.rgb(205, 205, 205));
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (ch_checkBox) {
			msgToServerHelp.sendMsgToServer("11", "1", "40", "0");
		} else if (en_checkBox) {
			msgToServerHelp.sendMsgToServer("21", "2", "40", "0");
		}

		bitmapTransportUtils = new BitmapTransportUtils(this, "192.168.1.6",
				9017, ch_checkBox, en_checkBox, msgToServerHelp);

		bitmapTransportUtils.startTransport();
		bitmapTransportUtils.startDrawBitmap();

		bitmapTransportUtils.setBitmapTransportInterface(this);
	}

	@Override
	public void setdownFlag(int moveFlag) {
		// TODO Auto-generated method stub
		this.moveFlag = moveFlag;
	}

	@Override
	public void startControlMoveFlagThread() {
		// TODO Auto-generated method stub
		if (controlMoveFlagThread == null) {
			controlMoveFlagThread = new ControlMoveFlagThread();
			Thread thread = new Thread(controlMoveFlagThread);
			thread.start();
		}
		if (!isSendControlFlag) {
			isSendControlFlag = true;
		}
	}

	@Override
	public void stopControlMoveFlagThread() {
		// TODO Auto-generated method stub
		try {
			isSendControlFlag = false;
			controlMoveFlagThread = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setFlag(int ortaion) {
		Log.e("downFlag", " downFlag " + ortaion);
		if (ch_checkBox) {
			msgToServerHelp.sendMsgToServer("11", "1", "17", ortaion + "");
		} else if (en_checkBox) {
			msgToServerHelp.sendMsgToServer("21", "2", "17", ortaion + "");
		}
	}

	class ControlMoveFlagThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isSendControlFlag) {
				if (moveFlag >= 0) {
					setFlag(moveFlag);
					Log.i(TAG, "发送线程一直在工作" + moveFlag);
					moveFlag = -1;
				}
				SystemClock.sleep(100);
			}
		}
	}

	@Override
	public void setPictureByte(Bitmap bitmap) {

	}

	@Override
	public void onBackPressed() {
		// CallControl.endCall();
		bitmapTransportUtils.closeTransport();

		if (ch_checkBox) {
			msgToServerHelp.sendMsgToServer("11", "1", "40", "1");
		} else if (en_checkBox) {
			msgToServerHelp.sendMsgToServer("21", "2", "40", "1");
		}

		finish();
	}

	@Override
	public void setMopacket(Bitmap map) {
		Message msg = new Message();
		msg.obj = map;
		msg.what = 1;
		// TODO Auto-generated method stub
		handler.sendMessage(msg);
	}
}
