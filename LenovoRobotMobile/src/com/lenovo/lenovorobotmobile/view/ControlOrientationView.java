package com.lenovo.lenovorobotmobile.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 方向键的选着
 * 
 * @author Administrator
 * 
 */
public class ControlOrientationView extends BaseView {

	private static final String TAG = "ControlView";

	private float originX = 0;
	private float originY = 0;

	public float touchX = 40;
	public float touchY = 50;

	public int screenWidth;
	public int screenHeight;

	private float proportion = 2.5f;

	final float smallCircleRadius = 65f / proportion;

	final float hightTriangle = smallCircleRadius * 2f;
	final float widthTriangle = hightTriangle * 1.1547f;
	final float bigCircleRadius = smallCircleRadius * 3 + 20;// 215
	private float centerX = originX + bigCircleRadius + 10;
	private float centerY = originY + bigCircleRadius + 10;

	private Paint paintTriangle = new Paint();
	Path upTriangle = new Path();
	Path downTriangle = new Path();
	Path leftTriangle = new Path();
	Path rightTriangle = new Path();

	public touchPositionEnum touchPosition = touchPositionEnum.NONE;

	public int moveDirection = -1;
	private int moveGear = 20;

	private TouchLisenter touchLisenter;

	public enum touchPositionEnum {
		UP, DOWN, LEFT, RIGHT, CENTER, NONE
	};

	public ControlOrientationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		moveDirection = -1;

		DisplayMetrics metrics = new DisplayMetrics();
		float density = metrics.density;

		if (density <= 1.5) {
			proportion = 5;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub

	}

	public void setOrigin(float x, float y) {
		originX = x;
		originY = y;

		centerX = originX + bigCircleRadius + 10;
		centerY = originY + bigCircleRadius + 10;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setColor(Color.RED);
		paint.setAlpha(100);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);

		// ����һ��СԲ
		if (touchPosition == touchPositionEnum.CENTER)
			paint.setAlpha(255);
		canvas.drawCircle(centerX, centerY, smallCircleRadius, paint);
		// ��Բ
		paint.setColor(Color.rgb(0x95, 0xa5, 0xa6));
		paint.setAlpha(60);
		canvas.drawCircle(centerX, centerY, bigCircleRadius, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setAlpha(220);
		canvas.drawCircle(centerX, centerY, bigCircleRadius, paint);

		/* ��ʵ������� */
		paintTriangle.setColor(Color.rgb(0x8e, 0x44, 0xad));
		paintTriangle.setAntiAlias(true);
		paintTriangle.setStyle(Paint.Style.FILL);

		float tmpX = 0f, tmpY = 0f;
		// �����
		paintTriangle.setAlpha(100);
		tmpX = (centerX + smallCircleRadius + hightTriangle + 10 / proportion);
		upTriangle.moveTo(tmpX, centerY);
		upTriangle.lineTo(tmpX - hightTriangle, centerY - widthTriangle / 2);
		upTriangle.lineTo(tmpX - hightTriangle, centerY + widthTriangle / 2);
		upTriangle.close();
		if (touchPosition == touchPositionEnum.RIGHT)
			paintTriangle.setAlpha(255);
		canvas.drawPath(upTriangle, paintTriangle);

		// �����
		paintTriangle.setAlpha(100);
		tmpX = (centerX - smallCircleRadius - hightTriangle - 10 / proportion);
		downTriangle.moveTo(tmpX, centerY);
		downTriangle.lineTo(tmpX + hightTriangle, centerY - widthTriangle / 2);
		downTriangle.lineTo(tmpX + hightTriangle, centerY + widthTriangle / 2);
		downTriangle.close();
		if (touchPosition == touchPositionEnum.LEFT)
			paintTriangle.setAlpha(255);
		canvas.drawPath(downTriangle, paintTriangle);

		// �����
		paintTriangle.setAlpha(100);
		tmpY = (centerY - smallCircleRadius - hightTriangle - 10 / proportion);
		leftTriangle.moveTo(centerX, tmpY);
		leftTriangle.lineTo(centerX - widthTriangle / 2, tmpY + hightTriangle);
		leftTriangle.lineTo(centerX + widthTriangle / 2, tmpY + hightTriangle);
		leftTriangle.close();
		if (touchPosition == touchPositionEnum.UP)
			paintTriangle.setAlpha(255);
		canvas.drawPath(leftTriangle, paintTriangle);

		// �����
		paintTriangle.setAlpha(100);
		tmpY = (centerY + smallCircleRadius + hightTriangle + 10 / proportion);
		rightTriangle.moveTo(centerX, tmpY);
		rightTriangle.lineTo(centerX - widthTriangle / 2, tmpY - hightTriangle);
		rightTriangle.lineTo(centerX + widthTriangle / 2, tmpY - hightTriangle);
		rightTriangle.close();
		if (touchPosition == touchPositionEnum.DOWN)
			paintTriangle.setAlpha(255);
		canvas.drawPath(rightTriangle, paintTriangle);

	}

	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		touchY = event.getY();
		touchPosition = touchPositionEnum.NONE;
		if ((Math.abs(touchX - centerX) < smallCircleRadius + 5)
				&& (Math.abs(touchY - centerY) < smallCircleRadius + 5))
			touchPosition = touchPositionEnum.CENTER;
		else if ((Math.abs(touchX - (centerX + 2 * smallCircleRadius + 10)) < smallCircleRadius)
				&& (Math.abs(touchY - centerY) < smallCircleRadius))
			touchPosition = touchPositionEnum.RIGHT;
		else if ((Math.abs(touchX - (centerX - 2 * smallCircleRadius - 10)) < smallCircleRadius)
				&& (Math.abs(touchY - centerY) < smallCircleRadius))
			touchPosition = touchPositionEnum.LEFT;
		else if ((Math.abs(touchX - centerX) < smallCircleRadius)
				&& (Math.abs(touchY - (centerY - 2 * smallCircleRadius - 10)) < smallCircleRadius))
			touchPosition = touchPositionEnum.UP;
		else if ((Math.abs(touchX - centerX) < smallCircleRadius)
				&& (Math.abs(touchY - (centerY + 2 * smallCircleRadius + 10)) < smallCircleRadius))
			touchPosition = touchPositionEnum.DOWN;

		Log.i(TAG, "touchPosition " + touchPosition);
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_UP:

			if (touchLisenter != null) {
				touchLisenter.setTouchPositionChange(touchPosition);
			}
			touchPosition = touchPositionEnum.NONE;
		default:

		}
		switch (touchPosition) {
		case CENTER:
			moveDirection = 0;
			break;
		case RIGHT:
			moveDirection = moveGear + 4;
			break;
		case LEFT:
			moveDirection = moveGear + 3;
			break;
		case UP:
			moveDirection = moveGear + 1;
			break;
		case DOWN:
			moveDirection = moveGear + 2;
			break;
		case NONE:
			moveDirection = -1;
			break;
		default:
		}
		invalidate();
		return true;
	}

	public void setTouchLisenter(TouchLisenter touchLisenter) {
		this.touchLisenter = touchLisenter;
	}

	public interface TouchLisenter {
		void setTouchPositionChange(touchPositionEnum touchPosition);
	}
}
