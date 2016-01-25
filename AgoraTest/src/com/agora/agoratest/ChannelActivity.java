package com.agora.agoratest;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by apple on 15/9/18.
 */
public class ChannelActivity extends BaseEngineHandlerActivity {

	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	private TextView mDuration;
	private TextView mByteCounts;

	private DrawerLayout mDrawerLayout;

	private SeekBar mResolutionSeekBar;
	private SeekBar mRateSeekBar;
	private SeekBar mFrameSeekBar;
	private SeekBar mVolumeSeekBar;
	private Switch mTapeSwitch;
	private Switch mFloatSwitch;

	private TextView mVendorKey;
	private TextView mUsername;
	private TextView mResolutionValue;
	private TextView mRateValue;
	private TextView mFrameValue;
	private TextView mVolumeValue;
	private TextView mPathValue;

	private LinearLayout mCameraEnabler;
	private LinearLayout mCameraSwitcher;

	private ImageView mNetworkQuality;

	private LinearLayout mRemoteUserContainer;

	private RelativeLayout mEvaluationContainer;

	private ImageView mStarOne;
	private ImageView mStarTwo;
	private ImageView mStarThree;
	private ImageView mStarFour;
	private ImageView mStarFive;

	private List<ImageView> stars = new ArrayList<ImageView>();

	private RelativeLayout mFloatContainer;
	private LinearLayout mStatsContainer;
	private TextView mNotificationOld;
	private TextView mNotificationNew;

	private int time = 0;
	private int mLastRxBytes = 0;
	private int mLastTxBytes = 0;
	private int mLastDuration = 0;
	private int mRemoteUserViewWidth = 0;

	private SurfaceView mLocalView;

	private RtcEngine rtcEngine;

	private int callingType;
	private String channel;

	private int userId = new Random().nextInt(Math.abs((int) System
			.currentTimeMillis()));

	private String callId;

	private boolean isMuted = false;
	private boolean isCorrect = true;

	private int score = 0;
	private int errorCount = 0;

	public final static String EXTRA_TYPE = "EXTRA_TYPE";
	public final static String EXTRA_CHANNEL = "EXTRA_CHANNEL";

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
				TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
						.getDisplayMetrics());

		setupRtcEngine();

		initViews();

		setupChannel();

		setupTime();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		rtcEngine.leaveChannel();

		// keep screen on - turned off
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0 && resultCode == RESULT_OK && data != null) {

			mVendorKey.setText(data
					.getStringExtra(ProfileActivity.EXTRA_NEW_KEY));
			mUsername.setText(data
					.getStringExtra(ProfileActivity.EXTRA_NEW_NAME));

			if (data.getBooleanExtra(ProfileActivity.EXTRA_HAS_CHANGED, false)) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChannelActivity.this);

				builder.setCancelable(false)
						.setMessage(getString(R.string.error))
						.setPositiveButton(getString(R.string.error_confirm),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										rtcEngine.leaveChannel();
									}
								}).show();

				AlertDialog dialog = builder.create();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onUserInteraction(View view) {

		switch (view.getId()) {

		default:

			super.onUserInteraction(view);
			break;

		case R.id.wrapper_action_video_calling: {

			callingType = CALLING_TYPE_VIDEO;

			mCameraEnabler.setVisibility(View.VISIBLE);
			mCameraSwitcher.setVisibility(View.VISIBLE);

			removeBackgroundOfCallingWrapper();

			findViewById(R.id.wrapper_action_video_calling)
					.setBackgroundResource(R.drawable.ic_room_button_yellow_bg);
			findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);

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
			break;

		case R.id.wrapper_action_voice_calling: {

			callingType = CALLING_TYPE_VOICE;

			mCameraEnabler.setVisibility(View.GONE);
			mCameraSwitcher.setVisibility(View.GONE);

			removeBackgroundOfCallingWrapper();
			findViewById(R.id.wrapper_action_voice_calling)
					.setBackgroundResource(R.drawable.ic_room_button_yellow_bg);

			// show background for voice call
			findViewById(R.id.user_local_voice_bg).setVisibility(View.VISIBLE);

			ensureLocalViewIsCreated();

			// disable video call when necessary
			rtcEngine.disableVideo();
			rtcEngine.muteLocalVideoStream(true);
			rtcEngine.muteRemoteVideoStreams(true);

			// join voice call
			if (mRemoteUserContainer.getChildCount() == 0) {

				setupChannel();
			}

			new android.os.Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					updateRemoteUserViews(CALLING_TYPE_VOICE);
				}
			}, 500);
		}
			break;

		case R.id.action_hung_up:
		case R.id.channel_back: {
			onBackPressed();
		}
			break;

		case R.id.evaluation_star_one: {

			setupStars(1);
		}
			break;

		case R.id.evaluation_star_two: {

			setupStars(2);
		}
			break;

		case R.id.evaluation_star_three: {

			setupStars(3);
		}
			break;

		case R.id.evaluation_star_four: {

			setupStars(4);
		}
			break;

		case R.id.evaluation_star_five: {

			setupStars(5);
		}
			break;

		// open or close drawer
		case R.id.channel_drawer_button: {

			if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
				mDrawerLayout.closeDrawer(GravityCompat.END);
			} else {
				mDrawerLayout.openDrawer(GravityCompat.END);
			}
		}
			break;

		// go to Profile
		case R.id.channel_drawer_profile: {

			mDrawerLayout.closeDrawer(GravityCompat.END);

			((TesterApplication) getApplication()).setChannelTime(time);

			Intent i = new Intent(ChannelActivity.this, ProfileActivity.class);
			startActivityForResult(i, 0);
		}
			break;

		// go to record
		case R.id.channel_drawer_record: {

			mDrawerLayout.closeDrawer(GravityCompat.END);

			((TesterApplication) getApplication()).setChannelTime(time);

			Intent i = new Intent(ChannelActivity.this, RecordActivity.class);
			startActivity(i);
		}
			break;

		// go to About
		case R.id.channel_drawer_about: {

			mDrawerLayout.closeDrawer(GravityCompat.END);

			((TesterApplication) getApplication()).setChannelTime(time);

			Intent i = new Intent(ChannelActivity.this, AboutActivity.class);
			startActivity(i);
		}
			break;
		}
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

		((TextView) findViewById(R.id.channel_id)).setText(String.format(
				getString(R.string.channel_title), channel));

		rtcEngine.joinChannel(
				((TesterApplication) getApplication()).getVendorKey(), channel,
				"", userId);
	}

	@SuppressLint("NewApi")
	private void initViews() {

		// muter
		CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
		muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean mutes) {

				rtcEngine.muteLocalAudioStream(mutes);
				compoundButton
						.setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed
								: R.drawable.ic_room_mute);
			}
		});

		// speaker
		CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
		speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean usesSpeaker) {

				rtcEngine.setEnableSpeakerphone(!usesSpeaker);
				compoundButton
						.setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker_pressed
								: R.drawable.ic_room_loudspeaker);
			}
		});

		// camera enabler
		CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
		cameraEnabler
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean disablesCamera) {

						rtcEngine.muteLocalVideoStream(disablesCamera);

						if (disablesCamera) {
							findViewById(R.id.user_local_voice_bg)
									.setVisibility(View.VISIBLE);
							rtcEngine.muteLocalVideoStream(true);

						} else {
							findViewById(R.id.user_local_voice_bg)
									.setVisibility(View.GONE);
							rtcEngine.muteLocalVideoStream(false);
						}

						compoundButton
								.setBackgroundResource(disablesCamera ? R.drawable.ic_room_button_close_pressed
										: R.drawable.ic_room_button_close);
					}
				});

		// camera switcher
		CheckBox cameraSwitch = (CheckBox) findViewById(R.id.action_camera_switcher);
		cameraSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean switches) {

						rtcEngine.switchCamera();

						compoundButton
								.setBackgroundResource(switches ? R.drawable.ic_room_button_change_pressed
										: R.drawable.ic_room_button_change);
					}
				});

		// setup states of action buttons
		muter.setChecked(false);
		speaker.setChecked(false);
		cameraEnabler.setChecked(false);
		cameraSwitch.setChecked(false);

		mDuration = (TextView) findViewById(R.id.stat_time);
		mByteCounts = (TextView) findViewById(R.id.stat_bytes);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.channel_drawer_layout);
		mResolutionValue = (TextView) findViewById(R.id.channel_drawer_resolution_value);
		mRateValue = (TextView) findViewById(R.id.channel_drawer_rate_value);
		mFrameValue = (TextView) findViewById(R.id.channel_drawer_frame_value);
		mVolumeValue = (TextView) findViewById(R.id.channel_drawer_volume_value);
		mCameraEnabler = (LinearLayout) findViewById(R.id.wrapper_action_camera_enabler);
		mCameraSwitcher = (LinearLayout) findViewById(R.id.wrapper_action_camera_switcher);
		mFloatContainer = (RelativeLayout) findViewById(R.id.channel_float_container);
		mStatsContainer = (LinearLayout) findViewById(R.id.wrapper_session_stats);
		mNotificationOld = (TextView) findViewById(R.id.channel_notification_old);
		mNotificationNew = (TextView) findViewById(R.id.channel_notification_new);
		mNetworkQuality = (ImageView) findViewById(R.id.channel_network_quality);
		mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);

		mEvaluationContainer = (RelativeLayout) findViewById(R.id.channel_evaluation);
		mEvaluationContainer.setVisibility(View.GONE);

		mStarOne = (ImageView) findViewById(R.id.evaluation_star_one);
		mStarTwo = (ImageView) findViewById(R.id.evaluation_star_two);
		mStarThree = (ImageView) findViewById(R.id.evaluation_star_three);
		mStarFour = (ImageView) findViewById(R.id.evaluation_star_four);
		mStarFive = (ImageView) findViewById(R.id.evaluation_star_five);

		stars.add(mStarOne);
		stars.add(mStarTwo);
		stars.add(mStarThree);
		stars.add(mStarFour);
		stars.add(mStarFive);

		if (callingType == CALLING_TYPE_VIDEO) {
			// video call

			View simulateClick = new View(getApplicationContext());
			simulateClick.setId(R.id.wrapper_action_video_calling);
			this.onUserInteraction(simulateClick);

		} else if (callingType == CALLING_TYPE_VOICE) {
			// voice call
			View simulateClick = new View(getApplicationContext());
			simulateClick.setId(R.id.wrapper_action_voice_calling);
			this.onUserInteraction(simulateClick);
		}

		findViewById(R.id.wrapper_action_video_calling).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.wrapper_action_voice_calling).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.action_hung_up).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.channel_back).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.channel_drawer_button).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.channel_drawer_profile).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.channel_drawer_record).setOnClickListener(
				getViewClickListener());
		findViewById(R.id.channel_drawer_about).setOnClickListener(
				getViewClickListener());

		mVendorKey = (TextView) findViewById(R.id.channel_drawer_profile_key);
		mVendorKey
				.setText(((TesterApplication) getApplication()).getVendorKey());

		mUsername = (TextView) findViewById(R.id.channel_drawer_profile_name);
		mUsername.setText(((TesterApplication) getApplication()).getUsername());

		mPathValue = (TextView) findViewById(R.id.channel_drawer_path_value);
		mPathValue.setText(((TesterApplication) getApplication()).getPath());

		mResolutionSeekBar = (SeekBar) findViewById(R.id.channel_drawer_resolution_seekbar);
		mResolutionSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						setResolution(progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		mRateSeekBar = (SeekBar) findViewById(R.id.channel_drawer_rate_seekbar);
		mRateSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						setRate(progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		mFrameSeekBar = (SeekBar) findViewById(R.id.channel_drawer_frame_seekbar);
		mFrameSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						setFrame(progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		mVolumeSeekBar = (SeekBar) findViewById(R.id.channel_drawer_volume_seekbar);
		mVolumeSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						setVolume(progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		mTapeSwitch = (Switch) findViewById(R.id.channel_drawer_tape_switch);
		mTapeSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						setTape(isChecked);
					}
				});

		mFloatSwitch = (Switch) findViewById(R.id.channel_drawer_float_switch);
		mFloatSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						setFloat(isChecked);
					}
				});

		setResolution(((TesterApplication) getApplication()).getResolution());

		setRate(((TesterApplication) getApplication()).getRate());

		setFrame(((TesterApplication) getApplication()).getFrame());

		setVolume(((TesterApplication) getApplication()).getVolume());

		setTape(((TesterApplication) getApplication()).getTape());

		setFloat(((TesterApplication) getApplication()).getFloat());
	}

	private void removeBackgroundOfCallingWrapper() {

		findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(
				R.drawable.shape_transparent);
		findViewById(R.id.wrapper_action_voice_calling).setBackgroundResource(
				R.drawable.shape_transparent);
	}

	// Show Local
	private void ensureLocalViewIsCreated() {

		if (this.mLocalView == null) {

			// local view has not been added before
			FrameLayout localViewContainer = (FrameLayout) findViewById(R.id.user_local_view);
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
		if (remoteViewParent != null) {
			View remoteUserVoiceContainer = remoteViewParent
					.findViewById(R.id.remote_user_voice_container);
			if (View.VISIBLE == remoteUserVoiceContainer.getVisibility()) {
				return;
			}
		}

		int from = (Integer) view.getTag();
		int to = (Integer) mLocalView.getTag();

		rtcEngine.switchView(from, to);

		mLocalView.setTag(from);
		view.setTag(to);
	}

	// mute others
	public void onMuteRemoteUsers(View view) {

		isMuted = !isMuted;

		rtcEngine.muteRemoteAudioStreams(isMuted);

		for (int i = 0; i < mRemoteUserContainer.getChildCount(); i++) {

			(mRemoteUserContainer.getChildAt(i)
					.findViewById(R.id.viewlet_remote_video_voice))
					.setBackgroundResource(isMuted ? R.drawable.ic_room_voice_red
							: R.drawable.ic_room_voice_grey);
			(mRemoteUserContainer.getChildAt(i).findViewById(
					R.id.remote_user_voice_container)
					.findViewById(R.id.remote_user_voice_icon))
					.setBackgroundResource(isMuted ? R.drawable.ic_room_voice_red
							: R.drawable.ic_room_voice_grey);
		}
	}

	public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {

		userId = uid;

		((TesterApplication) getApplication()).setIsInChannel(true);

		callId = rtcEngine.getCallId();

		((TesterApplication) getApplication()).setCallId(callId);

		String recordValue = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss")
				.format(Calendar.getInstance().getTime());

		String recordUrl = rtcEngine.makeQualityReportUrl(channel, uid, 0, 0);

		((TesterApplication) getApplication()).setRecordDate(callId, recordValue
				+ "+" + recordUrl);
	}

	public void onError(final int err) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (err == 101) {

					isCorrect = false;

					errorCount = errorCount + 1;

					if (errorCount == 1) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ChannelActivity.this);

						builder.setCancelable(false)
								.setMessage(getString(R.string.error_101))
								.setPositiveButton(
										getString(R.string.error_confirm),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												rtcEngine.leaveChannel();
											}
										}).show();

						AlertDialog dialog = builder.create();
					}
				}
			}
		});
	}

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

					TextView username = (TextView) singleRemoteUser
							.findViewById(R.id.remote_user_name);
					username.setText(String.valueOf(Math.abs(uid)));

					mRemoteUserContainer.addView(singleRemoteUser,
							new LinearLayout.LayoutParams(mRemoteUserViewWidth,
									mRemoteUserViewWidth));

					remoteUserView = singleRemoteUser;

					setupNotification(Math.abs(uid)
							+ getString(R.string.channel_join));
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

				if (remoteUserView != null && callingType == CALLING_TYPE_VIDEO) {
					remoteUserView.findViewById(
							R.id.remote_user_voice_container).setVisibility(
							View.GONE);
				} else {
					remoteUserView.findViewById(
							R.id.remote_user_voice_container).setVisibility(
							View.VISIBLE);
				}

				// app hints before you join
				TextView appNotification = (TextView) findViewById(R.id.app_notification);
				appNotification.setText("");
				setRemoteUserViewVisibility(true);
			}
		});
	}

	public synchronized void onUserJoined(final int uid, int elapsed) {

		View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
		if (existedUser != null) {
			// user view already added
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				// Handle the case onFirstRemoteVideoDecoded() is called before
				// onUserJoined()
				View singleRemoteUser = mRemoteUserContainer.findViewById(Math
						.abs(uid));
				if (singleRemoteUser != null) {
					return;
				}

				LayoutInflater layoutInflater = getLayoutInflater();
				singleRemoteUser = layoutInflater.inflate(
						R.layout.viewlet_remote_user, null);
				singleRemoteUser.setId(Math.abs(uid));

				TextView username = (TextView) singleRemoteUser
						.findViewById(R.id.remote_user_name);
				username.setText(String.valueOf(Math.abs(uid)));

				mRemoteUserContainer.addView(singleRemoteUser,
						new LinearLayout.LayoutParams(mRemoteUserViewWidth,
								mRemoteUserViewWidth));

				// app hints before you join
				TextView appNotification = (TextView) findViewById(R.id.app_notification);
				appNotification.setText("");

				// show container
				setRemoteUserViewVisibility(true);

				setupNotification(Math.abs(uid)
						+ getString(R.string.channel_join));
			}
		});
	}

	public void onUserOffline(final int uid) {

		if (mRemoteUserContainer == null || mLocalView == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if ((Integer) mLocalView.getTag() == uid) {

					rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView));
					mLocalView.setTag(0);
				}

				View userViewToRemove = mRemoteUserContainer.findViewById(Math
						.abs(uid));
				mRemoteUserContainer.removeView(userViewToRemove);

				// no joined users any more
				if (mRemoteUserContainer.getChildCount() == 0) {

					TextView appNotification = (TextView) findViewById(R.id.app_notification);
					appNotification.setText(R.string.channel_prepare);

					// hide container
					setRemoteUserViewVisibility(false);
				}

				setupNotification(Math.abs(uid)
						+ getString(R.string.channel_leave));
			}
		});
	}

	public void onUpdateSessionStats(
			final IRtcEngineEventHandler.SessionStats stats) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				// bytes
				mByteCounts
						.setText(((stats.txBytes + stats.rxBytes - mLastTxBytes - mLastRxBytes) / 1024 / (stats.totalDuration
								- mLastDuration + 1))
								+ "KB/s");

				// remember data from this call back
				mLastRxBytes = stats.rxBytes;
				mLastTxBytes = stats.txBytes;
				mLastDuration = stats.totalDuration;
			}
		});
	}

	public void onUserMuteVideo(final int uid, final boolean muted) {

		if (mRemoteUserContainer == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				View remoteView = mRemoteUserContainer.findViewById(Math
						.abs(uid));

				if (remoteView != null) {

					remoteView
							.findViewById(R.id.remote_user_voice_container)
							.setVisibility(
									(CALLING_TYPE_VOICE == callingType || (CALLING_TYPE_VIDEO == callingType && muted)) ? View.VISIBLE
											: View.GONE);

					remoteView.invalidate();
				}

				setupNotification(muted ? Math.abs(uid)
						+ getString(R.string.channel_mute_video) : Math
						.abs(uid) + getString(R.string.channel_open_video));
			}
		});
	}

	public void onUserMuteAudio(final int uid, final boolean muted) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				setupNotification(muted ? Math.abs(uid)
						+ getString(R.string.channel_mute_audio) : Math
						.abs(uid) + getString(R.string.channel_open_audio));
			}
		});
	}

	public void onNetworkQuality(final int quality) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				switch (quality) {

				case 1: {
					mNetworkQuality
							.setImageResource(R.drawable.ic_room_signal_three);
				}
					break;

				case 2:
				case 3: {
					mNetworkQuality
							.setImageResource(R.drawable.ic_room_signal_two);
				}
					break;

				case 4:
				case 5: {
					mNetworkQuality
							.setImageResource(R.drawable.ic_room_signal_one);
				}
					break;

				case 6: {
					mNetworkQuality
							.setImageResource(R.drawable.ic_room_signal_none);
				}
					break;

				default:
					break;
				}
			}
		});
	}

	public void onAudioQuality(final int uid, final int quality, short delay,
			short lost) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				try {

					View remoteView = mRemoteUserContainer.findViewById(Math
							.abs(uid));

					if (remoteView == null) {
						return;
					}

					switch (quality) {

					case 1: {
						((ImageView) remoteView
								.findViewById(R.id.viewlet_remote_video_signal))
								.setImageResource(R.drawable.ic_room_signal_three);
					}
						break;

					case 2:
					case 3: {
						((ImageView) remoteView
								.findViewById(R.id.viewlet_remote_video_signal))
								.setImageResource(R.drawable.ic_room_signal_two);
					}
						break;

					case 4:
					case 5: {
						((ImageView) remoteView
								.findViewById(R.id.viewlet_remote_video_signal))
								.setImageResource(R.drawable.ic_room_signal_one);
					}
						break;

					case 6: {
						((ImageView) remoteView
								.findViewById(R.id.viewlet_remote_video_signal))
								.setImageResource(R.drawable.ic_room_signal_none);
					}
						break;

					default:
						break;
					}

				} catch (Exception e) {

					return;
				}
			}
		});
	}

	public void onLeaveChannel(final IRtcEngineEventHandler.SessionStats stats) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				((TesterApplication) getApplication()).setIsInChannel(false);
				((TesterApplication) getApplication()).setChannelTime(0);

				if (isCorrect) {

					mEvaluationContainer.setVisibility(View.VISIBLE);

					if (stats.totalDuration >= 3600) {
						((TextView) findViewById(R.id.evaluation_time))
								.setText(String.format("%d:%02d:%02d",
										time / 3600, (time % 3600) / 60,
										(time % 60)));
					} else {
						((TextView) findViewById(R.id.evaluation_time))
								.setText(String.format("%02d:%02d",
										(time % 3600) / 60, (time % 60)));
					}

					if (((stats.txBytes + stats.rxBytes) / 1024 / 1024) > 0) {
						((TextView) findViewById(R.id.evaluation_bytes))
								.setText(Integer
										.toString((stats.txBytes + stats.rxBytes) / 1024 / 1024)
										+ "MB");
					} else {
						((TextView) findViewById(R.id.evaluation_bytes))
								.setText(Integer
										.toString((stats.txBytes + stats.rxBytes) / 1024)
										+ "KB");
					}

					mStarOne.setOnClickListener(getViewClickListener());
					mStarTwo.setOnClickListener(getViewClickListener());
					mStarThree.setOnClickListener(getViewClickListener());
					mStarFour.setOnClickListener(getViewClickListener());
					mStarFive.setOnClickListener(getViewClickListener());

					((TesterApplication) getApplication()).setIsInChannel(false);

					(findViewById(R.id.evaluation_close))
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									mEvaluationContainer
											.setVisibility(View.GONE);
									finish();

									Intent i = new Intent(ChannelActivity.this,
											SelectActivity.class);
									startActivity(i);
								}
							});

					(findViewById(R.id.evaluation_evaluate))
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									rtcEngine.rate(callId, score * 2, "");

									mEvaluationContainer
											.setVisibility(View.GONE);
									finish();

									Intent i = new Intent(ChannelActivity.this,
											SelectActivity.class);
									startActivity(i);
								}
							});

				} else {

					finish();

					Intent i = new Intent(ChannelActivity.this,
							LoginActivity.class);
					startActivity(i);
				}
			}
		});
	}

	// show or hide remote user container
	private void setRemoteUserViewVisibility(boolean isVisible) {

		findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ? (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
						.getDisplayMetrics()) : 0;
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
			singleRemoteView.findViewById(R.id.remote_user_voice_container)
					.setVisibility(visibility);

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

	private void setupTime() {

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						time++;

						if (time >= 3600) {
							mDuration.setText(String.format("%d:%02d:%02d",
									time / 3600, (time % 3600) / 60,
									(time % 60)));
						} else {
							mDuration.setText(String.format("%02d:%02d",
									(time % 3600) / 60, (time % 60)));
						}
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	private void setupNotification(String notification) {

		if (TextUtils.isEmpty(mNotificationNew.getText())) {
			mNotificationNew.setText(notification);
		} else {
			mNotificationOld.setText(mNotificationNew.getText().toString());
			mNotificationNew.setText(notification);
		}
	}

	private void clearStars() {

		for (int i = 0; i < stars.size(); i++) {
			stars.get(i).setImageResource(
					R.drawable.ic_evaluate_dialog_cell_star);
		}
	}

	private void setupStars(int i) {

		clearStars();

		if (score == i) {

			score = 0;

		} else {

			score = i;

			for (int j = 0; j < i; j++) {
				stars.get(j).setImageResource(
						R.drawable.ic_evaluate_dialog_cell_star_yellow);
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

		mResolutionSeekBar.setProgress(resolution);
		((TesterApplication) getApplication()).setResolution(resolution);

		mResolutionValue.setText(resolutionValues[resolution]);
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

		mRateSeekBar.setProgress(rate);
		((TesterApplication) getApplication()).setRate(rate);

		mRateValue.setText(rateValues[rate]);
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

		mFrameSeekBar.setProgress(frame);
		((TesterApplication) getApplication()).setFrame(frame);

		mFrameValue.setText(frameValues[frame]);
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

		mVolumeSeekBar.setProgress(volume);
		((TesterApplication) getApplication()).setVolume(volume);

		// mVolumeValue.setText(volumeValues[volume]);
		rtcEngine.setSpeakerphoneVolume(speakerPhoneVolume[volume]);
	}

	// set tape
	@SuppressLint("NewApi")
	private void setTape(boolean isChecked) {

		mTapeSwitch.setChecked(isChecked);

		((TesterApplication) getApplication()).setTape(isChecked);

		if (isChecked) {

			rtcEngine.startAudioRecording(((TesterApplication) getApplication())
					.getPath()
					+ "/"
					+ Integer.toString(Math.abs((int) System
							.currentTimeMillis())) + ".wav");

		} else {

			rtcEngine.stopAudioRecording();
		}
	}

	// set float
	@SuppressLint("NewApi")
	private void setFloat(boolean isChecked) {

		mFloatSwitch.setChecked(isChecked);

		((TesterApplication) getApplication()).setFloat(isChecked);

		mFloatContainer
				.setBackgroundResource(isChecked ? R.drawable.ic_room_bg_talk_time
						: R.color.transparent);
		mStatsContainer.setBackgroundResource(isChecked ? R.color.transparent
				: R.drawable.ic_room_bg_talk_time);
		mNotificationOld.setVisibility(isChecked ? View.VISIBLE : View.GONE);
		mNotificationNew.setVisibility(isChecked ? View.VISIBLE : View.GONE);
		(findViewById(R.id.channel_notification_icon))
				.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	}
}