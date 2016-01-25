package cn.com.lenovo.homepager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import android.widget.Toast;

public class MainActivity extends HomePageBaseActivity implements
		OnClickListener {

	// 表情图片
	private ImageView im_face;
	// 中英文切换
	private ImageView userChange;
	//
	private SharedPreferences sharedPreferences;
	// 控件声明
	private Button bt_video_call;
	private Button bt_chats;
	private Button bt_video_player;
	private Button bt_health;
	private TextView tv_title;
	private TextView tv_video_call;
	private TextView tv_chats;
	private TextView tv_video_player;
	private TextView tv_health;
	private TextView tv_time1;
	private TextView tv_time2;

	private FaceBroadcastReceiver broadcastReceiver;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int a = msg.what;
			switch (a) {
			case 0:
				// 显示表情
				im_face.setVisibility(View.VISIBLE);
				rl_face_bg.setVisibility(View.VISIBLE);
				sendFaceState(1);
				// 闭眼
				setFace(1);
				// 设置按钮是不可以点击的
				bt_video_call.setVisibility(View.GONE);
				bt_chats.setVisibility(View.GONE);
				bt_video_player.setVisibility(View.GONE);
				bt_health.setVisibility(View.GONE);
				userChange.setVisibility(View.GONE);
				break;
			case 1:
				if (im_face.getVisibility() == View.VISIBLE) {
					// 闭眼
					setFace(1);
				}
				break;
			default:
				break;
			}
		}
	};
	private RelativeLayout rl_face_bg;
	private ImageView imageView2;
	private ImageView imageView4;
	private CountDownTimer countDownTimer;
	private CountDownTimer countDownTimer_1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 保存中英文用户状态
		sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		// 中英文用户切换
		userChange = (ImageView) findViewById(R.id.userChange);
		userChange.setOnClickListener(this);
		// 视频通话
		bt_video_call = (Button) findViewById(R.id.bt_video_call);
		bt_video_call.setOnClickListener(this);
		// 聊天
		bt_chats = (Button) findViewById(R.id.bt_chats);
		bt_chats.setOnClickListener(this);

		// 视频播放
		bt_video_player = (Button) findViewById(R.id.bt_video_player);
		bt_video_player.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置重新倒计时
				countDownTimer.start();
				// 打开视频播放器
				startActivity(getPackageManager().getLaunchIntentForPackage(
						"cn.com.lenovo.videoplayer"));
			}
		});
		bt_video_player.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// 设置重新倒计时
				countDownTimer.start();
				// 走到指定地点开启投影播放视频
				Intent intent = new Intent();
				intent.setAction("projection");
				sendBroadcast(intent);
				// Toast.makeText(MainActivity.this, "发送了Action为projection的广播",
				// Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		// 健康App
		bt_health = (Button) findViewById(R.id.bt_health);
		bt_health.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置重新倒计时
				countDownTimer.start();
				if (sharedPreferences.getString("LANGUAGE", "CHINA").equals(
						"CHINA")) {
					sendBroadCast(2);
				} else {
					sendBroadCast(0);
				}
			}
		});
		bt_health.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// 设置重新倒计时
				countDownTimer.start();
				if (sharedPreferences.getString("LANGUAGE", "CHINA").equals(
						"CHINA")) {
					sendBroadCast(3);
				} else {

				}
				return true;
			}
		});

		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView4 = (ImageView) findViewById(R.id.imageView4);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/lenovo_font.ttf");
		// 设置文字
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setTypeface(typeface);
		tv_video_call = (TextView) findViewById(R.id.tv_video_call);
		tv_video_call.setTypeface(typeface);
		tv_chats = (TextView) findViewById(R.id.tv_chats);
		tv_chats.setTypeface(typeface);
		tv_video_player = (TextView) findViewById(R.id.tv_video_player);
		tv_video_player.setTypeface(typeface);
		tv_health = (TextView) findViewById(R.id.tv_health);
		tv_health.setTypeface(typeface);
		tv_time1 = (TextView) findViewById(R.id.tv_time1);
		tv_time1.setTypeface(typeface);
		tv_time2 = (TextView) findViewById(R.id.tv_time2);
		tv_time2.setTypeface(typeface);

		im_face = (ImageView) findViewById(R.id.im_face);
		rl_face_bg = (RelativeLayout) findViewById(R.id.rl_face_bg);

		// 中英文用户回显
		if (sharedPreferences.getString("LANGUAGE", "CHINA").equals("CHINA")) {
			// 显示中文
			userChange.setBackgroundResource(R.drawable.selecter_user_cn);
			// 中文用户登录
			cutUser("CHINA", "18616609483", "123456");
		} else {
			// 显示英文
			userChange.setBackgroundResource(R.drawable.selecter_user_us);
			// 英文用户登录
			cutUser("ENGLISH", "18616609483", "123456");
		}

		broadcastReceiver = new FaceBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("cn.com.lenovo.homepager");
		registerReceiver(broadcastReceiver, filter);

		// 显示表情的倒计时
		countDownTimer = new CountDownTimer(10000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// tv_time1.setText("显示表情的倒计时: " + millisUntilFinished / 1000);
			}

			@Override
			public void onFinish() {
				// tv_time1.setText("显示表情的倒计时: 0");
				Message msg = Message.obtain();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		};

		// 切换表情的倒计时
		countDownTimer_1 = new CountDownTimer(5000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// tv_time2.setText("切换表情的倒计时: " + millisUntilFinished / 1000);
			}

			@Override
			public void onFinish() {
				// tv_time2.setText("切换表情的倒计时: 0");
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};

		// 打开LenovoRobotService
		Intent service = new Intent();
		service.setAction("com.lenovo.www");
		startService(service);

		// 打开语音服务
		Intent service_speech = new Intent();
		service_speech.setAction("com.lenovo.speechservice_newplan");
		startService(service_speech);

	}

	@Override
	public void onClick(View v) {
		// 设置重新倒计时
		countDownTimer.start();
		switch (v.getId()) {
		case R.id.bt_video_call:
			// 视频通话
			startActivity(getPackageManager().getLaunchIntentForPackage(
					"com.lenovo.contactslist"));
			break;
		case R.id.bt_chats:
			if (sharedPreferences.getString("LANGUAGE", "CHINA")
					.equals("CHINA")) {

				startActivity(getPackageManager().getLaunchIntentForPackage(
						"com.lenovo.recordvideo"));

				// startActivity(getPackageManager().getLaunchIntentForPackage(
				// "cn.com.lenovo.darwin"));
				/**
				 * 这个地方是才加上去的,点击切换到达尔文聊天界面的时候,通知底层的 service 不在获取数据
				 */
				sendFaceState(0);

			} else {
				sendBroadCast(1);
			}
			break;
		case R.id.userChange:
			// 中英文用户切换
			if (sharedPreferences.getString("LANGUAGE", "CHINA")
					.equals("CHINA")) {
				// 保存英文
				sharedPreferences.edit().putString("LANGUAGE", "ENGLISH")
						.commit();
				// 显示英文
				userChange.setBackgroundResource(R.drawable.selecter_user_us);
				// 将语音服务切换至英文
				sendEnglish();
				// 将用户切换至英文
				cutUser("ENGLISH", "18616609483", "123456");
			} else {
				// 保存中文
				sharedPreferences.edit().putString("LANGUAGE", "CHINA")
						.commit();
				// 显示中文
				userChange.setBackgroundResource(R.drawable.selecter_user_cn);
				// 将语音服务切换至中文
				sendChina();
				// 将用户切换至中文
				cutUser("CHINA", "18616609483", "123456");
			}
			// 初始化语音
			initLanguage();
			break;
		default:
			break;
		}
	}

	/**
	 * 切换中英文用户
	 */
	private void cutUser(String country, String userName, String userPassworld) {
		Intent cut_user_intent = new Intent();
		cut_user_intent.putExtra("flag", country);
		cut_user_intent.putExtra("userName", userName);
		cut_user_intent.putExtra("userPassworld", userPassworld);
		cut_user_intent.setAction("userChange");
		sendBroadcast(cut_user_intent);
	}

	/**
	 * 切换中英文语音
	 */
	private void sendEnglish() {
		// 发送切换成英文识别的广播
		Intent stotSpeechService_intent = new Intent();
		stotSpeechService_intent.setAction("cn.com.lenovo.speechservice");
		stotSpeechService_intent.putExtra("speechService_language_us", true);
		sendBroadcast(stotSpeechService_intent);
	}

	/**
	 * 切换中英文语音
	 */
	private void sendChina() {
		// 发送切换成中文识别的广播
		Intent stotSpeechService_intent = new Intent();
		stotSpeechService_intent.setAction("cn.com.lenovo.speechservice");
		stotSpeechService_intent.putExtra("speechService_language_cn", true);
		sendBroadcast(stotSpeechService_intent);
	}

	/**
	 * 屏幕被点击
	 */
	@Override
	public void singleTapUp() {
		countDownTimer.start();
		if (im_face.getVisibility() == View.VISIBLE) {
			im_face.setVisibility(View.GONE);
			rl_face_bg.setVisibility(View.GONE);
			sendFaceState(0);

			// 设置按钮是可以点击的
			bt_video_call.setVisibility(View.VISIBLE);
			bt_chats.setVisibility(View.VISIBLE);
			bt_video_player.setVisibility(View.VISIBLE);
			bt_health.setVisibility(View.VISIBLE);
			userChange.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化语言
	 */
	private void initLanguage() {
		if (sharedPreferences.getString("LANGUAGE", "CHINA").equals("CHINA")) {
			tv_title.setText("达尔文");
			tv_video_call.setText("视频通话");
			imageView2.setBackgroundResource(R.drawable.icon_chats);
			tv_chats.setText("测量心率");
			tv_video_player.setText("媒体播放器");
			imageView4.setBackgroundResource(R.drawable.height_face);
			tv_health.setText("测身高/人脸识别");
		} else {
			tv_title.setText("Lewis");
			tv_video_call.setText("Video Call");
			imageView2.setBackgroundResource(R.drawable.face_scan);
			tv_chats.setText("Recognize Person");
			tv_video_player.setText("Video Player");
			imageView4.setBackgroundResource(R.drawable.height);
			tv_health.setText("Recognize Height");
		}
	}

	public void setFace(int flag) {
		switch (flag) {
		case 0:
			// 睁眼
			// Toast.makeText(this, "睁眼", Toast.LENGTH_SHORT).show();
			im_face.setImageResource(R.drawable.eyes_open);
			im_face.setVisibility(View.VISIBLE);
			rl_face_bg.setVisibility(View.VISIBLE);
			break;
		case 1:
			// 闭眼
			// Toast.makeText(this, "闭眼", Toast.LENGTH_SHORT).show();
			im_face.setImageResource(R.drawable.eyes_close);
			im_face.setVisibility(View.VISIBLE);
			rl_face_bg.setVisibility(View.VISIBLE);
			break;
		case 2:
			// 闭右眼
			// Toast.makeText(this, "闭右眼", Toast.LENGTH_SHORT).show();
			im_face.setImageResource(R.drawable.eyes_close_right);
			im_face.setVisibility(View.VISIBLE);
			rl_face_bg.setVisibility(View.VISIBLE);
			break;
		case 3:
			// 咧嘴
			// Toast.makeText(this, "闭左眼", Toast.LENGTH_SHORT).show();
			im_face.setImageResource(R.drawable.face1);
			im_face.setVisibility(View.VISIBLE);
			rl_face_bg.setVisibility(View.VISIBLE);
			break;
		case 4:
			// 生气
			// Toast.makeText(this, "生气", Toast.LENGTH_SHORT).show();
			im_face.setImageResource(R.drawable.eyes_angry);
			im_face.setVisibility(View.VISIBLE);
			rl_face_bg.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		countDownTimer_1.start();
		// 设置按钮是不可以点击的
		bt_video_call.setVisibility(View.GONE);
		bt_chats.setVisibility(View.GONE);
		bt_video_player.setVisibility(View.GONE);
		bt_health.setVisibility(View.GONE);
		userChange.setVisibility(View.GONE);
	}

	/**
	 * 禁用返回键
	 */
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 初始化语音
		initLanguage();
		// im_face.setVisibility(View.GONE);
		// // 设置重新倒计时
		// time = RECLEN;

		singleTapUp();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
		}
	}

	public class FaceBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int faceNum = intent.getIntExtra("face", -1);
			switch (faceNum) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				// 重新设置倒计时时间
				// countDownTimer.start();
				if (View.VISIBLE == im_face.getVisibility()) {
					setFace(faceNum);
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 雪成+
	 * 
	 * @param i
	 */
	private void sendBroadCast(int i) {
		Intent recognizePerson = new Intent();
		recognizePerson.setAction("RecognizeDate");
		recognizePerson.putExtra("type", i);
		sendBroadcast(recognizePerson);
		// Toast.makeText(this, "Action : RecognizeDate\nkey : type\nvalue : " +
		// i, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 发送表情显示状态的广播 0 隐藏 1显示
	 * 
	 * @param i
	 */
	private void sendFaceState(int i) {
		Intent faceState = new Intent();
		faceState.setAction("faceState");
		faceState.putExtra("state", i);
		sendBroadcast(faceState);
		// Toast.makeText(this, "Action : faceState\nkey : state\nvalue : " + i,
		// Toast.LENGTH_SHORT).show();
	}
}
