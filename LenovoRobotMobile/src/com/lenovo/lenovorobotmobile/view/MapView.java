//package com.lenovo.lenovorobotmobile.view;
//
//import java.text.DecimalFormat;
//
//import java.util.ArrayList;
//
//import com.lenovo.lenovorobot.R;
//import com.lenovo.lenovorobotmobile.MInterface.ServerPacketInterfaceObserver;
//import com.lenovo.lenovorobotmobile.net.ServerConnect;
//import com.lenovo.lenovorobotmobile.utils.RobotAngle;
//import com.lenovo.lenovorobotmobile.utils.RobotUtil;
//import com.lenovo.lenovorobotmobile.utils.ScaleConverter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.BitmapFactory.Options;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Paint.Align;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
//
///**
// * 当前没有使用该类
// * 
// * @author Administrator
// * 
// */
//@SuppressLint("DrawAllocation")
//public class MapView extends BaseView implements ServerPacketInterfaceObserver {
//	private static final String TAG = "DrawPathView";
//	private GestureDetector gd;
//	ScaleGestureDetector SGD;
//	private float scrollingOffsetY;
//	private float scrollingOffsetX;
//	private float scaleRate;
//	private Bitmap bmp;
//	private double robotX;
//	private double robotY;
//	private double robotAngle;
//	private double robotPhysicalX;
//	private double robotPhysicalY;
//	private double robotPhysicalAngle;
//	private Paint paint = new Paint();
//	private Bitmap bitmap2;
//	private RobotAngle robotAngle2;
//
//	private DecimalFormat df;
//
//	private boolean isPlanPath = false;
//
//	private OnThouchAddPoint addPoint;
//	private Bitmap bmpForInitialPoint;
//	private Bitmap bmpForGoalPoint;
//	private onTouchIsDown touchIsDown;
//	private float x2;
//	private float y2;
//
//	private boolean isClickStart = false;
//	private boolean isClickEnd = false;
//
//	public float startPointX = 0;
//	public float startPointY = 0;
//
//	ArrayList<PointF> start_Point;
//	private boolean isShowBigMap;
//	private ArrayList<PointF> end_Point;
//	private ArrayList<PointF> planPathList;
//	private boolean isScroll = false;
//	@SuppressWarnings("unused")
//	private float originX = 0;
//	@SuppressWarnings("unused")
//	private float originY = 0;
//
//	public float touchX = 40;
//	public float touchY = 50;
//	public int screenWidth;
//	public int screenHeight;
//	private Bitmap directionIndicator;
//	private boolean isCoordSet = false;
//
//	ScaleConverter mScaleConverter = null;
//
//	// private int flag_ = 0;
//	PointF currentPoint;
//	PointF nextPoint;
//	// private int flag;
//	private ServerConnect connectServer;
//
//	public void setPlanPath(boolean isPlanPath) {
//		this.isPlanPath = isPlanPath;
//	}
//
//	public void setStart_Point(ArrayList<PointF> start_Point) {
//		this.start_Point = start_Point;
//	}
//
//	public void setIsBigMap(boolean isShowBigMap) {
//		this.isShowBigMap = isShowBigMap;
//	}
//
//	public boolean isClick() {
//		return isClickStart;
//	}
//
//	public void setClick(boolean isClick) {
//		this.isClickStart = isClick;
//	}
//
//	public boolean isClickEnd() {
//		return isClickEnd;
//	}
//
//	public void setClickEnd(boolean isClickEnd) {
//		this.isClickEnd = isClickEnd;
//	}
//
//	public void setScrollingOffsetX_Y(double scrollingOffsetX,
//			double scrollingOffsetY, double x, double y) {
//		this.robotX = x;
//		this.robotY = y;
//		this.scrollingOffsetX = (float) (scrollingOffsetX - (x * scaleRate));
//		this.scrollingOffsetY = (float) (scrollingOffsetY - (y * scaleRate));
//		invalidate();
//	}
//
//	public boolean isScroll() {
//		return isScroll;
//	}
//
//	private ArrayList<PointF> initialPointList = new ArrayList<PointF>();
//	private ArrayList<PointF> endPointList = new ArrayList<PointF>();
//
//	public void setInitialPointList(ArrayList<PointF> initialPointList) {
//		this.initialPointList = initialPointList;
//		invalidate();
//	}
//
//	public void setEndList(ArrayList<PointF> endList) {
//		this.endPointList = endList;
//		invalidate();
//	}
//
//	public void setPathPointList(ArrayList<PointF> pathPointList) {
//		this.planPathList = pathPointList;
//		invalidate();
//	}
//
//	public void setRobotX_Y(double x, double y, double robotAngle) {
//
//		if (mScaleConverter == null) {
//			mScaleConverter = new ScaleConverter();
//		}
//		isCoordSet = true;
//		robotPhysicalX = x;
//		robotPhysicalY = y;
//		robotPhysicalAngle = robotAngle;
//
//		mScaleConverter.setX(x);
//		mScaleConverter.setY(y);
//
//		this.robotX = mScaleConverter.getX();
//		this.robotY = mScaleConverter.getY();
//		this.robotAngle = robotAngle;
//
//		invalidate();
//	}
//
//	public void setScaleOfScreenAndPhysical(double scaleOfScreenAndPhysical) {
//		mScaleConverter.setScaleOfScreenAndPhysical(scaleOfScreenAndPhysical);
//
//		invalidate();
//	}
//
//	public double getRobotX() {
//		return robotX;
//	}
//
//	public double getRobotY() {
//		return robotY;
//	}
//
//	public MapView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void initData() {
//		// TODO Auto-generated method stub
//		df = new DecimalFormat();
//		gd = new GestureDetector(context, new InnerGestureListener());
//		SGD = new ScaleGestureDetector(context, new ScaleListener());
//		scaleRate = 1.0f;
//
//		robotAngle2 = new RobotAngle();
//		Options opts = new Options();
//		opts.inSampleSize = 1;
//
//		mScaleConverter = new ScaleConverter();
//
//		start_Point = new ArrayList<PointF>();
//		end_Point = new ArrayList<PointF>();
//
//		planPathList = new ArrayList<PointF>();
//
//		df.applyPattern("0.000");
//
//		connectServer = ServerConnect.getConnectServer();
//
//		connectServer.registerObserver(this);
//	}
//
//	@Override
//	public void initLayout() {
//		// TODO Auto-generated method stub
//		setBackgroundColor(Color.rgb(205, 205, 205));
//		bmpForInitialPoint = BitmapFactory.decodeResource(getResources(),
//				R.drawable.bubble_start);
//		bmpForGoalPoint = BitmapFactory.decodeResource(getResources(),
//				R.drawable.bubble_end);
//		
//		bitmap2 = BitmapFactory.decodeResource(getResources(),
//				R.drawable.mapnew_yanshi);
//
//		bmp = BitmapFactory.decodeResource(getResources(),
//				R.drawable.navi_map_gps_locked);
//	}
//
//	public void setOrigin(float x, float y) {
//		this.originX = x;
//		this.originY = y;
//	}
//
//	// 开始画图
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//
//		// 设置
//		canvas.translate(scrollingOffsetX, scrollingOffsetY);
//		canvas.scale(scaleRate, scaleRate);
//
//		// 创建画笔
//		// paint.setColor(Color.rgb(0xf3, 0x9c, 0x12));
//		paint.setAlpha(255);
//		paint.setAntiAlias(true);
//		paint.setStyle(Paint.Style.FILL);
//		paint.setStrokeWidth(3);
//
//		paint.setColor(Color.BLUE);
//
//		// 画地图
//		canvas.drawBitmap(bitmap2, null, new Rect(0, 0, RobotUtil.MAP_SIZE_X,
//				RobotUtil.MAP_SIZE_Y), null);
//
//		// 原点
//		canvas.drawCircle(RobotUtil.ORIGIN_IN_PIX_X, RobotUtil.ORIGIN_IN_PIX_Y,
//				2, paint);
//
//		if (planPathList != null && !planPathList.isEmpty()
//				&& planPathList.size() > 0) {
//
//			drawPath(canvas, planPathList);
//		}
//
//		canvas.scale(1.0f / scaleRate, 1.0f / scaleRate);
//
//		if (isCoordSet) {
//
//			// 画箭头方向
//			drawOrtationBitmap(canvas);
//		}
//
//		if (initialPointList != null && initialPointList.size() > 0) {
//			canvas.drawBitmap(
//					bmpForInitialPoint,
//					initialPointList.get(0).x * scaleRate
//							- bmpForInitialPoint.getWidth() / 2,
//					initialPointList.get(0).y * scaleRate
//							- bmpForInitialPoint.getHeight(), null);
//		}
//		if (endPointList != null && endPointList.size() > 0) {
//			canvas.drawBitmap(
//					bmpForGoalPoint,
//					endPointList.get(0).x * scaleRate
//							- bmpForGoalPoint.getWidth() / 2,
//					endPointList.get(0).y * scaleRate
//							- bmpForGoalPoint.getHeight(), null);
//		}
//	}
//
//	private void drawOrtationBitmap(Canvas canvas) {
//		directionIndicator = robotAngle2.adjustPhotoRotation(bmp,
//				(float) robotAngle);
//
//		canvas.drawBitmap(
//				directionIndicator,
//				(float) (robotX * scaleRate - directionIndicator.getWidth() / 2),
//				(float) (robotY * scaleRate - directionIndicator.getHeight() / 2),
//				null);
//		canvas.drawCircle((float) robotX * scaleRate, (float) robotY
//				* scaleRate, 4, paint);
//		paint.setTextSize(25);
//		paint.setColor(Color.BLACK);
//		paint.setTextAlign(Align.LEFT);
//		String textForDraw = "(" + df.format(robotPhysicalX) + ", "
//				+ df.format(robotPhysicalY) + ", "
//				+ df.format(robotPhysicalAngle) + ")";
//		canvas.drawText(textForDraw, (float) (robotX * scaleRate + 30),
//				(float) (robotY * scaleRate - 30), paint);
//		Log.d(TAG, textForDraw);
//	}
//
//	private void drawPath(Canvas canvas, ArrayList<PointF> pointList) {
//		Paint paint = new Paint();
//		paint.setColor(Color.rgb(0xf3, 0x9c, 0x12));
//		paint.setAlpha(255);
//		paint.setAntiAlias(true);
//		paint.setStyle(Paint.Style.FILL);
//		paint.setStrokeWidth(2);
//
//		if (pointList != null && !pointList.isEmpty() && pointList.size() > 0) {
//			for (int i = 0; i < pointList.size() - 1; i++) {
//				currentPoint = pointList.get(i);
//				nextPoint = pointList.get(i + 1);
//				canvas.drawCircle(currentPoint.x, currentPoint.y, 8, paint);
//				canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
//						nextPoint.y, paint);
//			}
//			currentPoint = pointList.get(0);
//			paint.setColor(Color.rgb(0x29, 0x80, 0x89));
//			canvas.drawCircle(currentPoint.x, currentPoint.y, 10, paint);
//
//			currentPoint = pointList.get(pointList.size() - 1);
//			paint.setColor(Color.rgb(0xc0, 0x39, 0x2b));
//			canvas.drawCircle(currentPoint.x, currentPoint.y, 8, paint);
//		}
//	}
//
//	public boolean onTouchEvent(MotionEvent event) {
//		touchX = event.getX();
//		touchY = event.getY();
//		Paint paint = new Paint();
//		paint.setColor(Color.rgb(85, 54, 121));
//		paint.setAlpha(200);
//		paint.setAntiAlias(true);
//		paint.setStyle(Paint.Style.FILL);
//		int action = event.getAction();
//		switch (action) {
//		case MotionEvent.ACTION_MOVE:
//			break;
//		case MotionEvent.ACTION_DOWN:
//			x2 = event.getX();
//			y2 = event.getY();
//			break;
//		case MotionEvent.ACTION_UP:
//			if (!isScroll && isShowBigMap) {
//				if (isPlanPath) {
//					planPathList.add(0, new PointF(
//							(float) ((touchX - scrollingOffsetX) / scaleRate),
//							(float) ((touchY - scrollingOffsetY) / scaleRate)));
//					if (addPoint != null) {
//						addPoint.getList(planPathList, null, null);
//					}
//				}
//				if (isClickStart) {
//					end_Point.add(0, new PointF(
//							(float) ((touchX - scrollingOffsetX) / scaleRate),
//							(float) ((touchY - scrollingOffsetY) / scaleRate)));
//					if (addPoint != null) {
//						addPoint.getList(null, end_Point, null);
//					}
//				}
//				if (isClickEnd) {
//					start_Point.add(0, new PointF(
//							(float) ((touchX - scrollingOffsetX) / scaleRate),
//							(float) ((touchY - scrollingOffsetY) / scaleRate)));
//					if (addPoint != null) {
//						addPoint.getList(null, null, start_Point);
//					}
//				}
//			}
//			if (!isClickStart && !isClickEnd) {
//				if (touchIsDown != null) {
//					if (x2 >= 20 && x2 <= 40 && y2 >= 20 && y2 <= 40) {
//
//						touchIsDown.setDown(true);
//
//					}
//				}
//			}
//			isScroll = false;
//			invalidate();
//		}
//		gd.setIsLongpressEnabled(true);
//		SGD.onTouchEvent(event);
//		return gd.onTouchEvent(event);
//	}
//
//	class InnerGestureListener extends SimpleOnGestureListener {
//
//		@Override
//		public boolean onDown(MotionEvent e) {
//			return true;
//		}
//
//		@Override
//		public boolean onScroll(MotionEvent e1, MotionEvent e2,
//				float distanceX, float distanceY) {
//
//			isScroll = true;
//			scrollingOffsetY += -distanceY;
//			scrollingOffsetX += -distanceX;
//			invalidate();
//
//			return super.onScroll(e1, e2, distanceX, distanceY);
//		}
//
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}
//
//		@Override
//		public void onLongPress(MotionEvent e) {
//		}
//	}
//
//	private class ScaleListener extends SimpleOnScaleGestureListener {
//		@Override
//		public boolean onScale(ScaleGestureDetector detector) {
//			float currentRate = detector.getScaleFactor();
//			// Log.d(TAG, String.valueOf(currentRate));
//			float focusX = detector.getFocusX();
//			float focusY = detector.getFocusY();
//			scaleRate *= currentRate;
//			scrollingOffsetX = (int) ((1 - currentRate)
//					* (focusX - scrollingOffsetX) + scrollingOffsetX);
//			scrollingOffsetY = (int) ((1 - currentRate)
//					* (focusY - scrollingOffsetY) + scrollingOffsetY);
//			invalidate();
//			return true;
//		}
//	}
//
//	public void setOnThouchAddPoint(OnThouchAddPoint addPoint) {
//		this.addPoint = addPoint;
//	}
//
//	public interface OnThouchAddPoint {
//		void getList(ArrayList<PointF> list, ArrayList<PointF> start_Point,
//				ArrayList<PointF> end_Point);
//	}
//
//	public void setOnTouchIsDown(onTouchIsDown touchIsDow) {
//		this.touchIsDown = touchIsDow;
//	}
//
//	public interface onTouchIsDown {
//		void setDown(boolean isDown);
//	}
//
//	public void setflagChange(int i) {
//		// TODO Auto-generated method stub
//		// this.flag = i;
//		invalidate();
//	}
//
//	@Override
//	public void setServerResult(String resut, int flag) {
//		// TODO Auto-generated method stub
//		if (flag == 4) {
//			if (resut != null) {
//				String[] split = resut.split(",");
//				setRobotX_Y(Double.parseDouble(split[0]),
//						Double.parseDouble(split[1]),
//						Double.parseDouble(split[2]));
//			}
//		}
//	}
//
//	@Override
//	public void setPictureByte(Bitmap bitmap) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void setResultInteger(int result, int flag) {
//		// TODO Auto-generated method stub
//
//	}
//}
