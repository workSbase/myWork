package com.lenovo.lenovorobotmobile.bin;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Location {
	public String intent;
	public ArrayList<Locations> locations;

	public class Locations {
		public String key;
		public String value;
	}

	/**
	 * 获取坐标
	 * @param location
	 * @return
	 */
	public String getLocation(String location) {
		if (null == locations) {
			return "0,0";
		}
		// 获取注册的位置数量
		int locationCount = locations.size();
		for (int index = 0; index < locationCount; index++) {
			String key = locations.get(index).key;

			Pattern pattern = Pattern.compile("^(" + key + ")$");
			Matcher matcher = pattern.matcher(location);
			if (matcher.matches()) {
				// 匹配
				return locations.get(index).value;
			}
		}
		return "0,0";
	}
}
