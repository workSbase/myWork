package com.lenovo.lenovorobotmobile.activity;

import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.utils.AnimationTools;
import com.lenovo.lenovorobotmobile.utils.MToast;
import com.lenovo.lenovorobotmobile.utils.ServerUtils;
import com.lenovo.lenovorobotmobile.utils.AnimationTools.AnimationEndListenr;

public class FindingPeopleActivity extends BaseActivity {
	// 要找的人的名字
	private String friendPeopleName;

	private TextView showName;
	private ImageView iv_icon;
	private TextView textView_1;
	private TextView findingTime;
	private int timeS;
	private int timeM;

	private ImageView iv_iconImg;
	private ImageView iv_iconImg1;
	private ImageView iv_iconImg2;
	private AnimationTools animationClass;
	private ServerUtils msgToServerHelp;

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.speachpopleview);

		// 寻找的时间
		findingTime = (TextView) findViewById(R.id.textView_2);
		findingTime.setTypeface(typeface);

		// 寻找的谁
		textView_1 = (TextView) findViewById(R.id.textView_1);
		textView_1.setTypeface(typeface);

		// 要找的人的头像
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		// 要找的人
		showName = (TextView) findViewById(R.id.showName);
		showName.setTypeface(typeface);

		// 这个是 关闭当前界面的 按钮
		ImageButton myImageButton = (ImageButton) findViewById(R.id.myImageButton);
		myImageButton.setOnClickListener(this);

		iv_iconImg = (ImageView) findViewById(R.id.iv_iconImg);
		iv_iconImg1 = (ImageView) findViewById(R.id.iv_iconImg1);
		iv_iconImg2 = (ImageView) findViewById(R.id.iv_iconImg2);
	}

	@Override
	public void initData() {
		// 得到 从FriendAcivity 中传递过来的数据
		friendPeopleName = getIntent().getStringExtra("findPeople");

		if (isConnect) {
			moTransport = connectServer.getMoTransport();
		}
		if (friendPeopleName != null) {
			showName.setText(friendPeopleName);
			if (ch_checkBox) {
				textView_1.setText("达尔文在寻找" + friendPeopleName + "请....");
				if (friendPeopleName.equals("黄莹")) {
					iv_icon.setImageResource(R.drawable.huangying2);
				} else {
					iv_icon.setImageResource(R.drawable.songshuang2);
				}

			} else if (en_checkBox) {
				if (friendPeopleName.equals("Mother")) {
					iv_icon.setImageResource(R.drawable.pic_woman_cn);
				} else {
					iv_icon.setImageResource(R.drawable.pic_man_cn);
				}
				textView_1.setText("Lewis is finding " + friendPeopleName
						+ ".......");
			}
		}
		msgToServerHelp = new ServerUtils(isConnect, moTransport);
		animationClass = new AnimationTools(iv_iconImg, iv_iconImg1,
				iv_iconImg2);
		animationClass.startAnimation_1();
		animationClass.setAnimationEndListenr(new AnimationEndListenr() {

			@Override
			public void endAnimation() {
				// TODO Auto-generated method stub
				animationClass.startAnimation_1();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		animationClass.cancelAnimation();
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.myImageButton:
			// 通知机器人找人结束了
			if (ch_checkBox) {
				msgToServerHelp.sendMsgToServer("11", "1", "35", "1");
			} else if (en_checkBox) {
				msgToServerHelp.sendMsgToServer("21", "2", "35", "1");
			}
			finish();
			break;
		}
	}

	@Override
	public void setResultInteger(int result, int flag) {
		// TODO Auto-generated method stub
		switch (result) {
		case 1:
			// 正在找人.

			timeS++;
			if (timeS > 59) {
				timeS = 0;
				timeM += 1;
			}
			findingTime.setText("0" + timeM + ":"
					+ (timeS < 10 ? "0" + timeS : timeS));
			break;
		case 2:
			// 找到人.
			MToast.showToast(this, "找到人了", 0);
			callOut();
			break;
		case 3:
			// 家中没有人
			showToast(result);
			break;
		case 4:
			// 没有找到人
			showToast(result);
			break;
		case 5:
			// 表示错误信息
			showToast(result);
			break;
		case 6:
			// 接到这个消息的话,直接拨通电话
			//UserPreferences.setInt("value", 1);
			Toast.makeText(this, "找到人了开始拨通电话", Toast.LENGTH_SHORT).show();
			if (ch_checkBox) {
				msgToServerHelp.sendMsgToServer("11", "1", "25", "0", "6");
			} else if (en_checkBox) {
				msgToServerHelp.sendMsgToServer("21", "2", "25", "0", "6");
			}
			callOut();
			break;
		case 7:
			showToast(result);
			break;
		case 9:

			break;
		}
	}

	protected void showToast(int result) {
		// TODO Auto-generated method stub
		switch (result) {
		case 3:
			Toast.makeText(this, "家中没有人", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 4:
			Toast.makeText(this, "没有找到人", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 5:
			Toast.makeText(this, "错误信息", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 7:
			Toast.makeText(this, "对方主动停止找人", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
	}

	public void callOut() {
		// SystemClock.sleep(1000);
		// WeaverService.getInstance().dispatchRequest(
		// WeaverAPI.sipMakeCall("18911518445", false, null));
		// finish();
	}

}
