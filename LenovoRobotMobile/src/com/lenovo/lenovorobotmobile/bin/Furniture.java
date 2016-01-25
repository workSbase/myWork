package com.lenovo.lenovorobotmobile.bin;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Furniture {

	public String intent;
	public ArrayList<Furnitures> furnitures;

	public class Furnitures {
		public String furnitureName;
		public String furnitureId;
	}

	/**
	 * 获取家具编号
	 * @param name
	 * @return
	 */
	public String getFurnitureId(String name) {
		// 获取家具个数
		int furnitureCount = furnitures.size();
		for (int index = 0; index < furnitureCount; index++) {
			Pattern pattern = Pattern.compile("^(" + furnitures.get(index).furnitureName + ")$");
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
				// 匹配
				return furnitures.get(index).furnitureId;
			}
		}
		return null;
	}
}
