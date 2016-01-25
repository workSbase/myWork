package com.lenovo.lenovorobotmobile.utils;

import com.lenovo.lenovorobot.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 显示聊天内容的,里面封装的就是一个LinearLayout,向左右两边,插入数据
 * 
 * @author Administrator
 */
public class ChatsContentShowTools {

	private static final String TAG = "LinearLayoutHelpClass";
	private Context context;
	private LinearLayout layout;

	private ScrollView myScrollViewv;
	private Typeface typeface;

	public ChatsContentShowTools(Context context, LinearLayout layout,
			ScrollView myScrollView) {
		this.myScrollViewv = myScrollView;
		this.layout = layout;
		this.context = context;
	}

	/**
	 * 设置字体
	 * 
	 * @param typeface
	 */
	public void setTypeface(Typeface typeface) {
		// TODO Auto-generated method stub
		this.typeface = typeface;
	}

	/**
	 * 显示 人说话的内容,
	 * 
	 * @param content
	 */
	public void addReightView(String content) {
		Log.i(TAG, "右边添加数据了");
		View view = View.inflate(context, R.layout.liftview, null);
		TextView showContentView = (TextView) view
				.findViewById(R.id.showContentView);
		showContentView.setTypeface(typeface);
		showContentView.setText(content);
		layout.addView(view);

		// View 置为 null ,让 GC 回收用完的View
		view = null;

		/**
		 * 调用系统的 垃圾回收器,回收 null 对象至于什么时候调用,那就 要看 JVM
		 */
		System.gc();

		showDown();
	}

	/**
	 * 后面一个int 型的参数,表示显示不同的 布局,最后面是 图片的 ID 不显示图片 flag 为 false imageSource 为 0.
	 * 
	 * @param content
	 * @param flag
	 */
	public void addLiftView(String content, boolean flag, int imageSource) {

		View view = View.inflate(context, R.layout.rightview, null);
		TextView showContentView = (TextView) view
				.findViewById(R.id.showContentView_1);
		showContentView.setTypeface(typeface);
		ImageView tasckImage = (ImageView) view.findViewById(R.id.tasckImage);
		if (flag) {
			showContentView.setVisibility(View.GONE);
			tasckImage.setVisibility(View.VISIBLE);
			tasckImage.setBackgroundResource(imageSource);
		}
		showContentView.setText(content);
		layout.addView(view);

		// View 置为 null ,让 GC 回收用完的View
		view = null;
		/**
		 * 调用系统的 垃圾回收器,回收 null 对象至于什么时候调用,那就 要看 JVM
		 */
		System.gc();
		showDown();
	}

	public void addReightTextView(View child) {
		layout.addView(child, new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.RIGHT));
	}

	/**
	 * 判断 linearLayout 中的 子View 的个数,如果是超过一定范围的话,还可以删除
	 */
	public void isDeleteChildView() {
		int childCount = layout.getChildCount();
		if (childCount > 20) {
			for (int i = 0; i < childCount; i++) {
				if (i < 10) {
					View childAt = layout.getChildAt(i);
					layout.removeView(childAt);
					childAt = null;
				}
			}
		}
	}

	/**
	 * 如果 屏幕显示 不下的话,会显示最后面的 加上 ScrollViewv 的目的就是为了这样
	 */
	private void showDown() {
		myScrollViewv.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				myScrollViewv.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
}
