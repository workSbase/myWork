package com.lenovo.lenovorobotmobile.utils;

import android.content.Context;
import android.media.AudioManager;

public class SoundUtils {
	/**
	 * 设置媒体音量的大小
	 * 
	 * @param context
	 * @param sound
	 */
	public static void setSoundData(Context context, int sound) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sound, 0);
	}
}
