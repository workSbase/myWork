package com.lenovo.lenovorobotmobile.view;

import com.lenovo.lenovorobotmobile.utils.SharedpreferencesUilts;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.view.View;

/**
 * 所有 View 对象的一个基类.初始化一些子类都需要 用到的 东西.往外暴露两个 抽象方法
 * 
 * @author Administrator
 * 
 */
public abstract class BaseView extends View {

	protected Context context;
	protected Options options = new Options();
	protected Paint paint = new Paint();
	/**
	 * 缩放比例
	 */
	public int SCALING = 1;

	public Typeface typeface;

	public SharedpreferencesUilts sharedpreferencesUilts;
	public boolean ch_checkBox;
	public boolean en_checkBox;

	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/lenovo_font.ttf");

		sharedpreferencesUilts = new SharedpreferencesUilts(context);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");

		initLayout();
		initData();
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/lenovo_font.ttf");

		sharedpreferencesUilts = new SharedpreferencesUilts(context);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");

		initLayout();
		initData();
	}

	public BaseView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/lenovo_font.ttf");
		sharedpreferencesUilts = new SharedpreferencesUilts(context);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");
		initLayout();
		initData();
	}

	public abstract void initData();

	public abstract void initLayout();
}
