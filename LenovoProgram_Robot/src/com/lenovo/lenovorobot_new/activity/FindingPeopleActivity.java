package com.lenovo.lenovorobot_new.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 找人界面
 * 
 * @author Administrator
 * 
 */
public class FindingPeopleActivity extends BaseEngineHandlerActivity {
	// 找人任务中,如果是找到人把当前的界面给结束掉
	public static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initHandler();
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int key = msg.what;
				if (key == 1) {
					FindingPeopleActivity.this.finish();
				}
			}
		};
	}
}
