package cn.com.lenovo.speechservice.receiver;

import com.iflytek.cloud.SpeechConstant;

import cn.com.lenovo.speechservice.utils.Constant;
import cn.com.lenovo.speechservice.utils.Loger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectivityChangeBroadcastReceiver extends BroadcastReceiver {

	/**
	 * 监听网络状态变化的广播接收者
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取网络连接管理者
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取网络状态信息
		NetworkInfo _networkInfo = manager.getActiveNetworkInfo();
		// 判断当前网络状态
		if (_networkInfo == null || !_networkInfo.isAvailable() || !_networkInfo.isConnected()) { //没有网络的时候，或者其他判断。
			// 网络连接不可用
			Toast.makeText(context, "网络状态发生变化：\n网络不可用\n已经变更为本地引擎", Toast.LENGTH_LONG).show();
			Loger.i("ConnectivityChangeBroadcastReceiver", "网络状态发生变化：\n网络不可用\n已经变更为本地引擎");
			// 将讯飞引擎该外本地引擎
			Constant.EngineType = SpeechConstant.TYPE_LOCAL;
			// 修改网络状态
			Constant.NETWORK_STATE = false;
		} else {
			// 网络连接可用
			Toast.makeText(context, "网络状态发生变化：\n网络可用\n已经变更为网络引擎", Toast.LENGTH_LONG).show();
			Loger.i("ConnectivityChangeBroadcastReceiver", "网络状态发生变化：\n网络可用\n已经变更为网络引擎");
			// 将讯飞引擎该外网络引擎
			Constant.EngineType = SpeechConstant.TYPE_CLOUD;
			// 修改网络状态
			Constant.NETWORK_STATE = true;
			// 网络可用的时候检查词表是否上传
			if (!Constant.UPLOAD_WORD_STATE) {
				// 上传词表
				//				Worder worder = new Worder(context);
				//				worder.uploadeWords(new Applicationer(context).getUploadWords());
			}
		}
	}
}
