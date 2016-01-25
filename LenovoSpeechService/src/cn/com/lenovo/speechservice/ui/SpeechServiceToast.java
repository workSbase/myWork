package cn.com.lenovo.speechservice.ui;

import cn.com.lenovo.speechservice.R;
import cn.com.lenovo.speechservice.utils.Constant;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 自定义Toast
 * 显示录音状态信息
 * @author kongqw
 *
 */
public class SpeechServiceToast {
	// 窗口的管理者
	private static WindowManager windowManager;
	// 界面布局对象
	private static View view;
	// 自定义Toast对象
	private static SpeechServiceToast speechServiceToast;

	private static int widthPixels;
	private static int heightPixels;

	private static SharedPreferences mSharedPreferences;

	/*
	 * 构造方法私有化
	 * 不允许new对象
	 */
	private SpeechServiceToast() {
	}

	// 单例设计模式
	public static SpeechServiceToast getInstance() {
		if (null == speechServiceToast) {
			speechServiceToast = new SpeechServiceToast();
		}
		return speechServiceToast;
	}

	/**
	 * 显示自定义Toast
	 * @param context 上下文
	 * @param showText 显示的文字
	 */
	public void showMyToast(Context context, String showText) {

		// 关闭之前打开的Toast
		hideMyToast();

		// 初始化窗口的管理者
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// 获取手机屏幕尺寸
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		widthPixels = outMetrics.widthPixels;
		heightPixels = outMetrics.heightPixels;

		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

		// 加载自定义布局
		view = View.inflate(context, R.layout.speechservice_toast, null);
		// 给控件添加触摸移动事件
		onTouchListener();

		// 获取自定义布局的TextView控件
		TextView tv_speechservice = (TextView) view.findViewById(R.id.tv_speechservice);

		// 设置控件显示内容
		tv_speechservice.setText(showText);

		// 创建布局的参数
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		/*
		 * 设置Toast的位置
		 * 以距离顶部距离和左侧距离作为定位标准
		 */
		params.gravity = Gravity.LEFT + Gravity.TOP;

		// 设置Toast显示位置
		//		params.x = Constant.TOAST_LOCATION_X; // sharedPreferences
		//		params.y = Constant.TOAST_LOCATION_Y;
		params.x = mSharedPreferences.getInt("Left", 0);
		params.y = mSharedPreferences.getInt("Top", 0);

		// 高度包裹内容
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;

		// 宽度包裹内容
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		/*
		 * 不可获得焦点
		 * 不可触摸
		 * 屏幕常亮
		 */
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

		// 半透明
		params.format = PixelFormat.TRANSLUCENT;

		/*
		 * 设置Toast类型
		 * 小米使用TYPE_TOAST类型就可以触摸
		 * 模拟器使用TYPE_PRIORITY_PHONE类型就可以触摸
		 */
		params.type = WindowManager.LayoutParams.TYPE_TOAST;

		// 显示自定义Toast
		windowManager.addView(view, params);
	}

	/**
	 * 自定义Toast的触摸事件
	 */
	private static void onTouchListener() {
		view.setOnTouchListener(new OnTouchListener() {

			// 手指按下屏幕的坐标
			private int rawX;
			private int rawY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					System.out.println("按下");
					rawX = (int) event.getRawX();
					rawY = (int) event.getRawY();
					System.out.println(rawX + "   " + rawY);
					break;

				case MotionEvent.ACTION_MOVE:// 移动
					System.out.println("移动");
					// 获取手指新的位置
					int newRawX = (int) event.getRawX();
					int newRawY = (int) event.getRawY();

					// 计算移动的差值
					int moveX = newRawX - rawX;
					int moveY = newRawY - rawY;
					// 重新设置控件的显示位置
					WindowManager.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
					layoutParams.x += moveX;
					layoutParams.y += moveY;

					// 处理手机屏幕移动出界问题
					if (layoutParams.x < 0) {
						layoutParams.x = 0;
					}
					if (layoutParams.y < 0) {
						layoutParams.y = 0;
					}
					if (layoutParams.x + view.getWidth() > widthPixels) {
						layoutParams.x = widthPixels - view.getWidth();
					}
					if (layoutParams.y + view.getHeight() > heightPixels - 30) {
						layoutParams.y = heightPixels - 30 - view.getHeight();
					}

					windowManager.updateViewLayout(view, layoutParams);

					// 将手指的位置更新为新的位置
					rawX = newRawX;
					rawY = newRawY;
					break;

				case MotionEvent.ACTION_UP:// 抬起
					System.out.println("抬起");
					WindowManager.LayoutParams params = (LayoutParams) view.getLayoutParams();
					int left = params.x;
					int top = params.y;
					// 将移动后的坐标坐标保存到sp
					Editor edit = mSharedPreferences.edit();
					// 要保存的左边距
					edit.putInt("Left", left);
					// 要保存的上边距
					edit.putInt("Top", top);
					edit.commit();
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	/**
	 * 关闭自定义Toast
	 */
	public void hideMyToast() {
		if (null != windowManager && view != null) {
			windowManager.removeView(view);
			windowManager = null;
		}
	}
}
