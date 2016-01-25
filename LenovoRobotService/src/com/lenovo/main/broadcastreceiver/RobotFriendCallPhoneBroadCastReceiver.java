package com.lenovo.main.broadcastreceiver;

import com.lenovo.main.util.Constant;
import com.lenovo.main.util.SendBroadCastTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * @author Administrator 这个广播主要是给主叫 app 使用的.当主叫 app 点击其中每一条好友的时候,这个时候就会发送相应的广播
 */

public class RobotFriendCallPhoneBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_MULTI_PROCESS);
		sp.edit().putInt("value", 6).commit();

		sp.edit().putInt("followFlag", 0).commit();

		SendBroadCastTools.myBroadCast(context, Constant.SPEECH_ACTION,
				Constant.STOP_SPEECH_FLAG, true, true);
	}
}