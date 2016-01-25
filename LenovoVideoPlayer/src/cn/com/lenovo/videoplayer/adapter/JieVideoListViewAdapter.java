package cn.com.lenovo.videoplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.lenovo.videoplayer.R;
import cn.com.lenovo.videoplayer.component.LoadedImage;
import cn.com.lenovo.videoplayer.videofile.Video;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JieVideoListViewAdapter extends BaseAdapter {

	List<Video> listVideos;
	int local_postion = 0;
	boolean imageChage = false;
	private LayoutInflater mLayoutInflater;
	private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();
	private Typeface typeface;

	public JieVideoListViewAdapter(Context context, List<Video> listVideos) {
		mLayoutInflater = LayoutInflater.from(context);
		this.listVideos = listVideos;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/lenovo_font.ttf");
	}

	@Override
	public int getCount() {
		int a = photos.size();
		return a;
	}

	public void addPhoto(LoadedImage image) {
		photos.add(image);
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.video_list_view, null);
			holder.img = (ImageView) convertView.findViewById(R.id.video_img);
			holder.title1 = (TextView) convertView.findViewById(R.id.video_title1);
			holder.title1.setTypeface(typeface);
			holder.time1 = (TextView) convertView.findViewById(R.id.video_time1);
			holder.time1.setTypeface(typeface);

			holder.title = (TextView) convertView.findViewById(R.id.video_title);
			holder.title.setTypeface(typeface);
			holder.time = (TextView) convertView.findViewById(R.id.video_time);
			holder.time.setTypeface(typeface);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(listVideos.get(position).getTitle());
		long min = listVideos.get(position).getDuration() / 1000 / 60;
		long sec = listVideos.get(position).getDuration() / 1000 % 60;
		holder.time.setText(min + " : " + sec);
		holder.img.setImageBitmap(photos.get(position).getBitmap());

		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title1;
		public TextView time1;
		public TextView title;
		public TextView time;
	}
}
