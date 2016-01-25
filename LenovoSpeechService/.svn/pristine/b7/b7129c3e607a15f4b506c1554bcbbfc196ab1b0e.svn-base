package cn.com.lenovo.speechservice.engine;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import cn.com.lenovo.speechservice.service.SpeechService;
import cn.com.lenovo.speechservice.utils.Broadcast;
import cn.com.lenovo.speechservice.utils.Constant;
import cn.com.lenovo.speechservice.utils.Loger;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 语音合成的类
 * 
 * @author kongqw
 * 
 */
public abstract class SpeechCompound {
	// Log标签
	private static final String TAG = "SpeechCompound";
	// 上下文
	private Context mContext;
	// 语音合成对象
	private static SpeechSynthesizer mTts;
	// 缓冲进度
	//	private int mPercentForBuffering = 0;
	//	// 播放进度
	//	private int mPercentForPlaying = 0;
	// 语音播报的文字
	private String speakText;

	private SharedPreferences mSharedPreferences;

	/**
	 * 录音的回调
	 * @param percent
	 * @param beginPos
	 * @param endPos
	 */
	public abstract void speakProgress(int percent, int beginPos, int endPos);

	/**
	 * 录音完成的回调
	 */
	public abstract void completed();

	/**
	 * 构造方法
	 * @param context
	 */
	public SpeechCompound(Context context) {
		mContext = context;
		// 初始化SharedPreferences缓存
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	};

	public void speaking(String text) {
		SpeechService.handler.sendEmptyMessage(1);
		speakText = text;
		if (null == mTts) {
			// 初始化合成对象
			mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
		} else {
			if (!TextUtils.isEmpty(speakText)) {
				setParam();
				int code1 = mTts.startSpeaking(speakText, mTtsListener);
				if (code1 != ErrorCode.SUCCESS) {
					if (code1 == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
						Toast.makeText(mContext, "没有安装语音+ code = " + code1, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "语音合成失败,错误码222: " + code1, Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	/*
	 * 停止语音播报
	 */
	public void stopSpeaking() {
		// 对象非空并且正在说话
		if (null != mTts && mTts.isSpeaking()) {
			// 停止说话
			mTts.stopSpeaking();
			// 发送广播开启录音
			Broadcast.sendBroadcastToStartSpeech(mContext);
		}
	}

	/**
	 * 判断达尔文当前有没有说话
	 * @return
	 */
	public boolean isSpeaking() {
		if (null != mTts) {
			return mTts.isSpeaking();
		} else {
			return false;
		}
	}

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				Loger.i(TAG, "初始化失败,错误码：" + code);
			} else {
				if (!TextUtils.isEmpty(speakText)) {
					// 设置参数
					setParam();
					int code1 = mTts.startSpeaking(speakText, mTtsListener);
					if (code1 != ErrorCode.SUCCESS) {
						if (code1 == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
							Toast.makeText(mContext, "没有安装语音+ code = " + code1, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(mContext, "语音合成失败,错误码111: " + code1, Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			Loger.i(TAG, "开始播放");
			// 语音播报的时候停止录音
			Broadcast.sendBroadcastToStopSpeech(mContext);
			// 发送语音合成开始的广播
			Broadcast.sendBroadcastSpeechCompoundStart(mContext);
			//			SpeechServiceToast.getInstance().showMyToast(mContext, "语言 ：" + mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN));
		}

		@Override
		public void onSpeakPaused() {
			//Loger.i(TAG, "暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			//Loger.i(TAG, "继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
			//mPercentForBuffering = percent;
			//Log.i(TAG, "缓冲进度为" + mPercentForBuffering + "，播放进度为" + mPercentForPlaying);
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			speakProgress(percent, beginPos, endPos);
			//mPercentForPlaying = percent;
			//Log.i(TAG, "缓冲进度为" + mPercentForBuffering + "，播放进度为" + mPercentForPlaying);
			//SpeechServiceToast.getInstance().showMyToast(mContext, "缓冲进度为" + mPercentForBuffering + ",播放进度为:" + mPercentForPlaying);
		}

		@Override
		public void onCompleted(SpeechError error) {
			completed();
			if (error == null) {
				//Loger.i(TAG, "播放完成");
			} else if (error != null) {
				//Loger.i(TAG, error.getPlainDescription(true));
			}
			SpeechService.handler.sendEmptyMessage(2);
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
	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 引擎类型
		//		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置发音人
		if (Constant.LANGUAGE_CN.equals(mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN))) {
			mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME_CN);
		} else {
			mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME_US);
		}
		// 设置语速
		if (Constant.LANGUAGE_CN.equals(mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN))) {
			mTts.setParameter(SpeechConstant.SPEED, Constant.SPEED);
		} else {
			mTts.setParameter(SpeechConstant.SPEED, "50");
		}
		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, Constant.PITCH);
		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, Constant.VOLUME);
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, Constant.STREAM_TYPE);

	}

}
