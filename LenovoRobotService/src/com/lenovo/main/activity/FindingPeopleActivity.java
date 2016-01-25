package com.lenovo.main.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.BaseService;
//import com.lenovo.main.MIService.ServiceHelpClass;
import com.lenovo.main.util.AnimationClass;
import com.lenovo.main.util.AnimationClass.AnimationEndListenr;
import com.lenovo.main.util.SharedPreferencesHelpClass;

public class FindingPeopleActivity extends Activity implements OnClickListener {
	private Button answerButton;
	private Button overButton;
	private MySpeachActivity receiver;
	private ImageView iv_incomingcall_active_bg2;
	private ImageView iv_incomingcall_active_bg3;
	private ImageView iv_incomingcall_active_bg4;
	private AnimationClass animationClass;
	private SharedPreferencesHelpClass sharedPreferencesHelpClassInfor;
	private int userFlag;
	private ImageView showPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initLayout();

		initData();
	}

	private void initData() {

		// serviceHelpClassInfo = ServiceHelpClass.getServiceHelpClassInfo();
		IntentFilter filter = new IntentFilter();
		filter.addAction("finishspeachpopleactivity");
		receiver = new MySpeachActivity();
		registerReceiver(receiver, filter);

		animationClass = new AnimationClass(iv_incomingcall_active_bg2,
				iv_incomingcall_active_bg3, iv_incomingcall_active_bg4);
		animationClass.startAnimation_1();
		animationClass.setAnimationEndListenr(new AnimationEndListenr() {

			@Override
			public void endAnimation() {
				// TODO Auto-generated method stub
				animationClass.startAnimation_1();
			}
		});
	}

	private void initLayout() {

		sharedPreferencesHelpClassInfor = SharedPreferencesHelpClass
				.getSharedPreferencesHelpClassInfor();
		userFlag = sharedPreferencesHelpClassInfor.getUserFlag(this);

		setContentView(R.layout.findingpeople);
		answerButton = (Button) findViewById(R.id.bt_answer);
		answerButton.setOnClickListener(this);
		overButton = (Button) findViewById(R.id.bt_reject);
		overButton.setOnClickListener(this);
		TextView tv_personName = (TextView) findViewById(R.id.tv_personName);
		TextView tv_incoming = (TextView) findViewById(R.id.tv_incoming);

		showPicture = (ImageView) findViewById(R.id.showPicture);

		if (userFlag == 0) {
			tv_personName.setText("晓峰");
			tv_incoming.setText("晓峰正在呼叫您...");
			showPicture.setImageResource(R.drawable.pic_boy_cn);
		}
		iv_incomingcall_active_bg2 = (ImageView) findViewById(R.id.iv_incomingcall_active_bg2);
		iv_incomingcall_active_bg3 = (ImageView) findViewById(R.id.iv_incomingcall_active_bg3);
		iv_incomingcall_active_bg4 = (ImageView) findViewById(R.id.iv_incomingcall_active_bg4);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.bt_answer:
			// 通知手机端,打电话过来.这个打电话是表示直接接通的发送打电话过来的广播
			BaseService.answerPhoneInterface.stopSpeaking();
			if (userFlag == 0) {
				BaseService.answerPhoneInterface.drawinSendInstructToPhone(1,
						11, 6);
			} else {
				BaseService.answerPhoneInterface.drawinSendInstructToPhone(2,
						21, 6);
			}
			BaseService.answerPhoneInterface.PhoneConnected();
			BaseService.answerPhoneInterface.isWait(true);
			
			finish();
			break;
		case R.id.bt_reject:
			// 这个表示的就是停止找人,发送停止找人的广播
			BaseService.answerPhoneInterface.stopSpeaking();
			BaseService.answerPhoneInterface.isWait(true);
			if (userFlag == 0) {
				BaseService.answerPhoneInterface.drawinSendInstructToPhone(1,
						11, 7);
			} else {
				BaseService.answerPhoneInterface.drawinSendInstructToPhone(1,
						11, 7);
			}
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {

		// 把广播给注销掉
		unregisterReceiver(receiver);

		super.onDestroy();
	}

	class MySpeachActivity extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	}
}
