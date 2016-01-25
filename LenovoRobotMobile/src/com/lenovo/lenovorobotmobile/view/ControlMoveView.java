package com.lenovo.lenovorobotmobile.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.MInterface.ControlMoveInterface;

/**
 * 控制按钮
 * 
 * @author Administrator
 * 
 */
@SuppressLint("DrawAllocation")
public class ControlMoveView extends BaseView {

	int downFlag = -1;
	private float touchX;
	private float touchY;

	private Bitmap normal;
	private Bitmap down_actived;
	private Bitmap up_actived;
	private Bitmap left_actived;
	private Bitmap right_actived;
	private MoveControlLister bitmapLister;
	private ControlMoveInterface controlMoveInterface;

	public ControlMoveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		normal = BitmapFactory
				.decodeResource(getResources(), R.drawable.normal);
		down_actived = BitmapFactory.decodeResource(getResources(),
				R.drawable.down_actived);
		up_actived = BitmapFactory.decodeResource(getResources(),
				R.drawable.up_actived);
		left_actived = BitmapFactory.decodeResource(getResources(),
				R.drawable.left_actived);
		right_actived = BitmapFactory.decodeResource(getResources(),
				R.drawable.right_actived);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(322, 322);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (downFlag == -1) {
			canvas.drawBitmap(normal, null, new Rect(0, 0, 322, 322), paint);
		} else if (downFlag == 0) {
			canvas.drawBitmap(up_actived, null, new Rect(0, 0, 322, 322), paint);
		} else if (downFlag == 1) {
			canvas.drawBitmap(down_actived, null, new Rect(0, 0, 322, 322),
					paint);
		} else if (downFlag == 2) {
			canvas.drawBitmap(left_actived, null, new Rect(0, 0, 322, 322),
					paint);
		} else if (downFlag == 3) {
			canvas.drawBitmap(right_actived, null, new Rect(0, 0, 322, 322),
					paint);
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();

		if (controlMoveInterface != null) {
			controlMoveInterface.startControlMoveFlagThread();
		}
		if (touchX > 0 && touchX < 266 && touchY < 114 && touchY > 0) {
			downFlag = 0;
			invalidate();
		}
		if (touchX > 226 && touchY < 208) {
			downFlag = 3;
			invalidate();
		}
		if (touchX < 113 && touchY > 113 && touchY < 264) {
			downFlag = 2;
			invalidate();
		}
		if (touchY > 264) {
			downFlag = 1;
			invalidate();
		}
		if (bitmapLister != null) {
			bitmapLister.setdownFlag(downFlag);
		}
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (controlMoveInterface != null) {
				controlMoveInterface.stopControlMoveFlagThread();
			}
			downFlag = -1;
			invalidate();
			break;
		}
		return true;
	}

	public interface MoveControlLister {
		void setdownFlag(int moveFlag);
	}

	public void setControlBitmapLister(MoveControlLister bitmapLister) {
		this.bitmapLister = bitmapLister;
	}

	public void setControlMoveInterface(
			ControlMoveInterface controlMoveInterface) {
		this.controlMoveInterface = controlMoveInterface;
	}
}
