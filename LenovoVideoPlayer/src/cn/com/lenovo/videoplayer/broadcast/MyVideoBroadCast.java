package cn.com.lenovo.videoplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.com.lenovo.videoplayer.VideoListActivity;
import cn.com.lenovo.videoplayer.utils.PackageUtils;

public class MyVideoBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String activityName = "ComponentInfo{cn.com.lenovo.videoplayer/cn.com.lenovo.videoplayer.VideoListActivity}";
		String topActivityName = PackageUtils.getTopActivity(context);

		if (VideoListActivity.projecterUtils != null && VideoListActivity.projecterUtils.getPresentationInfo()) {
			if (topActivityName.equals(activityName)) {
				int intExtra = -1;
				String stringExtra = intent.getStringExtra("key");
				if (stringExtra != null) {
					intExtra = Integer.parseInt(stringExtra);
				}
				if (intExtra != -1) {
					switch (intExtra) {
					case 0:
						// 播放第一条
						VideoListActivity.projecterUtils.startVideo(1);

						break;
					case 1:
						// 上一条
						VideoListActivity.projecterUtils.upStartVideo();
						break;

					case 2:
						// 下一条
						VideoListActivity.projecterUtils.nextStartVideo();
						break;
					}
				}
			} else {
				Toast.makeText(context, "请到视频列表界面", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "投影还没有打开请点击播放视频", Toast.LENGTH_SHORT).show();
		}
	}
}
