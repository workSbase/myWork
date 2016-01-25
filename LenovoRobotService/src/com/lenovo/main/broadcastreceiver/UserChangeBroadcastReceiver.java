//package com.lenovo.main.broadcastreceiver;
//
//import android.os.Bundle;
//
//import android.os.Handler;
//import android.os.Message;
//import com.lenovo.main.MIService.MyService;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.widget.Toast;
//
///**
// * 用户角色变动的时候,该广播就可以接受的到
// * 
// * @author Administrator
// */
//public class UserChangeBroadcastReceiver extends BroadcastReceiver {
//	public int userFlag_numebr;
//	private Handler myServiceHandler;
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		/**
//		 * 开启 service
//		 */
//		String userName = intent.getStringExtra("userName");
//		String passWorld = intent.getStringExtra("userPassworld");
//		String userFlag = intent.getStringExtra("flag");
//		if (userFlag != null && userFlag.equals("CHINA")) {
//			this.userFlag_numebr = 1;
//		} else if (userFlag != null && userFlag.equals("ENGLISH")) {
//			this.userFlag_numebr = 2;
//		}
//		// 发送一个广播告诉别的app 角色改变了
//		sendBroadCastReceiverTellAnotherApp(context, userFlag);
//
//		// 把变换的角色存放在 preferences 里面
//		saveUserFlag(context, userFlag);
//
//		// 得到MyService 中的一个静态的 handler
//		myServiceHandler = MyService.myServiceHandler;
//
//		Message message = new Message();
//
//		Bundle bundle = new Bundle();
//		bundle.putString("userName", userName);
//		bundle.putString("passWorld", passWorld);
//		bundle.putInt("userFlag_numebr", userFlag_numebr);
//		message.setData(bundle);
//		message.what = -1;
//		// 把相关的数据发送到静态的handler 中来完成
//		if (myServiceHandler != null) {
//			myServiceHandler.sendMessage(message);
//		}
//	}
//
//	private void saveUserFlag(Context context, String userFlag) {
//		// TODO Auto-generated method stub
//		SharedPreferences preferences = context.getSharedPreferences(
//				"userChangeCinfig", Context.MODE_PRIVATE);
//		boolean commit = preferences.edit().putString("userFlag", userFlag)
//				.commit();
//		if (!commit) {
//			Toast.makeText(context, "用户角色保存失败", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	private void sendBroadCastReceiverTellAnotherApp(Context context,
//			String userFlag) {
//		Intent intent = new Intent();
//		intent.setAction("com.example.demo_userChange");
//		intent.putExtra("userFlag", userFlag);
//		context.sendBroadcast(intent);
//	}
// }
