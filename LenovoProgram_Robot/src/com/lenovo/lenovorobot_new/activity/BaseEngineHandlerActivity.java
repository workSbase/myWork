package com.lenovo.lenovorobot_new.activity;

import io.agora.rtc.IRtcEngineEventHandler;
import android.app.Activity;
import android.view.View;

public class BaseEngineHandlerActivity extends Activity {

	public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
	}

	public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
	}

	public void onError(int err) {
	}

	public void onCameraReady() {
	}

	public void onAudioQuality(int uid, int quality, short delay, short lost) {
	}

	public void onAudioTransportQuality(int uid, short delay, short lost) {
	}

	public void onVideoTransportQuality(int uid, short delay, short lost) {
	}

	public void onLeaveChannel(IRtcEngineEventHandler.SessionStats stats) {
	}

	public void onUpdateSessionStats(IRtcEngineEventHandler.SessionStats stats) {
	}

	public void onRecap(byte[] recap) {
	}

	public void onAudioVolumeIndication(
			IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
	}

	public void onNetworkQuality(int quality) {
	}

	public void onUserJoined(int uid, int elapsed) {
	}

	public void onUserOffline(int uid) {
	}

	public void onUserMuteAudio(int uid, boolean muted) {
	}

	public void onUserMuteVideo(int uid, boolean muted) {
	}

	public void onUserBitrateChanged(int uid, boolean lowBitrate) {
	}

	public void onAudioRecorderException(int nLastTimeStamp) {
	}

	public void onRemoteVideoStat(int uid, int frameCount, int delay,
			int receivedBytes, int width, int height) {
	}

	public void onLocalVideoStat(int sentBytes, int sentFrames, int sentQP,
			int sentRtt, int sentLoss) {
	}

	public void onFirstRemoteVideoFrame(int view, int uid, int width,
			int height, int elapsed) {
	}

	public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
	}

	public void onFirstRemoteVideoDecoded(int uid, int width, int height,
			int elapsed) {
	}

	public void onConnectionLost() {
	}

	public void onMediaEngineEvent(int code) {
	}

	public void onUserInteraction(View view) {
	}
}
