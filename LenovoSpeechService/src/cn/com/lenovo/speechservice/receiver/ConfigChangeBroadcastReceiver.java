package cn.com.lenovo.speechservice.receiver;

import cn.com.lenovo.speechservice.engine.SpeechCompound;
import cn.com.lenovo.speechservice.utils.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class ConfigChangeBroadcastReceiver extends BroadcastReceiver {

	/**
	 * 监听网络状态变化的广播接收者
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		// 切换至中文语音识别
		boolean isLanguageCn = intent.getBooleanExtra("speechService_language_cn", false);
		if (isLanguageCn) {
			// 切换至中文语音识别
			Toast.makeText(context, "切换至中文语音识别", Toast.LENGTH_LONG).show();
			edit.putString("LANGUAGE", Constant.LANGUAGE_CN).commit();
			new SpeechCompound(context) {

				@Override
				public void speakProgress(int percent, int beginPos, int endPos) {
				}

				@Override
				public void completed() {
				}
			}.speaking("中文识别");
		}

		// 切换至英文语音识别
		boolean isLanguageUs = intent.getBooleanExtra("speechService_language_us", false);
		if (isLanguageUs) {
			// 切换至英文语音识别
			Toast.makeText(context, "Cut English", Toast.LENGTH_LONG).show();
			edit.putString("LANGUAGE", Constant.LANGUAGE_US).commit();
			new SpeechCompound(context) {

				@Override
				public void speakProgress(int percent, int beginPos, int endPos) {
				}

				@Override
				public void completed() {
				}
			}.speaking("Cut English");
		}
	}
}
