package com.lenovo.lenovorobot_new.activity;

import io.agora.rtc.RtcEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * 程序的入口类,这边是 实验室的程序
 * 
 * @author luolong1
 */
public class TesterApplication extends Application {

	public MoTransport mt = new MoTransport();
	public boolean startDetection = true;
	private RtcEngine rtcEngine;

	private MessageHandler messageHandler;

	private boolean isInChannel;
	private int channelTime;

	// Leancloud
	private final static String EXTRA_APP_KEY = "28JP7oxLHbCNi7ofL6YgywT0";
	private final static String EXTRA_MASTER_KEY = "aQpqEH0qmo0bGLaCzNlYkra3";

	private SharedPreferences user;
	private SharedPreferences settings;
	private SharedPreferences call;
	private SharedPreferences record;

	private SharedPreferences.Editor userEditor;
	private SharedPreferences.Editor settingsEditor;
	private SharedPreferences.Editor callEditor;
	private SharedPreferences.Editor recordEditor;

	private final static String EXTRA_USER = "user";
	private final static String EXTRA_SETTINGS = "settings";
	private final static String EXTRA_CALL = "call";
	private final static String EXTRA_RECORDS = "records";
	private final static String EXTRA_VENDORKEY = "vendorKey";
	private final static String EXTRA_USERNAME = "username";
	private final static String EXTRA_RESOLUTION = "resolution";
	private final static String EXTRA_RATE = "rate";
	private final static String EXTRA_FRAME = "frame";
	private final static String EXTRA_VOLUME = "volume";
	private final static String EXTRA_TAPE = "tape";
	private final static String EXTRA_PATH = "path";
	private final static String EXTRA_FLOAT = "float";

	private final static int EXTRA_RESOLUTION_DEFAULT = 1;
	private final static int EXTRA_RATE_DEFAULT = 2;

	private final static int EXTRA_FRAME_DEFAULT = 1;
	private final static int EXTRA_VOLUME_DEFAULT = 2;

	private final static String VIDEO_STATE = "videoState";

	private ArrayList<Record> recordsList = new ArrayList<Record>();

	@Override
	public void onCreate() {

		super.onCreate();

		AVOSCloud.initialize(this, EXTRA_APP_KEY, EXTRA_MASTER_KEY);
		AVAnalytics.enableCrashReport(this, true);
		AVOSCloud.setLastModifyEnabled(true);
		AVOSCloud.setDebugLogEnabled(true);

		messageHandler = new MessageHandler();

		user = this.getSharedPreferences(EXTRA_USER, MODE_PRIVATE);
		settings = this.getSharedPreferences(EXTRA_SETTINGS, MODE_PRIVATE);
		call = this.getSharedPreferences(EXTRA_CALL, MODE_PRIVATE);
		record = this.getSharedPreferences(EXTRA_RECORDS, MODE_PRIVATE);

		userEditor = user.edit();
		settingsEditor = settings.edit();
		callEditor = call.edit();
		recordEditor = record.edit();

		if (rtcEngine != null) {
			rtcEngine.enableNetworkTest();
		}
	}

	// create RtcEngine
	public void setRtcEngine(String vendorKey) {

		if (rtcEngine != null) {
			rtcEngine = null;
		}

		rtcEngine = RtcEngine.create(getApplicationContext(), vendorKey,
				messageHandler);
	}

	// get RtcEngine
	public RtcEngine getRtcEngine() {

		return rtcEngine;
	}

	// ----------------------------------------------------------------------------------------------

	// set User SharedPreference
	@SuppressLint("NewApi")
	public void setUserInformation(String vendorKey, String username) {

		userEditor.putString(EXTRA_VENDORKEY, vendorKey);
		userEditor.putString(EXTRA_USERNAME, username);

		userEditor.apply();
	}

	// get vendorKey
	public String getVendorKey() {

		return user.getString(EXTRA_VENDORKEY, "");
	}

	// get username
	public String getUsername() {

		return user.getString(EXTRA_USERNAME, "");
	}

	// ----------------------------------------------------------------------------------------------

	// set isInChannel
	public void setIsInChannel(boolean isInChannel) {

		this.isInChannel = isInChannel;
	}

	// get isInChannel
	public boolean getIsInChannel() {

		return isInChannel;
	}

	// ----------------------------------------------------------------------------------------------

	// set channelTime
	public void setChannelTime(int channelTime) {

		this.channelTime = channelTime;
	}

	// get isInChannel
	public int getChannelTime() {

		return channelTime;
	}

	// ----------------------------------------------------------------------------------------------

	// set Resolution SharedPreference
	@SuppressLint("NewApi")
	public void setResolution(int resolution) {

		settingsEditor.putInt(EXTRA_RESOLUTION, resolution);

		settingsEditor.apply();
	}

	// get resolution
	public int getResolution() {

		return settings.getInt(EXTRA_RESOLUTION, EXTRA_RESOLUTION_DEFAULT);
	}

	// ----------------------------------------------------------------------------------------------

	// set Rate SharedPreference
	@SuppressLint("NewApi")
	public void setRate(int rate) {

		settingsEditor.putInt(EXTRA_RATE, rate);

		settingsEditor.apply();
	}

	// get Rate
	public int getRate() {

		return settings.getInt(EXTRA_RATE, EXTRA_RATE_DEFAULT);
	}

	// ----------------------------------------------------------------------------------------------

	// set Frame SharedPreference
	@SuppressLint("NewApi")
	public void setFrame(int frame) {

		settingsEditor.putInt(EXTRA_FRAME, frame);

		settingsEditor.apply();
	}

	// get Frame
	public int getFrame() {

		return settings.getInt(EXTRA_FRAME, EXTRA_FRAME_DEFAULT);
	}

	// ----------------------------------------------------------------------------------------------

	// set Volume SharedPreference
	@SuppressLint("NewApi")
	public void setVolume(int volume) {

		settingsEditor.putInt(EXTRA_VOLUME, volume);

		settingsEditor.apply();
	}

	// get Volume
	public int getVolume() {

		return settings.getInt(EXTRA_VOLUME, EXTRA_VOLUME_DEFAULT);
	}

	// ----------------------------------------------------------------------------------------------

	// set Tape SharedPreference
	@SuppressLint("NewApi")
	public void setTape(boolean isChecked) {

		settingsEditor.putBoolean(EXTRA_TAPE, isChecked);

		settingsEditor.apply();
	}

	// get tape
	public boolean getTape() {

		return settings.getBoolean(EXTRA_TAPE, false);
	}

	// ----------------------------------------------------------------------------------------------

	// set Path SharedPreference
	@SuppressLint("NewApi")
	public void setPath(String path) {

		settingsEditor.putString(EXTRA_PATH, path);

		settingsEditor.apply();
	}

	// get path
	public String getPath() {

		return settings.getString(EXTRA_PATH, getApplicationContext()
				.getExternalFilesDir(null).toString());
	}

	// ----------------------------------------------------------------------------------------------

	// set Float SharedPreference
	@SuppressLint("NewApi")
	public void setFloat(boolean isChecked) {

		settingsEditor.putBoolean(EXTRA_FLOAT, isChecked);

		settingsEditor.apply();
	}

	// get float
	public boolean getFloat() {

		return settings.getBoolean(EXTRA_FLOAT, true);
	}

	// ----------------------------------------------------------------------------------------------

	// set CallId SharedPreference
	@SuppressLint("NewApi")
	public void setCallId(String callId) {

		callEditor.putString(callId, callId);

		callEditor.apply();
	}

	// get callId
	public String getCallId(String callId) {

		return call.getString(callId, callId);
	}

	// ----------------------------------------------------------------------------------------------

	// set Records SharedPreference
	@SuppressLint("NewApi")
	public void setRecordDate(String callId, String value) {

		recordEditor.putString(callId, value);

		recordEditor.apply();
	}

	// get records
	public String getRecordDate(String callId) {

		return record.getString(callId, "");
	}

	// ----------------------------------------------------------------------------------------------

	public Map<String, ?> getAllCallIds() {

		return call.getAll();
	}

	public Map<String, ?> getAllRecords() {

		return record.getAll();
	}

	// ----------------------------------------------------------------------------------------------

	public void initRecordsList() {

		for (String callIdKey : getAllCallIds().keySet()) {

			for (String recordsKey : getAllRecords().keySet()) {

				if (recordsKey.equals(callIdKey)) {

					recordsList.add(new Record(callIdKey,
							(String) getAllRecords().get(recordsKey)));
				}
			}
		}
	}

	public List<Record> getRecordsList() {

		return recordsList;
	}

	// set Handler Activity
	public void setEngineHandlerActivity(
			BaseEngineHandlerActivity baseEngineHandlerActivity) {

		messageHandler.setActivity(baseEngineHandlerActivity);
	}

	// 用来保存用户呼叫视频的状态,1,表示正常的视频通话,2,表示视频监控模式
	public void setVideoState(int state) {
		userEditor.putInt(VIDEO_STATE, state);
		userEditor.apply();
	}

	public int getVideoState() {
		return user.getInt(VIDEO_STATE, -1);
	}
}
