package com.lenovo.lenovorobotmobile.view;

import java.util.Random;
import com.lenovo.lenovorobot.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;

/**
 * 这个是带有缩放的显示的一个View用来显示当前扫秒的地图
 * 
 * @author Administrator
 * 
 */
public class CreateUpdataMapView extends View {

	private GestureDetector gd;
	private ScaleGestureDetector sGD;
	// 缩放比例
	private float scaleRate;
	float scrollingOffsetX;
	float scrollingOffsetY;
	private Bitmap bitmap;
	private Paint paint;
	private int mapWidth;
	private int mapHeight;

	public CreateUpdataMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		gd = new GestureDetector(context, new MInnerGestureListener());
		sGD = new ScaleGestureDetector(context, new MScaleListener());
		scaleRate = 1.0f;

		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.b2map_02);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 设置,拖动,和缩放
		canvas.translate(scrollingOffsetX, scrollingOffsetY);

		canvas.scale(scaleRate, scaleRate);

		createMapmother(canvas);

		// drawPathPoint(canvas);

	}

	/**
	 * 画路径点
	 * 
	 * 
	 * private void drawPathPoint(Canvas canvas) {
	 * 
	 * if (pointList != null && pointList.size() > 0) { // for (int i = 0; i <
	 * pointList.size(); i++) { // paint.setColor(getColor()); // Point
	 * currentPoint = pointList.get(i); // canvas.drawCircle(currentPoint.x *
	 * scaleRate, currentPoint.y // * scaleRate, 8, paint); // if (i !=
	 * pointList.size() - 1) { // Point nextPoint = pointList.get(i + 1); //
	 * canvas.drawLine(currentPoint.x * scaleRate, currentPoint.y // *
	 * scaleRate, nextPoint.x * scaleRate, nextPoint.y // * scaleRate, paint);
	 * // } // } } }
	 */
	/**
	 * 画图
	 * 
	 * @param canvas
	 */
	private void createMapmother(Canvas canvas) {
		if (bitmap != null) {
			paint.setAlpha(255);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);

			canvas.drawBitmap(bitmap, null,
					new Rect(0, 0, mapWidth, mapHeight), null);

			// canvas.scale(1.0f / scaleRate, 1.0f / scaleRate);
			// canvas.drawCircle((float) 100 * scaleRate, (float) 100 *
			// scaleRate,
			// 10, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gd.setIsLongpressEnabled(true);
		sGD.onTouchEvent(event);
		return gd.onTouchEvent(event);
	}

	// 拖动
	class MInnerGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			// isScroll = true;
			scrollingOffsetY += -distanceY;
			scrollingOffsetX += -distanceX;
			invalidate();

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	// 缩放
	class MScaleListener extends SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// 缩放比例
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

	// 传递过来的图片
	public void setBitmap(Bitmap map) {
		// TODO Auto-generated method stub
		this.bitmap = map;
		int width = map.getWidth();
		int height = map.getHeight();
		this.mapWidth = width;
		this.mapHeight = height;
		invalidate();
	}

	/*
	 * public void setPonitList(List<Point> pointList) { this.pointList =
	 * pointList; }
	 */

	public int getColor() {
		int[] colorInt = { Color.RED, Color.BLUE, Color.GRAY, Color.GREEN };
		Random random = new Random();
		int nextInt = random.nextInt(colorInt.length - 1);
		return colorInt[nextInt];
	}
}
