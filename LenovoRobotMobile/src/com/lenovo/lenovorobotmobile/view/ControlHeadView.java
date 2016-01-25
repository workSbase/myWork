package com.lenovo.lenovorobotmobile.view;

/**
 * 这个类主要是用来,画出来抬头点头,和上升下降的按钮的
 */

import com.lenovo.lenovorobot.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

@SuppressLint("DrawAllocation")
public class ControlHeadView extends BaseView {

	float[] pts;
	private float x2;
	private float y2;
	private int downFlag = -1;
	private OnHeadContorl headContorl;
	private Bitmap bitmapNormal;
	private Bitmap controlUup;
	private Bitmap controlDown;
	private Bitmap bitmapNorma2;

	public ControlHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		initBitmap();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	private void initBitmap() {

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.control_normal,
				options);
		BitmapFactory.decodeResource(getResources(), R.drawable.control_up,
				options);
		BitmapFactory.decodeResource(getResources(), R.drawable.control_down,
				options);
		bitmapNorma2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.control_normal, options);
		options.inSampleSize = SCALING;

		options.inJustDecodeBounds = false;
		bitmapNormal = BitmapFactory.decodeResource(getResources(),
				R.drawable.control_normal, options);
		controlUup = BitmapFactory.decodeResource(getResources(),
				R.drawable.control_up, options);
		controlDown = BitmapFactory.decodeResource(getResources(),
				R.drawable.control_down, options);

		bitmapNorma2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.control_normal, options);
	}

	// 指定,自定义View 的大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(194, 232);
	}

	// 在View 里面画出图形来
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (downFlag == 1) {
			canvas.drawBitmap(controlUup, null, new Rect(0, 0, 92, 232), paint);
		} else if (downFlag == 0) {
			// canvas.drawBitmap(bitmapNormal, null, new Rect(0, 0, 92, 232),
			// paint);
		} else if (downFlag == 2) {
			canvas.drawBitmap(controlDown, null, new Rect(0, 0, 92, 232), paint);
		} else if (downFlag == 3) {
			canvas.drawBitmap(controlUup, null, new Rect(102, 0, 194, 232),
					paint);
		} else if (downFlag == 4) {
			canvas.drawBitmap(controlDown, null, new Rect(102, 0, 194, 232),
					paint);
		}

		canvas.drawBitmap(bitmapNormal, null, new Rect(0, 0, 92, 232), paint);
		canvas.drawBitmap(bitmapNorma2, null, new Rect(102, 0, 194, 232), paint);

		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(18);
		paint.setTextSize(20);
		paint.setTypeface(typeface);

		if (ch_checkBox) {
			canvas.drawText("头部", 25, 125, paint);
			canvas.drawText("颈部", 125, 125, paint);
		} else if (en_checkBox) {
			canvas.drawText("Head", 18, 125, paint);
			canvas.drawText("Neck", 125, 125, paint);
		}

		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			x2 = event.getX();
			y2 = event.getY();
			// 上升
			if (x2 <= 90 && y2 <= 80) {
				downFlag = 1;

				invalidate();
			}
			// 下降
			if (x2 >= 0 && y2 >= 151 && x2 <= 92 && y2 <= 232) {
				downFlag = 2;

				invalidate();
			}
			// 抬头
			if (x2 >= 102 && y2 >= 0 && x2 <= 194 && y2 <= 80) {
				downFlag = 3;
				invalidate();
			}
			// 点头
			if (x2 >= 102 && y2 >= 151 && x2 <= 194 && y2 <= 232) {
				downFlag = 4;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:

			if (x2 <= 90 && y2 <= 80) {
				if (headContorl != null) {
					headContorl.headMove(0);
				}
			}
			if (x2 >= 0 && y2 >= 151 && x2 <= 92 && y2 <= 232) {
				if (headContorl != null) {
					headContorl.headMove(1);
				}
			}
			if (x2 >= 102 && y2 >= 0 && x2 <= 194 && y2 <= 80) {
				if (headContorl != null) {
					headContorl.headMove(2);
				}
			}
			if (x2 >= 102 && y2 >= 151 && x2 <= 194 && y2 <= 232) {
				if (headContorl != null) {
					headContorl.headMove(3);
				}
			}
			downFlag = -1;
			invalidate();
			break;
		}
		return true;
	}

	public void setOnHeadContorl(OnHeadContorl headContorl) {
		this.headContorl = headContorl;
	}

	public interface OnHeadContorl {

		void headMove(int headFlag);
	}
}