package com.lenovo.lenovorobotmobile.view;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.utils.RobotUtil;
import com.lenovo.lenovorobotmobile.utils.ServerUtils;
import com.lenovo.lenovorobotmobile.utils.SharedpreferencesUilts;
import com.lenovo.lenovorobotmobile.view.ControlOrientationView.TouchLisenter;
import com.lenovo.lenovorobotmobile.view.ControlOrientationView.touchPositionEnum;
import com.lenovo.lenovorobotmobile.view.NewMapView.PointListListener;

/**
 * 自定义组合控件的使用
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class MLayout extends RelativeLayout implements PointListListener,
		TouchLisenter {

	protected static final String TAG = "MLayout";
	private int startFlag = 0;
	private int endFlag = 0;
	private ControlOrientationView orientationControl;
	private Button newStartPoint;
	private Button newEndPoint;
	private NewMapView newMapView;
	private AlertDialog.Builder builder;
	private ArrayList<PointF> startPointList;
	private ArrayList<PointF> endPointList;
	private double myaw;
	private ServerUtils msgToServerHelp;
	private boolean ch_checkBox;
	private boolean en_checkBox;

	// 2015_9_2 ss new add
	private Button btnCurrentPose;
	private int mSetCurPoseFlag = 0;

	private Handler UIHanlder = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			switch (key) {
			case 1:
				startPointList = (ArrayList<PointF>) msg.obj;
				orientationControl.setVisibility(View.VISIBLE);
				break;
			case 2:
				endPointList = (ArrayList<PointF>) msg.obj;
				orientationControl.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	public void setServerUtils(ServerUtils msgToServerHelp) {
		this.msgToServerHelp = msgToServerHelp;
	}

	public MLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		SharedpreferencesUilts sharedpreferencesUilts = new SharedpreferencesUilts(
				context);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");

		View view = View.inflate(context, R.layout.layout_newmapview,
				MLayout.this);

		newMapView = (NewMapView) view.findViewById(R.id.newMapView);
		newMapView.setBackgroundColor(Color.rgb(205, 205, 205));
		newMapView.setPointListListener(this);

		newStartPoint = (Button) view.findViewById(R.id.newStartPoint);

		newStartPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (startFlag == 0) {
					newStartPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_click);
					newMapView.isStartDrawPoint(true, 1);
					startFlag = 1;
				} else {
					newStartPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
					newMapView.isStartDrawPoint(false, 0);
					startFlag = 0;
				}
			}
		});
		newEndPoint = (Button) view.findViewById(R.id.newEndPoint);
		newEndPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (endFlag == 0) {
					newEndPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_click);
					newMapView.isStartDrawPoint(true, 2);
					endFlag = 1;
				} else {
					newEndPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
					newMapView.isStartDrawPoint(false, 0);
					endFlag = 0;
				}
			}
		});
		// ss 2015_9_2 new add
		btnCurrentPose = (Button) view.findViewById(R.id.setCurPose);
		btnCurrentPose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mSetCurPoseFlag == 0) {
					mSetCurPoseFlag = 1;
					btnCurrentPose
							.setBackgroundResource(R.drawable.sp_button_more_bank_click);
					newMapView.isStartDrawPoint(true, 101);
				} else {

					btnCurrentPose
							.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
					newMapView.isStartDrawPoint(false, 0);
					showDialog("您确定设置的Darwin位置吗?", "确定", "取消");
					// mSetCurPoseFlag=0;
				}
			}

		});

		if (en_checkBox) {
			newStartPoint.setText("startPoint");
			newEndPoint.setText("endPoint");
		}
		orientationControl = (ControlOrientationView) view
				.findViewById(R.id.orientationControl);
		orientationControl.setTouchLisenter(this);
		builder = new Builder(context);
	}

	@Override
	public void startPointList(ArrayList<PointF> startPointList) {
		// TODO Auto-generated method stub
		if (startPointList != null && startPointList.size() > 0) {
			Message msg = new Message();
			msg.obj = startPointList;
			msg.what = 1;
			UIHanlder.sendMessage(msg);
		}
	}

	@Override
	public void endPointList(ArrayList<PointF> endPointList) {
		// TODO Auto-generated method stub
		if (endPointList != null && endPointList.size() > 0) {
			Message msg = new Message();
			msg.obj = endPointList;
			msg.what = 2;
			UIHanlder.sendMessage(msg);
		}
	}

	@Override
	public void setTouchPositionChange(touchPositionEnum touchPosition) {
		switch (touchPosition) {
		case RIGHT:
			myaw = 0.0;
			break;
		case LEFT:
			myaw = 3.1416;
			break;
		case UP:
			myaw = 1.5708;
			break;
		case DOWN:
			myaw = -1.5708;
			break;
		}
		if (startFlag == 1) {

			showDialog("您确定设置该点为起始点吗", "确定", "取消");

		} else if (endFlag == 1) {

			showDialog("您确定设置该点为终点吗", "确定", "取消");

		}
		orientationControl.setVisibility(View.GONE);
	}

	private void showDialog(String content, String sure, String cancel) {
		builder.setTitle(content);
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						newMapView.cancelDrawPoint();
						newMapView.isStartDrawPoint(false, 0);
						if (startFlag == 1) {
							// 发送起点数据
							newStartPoint
									.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
							startFlag = 0;
						} else if (endFlag == 1) {
							// 发送终点
							newEndPoint
									.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
							endFlag = 0;
						} else if (mSetCurPoseFlag == 1) {
							newMapView.DrawRobotPose(0);
							mSetCurPoseFlag = 0;
						}
					}
				});
		builder.setPositiveButton(sure, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				newMapView.isStartDrawPoint(false, 0);
				// 把点发送给服务器
				if (startFlag == 1) {
					// 发送起点数据
					newStartPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
					startFlag = 0;
					sendMsg(startPointList, 1);
				} else if (endFlag == 1) {
					// 发送终点
					newEndPoint
							.setBackgroundResource(R.drawable.sp_button_more_bank_normal);
					endFlag = 0;
					sendMsg(endPointList, 2);
				} else if (mSetCurPoseFlag == 1) {
					newMapView.DrawRobotPose(1);
					mSetCurPoseFlag = 0;
					if (NewMapView.mCurPose1 != null
							&& NewMapView.mCurPose2 != null) {
						float tX = NewMapView.mCurPose2.x
								- NewMapView.mCurPose1.x;
						float tY = NewMapView.mCurPose1.y
								- NewMapView.mCurPose2.y;

						double yaw = Math.atan(tY / tX);
						if (tX > 0) {
							msgToServerHelp.sendMsgToServer(
									"11",
									"1",
									"20",
									"1",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ "",
									RobotUtil
											.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "", yaw + "");
							Log.i("POINT_",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ ""
											+ RobotUtil
													.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "...." + (yaw) + "");
						} else if (tY > 0 && tX < 0) {
							msgToServerHelp.sendMsgToServer(
									"11",
									"1",
									"20",
									"1",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ "",
									RobotUtil
											.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "", (yaw + 3.1415) + "");
							Log.i("POINT_",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ ""
											+ RobotUtil
													.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "...." + (yaw + 3.1415) + "");
						} else if (tY < 0 && tX < 0) {
							msgToServerHelp.sendMsgToServer(
									"11",
									"1",
									"20",
									"1",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ "",
									RobotUtil
											.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "", (yaw - 3.1415) + "");
							Log.i("POINT_",
									RobotUtil
											.yyToPixCoordX(NewMapView.mCurPose1.x)
											+ ""
											+ RobotUtil
													.yyToPixCoordY(NewMapView.mCurPose1.y)
											+ "...." + (yaw - 3.1415) + "");
						}
					}
				}
			}
		});
		builder.show();
	}

	public void sendMsg(ArrayList<PointF> arrPoint, int flag) {
		if (msgToServerHelp != null) {
			if (ch_checkBox) {
				if (flag == 1) {
					msgToServerHelp.sendMsgToServer("11", "1", "20", "1",
							RobotUtil.yyToPixCoordX(arrPoint.get(0).x) + "",
							RobotUtil.yyToPixCoordY(arrPoint.get(0).y) + "",
							myaw + "");
				} else {
					msgToServerHelp.sendMsgToServer("11", "1", "20", "2",
							RobotUtil.yyToPixCoordX(arrPoint.get(0).x) + "",
							RobotUtil.yyToPixCoordY(arrPoint.get(0).y) + "",
							myaw + "");
				}

			} else if (en_checkBox) {
				if (flag == 1) {

					msgToServerHelp.sendMsgToServer("21", "2", "20", "1",
							RobotUtil.yyToPixCoordX(arrPoint.get(0).x) + "",
							RobotUtil.yyToPixCoordY(arrPoint.get(0).y) + "",
							myaw + "");
				} else {
					msgToServerHelp.sendMsgToServer("21", "2", "20", "2",
							RobotUtil.yyToPixCoordX(arrPoint.get(0).x) + "",
							RobotUtil.yyToPixCoordY(arrPoint.get(0).y) + "",
							myaw + "");
				}
			}
		}
		arrPoint.clear();
	}
}
