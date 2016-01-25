package com.lenovo.contactslist.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * 用户切换时候的广播
 * 
 * @author Administrator
 * 
 */
public class MyUserChangeBroadCastReceiver extends BroadcastReceiver {

	private SharedPreferences sharedPreferences;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String userFlag = intent.getStringExtra("userFlag");

		saveUserFlag(context, userFlag);
	}

	private void saveUserFlag(Context context, String userFlag) {
		// TODO Auto-generated method stub
		if (userFlag != null && !userFlag.equals("")) {
			sharedPreferences = context.getSharedPreferences("userFlagConfig",
					Context.MODE_PRIVATE);
			boolean commit = sharedPreferences.edit()
					.putString("userFlag", userFlag).commit();
			if (commit) {
				Toast.makeText(context, "主叫App保存成功", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
