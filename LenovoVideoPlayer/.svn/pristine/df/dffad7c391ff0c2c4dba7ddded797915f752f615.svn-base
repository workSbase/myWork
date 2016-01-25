package cn.com.lenovo.videoplayer.utils;

import java.util.List;
import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.view.Display;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;
import cn.com.lenovo.videoplayer.R;
import cn.com.lenovo.videoplayer.VideoListActivity;
import cn.com.lenovo.videoplayer.videofile.Video;

public class ProjecterUtils implements OnCompletionListener {

	private Context context;
	private RouteInfo selectedRoute;
	private VideoView myVideoView;
	private View view;
	private Presentation myPresentation;
	private Display presentationDisplay;
	private List<Video> listVideos;
	private int videoIndex;

	@SuppressWarnings("static-access")
	public ProjecterUtils(Context context) {
		this.context = context;
		MediaRouter mediaRouter = (MediaRouter) context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
		selectedRoute = mediaRouter.getSelectedRoute(mediaRouter.ROUTE_TYPE_LIVE_VIDEO);
		view = View.inflate(context, R.layout.projecterview, null);

		listVideos = VideoListActivity.listVideos;

		presentationDisplay = selectedRoute.getPresentationDisplay();
		if (presentationDisplay == null) {
			presentationDisplay = selectedRoute.getPresentationDisplay();
		}
		if (presentationDisplay != null) {
			myPresentation = new Presentation(context, presentationDisplay);

			myPresentation.setContentView(view);

		}

		myVideoView = (VideoView) view.findViewById(R.id.myVideoView);

		myVideoView.setOnCompletionListener(this);
	}

	/**
	 * 条目点击开始播放视频
	 * 
	 * @param path
	 * @return
	 */
	public boolean startVideo(String path) {

		myVideoView.stopPlayback();

		if (myVideoView.isPlaying()) {
			myVideoView.pause();
		}
		if (path != null) {
			myVideoView.setVideoPath(path);
		}
		myVideoView.start();

		if (myPresentation != null) {

			myPresentation.show();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (myPresentation != null) {
			myPresentation.dismiss();
		}
	}

	/**
	 * 语音控制开始播放视频
	 * 
	 * @param i
	 */
	public void startVideo(int i) {
		this.videoIndex = i;
		startVideo(listVideos.get(i).getPath());
	}

	/**
	 * 下一条视频
	 */
	public void nextStartVideo() {
		if (videoIndex < listVideos.size()) {
			startVideo(listVideos.get(videoIndex++).getPath());
		} else {
			Toast.makeText(context, "已经是最后一个了", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 上一条视频
	 */
	public void upStartVideo() {
		if (videoIndex < listVideos.size() && videoIndex >= 0) {
			startVideo(listVideos.get(videoIndex--).getPath());
		} else {
			Toast.makeText(context, "已经是第一个了", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean getPresentationInfo() {
		if (myPresentation != null) {
			return true;
		}
		return false;
	}

	/**
	 * 消除当前的dialog
	 */
	public void dimissDialog() {
		if (myPresentation != null) {
			myPresentation.dismiss();
		}
	}
}
