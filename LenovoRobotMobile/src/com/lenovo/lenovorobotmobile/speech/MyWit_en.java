package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public abstract class MyWit_en extends Activity implements IWitListener {

	Wit _wit;
	private Context mContext;
	private String reJson;
	private String error;

	/**
	 * Wit返回结果的回调
	 * 
	 * @param error
	 *            回调错误
	 * @param json
	 *            回调Json
	 */
	public abstract void getReJson(String error, String json);

	public MyWit_en(Context context) {
		mContext = context;
		// 初始化wit
		// bao
		// String accessToken = "DB2FMVK23ASBHS75MWSLTHNDB4F4ACXR";
		// kong 7SV6J3Y65M44CJI7Y3B5H7B2S7DYHV5X
		// zhou ZP7PWK6ZS6KVHMBGQGBMZVQE6F4KVWPG
		String accessToken = "ZP7PWK6ZS6KVHMBGQGBMZVQE6F4KVWPG";
		_wit = new Wit(accessToken, this);
		_wit.enableContextLocation(context.getApplicationContext());
	}

	public String getMessage(String text) {
		Toast.makeText(mContext, "wit请求  text = " + text, Toast.LENGTH_SHORT)
				.show();
		if (!TextUtils.isEmpty(text))
			_wit.captureTextIntent(text);

		getReJson(error, reJson);

		return null;
	}

	@Override
	public void witActivityDetectorStarted() {

	}

	@Override
	public void witDidGraspIntent(ArrayList<WitOutcome> witOutcomes,
			String messageId, Error error) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (error != null) {
			Toast.makeText(mContext, error.getLocalizedMessage(),
					Toast.LENGTH_LONG).show();
			getReJson(error.getLocalizedMessage(), reJson);
			return;
		}
		// 获取拿到的JSON数据
		String jsonOutput = gson.toJson(witOutcomes);
		// 去除返返回的JSON两边的方括号
		jsonOutput = jsonOutput.trim()
				.substring(1, jsonOutput.trim().length() - 1).trim();

		reJson = jsonOutput;
		Toast.makeText(mContext, "wit获取  reJson = " + reJson,
				Toast.LENGTH_SHORT).show();
		getReJson(null, reJson);
	}

	@Override
	public void witDidStartListening() {

	}

	@Override
	public void witDidStopListening() {

	}

	@Override
	public String witGenerateMessageId() {
		return null;
	}
}
