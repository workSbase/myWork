package com.lenovo.lenovorobotmobile.view;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.MInterface.ServerPacketInterfaceObserver;
import com.lenovo.lenovorobotmobile.net.ServerConnect;
import com.lenovo.lenovorobotmobile.utils.RobotAngle;
import com.lenovo.lenovorobotmobile.utils.RobotUtil;
import com.lenovo.lenovorobotmobile.utils.ScaleConverter;

public class NewMapView extends View implements ServerPacketInterfaceObserver {

	private ArrayList<PointF> startPointList = new ArrayList<PointF>();
	private ArrayList<PointF> endPointList = new ArrayList<PointF>();

	private PointF currentStartPoint = new PointF();
	private PointF oldStartPoint = new PointF();

	private PointF currentEndPoint = new PointF();
	private PointF oldEndPoint = new PointF();

	private Bitmap startPointBitmap;
	private Bitmap endPointBitmap;
	private Bitmap oretationBitmap;
	// 显示的地图
	private Bitmap mapBitmap;
	// 可以触摸的必要条件
	private boolean isStartDrawPointFlag;
	// 设置第几个点的标志
	private int flag;

	private Paint paint;
	private float scrollingOffsetX;
	private float scrollingOffsetY;

	private float scaleRate = 1.0f;
	private GestureDetector gD;
	private ScaleGestureDetector sGD;
	private boolean isScroll = false;
	private float downX;
	private float downY;
	private PointListListener pointListListener;
	private ScaleConverter mScaleConverter = null;
	private RobotAngle robotAngle2;
	// 是否显示机器人的指示箭头
	private boolean isShowOratationBitmap;
	private double robotX;
	private double robotY;
	private double robotAngle;
	private ServerConnect connectServer;

	// 2015_9_2 ss new add
	private int mDrawCurPoseStep = 0;
	private int mDrawSetRobotPose = 0;
	public static PointF mCurPose1 = new PointF();
	public static PointF mCurPose2 = new PointF();

	public void DrawRobotPose(int nFlag) {
		if (nFlag == 1) {
			mDrawSetRobotPose = 1;
			mDrawCurPoseStep = 0;
		} else if (nFlag == 0) {
			mDrawSetRobotPose = 0;
			mDrawCurPoseStep = 0;
			mCurPose1.x = 0;
			mCurPose1.y = 0;
			mCurPose2.x = 0;
			mCurPose2.y = 0;
		}
		invalidate();
	}

	public void isStartDrawPoint(boolean isStartDrawPointFlag, int flag) {
		this.isStartDrawPointFlag = isStartDrawPointFlag;
		this.flag = flag;
	}

	public void setRobotX_Y(double x, double y, double robotAngle) {

		if (mScaleConverter == null) {
			mScaleConverter = new ScaleConverter();
		}
		isShowOratationBitmap = true;

		// 做的一个实际距离和像素之间的转化
		// mScaleConverter.setX(x);
		// mScaleConverter.setY(y);
		// this.robotX = mScaleConverter.getX();
		// this.robotY = mScaleConverter.getY();

		this.robotX = (x + 10) / 0.05;
		this.robotY = 400 - ((y + 10) / 0.05);

		this.robotAngle = robotAngle;

		invalidate();
	}

	public NewMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		robotAngle2 = new RobotAngle();
		gD = new GestureDetector(context, new InnerGestureListener());
		sGD = new ScaleGestureDetector(context, new ScaleListener());
		connectServer = ServerConnect.getConnectServer();
		connectServer.registerObserver(this);

		// 初始化图片
		initBitmap();
	}

	private void initBitmap() {
		startPointBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bubble_start);
		endPointBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bubble_end);
		oretationBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.navi_map_gps_locked);
		mapBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.map_new);
		paint = new Paint();
	}

	// 开始画图
	@Override
	protected void onDraw(Canvas canvas) {

		canvas.translate(scrollingOffsetX, scrollingOffsetY);
		canvas.scale(scaleRate, scaleRate);
		// 画地图
		canvas.drawBitmap(mapBitmap, null, new Rect(0, 0, RobotUtil.MAP_SIZE_X,
				RobotUtil.MAP_SIZE_Y), null);
		// 原点
		paint.setColor(Color.RED);
		canvas.drawCircle(RobotUtil.ORIGIN_IN_PIX_X, RobotUtil.ORIGIN_IN_PIX_Y,
				2, paint);

		canvas.scale(1.0f / scaleRate, 1.0f / scaleRate);

		// 画方向箭头指向
		if (isShowOratationBitmap) {
			startDrawOratationBitmap(canvas);
		}
		// 画开始点
		if (currentStartPoint != null && currentStartPoint.x != 0.0f) {
			canvas.drawBitmap(startPointBitmap, currentStartPoint.x * scaleRate
					- startPointBitmap.getWidth() / 2, currentStartPoint.y
					* scaleRate - startPointBitmap.getHeight(), null);
		}
		// 画结束点
		if (currentEndPoint != null && currentEndPoint.x != 0.0f) {
			canvas.drawBitmap(endPointBitmap, currentEndPoint.x * scaleRate
					- endPointBitmap.getWidth() / 2, currentEndPoint.y
					* scaleRate - endPointBitmap.getHeight(), null);
		}

		// draw current robot pose
		if (mDrawCurPoseStep == 1) {
			paint.setColor(Color.GREEN);
			canvas.drawCircle(mCurPose1.x * scaleRate, mCurPose1.y * scaleRate,
					15, paint);
		} else if (mDrawCurPoseStep == 2) {
			paint.setColor(Color.GREEN);
			canvas.drawCircle(mCurPose1.x * scaleRate, mCurPose1.y * scaleRate,
					15, paint);

			paint.setColor(Color.RED);
			canvas.drawCircle(mCurPose2.x * scaleRate, mCurPose2.y * scaleRate,
					15, paint);
		}

		if (mDrawSetRobotPose == 1) {

			paint.setColor(Color.GREEN);
			paint.setStrokeWidth((float) 5);
			canvas.drawCircle(mCurPose1.x * scaleRate, mCurPose1.y * scaleRate,
					15, paint);

			float angle = (float) Math.atan2(mCurPose2.y - mCurPose1.y,
					mCurPose2.x - mCurPose1.x);

			float fOffetX = (float) (Math.cos(angle) * 25 * scaleRate);
			float fOffetY = (float) (Math.sin(angle) * 25 * scaleRate);
			paint.setColor(Color.RED);
			canvas.drawLine(mCurPose1.x * scaleRate, mCurPose1.y * scaleRate,
					mCurPose1.x * scaleRate + fOffetX, mCurPose1.y * scaleRate
							+ fOffetY, paint);

		}
		super.onDraw(canvas);

	}

	// 画方向箭头指向
	private void startDrawOratationBitmap(Canvas canvas) {
		Bitmap directionIndicator = robotAngle2.adjustPhotoRotation(
				oretationBitmap, (float) robotAngle);
		canvas.drawBitmap(
				directionIndicator,
				(float) (robotX * scaleRate - directionIndicator.getWidth() / 2),
				(float) (robotY * scaleRate - directionIndicator.getHeight() / 2),
				null);
		canvas.drawCircle((float) robotX * scaleRate, (float) robotY
				* scaleRate, 4, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		downX = event.getX();
		downY = event.getY();
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_UP:
			if (mDrawCurPoseStep != 0) {
				mCurPose2.x = (float) ((downX - scrollingOffsetX) / scaleRate);
				mCurPose2.y = (float) ((downY - scrollingOffsetY) / scaleRate);

				mDrawCurPoseStep = 2;
			}
			startDrawView();
		}
		gD.setIsLongpressEnabled(true);
		sGD.onTouchEvent(event);

		return gD.onTouchEvent(event);
	}

	private void startDrawView() {
		// 表示可以画图
		if (isStartDrawPointFlag) {
			switch (flag) {
			case 1:
				if (!isScroll) {
					// 表示现在画的是起点
					if (startPointList.size() > 0) {
						startPointList.clear();
					}

					oldStartPoint.x = currentStartPoint.x;
					oldStartPoint.y = currentStartPoint.y;
					currentStartPoint.x = (float) ((downX - scrollingOffsetX) / scaleRate);
					currentStartPoint.y = (float) ((downY - scrollingOffsetY) / scaleRate);

					startPointList.add(new PointF(
							(float) ((downX - scrollingOffsetX) / scaleRate),
							(float) ((downY - scrollingOffsetY) / scaleRate)));

					if (pointListListener != null) {
						pointListListener.startPointList(startPointList);
					}
				}
				break;
			case 2:
				// 表示现在是画终点
				if (!isScroll) {
					if (endPointList.size() > 0) {
						endPointList.clear();
					}

					oldEndPoint.x = currentEndPoint.x;
					oldEndPoint.y = currentEndPoint.y;
					currentEndPoint.x = (float) ((downX - scrollingOffsetX) / scaleRate);
					currentEndPoint.y = (float) ((downY - scrollingOffsetY) / scaleRate);

					endPointList.add(0, new PointF(
							(float) ((downX - scrollingOffsetX) / scaleRate),
							(float) ((downY - scrollingOffsetY) / scaleRate)));

					if (pointListListener != null) {
						pointListListener.endPointList(endPointList);
					}
				}
				break;
			case 101:
				if (!isScroll) {
					if (mDrawCurPoseStep == 0) {
						mDrawSetRobotPose = 0;
						mCurPose1.x = (float) ((downX - scrollingOffsetX) / scaleRate);
						mCurPose1.y = (float) ((downY - scrollingOffsetY) / scaleRate);
						mDrawCurPoseStep = 1;

					}
				}
				break;
			}
		}
		isScroll = false;
		invalidate();
	}

	// 取消画点
	public void cancelDrawPoint() {
		switch (flag) {
		case 1:
			startPointList.clear();

			currentStartPoint.x = oldStartPoint.x;
			currentStartPoint.y = oldStartPoint.y;

			invalidate();
			break;
		case 2:
			endPointList.clear();

			currentEndPoint.x = oldEndPoint.x;
			currentEndPoint.y = oldEndPoint.y;
			invalidate();
			break;
		}
	}

	// 回调接口，把收集到的集合给暴露出去，随想怎么用就怎么使用
	public interface PointListListener {
		void startPointList(ArrayList<PointF> startPointList);

		void endPointList(ArrayList<PointF> endPointList);
	}

	public void setPointListListener(PointListListener pointListListener) {
		this.pointListListener = pointListListener;
	}

	class InnerGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			isScroll = true;
			scrollingOffsetY += -distanceY;
			scrollingOffsetX += -distanceX;

			invalidate();

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}
	}

	private class ScaleListener extends SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float currentRate = detector.getScaleFactor();
			float focusX = detector.getFocusX();
			float focusY = detector.getFocusY();
			scaleRate *= currentRate;
			scrollingOffsetX = (int) ((1 - currentRate)
					* (focusX - scrollingOffsetX) + scrollingOffsetX);
			scrollingOffsetY = (int) ((1 - currentRate)
					* (focusY - scrollingOffsetY) + scrollingOffsetY);
			invalidate();
			return true;
		}
	}

	@Override
	public void setServerResult(String resut, int flag) {
		// TODO Auto-generated method stub
		if (flag == 4) {
			if (resut != null) {
				isShowOratationBitmap = true;
				String[] split = resut.split(",");
				setRobotX_Y(Double.parseDouble(split[0]),
						Double.parseDouble(split[1]),
						Double.parseDouble(split[2]));
			}
		}
	}

	@Override
	public void setPictureByte(Bitmap bitmap) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setResultInteger(int result, int flag) {
		// TODO Auto-generated method stub
	}
}
