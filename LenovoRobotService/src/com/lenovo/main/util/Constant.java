package com.lenovo.main.util;

import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;

/**
 * 管理程序中用到的常量
 * 
 * @author kongqw
 * 
 */
public class Constant {

	/**
	 * APPID
	 */
	public static final String APPID = "555ec16e";
	/**
	 * 关闭或开启录音的action
	 */
	public static final String SPEECH_ACTION = "cn.com.lenovo.speechservice";
	/**
	 * 获取好友列表的的内容提供者暴露的URI
	 */
	public static final String CONTACTER_URI = "content://www.lenovo.com";

	/**
	 * 存入SharedPreferences的标志位 用来标记当前服务状态是否在录音
	 */
	public static final String SERVICE_STATE = "isSpeeching";

	/**
	 * 广播监听开启录音的键值(标志位) 通过发送广播开启录音的参数： K StartSpeech V true
	 */
	public static final String START_SPEECH_FLAG = "StartSpeech";

	/**
	 * 广播监听关闭录音的键值(标志位) 通过发送广播广播录音的参数： K StopSpeech V true
	 */
	public static final String STOP_SPEECH_FLAG = "StopSpeech";

	/**
	 * 自定义的Toast显示位置距屏幕左侧的距离
	 */
	public static final int TOAST_LOCATION_X = 0;

	/**
	 * 自定义的Toast显示位置距屏幕顶部的距离
	 */
	public static final int TOAST_LOCATION_Y = 0;

	/**
	 * LOG等级
	 */
	public static final int LOG_VERBOSE_FLAG = 1;

	/**
	 * LOG等级
	 */
	public static final int LOG_DEBUG_FLAG = 2;

	/**
	 * LOG等级
	 */
	public static final int LOG_INFO_FLAG = 3;

	/**
	 * LOG等级
	 */
	public static final int LOG_WARN_FLAG = 4;

	/**
	 * LOG等级
	 */
	public static final int LOG_ERROR_FLAG = 5;

	/**
	 * 显示LOG的等级
	 */
	public static final int LOG_LEVEL_FLAG = 6;

	/**
	 * 标记上传词表初始化失败
	 */
	public static final int INIT_WORD_UNSUCCESSFUL = 0;

	/**
	 * 标记上传词表初始化成功
	 */
	public static final int INIT_WORD_SUCCESSFUL = 1;

	/**
	 * 标记上传词表失败
	 */
	public static final int UPLOAD_WORD_UNSUCCESSFUL = 2;

	/**
	 * 标记上传词表成功
	 */
	public static final int UPLOAD_WORD_SUCCESSFUL = 3;

	/**
	 * 讯飞听写引擎 默认网络引擎
	 * 
	 * 网络引擎 private String mEngineType = SpeechConstant.TYPE_CLOUD; 本地引擎 private
	 * String mEngineType = SpeechConstant.TYPE_LOCAL; 混合引擎，不建议使用，讯飞正在优化 private
	 * String mEngineType = SpeechConstant.TYPE_MIX;
	 */

	public static String EngineType = SpeechConstant.TYPE_CLOUD;

	/**
	 * 应用领域
	 */
	public static final String DOMAIN = "iat";

	/**
	 * 编码格式
	 */
	public static final String TEXT_ENCODING = "utf-8";

	/**
	 * 设置参数返回结果格式
	 */
	public static final String RESULT_TYPE = "json";

	/**
	 * 设置语言
	 */
	public static final String LANGUAGE_CN = "zh_cn";

	/**
	 * 设置语言
	 */
	public static final String LANGUAGE_US = "en_us";

	/**
	 * 设置语言区域
	 */
	public static final String ACCENT = "mandarin";

	/**
	 * 设置语音前端点
	 */
	public static final String VAD_BOS = "10000";

	/**
	 * 设置语音后端点
	 */
	public static final String VAD_EOS = "1000";

	/**
	 * 设置标点符号
	 */
	public static final String ASR_PTT = "0";

	/**
	 * 设置音频存储路径
	 */
	public static final String ASR_AUDIO_PATH = Environment
			.getExternalStorageDirectory() + "/SpeechService/";

	/**
	 * 网络状态
	 */
	public static boolean NETWORK_STATE = false;

	/**
	 * 词表上传状态
	 */
	public static String UPLOAD_WORD_STATE = "UPLOAD_WORD_STATE";

	/**
	 * 达尔文应用的包名 在达尔文应用界面可以人机交互
	 */
	public static final String DARWIN_PACKAGENAME = "cn.com.lenovo.darwin";

	/**
	 * 达尔文状态 false 人机交互关闭状态 true 人机交互开启状态
	 */
	public static boolean DARWIN_STATE = true;

	/**
	 * 标记达尔文当前处于选择状态 达尔文显示ListView
	 */
	public static boolean DARWIN_SELECT = false;

	/**
	 * AI请求数据成功标志位
	 */
	public static final int AISPEECH_SUCCESS = 1;

	/**
	 * AI请求数据失败标志位
	 */
	public static final int AISPEECH_ERROR = -1;

	/**
	 * AI请求数据超时标志位
	 */
	public static final int AISPEECH_FAILURE = 0;

	/**
	 * 图灵人机交互的APPKEY
	 */
	public static final String APPKEY = "13aec615fc76da61e2234b8f0a0af1bb";

	/**
	 * 图灵API接口地址
	 */
	public static final String TULING_URL = "http://www.tuling123.com/openapi/api";

	/**
	 * 设置发音人 nannan 楠楠 童年女声 汉语（普通话）
	 */
	public static final String VOICE_NAME = "nannan";

	/**
	 * 设置英文发音人 nannan 楠楠 童年女声 汉语（普通话）
	 */
	public static final String VOICE_NAME_US = "catherine";

	/**
	 * 设置发音语速
	 */
	public static final String SPEED = "60";

	/**
	 * 设置发音音调
	 */
	public static final String PITCH = "50";

	/**
	 * 设置发音音量
	 */
	public static final String VOLUME = "100";

	/**
	 * 设置播放器音频流类型
	 */
	public static final String STREAM_TYPE = "3";

	/**
	 * 欢迎模式
	 */
	public static boolean WELCOME_MODE = true;
}
