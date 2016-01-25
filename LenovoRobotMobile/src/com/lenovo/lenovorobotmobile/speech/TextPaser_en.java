package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;



import com.google.gson.Gson;


import android.app.Activity;
import android.content.Context;

public class TextPaser_en extends Activity {

	private Context mContext;
	private Gson gson;
	private String reJson;

	public TextPaser_en(Context context) {
		mContext = context;
		gson = new Gson();
	}

	/**
	 * 获取Json
	 * @param text
	 * @return
	 */
	public String getSendMessage(String text) {

		// Intent
		ArrayList<String> intents = getIntents();

		reJson = null;

		// 遍历Intent
		for (int index = 0; index < intents.size(); index++) {
			String json = FucUtil.readFile(mContext, intents.get(index), "utf-8");
			Intent intent = gson.fromJson(json, Intent.class);

			// 生成Json
			reJson = intent.makeJson(text);

			Message msg = gson.fromJson(reJson, Message.class);
			if (msg.isMatching()) {
				return msg.getSendMessage();
			}
		}
		return null;
	}

	/**
	 * 获取Intent
	 * @return
	 */
	public ArrayList<String> getIntents() {
		// TODO 获取Intent 暂时使用假数据
		ArrayList<String> intents = new ArrayList<String>();
		intents.add("GO");
		intents.add("MOVE");
		intents.add("ELECTRIC");
		intents.add("LOCATION");
		intents.add("SCHEDULE");
		intents.add("DEL_SCHEDULE");
		intents.add("DOING");
		intents.add("CLOSE");
		intents.add("OPEN");
		intents.add("ATHOME");
		intents.add("REPORT");
		intents.add("REMIDER");
		intents.add("IOT");
		intents.add("TEST");
		return intents;
	}

}
