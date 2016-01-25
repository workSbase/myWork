package com.lenovo.main.util;

import java.util.ArrayList;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 语音合成的类
 * 
 * @author kongqw
 */
public abstract class SpeechCompound {
	// Log标签
	private static final String TAG = "SpeechCompound";
	// 上下文
	private Context mContext;
	// 语音合成对象
	private static SpeechSynthesizer mTts;
	// 语音播报的文字
	private String speakText;

	private boolean speakMode;

	/**
	 * 录音的回调
	 * 
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
	 * 
	 * @param context
	 */
	public SpeechCompound(Context context) {
		mContext = context;
		// 初始化APPID
		SpeechUtility.createUtility(mContext, SpeechConstant.APPID
				+ "=5523a63e");
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
	}

	;

	/**
	 * 开始语音播报的方法
	 * 
	 * @param text
	 *            播报的文字
	 * @param language
	 *            语言设置 true 中文 false 英文
	 * @param flag
	 *            播报方式 true 循环播报 false 播报一次
	 */
	public void speaking(String text, boolean language, boolean flag) {

		CloseSpeechUtils.setCloseSpeechFlag(mContext, 1);

		speakText = text;
		speakMode = flag;

		if (language) {
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME);
		} else {
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME_US);
		}
		// // 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
		// Toast.makeText(mContext, "开始合成1 speakText = " + speakText,
		// Toast.LENGTH_SHORT).show();

		if (!TextUtils.isEmpty(speakText)) {
			Log.i("speechCompound", "------5");
			int code1 = mTts.startSpeaking(speakText, mTtsListener);
			if (code1 != ErrorCode.SUCCESS) {
				if (code1 == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
					Toast.makeText(mContext, "没有安装语音+ code = " + code1,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "语音合成失败,错误码: " + code1,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private SpeechCompound compound;
	int index;

	public void speaking(final ArrayList<String> names) {
		CloseSpeechUtils.setCloseSpeechFlag(mContext, 1);
		if (names != null) {
			index = 1;
			final int personCount = names.size();

			if (0 == personCount)
				return;
			compound = new SpeechCompound(mContext) {

				@Override
				public void speakProgress(int percent, int beginPos, int endPos) {
					// TODO Auto-generated method stub
				}

				@Override
				public void completed() {
					if (index < personCount - 1) {
						index++;
						if (Pattern.compile("^([a-zA-Z]{1,20})$")
								.matcher(names.get(index)).matches()) {
							compound.speaking(names.get(index), false, false);
						} else {
							// 中文
							compound.speaking(names.get(index) + "", true,
									false);
						}
					} else if (index == personCount - 1) {
						if (personCount > 1) {
							if (PublicData.getUserFlag(mContext) == 0) {
								compound.speaking("你们好 我是达尔文很高兴认识你们,欢迎光临",
										true, false);
							} /*
							 * else { compound.speaking("Nice to meet you",
							 * false, false); }
							 */
						} else if (personCount == 1 && names.get(0) != null
								&& !names.get(0).equals("")) {
							if (PublicData.getUserFlag(mContext) == 0) {
								compound.speaking("你好 我是达尔文很高兴认识你,欢迎光临", true,
										false);
							} /*
							 * else { compound.speaking("Nice to meet you",
							 * false, false); }
							 */
						}
						index++;
					}
				}
			};
			if (names.get(0) != null && !names.get(0).equals("")) {
				if (Pattern.compile("^([a-zA-Z]{1,20})$").matcher(names.get(0))
						.matches()) {
					// 英文
					compound.speaking(names.get(0), false, false);
				} else {
					// 中文
					compound.speaking(names.get(0), true, false);
				}
			} else {
				compound.speaking("尊贵的客人您好，欢迎光临，首次相见，请多关照", true, false);
			}
		}
	}

	/*
	 * 停止语音播报
	 */

	public void stopSpeaking() {
		CloseSpeechUtils.setCloseSpeechFlag(mContext, 2);
		// 对象非空并且正在说话
		if (null != mTts && mTts.isSpeaking()) {
			// 停止说话
			mTts.stopSpeaking();
		}
	}

	/**
	 * 判断达尔文当前有没有说话
	 * 
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
			Log.i("speechCompound", "------3");
			Log.d("SpeechCompound", "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Log.i(TAG, "初始化失败,错误码：" + code);
			} else {
				// 设置参数
				setParam();

				if (!TextUtils.isEmpty(speakText)) {
					Log.i("speechCompound", "------4");
					Toast.makeText(mContext, "开始合成2 speakText = " + speakText,
							Toast.LENGTH_SHORT).show();
					int code1 = mTts.startSpeaking(speakText, mTtsListener);
					if (code1 != ErrorCode.SUCCESS) {
						if (code1 == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
							Toast.makeText(mContext,
									"没有安装语音+ code = Drawin" + code1,
									Toast.LENGTH_SHORT).show();
						} else {
							// Toast.makeText(mContext, "语音合成失败,错误码: " + code1,
							// Toast.LENGTH_SHORT).show();
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
			CloseSpeechUtils.setCloseSpeechFlag(mContext, 1);
		}

		@Override
		public void onSpeakPaused() {
			Log.i(TAG, "暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			Log.i(TAG, "继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			speakProgress(percent, beginPos, endPos);
		}

		@Override
		public void onCompleted(SpeechError error) {
			Log.i("speechCompound", "------error = " + error);
			if (speakMode) {
				Log.i("speechCompound", "------speakMode = " + speakMode);
				int code1 = mTts.startSpeaking(speakText, mTtsListener);
				if (code1 != ErrorCode.SUCCESS) {
					if (code1 == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
						Toast.makeText(mContext, "没有安装语音+ code = " + code1,
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "语音合成失败,错误码: " + code1,
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				CloseSpeechUtils.setCloseSpeechFlag(mContext, 2);
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * 参数设置
	 * 
	 * @param
	 * @return
	 */
	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 引擎类型
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		// mTts.setParameter(SpeechConstant.ENGINE_TYPE,
		// SpeechConstant.TYPE_CLOUD);
		// 设置发音人
		// mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME);
		// mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.VOICE_NAME_US);
		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED, Constant.SPEED);
		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, Constant.PITCH);
		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, Constant.VOLUME);
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, Constant.STREAM_TYPE);

	}

	/*
	 * 发送开启录音的广播
	 */
	public void sendBroadcastToStartSpeech() {
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction("cn.com.lenovo.speechservice");
		startSpeech_intent.putExtra(Constant.START_SPEECH_FLAG, true);
		mContext.sendBroadcast(startSpeech_intent);
	}

	/*
	 * 发送停止录音的广播
	 */
	public void sendBroadcastToStopSpeech() {
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction("cn.com.lenovo.speechservice");
		startSpeech_intent.putExtra(Constant.STOP_SPEECH_FLAG, true);
		mContext.sendBroadcast(startSpeech_intent);
	}

	/**
	 * 发送开关录音的广播
	 * 
	 * public void sendSpeechSwitch(int speechFlag) { Intent startSpeech_intent
	 * = new Intent();
	 * startSpeech_intent.setAction("cn.com.lenovo.speechreceiver_switch");
	 * startSpeech_intent.putExtra("speechFlag", speechFlag);
	 * mContext.sendBroadcast(startSpeech_intent); }
	 */
}
