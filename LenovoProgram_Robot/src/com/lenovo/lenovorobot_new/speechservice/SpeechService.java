package com.lenovo.lenovorobot_new.speechservice;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.lenovo.lenovorobot_new.R;
import com.lenovo.lenovorobot_new.BaseClass.BaseService;
import com.lenovo.lenovorobot_new.bin.ResultBin;
import com.lenovo.lenovorobot_new.speechservice.SpeechCompoundUtils.CompoundListener;
import com.lenovo.lenovorobot_new.speechservice.SpeechWakeUtils.WakeListener;
import com.lenovo.lenovorobot_new.utils.KeyWordString;

/**
 * 语音服务 语音的相关部分全部要经过这个Service
 * 
 * @author Administrator
 * 
 */
public class SpeechService extends BaseService implements CompoundListener,
		WakeListener {

	private SpeechWakeUtils speechWakeUtils;
	private SpeechCompoundUtils compoundUtils;

	/**
	 * 每一个服务中,都要存在怎么一个handler才行,这样的话才能保证和外界的交互,只有是静态的话才可以用类名直接调用,这样做也是松耦合的,
	 * 只要用handler来进行数据的传递即可,如果是外面的地方想使用,语音合成都要使用该Service来完成,为了保证程序的一致性
	 */
	public static SpeechMessageHandler speechMessageHandler;
	private Gson gson;

	@Override
	public void initService() {
		if (compoundUtils == null) {
			compoundUtils = new SpeechCompoundUtils(getApplicationContext());
			compoundUtils.setOnCompoundListener(this);
		}

		speechMessageHandler = new SpeechMessageHandler(speechWakeUtils,
				compoundUtils);
		KeyWordString.initWordArray();
		gson = new Gson();
	}

	@Override
	public void initServiceDate() {

	}

	@Override
	public void endCompound() {
		/**
		 * 开场语说完之后,在开始唤醒和合成,把所有的具体的实现,全部封装起来,留有接口向外面展示最终的结果,只要设置回调即可
		 */
		if (speechWakeUtils == null) {
			speechWakeUtils = new SpeechWakeUtils(getApplicationContext());
			speechWakeUtils.startWake();
			speechWakeUtils.setOnWakeListener(this);
			speechWakeUtils.setIsUserKeyWord(true);
		}
	}

	/**
	 * 合成初始化完成,才可以进行语音的播放,欢迎词的播放
	 */
	@Override
	public void initCompound() {
		compoundUtils.startCompound(this.getString(R.string.hello_world));
	}

	// 最后的识别结果
	@Override
	public void result(RecognizerResult result) {
		speechWakeUtils.startWake();
		// compoundUtils.startCompound(result);
		// 如果是关键词识别的话,只要是识别到了关键词都可以从这个地方给传递到,MIService中的handler完成底层的调用
		// Toast.makeText(this, result, 0).show();
		// 如果只是普通的,聊天模式的话,识别到的话,发送出去即可

		String resultFinal = result.getResultString();
		ResultBin fromJson = gson.fromJson(resultFinal, ResultBin.class);
		float sc = fromJson.sc;
		if (sc >= 20) {
			KeyWordString.handleKeyWord(SpeechService.this, fromJson, sc);
		}
	}
}
