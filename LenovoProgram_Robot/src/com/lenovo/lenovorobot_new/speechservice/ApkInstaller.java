package com.lenovo.lenovorobot_new.speechservice;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;

import com.iflytek.cloud.SpeechUtility;

/**
 * 弹出提示框，下载服务组件
 */
public class ApkInstaller {
	private Context mActivity;
	private AlertDialog mAlertDialog;

	public ApkInstaller(Context activity) {
		mActivity = activity;
	}

	public void install() {
		AlertDialog.Builder builder = new Builder(mActivity);

		builder.setMessage("检测到您未安装语记！\n是否前往下载语记？");
		builder.setTitle("下载提示");
		builder.setPositiveButton("确认前往", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAlertDialog.dismiss();
				String url = SpeechUtility.getUtility().getComponentUrl();
				String assetsApk = "SpeechService.apk";
				processInstall(mActivity, url, assetsApk);
			}
		});
		builder.setNegativeButton("残忍拒绝", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		mAlertDialog = builder.create();
		mAlertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mAlertDialog.show();
		return;
	}

	/**
	 * 如果服务组件没有安装打开语音服务组件下载页面，进行下载后安装。
	 */
	private boolean processInstall(Context context, String url, String assetsApk) {
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
		return true;
	}
}
