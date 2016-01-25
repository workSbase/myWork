package com.lenovo.main.util;

import java.util.ArrayList;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterfaceInfo.LocationForProjectionInfo.ShowPPTInterface;

/**
 * 投影显示帮助类,该类的使用是在locationForProjectionInterface 中使用的
 * 
 */
public class PresentationClass implements OnCompletionListener,
		ShowPPTInterface {

	// private PresentationClass() {
	// }
	//
	// public static final PresentationClass PRESENTATIONCLASS = new
	// PresentationClass();
	//
	// public static PresentationClass getPresentationClassInfo() {
	// return PRESENTATIONCLASS;
	// }
	public PresentationClass(Context context) {
		BaseService.locationForProjectionInterface.setShowPPTInterface(this);
		this.context = context;
		MediaRouter mediaRouter = (MediaRouter) context
				.getSystemService(Context.MEDIA_ROUTER_SERVICE);
		selectedRoute = mediaRouter
				.getSelectedRoute(mediaRouter.ROUTE_TYPE_LIVE_VIDEO);
	}

	private ArrayList<Presentation> presentationList = new ArrayList<Presentation>();
	private ArrayList<String> sourceList = new ArrayList<String>();
	private RouteInfo selectedRoute;
	private Bitmap bitmap;
	private Context context;
	private int bitmapId;
	private VideoView myVideoView1;
	private Presentation myPresentation;
	private Presentation myPresentation2;

	// public void initContext(Context context) {
	//
	// }

	public void releaseSource() {
		if (myPresentation != null) {
			myPresentation = null;
		} else if (myPresentation2 != null) {
			myPresentation2 = null;
		}
	}

	/**
	 * 投影照片 PPT
	 * 
	 * @param bitmapSourceId
	 */
	private void startShowPPTResource(int bitmapSourceId) {
		Toast.makeText(context, "PPT 投影开始展示了", Toast.LENGTH_SHORT).show();
		sourceList.add(0, bitmapSourceId + "");
		if (selectedRoute != null) {
			// 这个就是第二屏幕的对象
			Display presentationDisplay = selectedRoute
					.getPresentationDisplay();
			if (presentationDisplay != null) {
				Presentation myPresentation = new Presentation(context,
						presentationDisplay);
				presentationList.add(myPresentation);

				View view = View.inflate(context,
						R.layout.presentationimageview, null);

				ImageView presentionImage = (ImageView) view
						.findViewById(R.id.presentionImage);
				presentionImage.setBackgroundResource(bitmapSourceId);

				myPresentation.setContentView(view);
				myPresentation.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

				myPresentation.dismiss();
				if (myVideoView1 != null && myVideoView1.isPlaying()) {
					myVideoView1.stopPlayback();
				}
				myPresentation.show();
			} else {
				Toast.makeText(context, "投影还未打开", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 投影视频
	 * 
	 * @param path
	 */
	private void startShowVideoResource(String path) {
		Toast.makeText(context, "Video 投影开始展示了", Toast.LENGTH_SHORT).show();
		sourceList.add(0, path);
		if (selectedRoute != null) {
			Display presentationDisplay = selectedRoute
					.getPresentationDisplay();
			if (presentationDisplay != null) {
				Presentation myPresentation = new Presentation(context,
						presentationDisplay);
				presentationList.add(myPresentation);
				View view = View.inflate(context,
						R.layout.presentationvideoview, null);
				myVideoView1 = (VideoView) view.findViewById(R.id.myVideoView);
				myVideoView1.setOnCompletionListener(this);

				myVideoView1.setVideoPath(path);
				myVideoView1.start();
				myPresentation.setContentView(view);
				myPresentation.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

				myPresentation.dismiss();
				myPresentation.show();
				myPresentation = null;
			} else {
				Toast.makeText(context, "投影还未打开", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean dismissPresentationList() {
		if (presentationList != null && presentationList.size() > 0) {
			for (Presentation presentation : presentationList) {
				presentation.dismiss();
			}
			presentationList.clear();
			return true;
		}
		return false;
	}

	/**
	 * 显示最后一次显示的内容
	 */
	public void showLastSource() {
		String string = sourceList.get(0);
		if (string != null) {
			if (string.contains(".mp4") && string.endsWith(".mp4")) {
				startShowVideoResource(string);
			} else {
				int parseInt = Integer.parseInt(string);
				startShowPPTResource(parseInt);
			}
		}
	}

	// 设置播放完成的回调
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// 这行代码打开的,视频播放完成的时候,就会把投影给关闭掉
		BaseService.projectorInterface.CloseProjector();
	}

	public void dismissDialog() {
		// TODO Auto-generated method stub
		if (myPresentation != null) {
			myPresentation.dismiss();
		} else if (myPresentation2 != null) {
			myPresentation2.dismiss();
		}
	}

	/**
	 * 这个就是机器人在途中的和到位置了以后的回调
	 */
	@Override
	public void setImageIndex(int index) {
		// if (index == 1) {
		// int imageIndex = CameraActivity.pictureId_video;
		// int pictureId_Camera = Source.getImageSourceId(imageIndex);
		// if (Source.setIndex(pictureId_Camera)) {
		// startShowPPTResource(pictureId_Camera);
		// } else {
		// String videoPath = Source.getVideoPath(imageIndex);
		// if (videoPath != null) {
		// startShowVideoResource(videoPath);
		// } else {
		// Toast.makeText(context, "该视频不存在", Toast.LENGTH_SHORT)
		// .show();
		// }
		// }
		// } else if (index == 0) {
		// Toast.makeText(context, "正在前往的途中", Toast.LENGTH_SHORT).show();
		// }
	}
}
