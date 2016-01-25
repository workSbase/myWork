package cn.com.lenovo.speechservice;

import cn.com.lenovo.speechservice.service.SpeechService;
//import cn.com.lenovo.speechservice.service.SpeechService.SpeechServiceBinder;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 程序入口 绑定服务,现在的语音的代码就是最新的代码
 * @author kongqw
 *
 */
public class SpeechServiceActivity extends Activity {

	/*
	 * 调用服务方法的中间对象,主要就是用来停止录音和开启录音的
	 
	private static SpeechServiceBinder mSpeechServiceBinder;
	*/
	/*
	 * SharedPreferences读写对象
	 */
	private static SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//		// 判断网络状态
		//		ConnectivityManager netManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		//		// 获取网络状态信息
		//		NetworkInfo _networkInfo = netManager.getActiveNetworkInfo();
		//		// 判断网络状态
		//		if (_networkInfo == null || !_networkInfo.isAvailable() || !_networkInfo.isConnected()) {
		//			// 网络不可用
		//			Constant.NETWORK_STATE = false;
		//		} else {
		//			// 网络可用
		//			Constant.NETWORK_STATE = true;
		//		}
		//
		//		// 绑定语音服务
		Intent speechService = new Intent(this, SpeechService.class);
		//bindService(speechService, new MyServiceConnection(), Context.BIND_AUTO_CREATE);
		startService(speechService);

		finish();
	}

	//	@Override
	//	protected void onStart() {
	//		super.onStart();
	//		/*
	//		 * 使用意图 最小化界面
	//		 * 不能使用finish()方法
	//		 * 使用绑定服务，finish()方法会销毁绑定的服务,直接退回到系统界面
	//		 */
	//		Intent intent = new Intent();
	//		intent.setAction("android.intent.action.MAIN");
	//		intent.addCategory("android.intent.category.HOME");
	//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//		startActivity(intent);
	//	}

	/**
	 * 绑定服务的回调
	 * @author kongqw
	 *
	 
	public class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			mSpeechServiceBinder = (SpeechServiceBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	}
	*/
	/**
	 * 监听录音开启关闭的广播接收者
	 * @author kongqw
	 *
	 */
	public static class SpeechServiceSwitchReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//			// 初始化SharedPreferences对象
			//			mSharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
			//
			//			if (null == mSpeechServiceBinder || null == mSharedPreferences) {
			//				return;
			//			}
			//
			//			boolean isStartSpeech = intent.getBooleanExtra(Constant.START_SPEECH_FLAG, false);
			//			if (isStartSpeech) {
			//				Toast.makeText(context, "开启录音", Toast.LENGTH_SHORT).show();
			//				// 修改录音状态标志位
			//				mSharedPreferences.edit().putBoolean(Constant.SERVICE_STATE, true).commit();
			//				// 开启录音
			//				mSpeechServiceBinder.runStartSpeech();
			//			}
			//			boolean isStopSpeech = intent.getBooleanExtra(Constant.STOP_SPEECH_FLAG, false);
			//			if (isStopSpeech) {
			//				Toast.makeText(context, "关闭录音", Toast.LENGTH_SHORT).show();
			//				// 修改录音状态标志位
			//				mSharedPreferences.edit().putBoolean(Constant.SERVICE_STATE, false).commit();
			//				// 关闭录音
			//				mSpeechServiceBinder.runStopSpeech();
			//			}
			//
			//			// 解析方式切换成本地
			//			boolean isLocal = intent.getBooleanExtra("speechService_local", false);
			//			if (isLocal) {
			//				// 解析方式切换成本地
			//				Constant.NETWORK_STATE = false;
			//				Constant.EngineType = SpeechConstant.TYPE_LOCAL;
			//			}
			//
			//			// 解析方式切换成网络
			//			boolean isNet = intent.getBooleanExtra("speechService_net", false);
			//			if (isNet) {
			//				// 解析方式切换成网络
			//				Constant.NETWORK_STATE = true;
			//				Constant.EngineType = SpeechConstant.TYPE_CLOUD;
			//			}
		}
	}
}
