package cn.com.lenovo.homepager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 模板设计模式
 * 
 * @author kongqw
 * 
 */
public abstract class HomePageBaseActivity extends Activity {
	// 1 定义手势识别器
	private GestureDetector detector;
	protected SharedPreferences sp;

	public abstract void singleTapUp();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		// 2 初始化手势识别器
		detector = new GestureDetector(this, new MySimpleOnGestureListener());

	}

	/**
	 * 手势识别器
	 */
	private class MySimpleOnGestureListener extends SimpleOnGestureListener {


		/**
		 * 点击屏幕
		 */
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			singleTapUp();
			return super.onSingleTapUp(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			send(0);
		}

	}

	/**
	 * 所有触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 4 注册手势识别器
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * 发送表情切换的广播 睁眼 0 闭眼 1 闭右眼 2 闭左眼 3 生气 4
	 * 
	 * @param flag
	 */
	private void send(int flag) {
		Intent intent = new Intent();
		intent.setAction("cn.com.lenovo.homepager");
		intent.putExtra("face", flag);
		sendBroadcast(intent);
	}

}
