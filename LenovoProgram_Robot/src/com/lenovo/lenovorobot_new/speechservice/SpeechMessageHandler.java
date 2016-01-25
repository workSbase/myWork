package com.lenovo.lenovorobot_new.speechservice;


import android.os.Handler;
import android.os.Message;

public class SpeechMessageHandler extends Handler {
	private SpeechWakeUtils speechWakeUtils;
	private SpeechCompoundUtils compoundUtils;
	/**
	 * 想让该服务,合成一段话
	 */
	private final int SPEECHCOMPOUND = 1;
	/**
	 * 当开视频的时候,释放麦克给其他地方使用,停止唤醒
	 */
	private final int STOPWAKE = 2;
	/**
	 * 关闭视频的时候,开启唤醒
	 */
	private final int STARTWAKE = 3;

	public SpeechMessageHandler(SpeechWakeUtils speechWakeUtils,
			SpeechCompoundUtils compoundUtils) {
		this.speechWakeUtils = speechWakeUtils;
		this.compoundUtils = compoundUtils;
	}

	@Override
	public void handleMessage(Message msg) {
		if (speechWakeUtils != null && compoundUtils != null) {
			int key = msg.what;
			switch (key) {
			case SPEECHCOMPOUND:
				// 要合成的内容
				String compoundContent = (String) msg.obj;
				compoundUtils.startCompound(compoundContent);
				break;
			case STOPWAKE:
				if (speechWakeUtils != null) {
					speechWakeUtils.stopWake();
				}
				break;
			case STARTWAKE:
				if (speechWakeUtils != null) {
					speechWakeUtils.startWake();
				}
				break;
			}
		}
	}
}
