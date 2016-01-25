package cn.com.lenovo.videoplayer;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.lenovo.videoplayer.videofile.Video;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.annotation.SuppressLint;
import android.content.Intent;

public class VideoPlayerActivity extends VideoPlayerBaseActivity implements OnClickListener {

	// 隐藏控制台的标志位
	protected static final int HIDECONSOLE = 0;
	// 显示视频当前播放位置的标志位
	protected static final int CURRENTPOSITIONL = 1;
	// 播放视频的数据
	private Video playVideoFile;
	// 联想播放器
	private VideoView lenovoVideoPlayer;
	// 视频时长
	private TextView tv_timeSum;
	// 播放当时间
	private TextView tv_time;
	// 播放比率
	private TextView tv_percentage;
	// 进度条
	private SeekBar seekBar;
	// 定时器
	private Timer timer;
	private Timer controlTimer;
	// 定时任务
	private TimerTask task;
	private TimerTask consoleTask;
	// 倒计时3秒
	private int recLen = 3;
	// 视频总时长
	private long duration;
	// 视频控制台的布局
	private RelativeLayout rl_console;
	// 视频标题栏
	private RelativeLayout rl_title;
	// 控制台控制按钮
	private Button bt_start;
	private Button bt_pause;
	private Button bt_stop;
	private Button bt_rew;
	private Button bt_ff;
	private Button bt_previous;
	private Button bt_next;
	private Button bt_video_list;
	private Button bt_close;

	private TextView tv_title;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case HIDECONSOLE:
				hideConsole();
				break;
			case CURRENTPOSITIONL:
				int currentPosition = (Integer) msg.obj;
				tv_time.setText(currentPosition + "");
				tv_percentage.setText("进度：" + (currentPosition * 999 / duration / 10) + "%");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);

		// 注册播放器
		lenovoVideoPlayer = (VideoView) findViewById(R.id.lenovoVideoPlayer);
		bt_start = ((Button) findViewById(R.id.bt_start));
		bt_pause = (Button) findViewById(R.id.bt_pause);
		bt_stop = (Button) findViewById(R.id.bt_stop);
		bt_rew = (Button) findViewById(R.id.bt_rew);
		bt_ff = (Button) findViewById(R.id.bt_ff);
		bt_previous = (Button) findViewById(R.id.bt_previous);
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_video_list = (Button) findViewById(R.id.bt_video_list);
		bt_close = (Button) findViewById(R.id.bt_close);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_timeSum = (TextView) findViewById(R.id.tv_timeSum);
		tv_percentage = (TextView) findViewById(R.id.tv_percentage);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		rl_console = (RelativeLayout) findViewById(R.id.rl_console);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		tv_title = (TextView) findViewById(R.id.tv_title);
		// 添加控制按钮点击事件
		bt_start.setOnClickListener(this);
		bt_pause.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_rew.setOnClickListener(this);
		bt_ff.setOnClickListener(this);
		bt_previous.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_video_list.setOnClickListener(this);
		bt_close.setOnClickListener(this);

		// ~~~ 获取播放地址和标题
		Bundle bundle = getIntent().getExtras();
		playVideoFile = (Video) bundle.getSerializable("video");
		mPath = playVideoFile.getPath();
		String mTitle = playVideoFile.getTitle();
		duration = playVideoFile.getDuration();

		// 设置电影显示的标题
		tv_title.setText(mTitle);

		// 设置播放的路径
		lenovoVideoPlayer.setVideoPath(mPath);
		// 开始播放
		lenovoVideoPlayer.start();

		// 为进度条添加进度更改事件
		seekBar.setOnSeekBarChangeListener(change);

		// 设置总时间
		tv_timeSum.setText(duration + "");

		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// 当前时间
				int currentPosition = lenovoVideoPlayer.getCurrentPosition();
				long bfb = currentPosition * 999 / duration;
				Log.i("----", "---" + currentPosition + " / " + duration + "----" + bfb + "%");
				seekBar.setProgress(Integer.parseInt(String.valueOf(bfb)));
				Message msg = Message.obtain();
				msg.what = CURRENTPOSITIONL;
				msg.obj = currentPosition;
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 100, 100);

	}

	/**
	 * 播放进度条进度变化的监听器
	 */
	private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 设置重新倒计时
			recLen = 3;
			// 当进度条停止修改的时候触发
			// 取得当前进度条的刻度
			int progress = seekBar.getProgress();
			Toast.makeText(VideoPlayerActivity.this, "progress = " + progress, Toast.LENGTH_LONG).show();
			if (lenovoVideoPlayer != null && lenovoVideoPlayer.isPlaying()) {
				// 设置当前播放的位置
				long a = progress * duration / 999;
				lenovoVideoPlayer.seekTo(Integer.parseInt(String.valueOf(a)));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		}
	};
	private String mPath;

	/**
	 * 点击事件
	 */

	public void startVideo(String curPath) {
		if (lenovoVideoPlayer.isPlaying()) {
			// Toast.makeText(this, "暂停按钮被点击", Toast.LENGTH_SHORT).show();
			((Button) findViewById(R.id.bt_start)).setBackgroundResource(R.drawable.ic_media_play);
			// lenovoVideoPlayer.pause();
			// 设置播放的路径
			lenovoVideoPlayer.setVideoPath(curPath);
			lenovoVideoPlayer.start();
		} else {
			// Toast.makeText(this, "开始按钮被点击", Toast.LENGTH_SHORT).show();
			((Button) findViewById(R.id.bt_start)).setBackgroundResource(R.drawable.ic_media_pause);
			// 设置播放的路径
			lenovoVideoPlayer.setVideoPath(curPath);
			lenovoVideoPlayer.start();
		}
	}

	public void stopVideo() {
		((Button) findViewById(R.id.bt_start)).setBackgroundResource(R.drawable.ic_media_play);
		lenovoVideoPlayer.stopPlayback();
	}

	@Override
	public void onClick(View v) {
		// 设置重新倒计时
		recLen = 3;
		switch (v.getId()) {
		case R.id.bt_start:

			startVideo(mPath);

			break;
		case R.id.bt_stop:
			stopVideo();

			break;
		case R.id.bt_rew:
			slideToLeft();
			break;
		case R.id.bt_ff:
			slideToRight();
			break;
		case R.id.bt_previous:
			Toast.makeText(this, "上一个功能未实现", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_next:
			Toast.makeText(this, "下一个功能未实现", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_video_list:
			finish();
			break;
		case R.id.bt_close:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		task.cancel();
		timer.purge();
		timer.cancel();
		super.onDestroy();
	}

	/**
	 * 想左侧滑动 快退
	 */
	@Override
	public void slideToLeft() {
		// 取得当前进度条的刻度
		int progress = seekBar.getProgress();
		// 获取快退5%后的秒数
		long a = (progress - 50 > 0 ? progress - 50 : 0) * duration / seekBar.getMax();
		lenovoVideoPlayer.seekTo(Integer.parseInt(String.valueOf(a)));
		lenovoVideoPlayer.start();
	}

	/**
	 * 向右侧滑动 快进
	 */
	@Override
	public void slideToRight() {
		// 取得当前进度条的刻度
		int progress1 = seekBar.getProgress();
		// 获取快退5%后的秒数
		long b = (progress1 + 50 > duration ? duration : progress1 + 50) * duration / seekBar.getMax();
		lenovoVideoPlayer.seekTo(Integer.parseInt(String.valueOf(b)));
		lenovoVideoPlayer.start();
	}

	/**
	 * 屏幕被点击
	 */
	@Override
	public void clickScreen() {
		if (rl_console.getVisibility() == View.INVISIBLE) {
			showConsole();
		} else {
			// 设置重新倒计时
			recLen = 3;
			if (lenovoVideoPlayer.isPlaying()) {
				// Toast.makeText(this, "暂停按钮被点击", Toast.LENGTH_SHORT).show();
				((Button) findViewById(R.id.bt_start)).setBackgroundResource(R.drawable.ic_media_play);
				lenovoVideoPlayer.pause();
			} else {
				// Toast.makeText(this, "开始按钮被点击", Toast.LENGTH_SHORT).show();
				((Button) findViewById(R.id.bt_start)).setBackgroundResource(R.drawable.ic_media_pause);
				lenovoVideoPlayer.start();
			}
		}
	}

	/**
	 * 隐藏控制台
	 */
	public void hideConsole() {
		disShowMenu(rl_console);
		disShowMenu(rl_title);
	}

	/**
	 * 显示控制台
	 */
	public void showConsole() {
		showMenu(rl_console);
		showMenu(rl_title);
		// 设置重新倒计时
		recLen = 3;
		// 计时3秒如果没有触摸显示屏，则隐藏控制台
		controlTimer = new Timer();
		consoleTask = new TimerTask() {
			@Override
			public void run() {
				recLen--;
				if (0 == recLen) {
					Message msg = Message.obtain();
					msg.what = HIDECONSOLE;
					handler.sendMessage(msg);
					consoleTask.cancel();
					controlTimer.purge();
					controlTimer.cancel();
				}
			}
		};
		controlTimer.schedule(consoleTask, 1000, 1000);
	}

	// 隐藏菜单
	public void disShowMenu(ViewGroup iv) {
		Animation translateIn = null;
		if (R.id.rl_console == iv.getId()) {
			// 控制台向下平移隐藏
			translateIn = new TranslateAnimation(0, 0, 0, iv.getHeight());
		} else if (R.id.rl_title == iv.getId()) {
			translateIn = new TranslateAnimation(0, 0, 0, -iv.getHeight());
		} else {
			return;
		}
		// 移动时间
		translateIn.setDuration(500);
		// 移动后保持移动后的状态
		translateIn.setFillAfter(true);
		// 禁用控制台中所有的控件
		setConsoleState(false);
		// 开始移动
		iv.startAnimation(translateIn);
		// 控件这只隐藏
		iv.setVisibility(View.INVISIBLE);
	}

	// 显示菜单
	public void showMenu(ViewGroup iv) {
		// 控制台向下平移隐藏
		// Animation translateIn = new TranslateAnimation(0, 0, iv.getHeight(),
		// 0);

		Animation translateIn = null;
		if (R.id.rl_console == iv.getId()) {
			// 控制台向下平移隐藏
			translateIn = new TranslateAnimation(0, 0, iv.getHeight(), 0);
		} else if (R.id.rl_title == iv.getId()) {
			translateIn = new TranslateAnimation(0, 0, -iv.getHeight(), 0);
		} else {
			return;
		}
		// 移动时间
		translateIn.setDuration(500);
		// 移动后保持移动后的状态
		translateIn.setFillAfter(true);
		// 禁用控制台中所有的控件
		setConsoleState(true);
		// 开始移动
		iv.startAnimation(translateIn);
		// 控件这只隐藏
		iv.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置控制台按钮的状态
	 * 
	 * @param state
	 */
	private void setConsoleState(boolean state) {
		bt_start.setEnabled(state);
		bt_pause.setEnabled(state);
		bt_stop.setEnabled(state);
		bt_rew.setEnabled(state);
		bt_ff.setEnabled(state);
		bt_previous.setEnabled(state);
		bt_next.setEnabled(state);
		bt_video_list.setEnabled(state);
		seekBar.setEnabled(state);
		bt_close.setEnabled(state);
	}

	@Override
	protected void onStart() {
		super.onStart();
		showConsole();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onStop() {
		Intent intent = new Intent();
		intent.setAction("closeProject");
		sendBroadcast(intent);
		Toast.makeText(this, "播放完成\naction：closeProject", Toast.LENGTH_SHORT).show();
		super.onStop();
	}
}
