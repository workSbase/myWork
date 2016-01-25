package cn.com.lenovo.speechservice.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cn.com.lenovo.speechservice.ui.SpeechServiceToast;
import cn.com.lenovo.speechservice.utils.Constant;
import cn.com.lenovo.speechservice.utils.Loger;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 管理词表的类
 * @author kongqw
 *
 */
public class Worder {
	// Log标签
	private static final String TAG = "Worder";
	// 上下文
	private Context mContext;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 自定义Toast
	private SpeechServiceToast mSpeechServiceToast;
	// sp
	private SharedPreferences mSharedPreferences;
	/*
	 * Hander
	 * 接收上传词表的结果并显示
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			/*
			 * 词表上传完成 发送广播 开启录音
			 * 发送广播开启录音
			 */
			Intent startSpeech_intent = new Intent();
			startSpeech_intent.setAction(mContext.getPackageName());
			startSpeech_intent.putExtra(Constant.START_SPEECH_FLAG, true);
			mContext.sendBroadcast(startSpeech_intent);
			// 获取显示的信息
			String message = (String) msg.obj;
			switch (msg.what) {
			case Constant.INIT_WORD_SUCCESSFUL:
				// 上传词表初始化成功
				Toast.makeText(mContext, "\n上传词表 : \n" + message, Toast.LENGTH_LONG).show();
				break;
			case Constant.INIT_WORD_UNSUCCESSFUL:
				// 上传词表初始化失败
				Toast.makeText(mContext, "\n上传词表 : \n" + message, Toast.LENGTH_LONG).show();
				break;
			case Constant.UPLOAD_WORD_SUCCESSFUL:
				// 上传词表成功
				Toast.makeText(mContext, "\n上传词表 : \n" + message, Toast.LENGTH_LONG).show();
				// 设置标志位 词表上传成功
				Constant.UPLOAD_WORD_STATE = true;
				break;
			case Constant.UPLOAD_WORD_UNSUCCESSFUL:
				// 上传词表失败
				Toast.makeText(mContext, "\n上传词表 : \n" + message, Toast.LENGTH_LONG).show();
				// 设置标志位 词表上传失败
				Constant.UPLOAD_WORD_STATE = false;
				break;
			}
			new SpeechCompound(mContext) {

				@Override
				public void speakProgress(int percent, int beginPos, int endPos) {

				}

				@Override
				public void completed() {

				}
			}.speaking(message);
		}
	};

	/**
	 * 构造方法
	 * @param context
	 */
	public Worder(Context context) {
		// 获取上下文
		mContext = context;
		// 初始化自定义Toast
		mSpeechServiceToast = SpeechServiceToast.getInstance();
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
		// 指定引擎类型
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// sp
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}

	/**
	 * 上传用户词表
	 * @param words
	 */
	public void uploadeWords(HashMap<String, ArrayList<String>> words) {
		// 词表非空判断
		if (null == words) {
			return;
		}
		// 获取上传词表的JSON
		String contents = makeJsonFromWords(words);
		Loger.i(TAG, contents);

		/*
		 * 上传词表 停止录音
		 * 停止录音
		 */
		Intent startSpeech_intent = new Intent();
		startSpeech_intent.setAction(mContext.getPackageName());
		startSpeech_intent.putExtra(Constant.STOP_SPEECH_FLAG, true);
		mContext.sendBroadcast(startSpeech_intent);

		// 弹窗提示信息
		mSpeechServiceToast.showMyToast(mContext, "正在上传词表...");

		/*
		 * 上传词表
		 */
		int ret = mIat.updateLexicon("userword", contents, lexiconListener);
		if (ret != ErrorCode.SUCCESS) {
			Toast.makeText(mContext, "上传热词失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 生产上传词表的JSON
	 */
	private String makeJsonFromWords(HashMap<String, ArrayList<String>> words) {
		// 拆分List，拼接JSON词表
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"userword\":[");
		Iterator<Entry<String, ArrayList<String>>> iterator = words.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, ArrayList<String>> next = iterator.next();
			buffer.append("{\"name\":\"").append(next.getKey()).append("\",\"words\":[");
			ArrayList<String> value = next.getValue();
			for (int arrIndex = 0; arrIndex < value.size(); arrIndex++) {
				buffer.append("\"").append(value.get(arrIndex)).append("\"");
				if (arrIndex < value.size() - 1) {
					buffer.append(",");
				}
			}
			if (iterator.hasNext()) {
				buffer.append("]},");
			} else {
				buffer.append("]}");
			}
		}

		buffer.append("]}");
		return buffer.toString();
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				// 打印LOG
				Loger.i(TAG, "上传词表初始化失败,错误码：" + code);
				// 发送Handler
				Message msg = Message.obtain();
				msg.what = Constant.INIT_WORD_UNSUCCESSFUL;
				if (Constant.LANGUAGE_CN.equals(mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN))) {
					msg.obj = "上传词表初始化失败\n错误码：" + code + "\n";
				} else {
					msg.obj = "Words is uploaded init failure, error code " + code;
				}
				handler.sendMessage(msg);
			} else {
				// 打印LOG
				Loger.i(TAG, "上传词表初始化成功");
				// 发送Handler
				Message msg = Message.obtain();
				msg.what = Constant.INIT_WORD_SUCCESSFUL;
				msg.obj = "上传词表初始化成功\n";
				handler.sendMessage(msg);
			}
		}
	};

	/**
	 * 上传联系人/词表监听器。
	 */
	private LexiconListener lexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			Log.d(TAG, "onLexiconUpdated() error = " + error);
			if (error != null) {
				// 打印LOG
				Loger.i(TAG, "词表上传失败" + error.toString());
				// 发送Handler
				Message msg = Message.obtain();
				msg.what = Constant.UPLOAD_WORD_UNSUCCESSFUL;
				if (Constant.LANGUAGE_CN.equals(mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN))) {
					msg.obj = "词表上传失败\n" + error.toString() + "\n";
				} else {
					msg.obj = "Words is uploading failure\n";
				}
				handler.sendMessage(msg);
			} else {
				// 打印LOG
				Loger.i(TAG, "词表上传成功");
				// 发送Handler
				Message msg = Message.obtain();
				msg.what = Constant.UPLOAD_WORD_SUCCESSFUL;
				if (Constant.LANGUAGE_CN.equals(mSharedPreferences.getString("LANGUAGE", Constant.LANGUAGE_CN))) {
					msg.obj = "词表上传成功\n";
				} else {
					msg.obj = "Words is uploaded successfully\n";
				}

				handler.sendMessage(msg);
			}
		}
	};
}
