package cn.com.lenovo.speechservice.receiver;

import cn.com.lenovo.speechservice.engine.SpeechCompound;
import cn.com.lenovo.speechservice.utils.Constant;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DarwinSpeakBroadcastReceiver extends BroadcastReceiver {

	private SpeechCompound speechCompound;

	@Override
	public void onReceive(Context context, Intent intent) {
		speechCompound = new SpeechCompound(context) {

			@Override
			public void speakProgress(int percent, int beginPos, int endPos) {

			}

			@Override
			public void completed() {

			}

		};
		boolean isShutUp = intent.getBooleanExtra("shutUp", false);
		if (isShutUp) {
			// 让达尔文停止说话
			if (speechCompound.isSpeaking()) {
				speechCompound.stopSpeaking();
				speechCompound.speaking("好的");
			}
		}

		// 开启人机交互
		boolean isAiOn = intent.getBooleanExtra("ai_on", false);
		if (isAiOn) {
			Constant.DARWIN_STATE = true;
		}
		// 关闭人机交互
		boolean isAiOff = intent.getBooleanExtra("ai_off", false);
		if (isAiOff) {
			Constant.DARWIN_STATE = false;
		}

		// 达尔文开启选择状态
		boolean isShow = intent.getBooleanExtra("showLinearLayout", false);
		if (isShow) {
			Constant.DARWIN_SELECT = true;
		}

		// 达尔文关闭选择状态
		boolean isHide = intent.getBooleanExtra("hideLinearLayout", false);
		if (isHide) {
			Constant.DARWIN_SELECT = false;
		}

		String speakText = intent.getStringExtra("welcome");
		if (null != speakText) {
			speechCompound.speaking(speakText);
		}

		String cookStep = intent.getStringExtra("cookStep");
		if (null != cookStep) {
			speechCompound.speaking(cookStep);
		}
	}
}
