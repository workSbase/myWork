package com.lenovo.lenovorobotmobile.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.utils.MToast;
import com.lenovo.lenovorobotmobile.utils.SharedpreferencesUilts;

public class SettingActivity extends BaseActivity implements
		TextWatcher, OnCheckedChangeListener {

	private static final String TAG = "StettingActivity";
	private EditText ipEditText;
	private EditText portEditText;
	private Button startButton;
	private AlphaAnimation startButtonAppear;
	private AlphaAnimation startButtonDisappear;
	protected boolean isCheckBox_ch;
	protected boolean isCheckBox_en;
	private SharedpreferencesUilts sharedpreferencesUilts;
	private RadioButton childRadioButton_ch;
	private RadioButton childRadioButton_en;

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.settingview);
		ipEditText = (EditText) findViewById(R.id.ipEditText);
		portEditText = (EditText) findViewById(R.id.portEditText);
		portEditText.addTextChangedListener(this);

		startButton = (Button) findViewById(R.id.start);
		startButton.setOnClickListener(this);

		RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
		myRadioGroup.setOnCheckedChangeListener(this);

		childRadioButton_ch = (RadioButton) findViewById(R.id.childRadioButton_ch);
		childRadioButton_en = (RadioButton) findViewById(R.id.childRadioButton_en);
	}

	@Override
	public void initData() {

		initAnimation();

		// 数据回显
		sharedpreferencesUilts = new SharedpreferencesUilts(this);
		String ip = sharedpreferencesUilts.getString("ipText", null);
		String port = sharedpreferencesUilts.getString("portText", null);
		boolean ch_checkBox = sharedpreferencesUilts
				.getBoolean("isCheckBox_ch");
		boolean en_checkBox = sharedpreferencesUilts
				.getBoolean("isCheckBox_en");
		if (ip != null && port != null) {
			ipEditText.setText(ip);
			portEditText.setText(port);
			childRadioButton_ch.setChecked(ch_checkBox);
			childRadioButton_en.setChecked(en_checkBox);
		}
	}

	private void initAnimation() {

		startButtonAppear = new AlphaAnimation(0.1f, 1.0f);
		startButtonAppear.setDuration(3000);
		startButtonAppear.setFillAfter(true);
		startButtonDisappear = new AlphaAnimation(1.0f, 0.1f);
		startButtonDisappear.setDuration(3000);
		startButtonDisappear.setFillAfter(true);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.start:
			String ipText = ipEditText.getText().toString();
			String portText = portEditText.getText().toString();
//			if (ipCheck(ipText) && portCheck(portText)
//					&& (isCheckBox_ch || isCheckBox_en)) {
				// 保存
				sharedpreferencesUilts.saveString("ipText", ipText);
				sharedpreferencesUilts.saveString("portText", portText);
				sharedpreferencesUilts.saveBoolean("isCheckBox_ch",
						isCheckBox_ch);
				sharedpreferencesUilts.saveBoolean("isCheckBox_en",
						isCheckBox_en);
				// startActivity(new Intent(this, LoginActivity.class));

				finish();
			// } else {
			// MToast.showToast(this, "请输入合法内容", 0);
			// }
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.i(TAG, "中间有内容了" + s);
		if (portEditText.getText().toString() != null && !s.equals("")) {
			startButton.setVisibility(View.VISIBLE);
			startButton.startAnimation(startButtonAppear);
		} else {
			startButton.startAnimation(startButtonDisappear);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	/**
	 * 验证ip是否合法
	 * 
	 * @param text
	 *            ip地址
	 * @return 验证信息
	 */
	private boolean ipCheck(String text) {
		if (text != null && !text.isEmpty()) {
			// 定义正则表达式
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			// 判断ip地址是否与正则表达式匹配
			if (text.matches(regex)) {
				// 返回判断信息
				return true;
			} else {
				// 返回判断信息
				return false;
			}
		}
		// 返回判断信息
		return false;
	}

	private boolean portCheck(String text) {
		if (text != null && !text.isEmpty()) {
			return text.matches("[0-9]+");
		}
		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.childRadioButton_ch:
			SettingActivity.this.isCheckBox_ch = true;
			SettingActivity.this.isCheckBox_en = false;
			break;
		case R.id.childRadioButton_en:
			SettingActivity.this.isCheckBox_en = true;
			SettingActivity.this.isCheckBox_ch = false;
			break;
		default:
			break;
		}
	}
}
