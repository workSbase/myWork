package com.lenovo.lenovorobotmobile.speech;

public class StringUtils {
	/**
	 * 中文数字转换为阿拉伯数字
	 * @param cn
	 * @return
	 */
	public static String strNum2IntNum(String cn) {
		String intNum = "0";
		if ("一".equals(cn)) {
			intNum = "1";
		} else if ("二".equals(cn)) {
			intNum = "2";
		} else if ("两".equals(cn)) {
			intNum = "2";
		} else if ("三".equals(cn)) {
			intNum = "3";
		} else if ("四".equals(cn)) {
			intNum = "4";
		} else if ("五".equals(cn)) {
			intNum = "5";
		} else if ("六".equals(cn)) {
			intNum = "6";
		} else if ("七".equals(cn)) {
			intNum = "7";
		} else if ("八".equals(cn)) {
			intNum = "8";
		} else if ("九".equals(cn)) {
			intNum = "9";
		} else if ("十".equals(cn)) {
			if (1 == cn.length()) {
				intNum = "10";
			}
		} else {
			intNum = "" + cn;
		}

		return intNum;
	}
}
