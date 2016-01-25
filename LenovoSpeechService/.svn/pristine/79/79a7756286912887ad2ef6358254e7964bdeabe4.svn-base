package cn.com.lenovo.speechservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.com.lenovo.speechservice.domain.ResultBin;
import cn.com.lenovo.speechservice.engine.SpeechCompound;
import cn.com.lenovo.speechservice.ui.SpeechServiceToast;
import cn.com.lenovo.speechservice.utils.FucUtil;
import cn.com.lenovo.speechservice.utils.KeyWordString;
import cn.com.lenovo.speechservice.utils.Loger;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;

public class SpeechService extends Service {

	// 获取类名
	private final String TAG = "SpeechService";

	// 录音对象
	private SpeechRecognizer mAsr;

	private String mLocalGrammar = null;

	// SharedPreferences缓存
	//	private SharedPreferences mSharedPreferences;

	// 系统时间毫秒值作为音频文件的文件名
	//private long mAsrAudioName;

	//private WelcomeMode welcomeMode;
	public static final String GRAMMAR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test";

	private String mResultType = "json";

	private Toast mToast;
	//	private static AISpeech aiSpeech;
	//	private Tasker mtasker;
	private Gson gson;

	//private WordKey wordKey;

	public static SpeechCompound compound;

	private SpeechServiceToast instance;

	/**
	 * 绑定服务成功的回调方法
	 * 返回调用服务方法的中间对象
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test";

	String resPath;

	public static Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();

		compound = new SpeechCompound(this) {

			@Override
			public void speakProgress(int percent, int beginPos, int endPos) {
				// TODO Auto-generated method stub

			}

			@Override
			public void completed() {
				// TODO Auto-generated method stub

			}
		};

		/**
		 * 这个 handler 主要是用来,关闭和开启录音的,在广播中使用的多一些,
		 * 别的app发送关闭录音的广播
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int key = msg.what;
				switch (key) {
				case 1:
					stopSpeech();
					break;
				case 2:
					startSpeech();
					break;
				}
			}
		};

		//		aiSpeech = new AISpeech(this);
		//		mtasker = new Tasker(this);

		//welcomeMode = WelcomeMode.getInstance(this);

		Toast.makeText(this, "语音服务启动...", Toast.LENGTH_SHORT).show();

		// 初始化SharedPreferences缓存
		//		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

		resPath = ResourceUtil.generateResourcePath(SpeechService.this, RESOURCE_TYPE.assets, "asr/common.jet");

		StringBuffer param = new StringBuffer();
		//	param.append(","+ResourceUtil.ASR_RES_PATH+"="+resPath); param.append(","+ResourceUtil.ENGINE_START+"="+SpeechConstant.ENG_ASR+";");
		//		param.append("appid=55d97b13");
		param.append("appid=5631f44f");
		param.append(",");
		param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

		SpeechUtility.createUtility(SpeechService.this, param.toString());

		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		mAsr = SpeechRecognizer.createRecognizer(this, mInitListener);

		mLocalGrammar = FucUtil.readFile(this, "call.bnf", "utf-8");

		mAsr.setParameter(SpeechConstant.PARAMS, null);
		// 设置文本编码格式
		mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		// 设置引擎类型
		mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		// 设置语法构建路径
		mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);

		mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");

		mAsr.setParameter(SpeechConstant.MIXED_THRESHOLD, "60");
		//使用8k音频的时候请解开注释
		//		mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
		// 设置资源路径
		//		String see = getResourcePath();
		mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());

		//if (!Constant.DARWIN_STATE) {
		int ret = mAsr.buildGrammar("bnf", mLocalGrammar, grammarListener);
		if (ret != ErrorCode.SUCCESS) {
			Toast.makeText(this, "初始化失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
			//showTip("语法构建失败,错误码：" + ret);
		}
		ret = mAsr.startListening(mRecognizerListener);
		if (ret != ErrorCode.SUCCESS) {
			//showTip("识别失败,错误码: " + ret);	
		}
		//}

		SS_setParam();

		gson = new Gson();

		//	wordKey = new WordKey(this);

		instance = SpeechServiceToast.getInstance();

		KeyWordString.initWordArray();
	}

	// 获取识别资源路径
	private String getResourcePath() {
		StringBuffer tempBuffer = new StringBuffer();
		// 识别通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(SpeechService.this, RESOURCE_TYPE.assets, "asr/common.jet"));
		// 识别8k资源-使用8k的时候请解开注释
		// tempBuffer.append(";");
		// tempBuffer.append(ResourceUtil.generateResourcePath(this,
		// RESOURCE_TYPE.assets, "asr/common_8k.jet"));
		return tempBuffer.toString();
	}

	/**
	 * 设置下次开启录音的参数
	 
	private void setRestartParam() {
		// 设置下次录音的音频保存路径
		// 设置网络引擎
		mAsr.setParameter(SpeechConstant.ENGINE_TYPE, Constant.EngineType);
	}
	*/
	/**
	 * 开始录音
	 */
	public void startSpeech() {
		// 设置音频保存路径(文件名)
		//setAsrAudioPath();
		// 开始录音
		if (null != mAsr) {
			if (!mAsr.isListening()) {
				mAsr.startListening(mRecognizerListener);
			}
		}
	}

	/**
	 * 停止录音
	 */
	public void stopSpeech() {
		// 停止录音
		if (null != mAsr) {
			if (mAsr.isListening()) {
				mAsr.cancel();
			}
		}
	}

	/**
	 *  服务销毁的回调方法
	 */
	@Override
	public void onDestroy() {
		Loger.i(TAG, "callBack onDestroy");
		super.onDestroy();
		// 退出时销毁对象
		if (null != mAsr) {
			// 停止录音
			mAsr.cancel();
			// 销毁录音对象
			mAsr.destroy();
		}
	}

	/**
	 * 执行绑定服务后调用服务方法的中间对象，IBinder类型
	 * @author kongqw
	 *
	 
	public class SpeechServiceBinder extends Binder {
		public void runStartSpeech() {
			// 调用服务的业务逻辑方法 开始录音
			startSpeech();
		}

		public void runStopSpeech() {
			// 调用服务的业务逻辑方法 开始录音
			stopSpeech();
		}
	}
	*/
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);

			mAsr.startListening(mRecognizerListener);

			if (code != ErrorCode.SUCCESS) {

				//showTip("初始化失败,错误码："+code);
			}
		}
	};

	/**
	 * 识别监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		//		StringBuffer stringBuffer = new StringBuffer();

		@Override
		public void onResult(final RecognizerResult result, boolean isLast) {
			//			if (isLast) {
			if (null != result && !TextUtils.isEmpty(result.getResultString())) {
				//				Log.d(TAG, "recognizer result：" + result.getResultString() + ",,," +result.getResultString().length());
				String resultFinal = result.getResultString();
				ResultBin fromJson = gson.fromJson(resultFinal, ResultBin.class);
				float sc = fromJson.sc;
				//				Log.d("SSWana", "recognizer result：" + result.getResultString() + ",,," + sc);

				if (sc >= 30) {
					KeyWordString.handleKeyWord(SpeechService.this, fromJson, sc);
				}
				/* else if (sc < 30 && sc > 20) {
				SS_setParam();
				int ret = mAsr.startListening(mRecognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					//showTip("识别失败,错误码: " + ret);	
				}
				}
				*/
			}
			//			SS_setParam();
			int ret = mAsr.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
			}
		}

		@Override
		public void onEndOfSpeech() {

		}

		@Override
		public void onBeginOfSpeech() {
			//			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			//			SystemClock.sleep(50);
			//			SS_setParam();
			int ret = mAsr.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				//showTip("识别失败,错误码: " + ret);	
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}

		@Override
		public void onVolumeChanged(int volume) {
			//			showTip();
			instance.showMyToast(SpeechService.this, "可以说话   当前音量--" + volume);
		}

	};

	/**
	  返回栈顶的activity名字
	private String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return (runningTaskInfos.get(0).topActivity).toString();
		else
			return null;
	}
	*/

	/**
	 * 构建语法监听器。
	 */
	private GrammarListener grammarListener = new GrammarListener() {
		@Override
		public void onBuildFinish(String grammarId, SpeechError error) {
			if (error == null) {
				showTip("语法构建成功：" + grammarId);
			} else {
				showTip("语法构建失败,错误码：" + error.getErrorCode());
			}
		}
	};

	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	public boolean SS_setParam() {
		boolean result = false;
		// 清空参数
		mAsr.setParameter(SpeechConstant.PARAMS, null);
		mAsr.setParameter(ResourceUtil.ASR_RES_PATH, resPath); //设置引擎类型为本地 
		//mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		//设置识别引擎
		mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);

		//设置本地识别资源
		mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
		//设置语法构建路径
		mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
		//设置返回结果格式
		mAsr.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
		mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
		mAsr.setParameter(SpeechConstant.MIXED_THRESHOLD, "60");
		//使用8k音频的时候请解开注释
		//mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
		result = true;

		return result;
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
