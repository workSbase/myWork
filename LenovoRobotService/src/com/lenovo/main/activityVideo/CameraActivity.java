package com.lenovo.main.activityVideo;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lenovo.lenovoRobotService.R;

public class CameraActivity extends BaseEngineHandlerActivity {

	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	private LinearLayout mCameraEnabler;
	private LinearLayout mCameraSwitcher;

	private LinearLayout mRemoteUserContainer;

	private int mRemoteUserViewWidth = 0;

	private SurfaceView mLocalView;

	private RtcEngine rtcEngine;

	private int callingType;
	private String channel;

	private int userId = new Random().nextInt(Math.abs((int) System
			.currentTimeMillis()));

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";
	// private RelativeLayout controlView;
	private int videoState;

	private int viewControlShow = 0;
	private RelativeLayout mRelativeLayout;

	@Override
	public void onCreate(Bundle savedInstance) {

		super.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstance);
		setContentView(R.layout.activity_channel);

		callingType = getIntent().getIntExtra(EXTRA_TYPE, CALLING_TYPE_VIDEO);
		channel = getIntent().getStringExtra(EXTRA_CHANNEL);

		// keep screen on - turned on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mRemoteUserViewWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 100, getResources()
						.getDisplayMetrics());
		videoState = ((TesterApplication) getApplication()).getVideoState();

		setupRtcEngine();

		initViews();

		setupChannel();

		if (videoState == 2) {
			mRelativeLayout.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		rtcEngine.leaveChannel();

		finish();

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onUserInteraction(View view) {
		super.onUserInteraction(view);

		videoState = ((TesterApplication) getApplication()).getVideoState();

		callingType = CALLING_TYPE_VIDEO;

		mCameraEnabler.setVisibility(View.VISIBLE);
		mCameraSwitcher.setVisibility(View.VISIBLE);

		// enable video call
		ensureLocalViewIsCreated();

		rtcEngine.enableVideo();
		rtcEngine.muteLocalVideoStream(false);
		rtcEngine.muteLocalAudioStream(false);
		rtcEngine.muteRemoteVideoStreams(false);

		// join video call
		if (mRemoteUserContainer.getChildCount() == 0) {
			setupChannel();
		}
		new android.os.Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				updateRemoteUserViews(CALLING_TYPE_VIDEO);
			}
		}, 500);

		// ensure video camera enabler states
		CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
		cameraEnabler.setChecked(false);
	}

	private void setupRtcEngine() {

		rtcEngine = ((TesterApplication) getApplication()).getRtcEngine();

		((TesterApplication) getApplication()).setEngineHandlerActivity(this);

		rtcEngine.setLogFile(((TesterApplication) getApplication()).getPath()
				+ "/"
				+ Integer.toString(Math.abs((int) System.currentTimeMillis()))
				+ ".txt");
	}

	private void setupChannel() {

		rtcEngine.joinChannel(
				((TesterApplication) getApplication()).getVendorKey(), channel,
				"", userId);
	}

	@SuppressLint("NewApi")
	private void initViews() {

		// muter,静音
		CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
		muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean mutes) {
				rtcEngine.muteLocalAudioStream(mutes);
			}
		});

		// speaker,
		CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
		speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean usesSpeaker) {
				rtcEngine.setEnableSpeakerphone(!usesSpeaker);
			}
		});

		// camera enabler,关闭本地摄像头
		CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
		cameraEnabler
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean disablesCamera) {

						rtcEngine.muteLocalVideoStream(disablesCamera);

						if (disablesCamera) {
							rtcEngine.muteLocalVideoStream(true);
						} else {
							rtcEngine.muteLocalVideoStream(false);
						}
					}
				});

		// camera switcher,摄像头前后调动
		CheckBox cameraSwitch = (CheckBox) findViewById(R.id.action_camera_switcher);
		cameraSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean switches) {
						rtcEngine.switchCamera();
					}
				});

		// setup states of action buttons
		muter.setChecked(false);
		speaker.setChecked(false);
		cameraEnabler.setChecked(false);
		cameraSwitch.setChecked(false);

		mCameraEnabler = (LinearLayout) findViewById(R.id.wrapper_action_camera_enabler);
		mCameraSwitcher = (LinearLayout) findViewById(R.id.wrapper_action_camera_switcher);
		mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);

		if (callingType == CALLING_TYPE_VIDEO) {
			// video call
			View simulateClick = new View(getApplicationContext());
			this.onUserInteraction(simulateClick);
		}

		setResolution(((TesterApplication) getApplication()).getResolution());

		setRate(((TesterApplication) getApplication()).getRate());

		setFrame(((TesterApplication) getApplication()).getFrame());

		setVolume(((TesterApplication) getApplication()).getVolume());
	}

	// Show Local
	private void ensureLocalViewIsCreated() {

		if (this.mLocalView == null) {

			// local view has not been added before
			FrameLayout localViewContainer = (FrameLayout) findViewById(R.id.user_local_view);
			@SuppressWarnings("static-access")
			SurfaceView localView = rtcEngine
					.CreateRendererView(getApplicationContext());
			this.mLocalView = localView;
			localViewContainer.addView(localView, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT));

			rtcEngine.enableVideo();
			rtcEngine.setupLocalVideo(new VideoCanvas(this.mLocalView));

			mLocalView.setTag(0);
		}
	}

	// switch click
	public void onSwitchRemoteUsers(View view) {

		// In voice call
		if (CALLING_TYPE_VOICE == callingType) {
			return;
		}

		// In Video Call
		ViewGroup remoteViewParent = (ViewGroup) view.getParent();

		int from = (Integer) view.getTag();
		int to = (Integer) mLocalView.getTag();

		rtcEngine.switchView(from, to);

		mLocalView.setTag(from);
		view.setTag(to);
	}

	// 显示房间内其他用户的视频
	public synchronized void onFirstRemoteVideoDecoded(final int uid,
			int width, int height, final int elapsed) {

		runOnUiThread(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {

				View remoteUserView = mRemoteUserContainer.findViewById(Math
						.abs(uid));

				// ensure container is added
				if (remoteUserView == null) {

					LayoutInflater layoutInflater = getLayoutInflater();

					View singleRemoteUser = layoutInflater.inflate(
							R.layout.viewlet_remote_user, null);
					singleRemoteUser.setId(Math.abs(uid));

					// TextView username = (TextView) singleRemoteUser
					// .findViewById(R.id.remote_user_name);
					// username.setText(String.valueOf(Math.abs(uid)));

					mRemoteUserContainer.addView(singleRemoteUser,
							new LinearLayout.LayoutParams(mRemoteUserViewWidth,
									mRemoteUserViewWidth));

					remoteUserView = singleRemoteUser;

				}

				FrameLayout remoteVideoUser = (FrameLayout) remoteUserView
						.findViewById(R.id.viewlet_remote_video_user);
				remoteVideoUser.removeAllViews();
				remoteVideoUser.setTag(uid);

				// ensure remote video view setup
				final SurfaceView remoteView = RtcEngine
						.CreateRendererView(getApplicationContext());
				remoteVideoUser.addView(remoteView,
						new FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.MATCH_PARENT,
								FrameLayout.LayoutParams.MATCH_PARENT));
				remoteView.setZOrderOnTop(true);
				remoteView.setZOrderMediaOverlay(true);

				rtcEngine.enableVideo();
				int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(
						remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

				if (successCode < 0) {
					new android.os.Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							rtcEngine.setupRemoteVideo(new VideoCanvas(
									remoteView,
									VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
							remoteView.invalidate();
						}
					}, 500);
				}
				// app hints before you join
				setRemoteUserViewVisibility(true);
			}
		});
	}

	// 当其他用户加入房间的时候,回调该方法
	public synchronized void onUserJoined(final int uid, int elapsed) {

		View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
		if (existedUser != null) {
			// user view already added
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View singleRemoteUser = mRemoteUserContainer.findViewById(Math
						.abs(uid));
				if (singleRemoteUser != null) {
					return;
				}

				LayoutInflater layoutInflater = getLayoutInflater();
				singleRemoteUser = layoutInflater.inflate(
						R.layout.viewlet_remote_user, null);
				singleRemoteUser.setId(Math.abs(uid));

				// TextView username = (TextView) singleRemoteUser
				// .findViewById(R.id.remote_user_name);
				// username.setText(String.valueOf(Math.abs(uid)));

				mRemoteUserContainer.addView(singleRemoteUser,
						new LinearLayout.LayoutParams(mRemoteUserViewWidth,
								mRemoteUserViewWidth));

				// show container
				setRemoteUserViewVisibility(true);

			}
		});
	}

	// 用在退出视频通话的时候,会回调该方法
	public void onUserOffline(final int uid) {

		if (mRemoteUserContainer == null || mLocalView == null) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// zxc 添加的两行代码,用来退出当前的activity和释放camera 的权限
				rtcEngine.leaveChannel();
				finish();

				if ((Integer) mLocalView.getTag() == uid) {

					rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView));
					mLocalView.setTag(0);
				}

				View userViewToRemove = mRemoteUserContainer.findViewById(Math
						.abs(uid));
				mRemoteUserContainer.removeView(userViewToRemove);

				if (mRemoteUserContainer.getChildCount() == 0) {
					setRemoteUserViewVisibility(false);
				}
			}
		});
	}

	// show or hide remote user container,远端视频显示和界面的大小控制
	private void setRemoteUserViewVisibility(boolean isVisible) {

		if (videoState == 2) {

			findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ? (int) TypedValue
					.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
							getResources().getDisplayMetrics()) : 0;
		} else {
			findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ? (int) TypedValue
					.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
							getResources().getDisplayMetrics()) : 0;
		}
	}

	private void updateRemoteUserViews(int callingType) {

		int visibility = View.GONE;

		if (callingType == CALLING_TYPE_VIDEO) {
			visibility = View.GONE;

		} else if (callingType == CALLING_TYPE_VOICE) {
			visibility = View.VISIBLE;
		}

		for (int i = 0, size = mRemoteUserContainer.getChildCount(); i < size; i++) {

			View singleRemoteView = mRemoteUserContainer.getChildAt(i);
			// singleRemoteView.findViewById(R.id.remote_user_voice_container)
			// .setVisibility(visibility);

			if (callingType == CALLING_TYPE_VIDEO) {

				// re-setup remote video
				FrameLayout remoteVideoUser = (FrameLayout) singleRemoteView
						.findViewById(R.id.viewlet_remote_video_user);
				// ensure remote video view setup
				if (remoteVideoUser.getChildCount() > 0) {
					final SurfaceView remoteView = (SurfaceView) remoteVideoUser
							.getChildAt(0);
					remoteView.setZOrderOnTop(true);
					remoteView.setZOrderMediaOverlay(true);
					int savedUid = (Integer) remoteVideoUser.getTag();
					rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView,
							VideoCanvas.RENDER_MODE_ADAPTIVE, savedUid));
				}
			}
		}
	}

	// set resolution
	private void setResolution(int resolution) {

		String[] resolutionValues = new String[] {
				getString(R.string.sliding_value_cif),
				getString(R.string.sliding_value_480),
				getString(R.string.sliding_value_720),
				getString(R.string.sliding_value_1080) };

		int[][] videoResolutions = { { 352, 288 }, { 640, 480 }, { 1280, 720 },
				{ 1920, 1080 } };

		((TesterApplication) getApplication()).setResolution(resolution);

		rtcEngine.setVideoResolution(videoResolutions[resolution][0],
				videoResolutions[resolution][1]);
	}

	// set rate
	private void setRate(int rate) {

		String[] rateValues = new String[] {
				getString(R.string.sliding_value_150k),
				getString(R.string.sliding_value_500k),
				getString(R.string.sliding_value_800k),
				getString(R.string.sliding_value_1m),
				getString(R.string.sliding_value_2m),
				getString(R.string.sliding_value_5m),
				getString(R.string.sliding_value_10m) };

		int[] maxBitRates = new int[] { 150, 500, 800, 1024, 2048, 5120, 10240 };

		((TesterApplication) getApplication()).setRate(rate);

		rtcEngine.setVideoMaxBitrate(maxBitRates[rate]);
	}

	// set frame
	private void setFrame(int frame) {

		String[] frameValues = new String[] {
				getString(R.string.sliding_value_15),
				getString(R.string.sliding_value_20),
				getString(R.string.sliding_value_24),
				getString(R.string.sliding_value_30),
				getString(R.string.sliding_value_60) };

		int[] videoMaxFrameRates = new int[] { 15, 20, 24, 30, 60 };

		((TesterApplication) getApplication()).setFrame(frame);

		rtcEngine.setVideoMaxFrameRate(videoMaxFrameRates[frame]);
	}

	// set volume
	private void setVolume(int volume) {

		String[] volumeValues = new String[] {
				getString(R.string.sliding_value_0),
				getString(R.string.sliding_value_50),
				getString(R.string.sliding_value_100),
				getString(R.string.sliding_value_150),
				getString(R.string.sliding_value_200),
				getString(R.string.sliding_value_255) };

		int[] speakerPhoneVolume = new int[] { 0, 50, 100, 150, 200, 255 };

		((TesterApplication) getApplication()).setVolume(volume);

		rtcEngine.setSpeakerphoneVolume(speakerPhoneVolume[volume]);
	}
}
