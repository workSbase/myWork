package com.lenovo.lenovorobotmobile.adapter;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.bin.RobotFriendBin.FriendRobotList;
import com.lenovo.lenovorobotmobile.utils.BitmapTools;
import com.lenovo.lenovorobotmobile.utils.SharedpreferencesUilts;
import com.lenovo.lenovorobotmobile.view.RoundImageView;

@SuppressLint("UseSparseArrays")
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private TextView textView1Name;
	// 好友列表
	private ArrayList<FriendRobotList> robotFriendList;
	private TextView textViewName;
	// 好友是否在线
	private boolean friendIsOnLin;
	private RoundImageView friendPicture;
	// 主要是用来存放 mRoundImageView 的
	private Map<Integer, RoundImageView> mapRoundImageView = new HashMap<Integer, RoundImageView>();
	private Typeface typeface;
	private ImageView pic_down;
	private View viewButton1;
	private SharedpreferencesUilts sharedpreferencesUilts;
	private boolean ch_checkBox;
	private boolean en_checkBox;

	/**
	 * 设置好友列表
	 * 
	 * @param robotFriendList2
	 */

	public void setFriendList(ArrayList<FriendRobotList> robotFriendList2) {

		this.robotFriendList = robotFriendList2;
		// 刷新 View
		notifyDataSetChanged();
	}

	/**
	 * 好友是否在线
	 * 
	 * @param friendIsOnLin
	 */
	public void setFriendIsOnlin(boolean friendIsOnLin) {
		this.friendIsOnLin = friendIsOnLin;
		// 刷新 View
		notifyDataSetChanged();
	}

	/**
	 * 初始化字体
	 * 
	 * @param typeface
	 */
	public void setTypeface(Typeface typeface) {
		this.typeface = typeface;
	}

	public MyExpandableListViewAdapter(Context context) {
		this.context = context;

		sharedpreferencesUilts = new SharedpreferencesUilts(context);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return 3;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if (robotFriendList != null && robotFriendList.size() > 0) {
			return robotFriendList.size();
		}
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 加载父View
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = View.inflate(context, R.layout.groupview_item, null);
			textViewName = (TextView) view.findViewById(R.id.textView1);
			textViewName.setTypeface(typeface);
			view.setTag(textViewName);
			friendPicture = (RoundImageView) view
					.findViewById(R.id.imageView1_1);
			pic_down = (ImageView) view.findViewById(R.id.pic_down);
			mapRoundImageView.put(groupPosition, friendPicture);
		}
		if (isExpanded) {
			pic_down.setVisibility(View.VISIBLE);
			textViewName.setTextColor(Color.rgb(243, 131, 9));
		} else {
			pic_down.setVisibility(View.INVISIBLE);
			textViewName.setTextColor(Color.rgb(255, 255, 255));
		}
		RoundImageView mRoundImageView = mapRoundImageView.get(groupPosition);
		// 判断好友是否在线
		if (!friendIsOnLin) {
			mRoundImageView
					.setImageBitmap(BitmapTools.grey(BitmapFactory
							.decodeResource(context.getResources(),
									R.drawable.pic_001)));
		} else {
			mRoundImageView.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.pic_001));
		}
		TextView text = (TextView) view.getTag();
		text.setText(robotFriendList.get(groupPosition).robotFriendName);
		return view;
	}

	// 加载子View
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view;
		// if (convertView != null) {
		// view = convertView;
		// } else {
		view = View.inflate(context, R.layout.childview_item, null);
		textView1Name = (TextView) view.findViewById(R.id.textView1_1);
		textView1Name.setTypeface(typeface);
		viewButton1 = view.findViewById(R.id.button1);

		// Button button1 = (Button) view.findViewById(R.id.button_1);
		// Button button2 = (Button) view.findViewById(R.id.button_2);
		// mapTextView.put(childPosition, textView1Name);
		// }
		// TextView textViewName = mapTextView.get(childPosition);
		if (childPosition == 0) {
			if (ch_checkBox) {
				textView1Name.setText("视频通话");
			} else if (en_checkBox) {
				textView1Name.setText("Video Call");
			}
			textView1Name.setTextColor(Color.argb(153, 255, 255, 255));
		} else if (childPosition == 1) {
			if (ch_checkBox) {
				textView1Name.setText("监控");
			} else if (en_checkBox) {
				textView1Name.setText("Contorl");
			}
			textView1Name.setTextColor(Color.argb(153, 255, 255, 255));
			viewButton1.setBackgroundResource(R.drawable.icon_monitor);

		} else if (childPosition == 2) {
			if (ch_checkBox) {
				textView1Name.setText("手动建图");
			} else if (en_checkBox) {
				textView1Name.setText("Create Map");
			}
		}

		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}
