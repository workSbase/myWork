package com.lenovo.lenovorobot_new.BaseClass;

import com.lenovo.lenovorobot_new.activity.CameraActivity;
import com.lenovo.lenovorobot_new.activity.TesterApplication;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class BaseService extends Service {

	public Log_Toast log_Toast;
	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";
	public final static String ROOM_NUMBER = "1";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		log_Toast = new Log_Toast(this);
		initService();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initServiceDate();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public abstract void initServiceDate();

	public abstract void initService();

	/**
	 * 开启视频通话的activity,flag 是标记,用标记来显示不同风格的界面效果,是视频通话还是视频监控 抽取到父类中完成
	 * 
	 * @param flag
	 */
	public void startCamareActivity(int flag) {
		// 表示的控制
		((TesterApplication) this.getApplication()).setVideoState(flag);
		// 直接拨打电话
		((TesterApplication) this.getApplication())
				.setRtcEngine((((TesterApplication) this.getApplication()))
						.getVendorKey());

		Intent toChannel = new Intent(this, CameraActivity.class);
		toChannel.putExtra(BaseService.EXTRA_TYPE,
				BaseService.CALLING_TYPE_VIDEO);
		toChannel.putExtra(BaseService.EXTRA_CHANNEL, BaseService.ROOM_NUMBER);
		toChannel.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(toChannel);
	}
}
