package com.lenovo.lenovorobotmobile.activity;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.adapter.MyExpandableListViewAdapter;
import com.lenovo.lenovorobotmobile.bin.RobotFriendBin;
import com.lenovo.lenovorobotmobile.bin.RobotFriendBin.FriendRobotList;
import com.lenovo.lenovorobotmobile.utils.MToast;
import com.lenovo.lenovorobotmobile.utils.StringUtils;

public class FriendActivity extends BaseActivity implements
		OnChildClickListener {
	protected static final String TAG = "FriendActivity";

	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";

	private ExpandableListView myExpandableListView;
	private TextView textView1_1;
	// 好友列表的集合
	private ArrayList<FriendRobotList> robotFriendList;
	// 定时器对象
	private Timer timer = null;
	private TimerTask timerTask;
	private MyExpandableListViewAdapter adapter;
	// private ImageView userStatsFlagImageView;
	public static boolean isSendPicture = true;
	public static boolean isChOnlin;
	public static boolean isEnOnlin;

	public static boolean getisChOnlin() {
		return isChOnlin;
	}

	public static boolean getisEhOnlin() {
		return isEnOnlin;
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub

		// 初始化定时器
		timer = new Timer();

		setContentView(R.layout.friendactivityview);

		myExpandableListView = (ExpandableListView) findViewById(R.id.myExpandableListView);
		// 用户是否在线状态 图标
		// userStatsFlagImageView = (ImageView)
		// findViewById(R.id.userStatsFlagImageView);
		TextView titleid = (TextView) findViewById(R.id.titleid);
		if (en_checkBox) {
			titleid.setText("Contact");
		}
		titleid.setTypeface(typeface);

		// 初始化机器人好友
		// robotFriendList = new ArrayList<RobotFriendBin>();
		initRobotFriend();
	}

	@Override
	public void initData() {
		// 得到 ExpandableListView
		// connectServer.registerObserver(this);

		myExpandableListView.setGroupIndicator(null);
		adapter = new MyExpandableListViewAdapter(this);
		adapter.setTypeface(typeface);
		// 设置 adapter
		myExpandableListView.setAdapter(adapter);
		// 设置数据
		adapter.setFriendList(robotFriendList);
		// 设置回调
		myExpandableListView.setOnChildClickListener(this);

		timerTask = new TimerTask() {
			@Override
			public void run() {
				// 向服务器发送心跳数据,
				if (isConnect) {
					if (ch_checkBox) {
						FriendActivity.this.msgToServerHelp.sendMsgToServer(
								"11", "0", "7", "1");
						Log.i(TAG, "中文 心跳");
					} else if (en_checkBox) {
						Log.i(TAG, "英文 心跳");
						FriendActivity.this.msgToServerHelp.sendMsgToServer(
								"21", "0", "7", "1");
					}
				}
			}
		};
		if (isConnect) {
			timer.schedule(timerTask, 0, 1000);
		} else {
			MToast.showToast(this, "FriendActivity服务器连接失败", 0);
		}

		// boolean userIsOnLin = UserPreferences.getBool("userIsOnLin", false);
		// if (userIsOnLin) {
		// adapter.setFriendIsOnlin(true);
		// }
	}

	private void initRobotFriend() {
		// TODO Auto-generated method stub
		InputStream open = null;
		try {
			if (ch_checkBox) {

				open = this.getAssets().open("robotfriendlist");
			} else {
				open = this.getAssets().open("robotfriendlist_en");
			}
			String textFromStream = StringUtils.getTextFromStream(open);
			RobotFriendBin fromJson = gson.fromJson(textFromStream,
					RobotFriendBin.class);
			robotFriendList = fromJson.friendRobotList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer = null;

		finish();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		if (childPosition == 0) {
			RelativeLayout relativeLayout1 = (RelativeLayout) v
					.findViewById(R.id.relativeLayout1_1);
			relativeLayout1.setVisibility(View.VISIBLE);
			Button button_1 = (Button) relativeLayout1
					.findViewById(R.id.button_1);

			button_1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 直接拨通视频电话
					// if (isChOnlin) {
					// UserPreferences.setInt("value", 10/* 单击的时候直接拨打视频 */);
					callOut(1);
					// } else {
					// MToast.showToast(FriendActivity.this, "对不起对方不在线,请稍后", 0);
					// }
				}
			});
			button_1.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// UserPreferences.setInt("value", 1);
					// 通知机器人端开始找人.
					if (ch_checkBox) {
						if (isChOnlin) {
							msgToServerHelp.sendMsgToServer("11", "1", "25",
									"0", "1");
							speachPople("宋爽");
						} else {
							MToast.showToast(FriendActivity.this,
									"对不起对方不在线,请稍后", 0);
						}
					} else if (en_checkBox) {
						if (isEnOnlin) {
							msgToServerHelp.sendMsgToServer("21", "2", "25",
									"0", "1");
							speachPople("Father");
						} else {
							MToast.showToast(FriendActivity.this,
									"对不起对方不在线,请稍后", 0);
						}
					}
					return true;
				}
			});
			Button button_2 = (Button) relativeLayout1
					.findViewById(R.id.button_2);
			button_2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// if (isChOnlin) {
					// UserPreferences.setInt("value", 10/* 单击的时候直接拨打视频 */);
					callOut(1);
					// } else {
					// MToast.showToast(FriendActivity.this, "对不起对方不在线,请稍后", 0);
					// }
				}
			});

			button_2.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// UserPreferences.setInt("value", 1);
					// 通知机器人端开始找人.
					if (ch_checkBox) {
						if (isChOnlin) {
							msgToServerHelp.sendMsgToServer("11", "1", "25",
									"0", "2");
							speachPople("黄莹");
						} else {
							MToast.showToast(FriendActivity.this,
									"对不起对方不在线,请稍后", 0);
						}
					} else if (en_checkBox) {
						if (isEnOnlin) {
							msgToServerHelp.sendMsgToServer("21", "2", "25",
									"0", "2");
							speachPople("Mother");
						} else {
							MToast.showToast(FriendActivity.this,
									"对不起对方不在线,请稍后", 0);
						}
					}
					return true;
				}
			});

			textView1_1 = (TextView) v.findViewById(R.id.textView1_1);
			textView1_1.setVisibility(View.INVISIBLE);

		} else if (childPosition == 1) {
			// 跳转到语音控制界面
			Intent intent = new Intent(FriendActivity.this, ChatsActivity.class);
			// TODO Auto-generated method stub
			startActivity(intent);
		} else if (childPosition == 2) {
			// UserPreferences.setInt("value", -1);
			callOut(1);
		}
		return true;
	}

	// 跳转到找人界面上面
	protected void speachPople(String string) {
		Intent intent = new Intent(this, FindingPeopleActivity.class);
		intent.putExtra("findPeople", string);
		startActivity(intent);
	}

	@Override
	public void setServerResult(String mFriendIsOnline, int flag) {
		// TODO Auto-generated method stub
		if (flag == 0) {
			if (mFriendIsOnline != null && mFriendIsOnline.equals("1")
					&& !mFriendIsOnline.equals("")) {
				adapter.setFriendIsOnlin(true);
				// 表示当前是中文好友在线.
				if (ch_checkBox) {
					isChOnlin = true;
					isEnOnlin = false;
					// UserPreferences.setBool("userIsOnLin", true);
				}
			} else if (mFriendIsOnline != null && mFriendIsOnline.equals("2")
					&& !mFriendIsOnline.equals("")) {
				adapter.setFriendIsOnlin(true);
				if (en_checkBox) {
					isEnOnlin = true;
					isChOnlin = false;
					// UserPreferences.setBool("userIsOnLin", true);
				}
			} else if (mFriendIsOnline == null) {
				adapter.setFriendIsOnlin(false);
				isEnOnlin = false;
				isChOnlin = false;
				// 表示当前是中文好友在线.
				if (ch_checkBox) {
					MToast.showToast(FriendActivity.this, "当前中文机器人好友下线", 0);
				} else if (en_checkBox) {
					MToast.showToast(FriendActivity.this, "当前英文机器人好友下线", 0);
				}
				// UserPreferences.setBool("userIsOnLin", false);
			}
		}
	}

	@Override
	protected void onRestart() {
		if (timer == null && robotFriendList == null) {
			timer = new Timer();
		}
		initData();

		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// 开始手动建图
		msgToServerHelp.sendMsgToServer("21", "2", "40", "1");
		super.onDestroy();
	}

	// 用户是否在线回调
	// @Override
	// public void setUserStatsNumber(int statesNumber) {
	// // TODO Auto-generated method stub
	// if (statesNumber == 1) {
	// // 表示在线
	// userStatsFlagImageView.setImageResource(R.drawable.link_offline_);
	// } else if (statesNumber == 0) {
	// // 表示不在线状态
	// userStatsFlagImageView.setImageResource(R.drawable.link_offline);
	// }
	// }

}
