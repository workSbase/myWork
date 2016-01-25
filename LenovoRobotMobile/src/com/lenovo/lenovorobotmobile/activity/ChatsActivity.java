package com.lenovo.lenovorobotmobile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.*;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.activityVideo.CameraActivity;
import com.lenovo.lenovorobotmobile.activityVideo.TesterApplication;
import com.lenovo.lenovorobotmobile.speech.LenovoSpeechRecognizer;
import com.lenovo.lenovorobotmobile.speech.LenovoSpeechRecognizer_en;
import com.lenovo.lenovorobotmobile.speech.Message;
import com.lenovo.lenovorobotmobile.speech.Schedule;
import com.lenovo.lenovorobotmobile.speech.TextPaser;
import com.lenovo.lenovorobotmobile.utils.ChatsContentShowTools;
import com.lenovo.lenovorobotmobile.utils.ChineseUtils;
import com.lenovo.lenovorobotmobile.utils.EnglishUtils;
import com.lenovo.lenovorobotmobile.utils.RobotUtil;
import com.lenovo.lenovorobotmobile.view.NewMapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatsActivity extends BaseActivity implements OnTouchListener {
	private ChatsContentShowTools layout;
	private Button sendMsgBt;
	private Button thouchSpeakBt;
	private Button chatActivityToControl;
	private LinearLayout showContentView;
	private EditText contentShowEditText;
	private String contentText;
	private ScrollView myScrollView;

	private ImageView userIsOnlinImageView;
	private LenovoSpeechRecognizer lenovoSpeechRecognizer;
	private TextPaser textPaser;
	private LenovoSpeechRecognizer_en lenovoSpeechRecognizer_en;
	private ChineseUtils chineseUtils = null;
	private EnglishUtils englishUtils = null;
	public static Schedule schedule;
	private Button goHome;
	private Button weiZhi;

	public void initLayout() {
		// TODO Auto-generated method stub

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.speachitem);

		// 显示内容过多的时候,可以上下滚动
		myScrollView = (ScrollView) findViewById(R.id.myScrollView);
		// 所有聊天内容都放在showContentView这个layout中来进行显示
		showContentView = (LinearLayout) findViewById(R.id.showContentView);
		layout = new ChatsContentShowTools(this, showContentView, myScrollView);
		layout.setTypeface(typeface);
		// 发送按钮
		sendMsgBt = (Button) findViewById(R.id.btn_send);
		sendMsgBt.setTypeface(typeface);
		if (ch_checkBox) {
			sendMsgBt.setText("发送");
		} else if (en_checkBox) {
			sendMsgBt.setText("send");
		}
		sendMsgBt.setOnClickListener(this);

		// 按住说话的按钮
		thouchSpeakBt = (Button) findViewById(R.id.downBt);
		thouchSpeakBt.setOnTouchListener(this);

		// 语音控制界面跳转到视频控制界面
		chatActivityToControl = (Button) findViewById(R.id.btn_back);
		chatActivityToControl.setOnClickListener(this);

		// 自己的控制界面
		Button myVidoButton = (Button) findViewById(R.id.myVidoButton);
		myVidoButton.setOnClickListener(this);

		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setTypeface(typeface);
		if (ch_checkBox) {
			tv_title.setText("语音控制");
		} else if (en_checkBox) {
			tv_title.setText("Voice Control");
		}
		TextView tv_speak = (TextView) findViewById(R.id.tv_speak);
		tv_speak.setTypeface(typeface);
		if (ch_checkBox) {
			tv_speak.setText("按住说话");
		} else if (en_checkBox) {
			tv_speak.setText("SPEAK");
		}

		// 显示内容的 EditText SPEAK
		contentShowEditText = (EditText) findViewById(R.id.et_sendmessage);

		// 用户是否可用
		userIsOnlinImageView = (ImageView) findViewById(R.id.userIsOnlinImageView);

		goHome = (Button) findViewById(R.id.goHome);
		goHome.setOnClickListener(this);

		weiZhi = (Button) findViewById(R.id.weiZhi);
		weiZhi.setOnClickListener(this);
	}

	public void initData() {
		if (ch_checkBox) {
			// 初始化语音合成对象
			lenovoSpeechRecognizer = new LenovoSpeechRecognizer(this) {
				@Override
				public void resultText(String string) {
					// TODO Auto-generated method stub
					int length = string.length();
					contentShowEditText.setTypeface(typeface);
					if (string.substring(0, length - 1).contains("今天")
							|| string.substring(0, length - 1).contains("客人")
							|| string.substring(0, length - 1).contains("吃饭")) {
						// String substring = string.substring(0, length - 1);
						// String regEx = "[^0-9]";
						// Pattern p = Pattern.compile(regEx);
						// Matcher m = p.matcher(substring);
						// String trim = m.replaceAll("").trim();

						contentShowEditText.setText("今天" + setTimeData()
								+ "带个重要客人回家吃饭");
						// if (trim.length() == 4) {
						// contentShowEditText.setText("今天上午"
						// + trim.substring(0, 2) + ":"
						// + trim.substring(2, trim.length())
						// + "带个重要客人回家吃饭");
						// } else {
						// contentShowEditText.setText("今天上午"
						// + trim.substring(0, 1) + ":"
						// + trim.substring(1, trim.length())
						// + "带个重要客人回家吃饭");
						// }
						// if(){
						// }
					} else {
						contentShowEditText.setText(string.substring(0,
								length - 1));
					}
				}
			};
			// 初始化 帧号合成对象
			textPaser = new TextPaser(this);
		} else if (en_checkBox) {
			lenovoSpeechRecognizer_en = new LenovoSpeechRecognizer_en(this) {
				@Override
				public void resultText(String string) {
					// TODO Auto-generated method stub
					int length = string.length();
					contentShowEditText.setTypeface(typeface);
					contentShowEditText
							.setText(string.substring(0, length - 1));
				}
			};
		}

		if (chineseUtils == null) {
			chineseUtils = new ChineseUtils(this, moTransport, layout,
					textPaser, contentShowEditText, msgToServerHelp);
		}
		if (englishUtils == null) {

			englishUtils = new EnglishUtils(this, moTransport, layout,
					textPaser, contentShowEditText, msgToServerHelp);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO
		int id = v.getId();
		switch (id) {
		// 发送按钮
		case R.id.btn_send:
			// EditText 中的内容
			contentText = contentShowEditText.getText().toString();
			if (ch_checkBox) {
				chineseUtils.disposeMsg(contentText);
			} else if (en_checkBox) {
				englishUtils.disposeMsg(contentText);
			}
			break;
		// 跳转到控制界面的按钮
		case R.id.btn_back:
			// if (ch_checkBox) {
			// if (FriendActivity.getisChOnlin()) {
			// // UserPreferences.setInt("value", 2);
			msgToServerHelp.sendMsgToServer("11", "1", "25", "1", "0");
			// callOut();
			// } else {
			// MToast.showToast(ChatsActivity.this, "对方不在线,请稍后", 0);
			// }
			// } else if (en_checkBox) {
			// if (FriendActivity.getisEhOnlin()) {
			// // UserPreferences.setInt("value", 2);
			// msgToServerHelp.sendMsgToServer("21", "2", "25", "1", "0");
			// callOut();
			// } else {
			// MToast.showToast(ChatsActivity.this, "对方不在线,请稍后", 0);
			// }
			// }
			// 保存一下当前是,控制还是视频通话,这个保存的字段用来控制,camera 界面显示不同的效果
			// startActivity(new Intent(this, CameraActivity.class));
			callOut();
			break;
		case R.id.myVidoButton:
			// if (FriendActivity.getisChOnlin() ||
			// FriendActivity.getisEhOnlin()) {
			// 跳转到自己的activity,自己写的控制界面,没有加上友约的视频
			Intent intent = new Intent(this, MContorlActivityTest.class);
			startActivity(intent);
			// } else {
			// MToast.showToast(ChatsActivity.this, "对方不在线,请稍后", 0);
			// }
			break;
		// case R.id.goHome:
		// String setTimeData = setTimeData();
		// Toast.makeText(ChatsActivity.this, setTimeData, Toast.LENGTH_SHORT)
		// .show();
		// String str = "{robotid:" + "1," + "intent:" + "0," + "text:"
		// + "今天上午"
		// + setTimeData
		// + "带个重要客人回家吃饭,"
		// + "period:"
		// + "0,"
		// + "date:"
		// + "2015-9-13,"
		// + "time:"
		// + setTimeData
		// + ","
		// + "person:"
		// + "客人,"
		// + "reminderText:"
		// + "回家吃饭,"
		// + "during hour:"
		// + "-1,"
		// + "device:"
		// + ""
		// + ","
		// + "action:"
		// + "-1,"
		// + "condition:" + "-1}";
		// com.lenovo.lenovorobotmobile.speech.Intent intent2 = new
		// com.lenovo.lenovorobotmobile.speech.Intent();
		// String count = intent2.makeJson("1", "0", "今天上午" + setTimeData
		// + "带个重要客人回家吃饭", "0", "2015-9-13", setTimeData, null, "客人",
		// "回家吃饭", null, null, null, null, null);
		// msgToServerHelp.sendMsgToServer("11", "0", "9", count);
		// // goHome.setVisibility(View.GONE);
		// break;
		case R.id.weiZhi:
			weiZhi.setVisibility(View.GONE);

			msgToServerHelp.sendMsgToServer("11", "1", "20", "1",
					RobotUtil.yyToPixCoordX(NewMapView.mCurPose1.x) + "",
					RobotUtil.yyToPixCoordY(NewMapView.mCurPose1.y) + "",
					3.1415 + "");
			break;
		}
	}

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";
	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	// 跳转到视频界面
	private void callOut() {

		((TesterApplication) getApplication()).setVideoState(2);

		((TesterApplication) getApplication())
				.setRtcEngine(((TesterApplication) getApplication())
						.getVendorKey());
		Intent toChannel = new Intent(this, CameraActivity.class);
		toChannel.putExtra(ChatsActivity.EXTRA_TYPE,
				ChatsActivity.CALLING_TYPE_VIDEO);
		toChannel.putExtra(ChatsActivity.EXTRA_CHANNEL, "1");
		startActivity(toChannel);
	}

	// 按住说的按钮,要有两种事件一是按下,二是抬起
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (ch_checkBox) {
				// 拿到录音对象开始录音了
				lenovoSpeechRecognizer.start();
			} else if (en_checkBox) {
				lenovoSpeechRecognizer_en.start();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (ch_checkBox) {
				// 释放录音对像把录取到的声音给捕获显示出来
				lenovoSpeechRecognizer.cancel();
			} else if (en_checkBox) {
				lenovoSpeechRecognizer_en.cancel();
			}
			break;
		}
		return false;
	}

	@Override
	public void setServerResult(String resut, int flag) {
		// TODO Auto-generated method stub
		if (ch_checkBox) {
			switch (flag) {
			case 1:
				String[] split = resut.split(",");
				layout.addLiftView(
						(((int) (Float.parseFloat(split[0]) * 100)) + "%"),
						false, 0);
				break;
			case 3:
				// 获取当前 日程安排
				schedule = gson.fromJson(resut, Schedule.class);
				layout.addLiftView(getScheduleText(schedule), false, 0);

				new Message().setScheduleJson(resut);

				break;
			}
		} else if (en_checkBox) {
			switch (flag) {
			case 1:
				String[] split = resut.split(",");
				layout.addLiftView(
						(((int) (Float.parseFloat(split[0]) * 100)) + "%"),
						false, 0);
				break;
			case 3:
				// 获取当前 日程安排
				schedule = gson.fromJson(resut, Schedule.class);

				layout.addLiftView(getScheduleText(schedule), false, 0);

				new Message().setScheduleJson(resut);

				break;

			}
		}
	}

	@Override
	public void setResultInteger(int result, int flag) {
		// TODO Auto-generated method stub
		if (ch_checkBox) {
			if (flag == 0) {
				if (result == 9) {
					layout.addLiftView("日程添加成功", false, 0);
				} else if (result == 16) {
					layout.addLiftView("日程删除成功", false, 0);
				}
			} else if (flag == 1) {
				layout.addLiftView((result + "%"), false, 0);
			}
		} else if (en_checkBox) {
			if (flag == 0) {
				if (result == 9) {
					layout.addLiftView("succeed", false, 0);
				} else if (result == 16) {
					layout.addLiftView("delete succeed", false, 0);
				}
			} else if (flag == 1) {
				layout.addLiftView((result + "%"), false, 0);
			}
		}
	}

	public String getScheduleText(Schedule fromJson) {
		StringBuffer str = new StringBuffer();
		if (ch_checkBox) {
			if (fromJson != null) {
				ArrayList<String> texts = fromJson.getTexts();
				if (texts.size() < 1) {
					return "当前没有日程安排";
				}
				for (int i = 0; i < texts.size(); i++) {

					str.append((i + 1) + " : " + texts.get(i) + "\n");
				}
			}
		} else if (en_checkBox) {
			if (fromJson != null) {
				ArrayList<String> texts = fromJson.getTexts();
				if (texts.size() < 1) {
					return "Currently there is no schedule";
				}
				for (int i = 0; i < texts.size(); i++) {

					str.append((i + 1) + " : " + texts.get(i) + "\n");
				}
			}
		}
		return str.toString();
	}

	// 用户在线状态的回调
	@Override
	public void setUserStatsNumber(int statesNumber) {
		// TODO Auto-generated method stub
		if (statesNumber == 1) {
			// 表示用户状态可用
			userIsOnlinImageView.setImageResource(R.drawable.link_offline_);
		} else if (statesNumber == 0) {
			// 表示用户状态不可用
			userIsOnlinImageView.setImageResource(R.drawable.link_offline);
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		// initData();
		connectServer.registerObserver(this);
		super.onRestart();
	}

	@Override
	protected void onStop() {
		connectServer.removeObserver(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (chineseUtils != null || englishUtils != null) {
			chineseUtils = null;
			englishUtils = null;
		}
		super.onDestroy();
	}

	private String setTimeData() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		if (dateFormat.format(new Date(date.getTime() + 5 * 60 * 1000)) != null) {
			return dateFormat.format(new Date(date.getTime() + 5 * 60 * 1000));
		}
		return null;
	}
}
