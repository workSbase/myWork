package cn.com.lenovo.videoplayer;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * 模板设计模式
 * 
 * @author kongqw
 * 
 */
public abstract class VideoPlayerBaseActivity extends Activity {
	// 1 定义手势识别器
	private GestureDetector detector;
	protected SharedPreferences sp;

	// 向左滑动
	public abstract void slideToLeft();

	// 向右滑动
	public abstract void slideToRight();

	// 屏幕被点击
	public abstract void clickScreen();

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
		 * e1 第一次按下的事件 e2 离开屏幕的事件 velocityX 水平方向移动速率 velocityY 垂直方向移动速率
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int widthPixels = dm.widthPixels;
			int heightPixels = dm.heightPixels;
			// Toast.makeText(VideoPlayerBaseActivity.this, "宽 ： " + widthPixels
			// + "\n高 ：" + heightPixels, Toast.LENGTH_SHORT).show();

			int startRawX = (int) e1.getRawX();
			int startRawY = (int) e1.getRawY();
			int endRawX = (int) e2.getRawX();
			int endRawY = (int) e2.getRawY();

			// 向左滑动
			if ((startRawX - endRawX) > 200 && (Math.abs(startRawY - endRawY) < 200)) {
				slideToLeft();
			}
			// 向右滑动
			if ((endRawX - startRawX) > 200 && (Math.abs(startRawY - endRawY) < 200)) {
				slideToRight();
			}
			// 屏幕右侧向上滑动
			if ((widthPixels / 2) < startRawX && (heightPixels / 2) < startRawY && (startRawY - endRawY) > 200 && Math.abs(startRawX - endRawX) < 200) {
				// Toast.makeText(VideoPlayerBaseActivity.this, "音量增加",
				// Toast.LENGTH_SHORT).show();
				volumeUp();
			}
			// 屏幕右侧向下滑动
			if ((widthPixels / 2) < startRawX && (heightPixels / 2) > startRawY && (endRawY - startRawY) > 200 && Math.abs(startRawX - endRawX) < 200) {
				// Toast.makeText(VideoPlayerBaseActivity.this, "音量减小",
				// Toast.LENGTH_SHORT).show();
				volumeDown();
			}
			// 屏幕左侧向上滑动
			if ((widthPixels / 2) > startRawX && (heightPixels / 2) < startRawY && (startRawY - endRawY) > 200 && Math.abs(startRawX - endRawX) < 200) {
				// Toast.makeText(VideoPlayerBaseActivity.this, "亮度增加",
				// Toast.LENGTH_SHORT).show();
				setScreenBrightness(250);
			}
			// 屏幕左侧向下滑动
			if ((widthPixels / 2) > startRawX && (heightPixels / 2) > startRawY && (endRawY - startRawY) > 200 && Math.abs(startRawX - endRawX) < 200) {
				// Toast.makeText(VideoPlayerBaseActivity.this, "亮度降低",
				// Toast.LENGTH_SHORT).show();
				setScreenBrightness(0);
			}

			return true;
		}

		/**
		 * 点击屏幕
		 */
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			clickScreen();
			return super.onSingleTapUp(e);
		}

		/**
		 * 保存当前的屏幕亮度值，并使之生效
		 */
		private void setScreenBrightness(int paramInt) {
			Window localWindow = getWindow();
			WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
			float f = paramInt / 255.0F;
			localLayoutParams.screenBrightness = f;
			localWindow.setAttributes(localLayoutParams);
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
	 * 增加音量
	 */
	public void volumeUp() {
		AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
	};

	/**
	 * 降低音量
	 */
	public void volumeDown() {
		AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
	};

}
