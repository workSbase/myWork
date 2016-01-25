package com.lenovo.lenovorobotmobile.activity;

import io.agora.rtc.IRtcEngineEventHandler;
import android.view.View;
import com.google.gson.Gson;
import com.lenovo.lenovorobotmobile.MInterface.ServerPacketInterfaceObserver;
import com.lenovo.lenovorobotmobile.MInterface.UserStatesChangeInterface;
import com.lenovo.lenovorobotmobile.activityVideo.CameraActivity;
import com.lenovo.lenovorobotmobile.activityVideo.TesterApplication;
import com.lenovo.lenovorobotmobile.net.ServerConnect;
import com.lenovo.lenovorobotmobile.utils.ServerUtils;
import com.lenovo.lenovorobotmobile.utils.SharedpreferencesUilts;
import com.lenovo.mi.plato.comm.MoTransport;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;

/**
 * 最基础的 activity 是所有activity的父类,CameraActivity 除外 使用内容观察者模式
 * 
 * 
 * @author Administrator
 */
public abstract class BaseActivity extends Activity implements
		UserStatesChangeInterface, ServerPacketInterfaceObserver,
		View.OnClickListener {
	protected ServerUtils msgToServerHelp;
	protected MoTransport moTransport;
	protected ServerConnect connectServer;
	protected boolean isConnect;
	protected Typeface typeface;
	protected SharedpreferencesUilts sharedpreferencesUilts;
	protected String ip;
	protected String port;
	protected boolean ch_checkBox;
	protected boolean en_checkBox;
	protected Gson gson;
	protected TesterApplication testerApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化字体对象
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/lenovo_font.ttf");

		sharedpreferencesUilts = new SharedpreferencesUilts(this);
		ip = sharedpreferencesUilts.getString("ipText", null);
		port = sharedpreferencesUilts.getString("portText", null);
		ch_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_ch");
		en_checkBox = sharedpreferencesUilts.getBoolean("isCheckBox_en");

		testerApplication = (TesterApplication) getApplication();
		if (connectServer == null) {
			connectServer = ServerConnect.getConnectServer();
			// 注册内容观察者,是每一个子类都是一个观察者.
			connectServer.registerObserver(this);
			connectServer.setContext(this);
			isConnect = connectServer.getIsConnect();
			moTransport = connectServer.getMoTransport();
			msgToServerHelp = new ServerUtils(isConnect, moTransport);
		}
		gson = new Gson();
		initLayout();
		initData();
	}

	public abstract void initData();

	public abstract void initLayout();

	@Override
	public void setUserStatsNumber(int statesNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerResult(String resut, int flag) {
	}

	/**
	 * 如果服务器给返回的值是一张图片的 字节数组,转化成 bi tmap 传递 这个方法主要是在 CameraActivity 中使用的
	 * 
	 * @param
	 */
	@Override
	public void setPictureByte(Bitmap bitmap) {

	}

	/**
	 * 如果服务器给返回的值是一个 int
	 * 
	 */
	@Override
	public void setResultInteger(int result, int flag) {
	}

	@Override
	public void onClick(View v) {

	}

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

	public void callOut(int flag) {
		if (flag == 1) {
			// 设置拨打电话的标记
			((TesterApplication) getApplication()).setVideoState(1);
			// 直接拨打电话
			((TesterApplication) getApplication())
					.setRtcEngine(((TesterApplication) getApplication())
							.getVendorKey());
			Intent toChannel = new Intent(this, CameraActivity.class);
			toChannel.putExtra(FriendActivity.EXTRA_TYPE,
					FriendActivity.CALLING_TYPE_VIDEO);
			toChannel.putExtra(FriendActivity.EXTRA_CHANNEL, "1");
			startActivity(toChannel);
		}
	}
}
