package com.lenovo.lenovorobotmobile.adapter;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.utils.PicturePath;
import com.lenovo.lenovorobotmobile.utils.VideoFrame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 使用内存缓存做的一个 adapter 把加载到的图片保存在内存中
 * 
 * @author Administrator
 * 
 */
public class PhotoWall_Lrucache extends ArrayAdapter<String> {
	private GridView mGridView;
	// 图片缓存类
	private LruCache<String, Bitmap> mLruCache;
	// GridView中可见的第一张图片的下标
	private int mFirstVisibleItem;
	// GridView中可见的图片的数量
	private int mVisibleItemCount;
	// 记录是否是第一次进入该界面
	private boolean isFirstEnterThisActivity = true;
	private Bitmap bitmap;
	private ImageView iconVideoImag;

	public PhotoWall_Lrucache(Context context, int resource, String[] objects,
			GridView gridView) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mGridView = gridView;

		mGridView.setOnScrollListener(new ScrollListenerImpl());
		// 应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();

		// 设置图片缓存大小为maxMemory的1/3
		int cacheSize = maxMemory / 3;

		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 返回每一张图片的大小
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String path = (String) getItem(position);
		View view;
		// Log.i("PATH", path);
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.photo_layout, null);
		} else {
			view = convertView;
		}
		ImageView imageView = (ImageView) view.findViewById(R.id.photo);

		imageView.setTag(path);

		iconVideoImag = (ImageView) view.findViewById(R.id.iconVideo);

		// 让视频图标显示出来的条件
		if (path.contains(".mp4") && path.endsWith(".mp4")) {
			iconVideoImag.setVisibility(View.VISIBLE);
		} else {
			iconVideoImag.setVisibility(View.GONE);
		}
		// 为该ImageView设置显示的图片
		setImageForImageView(path, imageView);

		return view;
	}

	/**
	 * 为ImageView设置图片(Image) 1 从缓存中获取图片 2 若图片不在缓存中则为其设置默认图片
	 */
	@SuppressWarnings("deprecation")
	private void setImageForImageView(String imagePath, ImageView imageView) {
		Bitmap bitmap = getBitmapFromLruCache(imagePath);
		if (bitmap != null) {
			imageView.setBackground(new BitmapDrawable(bitmap));
		} else {
			imageView.setBackgroundResource(R.drawable.call_share);
		}
	}

	/**
	 * 将图片存储到LruCache
	 */
	public void addBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache缓存获取图片
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return (Bitmap) mLruCache.get(key);
	}

	private class ScrollListenerImpl implements OnScrollListener {
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;
			if (isFirstEnterThisActivity && visibleItemCount > 0) {
				System.out.println("---> 第一次进入该界面");
				loadBitmaps(firstVisibleItem, visibleItemCount);
				isFirstEnterThisActivity = false;
			}
		}

		/**
		 * GridView停止滑动时下载图片 其余情况下取消所有正在下载或者等待下载的任务
		 */
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (scrollState == SCROLL_STATE_IDLE) {
				System.out.println("---> GridView停止滑动  mFirstVisibleItem="
						+ mFirstVisibleItem + ",mVisibleItemCount="
						+ mVisibleItemCount);
				loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
			}
		}
	}

	/**
	 * 为GridView的item加载图片
	 * 
	 * @param firstVisibleItem
	 *            GridView中可见的第一张图片的下标
	 * @param visibleItemCount
	 *            GridView中可见的图片的数量
	 */
	@SuppressWarnings("deprecation")
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {

		try {
			for (int i = firstVisibleItem; i < firstVisibleItem
					+ visibleItemCount; i++) {
				String imagePath = PicturePath.IMAGES_PATH[i];

				if (imagePath.contains(".mp4") && imagePath.endsWith(".mp4")) {

					Bitmap bitmap = getBitmapFromLruCache(imagePath);
					if (bitmap == null) {
						// bitmap = BitmapFactory.decodeFile(imagePath);
						bitmap = VideoFrame.getVideoThumbnail(imagePath);
						ImageView imageView = (ImageView) mGridView
								.findViewWithTag(imagePath);

						if (imageView != null && bitmap != null) {
							imageView.setBackground(new BitmapDrawable(bitmap));
						}
						// 将从SDCard读取的图片添加到LruCache中
						addBitmapToLruCache(imagePath, bitmap);
					} else {
						// 依据Tag找到对应的ImageView显示图片
						ImageView imageView = (ImageView) mGridView
								.findViewWithTag(imagePath);
						if (imageView != null && bitmap != null) {
							imageView.setBackground(new BitmapDrawable(bitmap));
						}
					}
				} else {
					Bitmap bitmap = getBitmapFromLruCache(imagePath);
					if (bitmap == null) {
						bitmap = BitmapFactory.decodeFile(imagePath);
						ImageView imageView = (ImageView) mGridView
								.findViewWithTag(imagePath);
						if (imageView != null && bitmap != null) {
							imageView.setBackground(new BitmapDrawable(bitmap));
						}
						// 将从SDCard读取的图片添加到LruCache中
						addBitmapToLruCache(imagePath, bitmap);
					} else {
						// 依据Tag找到对应的ImageView显示图片
						ImageView imageView = (ImageView) mGridView
								.findViewWithTag(imagePath);
						if (imageView != null && bitmap != null) {
							imageView.setBackground(new BitmapDrawable(bitmap));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
