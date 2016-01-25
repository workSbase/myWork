package cn.com.lenovo.videoplayer;

import java.util.List;
import cn.com.lenovo.videoplayer.adapter.JieVideoListViewAdapter;
import cn.com.lenovo.videoplayer.component.LoadedImage;
import cn.com.lenovo.videoplayer.utils.ProjecterUtils;
import cn.com.lenovo.videoplayer.videofile.AbstructProvider;
import cn.com.lenovo.videoplayer.videofile.Video;
import cn.com.lenovo.videoplayer.videofile.VideoProvider;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public class VideoListActivity extends Activity {
	// 字母滚动查询表
	private ImageView alphabet_scroller;
	// 显示手指滑动到哪个字母
	private TextView first_letter_overlay;
	// ListView
	private ListView mJieVideoListView;
	// ListView数据适配器
	private JieVideoListViewAdapter mJieVideoListViewAdapter;
	// 获取的视频列表
	public static List<Video> listVideos;
	// 视频个数
	int videoSize;

	public VideoListActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		instance = this;
		provider = new VideoProvider(instance);

		listVideos = provider.getList();
		// 获取视频个数
		videoSize = listVideos.size();
		// 控件注册
		alphabet_scroller = (ImageView) findViewById(R.id.alphabet_scroller);
		alphabet_scroller.setClickable(true);
		alphabet_scroller.setOnTouchListener(asOnTouch);

		// 显示字母控件
		first_letter_overlay = (TextView) findViewById(R.id.first_letter_overlay);

		mJieVideoListViewAdapter = new JieVideoListViewAdapter(this, listVideos);
		// ListView控件
		mJieVideoListView = (ListView) findViewById(R.id.jievideolistfile);
		mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
		mJieVideoListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent();
				intent.setClass(VideoListActivity.this, VideoPlayerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("video", listVideos.get(position));
				intent.putExtras(bundle);
				boolean isOpenProjecter = projecterUtils.startVideo(listVideos.get(position).getPath());
				if (!isOpenProjecter) {
					startActivity(intent);
				}

			}
		});

		// 加载数据
		loadImages();

		// 头播放类
		projecterUtils = new ProjecterUtils(this);
	}

	/**
	 * Load images.
	 */
	private void loadImages() {
		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}
	}

	/**
	 * A-Z
	 */
	private OnTouchListener asOnTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// 0
				alphabet_scroller.setPressed(true);
				first_letter_overlay.setVisibility(View.VISIBLE);
				mathScrollerPosition(event.getY());
				break;
			case MotionEvent.ACTION_UP:// 1
				alphabet_scroller.setPressed(false);
				first_letter_overlay.setVisibility(View.GONE);
				break;
			case MotionEvent.ACTION_MOVE:
				mathScrollerPosition(event.getY());
				break;
			}
			return false;
		}
	};
	public static AbstructProvider provider;
	public static ProjecterUtils projecterUtils;

	/**
	 * 显示字符
	 * 
	 * @param y
	 */
	private void mathScrollerPosition(float y) {
		int height = alphabet_scroller.getHeight();
		float charHeight = height / 28.0f;
		char c = 'A';
		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;

		int index = (int) (y / charHeight) - 1;
		if (index < 0)
			index = 0;
		else if (index > 25)
			index = 25;

		String key = String.valueOf((char) (c + index));
		first_letter_overlay.setText(key);

		int position = 0;
		if (index == 0)
			mJieVideoListView.setSelection(0);
		else if (index == 25)
			mJieVideoListView.setSelection(mJieVideoListViewAdapter.getCount() - 1);
		else {
			int size = listVideos.size();
			for (int i = 0; i < size; i++) {
				if (listVideos.get(i).getTitle_key().startsWith(key)) {
					mJieVideoListView.setSelection(position);
					break;
				}
				position++;
			}
		}
	}

	/**
	 * 从SD卡获取数据
	 * 
	 * @author kongqw
	 * 
	 */
	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			for (int i = 0; i < videoSize; i++) {
				bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
				if (bitmap != null) {
					publishProgress(new LoadedImage(bitmap));
				}
			}
			return null;
		}

		@Override
		public void onProgressUpdate(LoadedImage... value) {
			addImage(value);
		}
	}

	/**
	 * 获取视频缩略图
	 * 
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 给适配器添加显示数据
	 * 
	 * @param value
	 */
	private void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			mJieVideoListViewAdapter.addPhoto(image);
			mJieVideoListViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onBackPressed() {

		projecterUtils.dimissDialog();

		super.onBackPressed();
	}
}
