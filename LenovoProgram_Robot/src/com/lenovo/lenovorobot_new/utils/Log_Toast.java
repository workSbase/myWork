package com.lenovo.lenovorobot_new.utils;

import com.google.gson.Gson;
import com.lenovo.lenovorobot_new.bin.LogToastContorlBin;
import com.lenovo.lenovorobot_new.utils.FileLoadUtils.FileLoadListener;

import android.content.Context;
import android.util.Log;

/**
 * 自定义的Log和Toast,判断是否打印log和toast的判断条件全部放在,文件中
 * 
 * @author ZXC
 * 
 */
public class Log_Toast implements FileLoadListener {
	private boolean isLog;
	private boolean isToast;
	private Context context;
	private Gson gson;
	private FileLoadUtils fileLoadUtilsInfo;

	public Log_Toast(Context context) {
		this.context = context;
		gson = new Gson();
		if (fileLoadUtilsInfo == null) {
			fileLoadUtilsInfo = FileLoadUtils.getFileLoadUtilsInfo();
			fileLoadUtilsInfo.loadFile(context, "log_toastcontorl");
			fileLoadUtilsInfo.setOnFileLoadListenerInfo(this);
		}
	}

	public void i(String TAG, String content) {
		if (isLog)
			Log.i(TAG, content);
	}

	public void Toast(String showContent, int time) {
		if (isToast)
			android.widget.Toast.makeText(context, showContent, time).show();
	}

	@Override
	public void setFileResult(String result) {
		LogToastContorlBin fromJson = gson.fromJson(result, LogToastContorlBin.class);
		this.isLog = fromJson.islog;
		this.isToast = fromJson.isToast;
	}

	@Override
	public void setError(int code) {

	}
}
