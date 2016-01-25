package cn.com.lenovo.speechservice.utils;

import com.google.gson.Gson;

/**
 * JSON 解析的工具类
 * @author kongqw
 *
 */
public class GsonUtil {
	/*
	 * 将JSON封装到JavaBean
	 */
	public static <T> T json2Bean(String json, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}
}
