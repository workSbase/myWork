package com.lenovo.lenovorobotmobile.speech;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public abstract class LenovoSpeechRecognizer_en {

	public abstract void resultText(String string);

	// 语音听写对象
	private SpeechRecognizer mIat;
	// TAG标签
	private static String TAG = "KRobotSpeechRecognizer";
	// 上下文
	private static Context mContext;

	// SharedPreferences
	// private SharedPreferences mSharedPreferences;

	public LenovoSpeechRecognizer_en(Context context) {
		// 获取上下文
		mContext = context;
		// 初始化SharedPreferences
		// mSharedPreferences = context.getSharedPreferences("config",
		// Context.MODE_PRIVATE);
		// 设置APPID
		SpeechUtility
				.createUtility(context, SpeechConstant.APPID + "=555ebd28");
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
	}

	/**
	 * 开始录音
	 */
	public void start() {
		if (null == mIat) {
			// 初始化识别对象
			mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
		} else {
			// 设置参数
			setParam();
			// 不显示听写对话框
			int ret = mIat.startListening(recognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				Toast.makeText(mContext, "听写失败,错误码111：" + ret,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 停止录音并获取录入的文字
	 */
	public void cancel() {
		if (null != mIat) {
			// mIat.cancel();
			mIat.stopListening();
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(mContext, "初始化失败,错误码：" + code,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		StringBuffer resultText;

		@Override
		public void onBeginOfSpeech() {
			Log.i(TAG, "开始说话");
			resultText = new StringBuffer();
		}

		@Override
		public void onError(SpeechError error) {
			Log.i(TAG, error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			Log.i(TAG, "结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			resultText.append(text);
			if (isLast) {
				// 最后的结果
				resultText(resultText.toString().trim());
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			Log.i(TAG, "当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		// String lag = mSharedPreferences.getString("iat_language_preference",
		// "mandarin");
		// if (lag.equals("en_us")) {
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		// } else {
		// // 设置语言
		// mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// // 设置语言区域
		// mIat.setParameter(SpeechConstant.ACCENT, lag);
		// }
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, "1");
		// 设置音频保存路径
		// mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
		// Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");
	}
}
