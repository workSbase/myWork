package com.lenovo.lenovorobotmobile.activityVideo;

import com.lenovo.lenovorobotmobile.activity.BaseActivity;

import io.agora.rtc.IRtcEngineEventHandler;

public class MessageHandler extends IRtcEngineEventHandler {

	private BaseActivity mHandlerActivity;

	public void setActivity(BaseActivity activity) {

		this.mHandlerActivity = activity;
	}

	public BaseActivity getActivity() {

		return mHandlerActivity;
	}

	// 返回错误
	@Override
	public void onError(int err) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onError(err);
		}
	}

	// 加入房间
	@Override
	public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onJoinChannelSuccess(channel, uid, elapsed);
		}
	}

	// 显示房间内其他用户的视频
	@Override
	public void onFirstRemoteVideoDecoded(int uid, int width, int height,
			int elapsed) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
		}
	}

	// 用户进入
	@Override
	public void onUserJoined(int uid, int elapsed) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onUserJoined(uid, elapsed);
		}
	}

	// 用户退出
	@Override
	public void onUserOffline(int uid) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onUserOffline(uid);
		}
	}

	// 监听其他用户是否关闭视频
	@Override
	public void onUserMuteVideo(int uid, boolean muted) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onUserMuteVideo(uid, muted);
		}
	}

	// 监听其他用户是否关闭音频
	@Override
	public void onUserMuteAudio(int uid, boolean muted) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onUserMuteAudio(uid, muted);
		}
	}

	// 监听网络质量
	@Override
	public void onNetworkQuality(int quality) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onNetworkQuality(quality);
		}
	}

	// 监听通话质量
	@Override
	public void onAudioQuality(int uid, int quality, short delay, short lost) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onAudioQuality(uid, quality, delay, lost);
		}
	}

	// 更新聊天数据
	@Override
	public void onUpdateSessionStats(SessionStats stats) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onUpdateSessionStats(stats);
		}
	}

	// 离开频道
	@Override
	public void onLeaveChannel(SessionStats stats) {

		BaseActivity activity = getActivity();

		if (activity != null) {
			activity.onLeaveChannel(stats);
		}
	}

}
