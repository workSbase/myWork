package com.lenovo.lenovorobotmobile.activity;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.activityVideo.TesterApplication;
import com.lenovo.lenovorobotmobile.utils.MToast;

/**
 * 开始连接服务器的操作
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	private Button login;
	// private AlertDialog mLoginFailedDialog;
	// private ProgressDialog mProgressDialog;
	private EditText userNameEditText;
	private EditText passWorldEditText;
	private String userName;
	private String passWorld;

	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// int key = msg.what;
	// switch (key) {
	// case 1:
	// mProgressDialog.dismiss();
	// startActivity(new Intent(LoginActivity.this,
	// FriendActivity.class));
	// break;
	// case 2:
	// MToast.showToast(LoginActivity.this, "用户登录失败", 0);
	// break;
	// }
	// };
	// };

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.login);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);

		userNameEditText = (EditText) findViewById(R.id.userName);
		passWorldEditText = (EditText) findViewById(R.id.passWorld);
	}

	// 重返界面的时候调用该方法
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (!connectServer.getIsConnect()) {
			initData();
		}

		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");

		if (en_checkBox) {
			login.setText("login");
		} else if (ch_checkBox) {
			login.setText("登陆");
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (ip != null && port != null) {
			// 开始连接服务器,这个时候服务器已经开始连接了
			connectServer.startConnect(ip, Integer.parseInt(port));
			if (en_checkBox) {
				login.setText("login");
			} else if (ch_checkBox) {
				login.setText("登陆");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_settings:
			MToast.showToast(this, "设置", 0);
			// 这个地方就是可以,去清理一些 常用的保存,页面
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	/**
	 * 按钮的点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login:
			String stringKey = userNameEditText.getText().toString();
			Log.i(TAG, stringKey);
			MToast.showToast(LoginActivity.this, stringKey, 0);
			((TesterApplication) getApplication()).setUserInformation(
					"6d10928c053d480988df9e8ee811671f", passWorldEditText
							.getText().toString());
			if (ip != null && port != null && (ch_checkBox || en_checkBox)) {
				startActivity(new Intent(LoginActivity.this,
						FriendActivity.class));
			} else {
				MToast.showToast(LoginActivity.this, "请到设置页面初始化", 0);
				startActivity(new Intent(LoginActivity.this,
						SettingActivity.class));
			}

			break;
		}
	}
}
