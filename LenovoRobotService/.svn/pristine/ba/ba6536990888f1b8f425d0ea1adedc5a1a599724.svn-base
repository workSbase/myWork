package com.lenovo.main.util;

import android.content.Context;
import android.media.AudioManager;

public class SoundUtils {
	/**
	 * 用来设置媒体音量大小的方法
	 * 
	 * @param context
	 *            上下文环境
	 * @param sound
	 *            设置的媒体音量的大小
	 */
	public static void setSoundData(Context context, int sound) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, sound, 0);
	}
}
