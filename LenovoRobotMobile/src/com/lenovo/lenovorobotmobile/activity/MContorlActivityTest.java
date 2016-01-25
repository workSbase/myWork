package com.lenovo.lenovorobotmobile.activity;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.MInterface.ControlMoveInterface;
import com.lenovo.lenovorobotmobile.utils.MToast;
import com.lenovo.lenovorobotmobile.view.ControlHeadView;
import com.lenovo.lenovorobotmobile.view.ControlHeadView.OnHeadContorl;
import com.lenovo.lenovorobotmobile.view.ControlMoveView;
import com.lenovo.lenovorobotmobile.view.ControlMoveView.MoveControlLister;
import com.lenovo.lenovorobotmobile.view.MLayout;

/**
 * Created by Administrator on 2015/5/29.
 */
public class MContorlActivityTest extends BaseActivity implements
		MoveControlLister, OnHeadContorl, ControlMoveInterface {
	public static final String TAG = "MyContorlActivity";
	private int ortaion;
	protected ArrayList<PointF> start_Point;
	protected ArrayList<PointF> end_Point;
	private ImageView mySmallView;
	private Button cameraButton;
	private Button saveData;
	private ControlHeadView upDown;
	protected int sendStartPoint;
	private int mySmallViewNumber;

	private boolean isSendControlFlag = true;
	Thread mControlMoveFlagThread = null;
	private ImageView showRobotPicture;
	private ImageView showRobotBigImg;
	private Bitmap robotPictureBitmap;
	private int backKeyswitch = 0;
	private MLayout mMLayout;

	@Override
	public void initLayout() {

		setContentView(R.layout.myvideoviewtest);

		ControlMoveView myControl_Bitmap = (ControlMoveView) findViewById(R.id.myControl_Bitmap);
		myControl_Bitmap.setControlMoveInterface(this);
		myControl_Bitmap.setControlBitmapLister(this);

		upDown = (ControlHeadView) findViewById(R.id.upDown);
		upDown.setOnHeadContorl(this);

		mySmallView = (ImageView) findViewById(R.id.mySmallView);
		mySmallView.setOnClickListener(this);

		cameraButton = (Button) findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(this);

		saveData = (Button) findViewById(R.id.saveData);
		saveData.setOnClickListener(this);

		showRobotPicture = (ImageView) findViewById(R.id.showRobotPicture);
		showRobotPicture.setOnClickListener(this);

		showRobotBigImg = (ImageView) findViewById(R.id.myBigImg);
		showRobotBigImg.setOnClickListener(this);

		mMLayout = (MLayout) findViewById(R.id.mMLayout);
		mMLayout.setServerUtils(msgToServerHelp);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setdownFlag(int downFlag) {
		this.ortaion = downFlag;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.mySmallView:
			if (mySmallViewNumber == 0) {
				mapShowSwitch(0);
				this.backKeyswitch = 1;
			} else {
				mapShowSwitch(1);
				this.backKeyswitch = 0;
			}
			break;
		case R.id.cameraButton:
			MToast.showToast(this, "通知机器人拍照", 0);
			if (ch_checkBox) {
				msgToServerHelp.sendMsgToServer("11", "1", "27", "1");
			} else if (en_checkBox) {
				msgToServerHelp.sendMsgToServer("21", "2", "27", "1");
			}
			break;
		case R.id.showRobotPicture:
			if (robotPictureBitmap != null) {
				showRobotBigImg.setVisibility(View.VISIBLE);
				showRobotBigImg.setBackgroundDrawable(new BitmapDrawable(
						robotPictureBitmap));
				this.backKeyswitch = 1;
			} else {
				MToast.showToast(this, "当前没有照片", 0);
			}
			break;
		case R.id.myBigImg:
			showRobotBigImg.setVisibility(View.GONE);
			this.backKeyswitch = 0;
			break;
		}
	}

	private void mapShowSwitch(int a) {
		switch (a) {
		case 0:
			cameraButton.setVisibility(View.INVISIBLE);
			mySmallViewNumber = 1;
			upDown.setVisibility(View.GONE);
			saveData.setVisibility(View.INVISIBLE);
			showRobotPicture.setVisibility(View.INVISIBLE);
			mMLayout.setVisibility(View.VISIBLE);
			break;
		case 1:
			cameraButton.setVisibility(View.VISIBLE);
			upDown.setVisibility(View.VISIBLE);
			mySmallView.setBackgroundResource(R.drawable.b2map_02);
			mySmallViewNumber = 0;
			saveData.setVisibility(View.VISIBLE);
			showRobotPicture.setVisibility(View.VISIBLE);
			mMLayout.setVisibility(View.GONE);
			break;
		}
	}

	public void setFlag(int ortaion) {
		Log.e("downFlag", " downFlag " + ortaion);
		if (ch_checkBox) {
			msgToServerHelp.sendMsgToServer("11", "1", "17", ortaion + "");
		} else if (en_checkBox) {
			msgToServerHelp.sendMsgToServer("21", "2", "17", ortaion + "");
		}
	}

	public void startControlMoveFlagThread() {
		if (mControlMoveFlagThread == null) {
			isSendControlFlag = true;
			mControlMoveFlagThread = new Thread(new ControlMoveFlagThread());
			mControlMoveFlagThread.start();
		}
	}

	public void stopControlMoveFlagThread() {
		MContorlActivityTest.this.ortaion = -1;
	}

	class ControlMoveFlagThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isSendControlFlag) {
				if (MContorlActivityTest.this.ortaion >= 0) {
					setFlag(MContorlActivityTest.this.ortaion);
				}
				SystemClock.sleep(100);
			}
		}
	}

	@Override
	public void headMove(int headFlag) {
		// TODO Auto-generated method stub
		if (ch_checkBox) {
			msgToServerHelp.sendMsgToServer("11", "1", "18", headFlag + "");
		} else if (en_checkBox) {
			msgToServerHelp.sendMsgToServer("21", "2", "18", headFlag + "");
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (backKeyswitch == 0) {

			// CallControl.endCall();
			if (robotPictureBitmap != null) {
				robotPictureBitmap = null;
			}
			try {
				isSendControlFlag = false;
				mControlMoveFlagThread = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
			finish();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setPictureByte(Bitmap bitmap) {
		// TODO Auto-generated method stub
		showRobotPicture.setBackgroundDrawable(new BitmapDrawable(bitmap));

		this.robotPictureBitmap = bitmap;
	}
}
