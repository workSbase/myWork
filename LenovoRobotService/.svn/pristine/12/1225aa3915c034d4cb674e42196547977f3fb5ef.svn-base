package com.lenovo.main.util;

import android.content.Context;
import android.content.Intent;

public class CloseSpeechUtils {
	public static void setCloseSpeechFlag(Context context, int speechFlag) {
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction("cn.com.lenovo.speechreceiver_switch");
		startSpeech_intent.putExtra("speechFlag", speechFlag);
		context.sendBroadcast(startSpeech_intent);
	}
}
