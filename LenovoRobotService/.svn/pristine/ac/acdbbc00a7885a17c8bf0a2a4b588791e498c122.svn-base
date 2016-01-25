package com.lenovo.main.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lenovo.lenovoRobotService.R;

/**
 * 用来保存图片和视频路径的一个帮助类
 * 
 * @author Administrator
 * 
 */
public class Source {

	private static Integer[] imgs = { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6,
			R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
			R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14,
			R.drawable.p15, R.drawable.p16, R.drawable.p17 };
	public static ArrayList<Integer> imagArray = new ArrayList<Integer>();
	private static Map<Integer, String> videoPathMap = new HashMap<Integer, String>();

	public static int getImageSourceId(int id) {

		return imgs[id];
	}

	public static boolean setIndex(int index) {
		for (Integer index1 : imgs) {
			imagArray.add(index1);
		}
		if (imagArray.contains(index)) {
			return true;
		}
		return false;
	}

	public static String getVideoPath(int index) {
		if (!(videoPathMap.size() > 0)) {

			videoPathMap.put(17, "/storage/emulated/0/123.mp4");
			videoPathMap.put(18, "/storage/emulated/0/RTKO.mp4");

			return videoPathMap.get(index);
		}
		return null;
	}
}
