package cn.com.lenovo.videoplayer.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class PackageUtils {
	
	public static String getTopActivity(Context context)
	  {
	       ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;
	       List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
	           
	       if(runningTaskInfos != null)
	         return (runningTaskInfos.get(0).topActivity).toString() ;
	            else
	         return null ;
	  }
}
