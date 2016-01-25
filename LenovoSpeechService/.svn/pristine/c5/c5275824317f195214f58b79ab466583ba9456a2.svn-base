package cn.com.lenovo.speechservice.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Broadcast {

	/**
	 * 发送广播
	 * @param context
	 * @param action 
	 * @param key
	 * @param value
	 */
	public static void mySendBroadcast(Context context, String action, String key, String value) {
		Intent intent = new Intent();
		// 发送广播的Action为应用的包名
		intent.setAction(action);
		// 
		intent.putExtra(key, value);
		// 发送广播
		context.sendBroadcast(intent);
		Toast.makeText(context, "action = " + action + "\nkey = " + key + "\nvalue = " + value, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 发送开启录音的广播
	 */
	public static void sendBroadcastToStartSpeech(Context context) {
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction(context.getPackageName());
		startSpeech_intent.putExtra(Constant.START_SPEECH_FLAG, true);
		context.sendBroadcast(startSpeech_intent);
	}

	/**
	 * 发送停止录音的广播
	 */
	public static void sendBroadcastToStopSpeech(Context context) {
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction(context.getPackageName());
		startSpeech_intent.putExtra(Constant.STOP_SPEECH_FLAG, true);
		context.sendBroadcast(startSpeech_intent);
	}

	/**
	 * 发送语音合成开始的广播
	 */
	public static void sendBroadcastSpeechCompoundStart(Context context) {
		Intent intent = new Intent();
		intent.setAction(context.getPackageName());
		intent.putExtra("SpeechCompound", 1);
		context.sendBroadcast(intent);
	}

	/**
	 * 发送语音合成停止的广播
	 */
	public static void sendBroadcastSpeechCompoundCompleted(Context context) {
		Intent intent = new Intent();
		intent.setAction(context.getPackageName());
		intent.putExtra("SpeechCompound", 0);
		context.sendBroadcast(intent);
	}
}
