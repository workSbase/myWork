package com.lenovo.main.util;


import android.content.Context;
import android.media.MediaPlayer;

import com.lenovo.lenovoRobotService.R;

public class PlayMusicTools {
	
	private Context context;
	private MediaPlayer mp;

	public PlayMusicTools(Context context){
		this.context = context;
		mp = MediaPlayer.create(context, R.raw.aesthete);
		mp.setLooping(true);
	}
	
	public  void startMusic(){
		mp.start();
	}
	public  void stopMusic(){
		mp.stop();
	}
}
