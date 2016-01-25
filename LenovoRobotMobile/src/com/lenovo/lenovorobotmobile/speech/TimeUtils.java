package com.lenovo.lenovorobotmobile.speech;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.text.TextUtils;

public class TimeUtils {
	/**
	 * 将中文格式的时间，转换为数字形式的时间 三点半 -> 03:30
	 * 
	 * @return
	 */
	public static String stringTime2IntTime(String stringTime) {
		StringBuffer intTime = new StringBuffer();
		String time = stringTime.substring(0, stringTime.indexOf("点"));
		if (1 == time.length()) {
			intTime.append(StringUtils.strNum2IntNum(time).length() < 2 ? "0"
					+ StringUtils.strNum2IntNum(time) : StringUtils
					.strNum2IntNum(time));
		} else if (2 == time.length()) {
			if (time.startsWith("十")) {
				intTime.append("1"
						+ StringUtils.strNum2IntNum(time.substring(1)));
			} else if ("二十".equals(time)) {
				intTime.append("20");
			} else if ("12".equals(time)) {
				intTime.append("12");
			} else if ("11".equals(time)) {
				intTime.append("11");
			} else if ("10".equals(time)) {
				intTime.append("10");
			} else {
				intTime.append("--");
			}
		} else if (3 == time.length()) {
			if (time.startsWith("二十")) {
				intTime.append("2"
						+ (Integer.parseInt(StringUtils.strNum2IntNum(time
								.substring(2))) <= 4 ? Integer
								.parseInt(StringUtils.strNum2IntNum(time
										.substring(2))) : 4));
			} else {
				intTime.append("--");
			}
		}

		if (stringTime.endsWith("点") || stringTime.endsWith("点整")
				|| stringTime.endsWith("点钟")) {
			// 整点时间
			intTime.append(":00");
		} else if (stringTime.endsWith("点半")) {
			// 半点时间
			intTime.append(":30");
		} else {
			// 在Json中没有注册的时间格式
			intTime.append(":--");
		}

		return intTime.toString();
	}

	/**
	 * 将中文格式的日期，转换为数字形式的日期 三十二 -> 32
	 * 
	 * @param stringDate
	 * @return
	 */
	public static String stringDate2IntDate(String stringDate) {
		StringBuffer intDate = new StringBuffer();
		if (1 == stringDate.length()) {
			intDate.append(StringUtils.strNum2IntNum(stringDate).length() < 2 ? "0"
					+ StringUtils.strNum2IntNum(stringDate)
					: StringUtils.strNum2IntNum(stringDate));
		} else if (2 == stringDate.length()) {
			if (stringDate.startsWith("十")) {
				intDate.append("1"
						+ StringUtils.strNum2IntNum(stringDate.substring(1)));
			} else if ("二十".equals(stringDate)) {
				intDate.append("20");
			} else if ("三十".equals(stringDate)) {
				intDate.append("30");
			} else {
				intDate.append("--");
			}
		} else if (3 == stringDate.length()) {
			if (stringDate.startsWith("二十")) {
				intDate.append("2"
						+ StringUtils.strNum2IntNum(stringDate.substring(2)));
			} else if (stringDate.startsWith("三十")) {
				intDate.append("3"
						+ (Integer.parseInt(StringUtils
								.strNum2IntNum(stringDate.substring(2))) <= 2 ? Integer
								.parseInt(StringUtils.strNum2IntNum(stringDate
										.substring(2))) : 2));
			} else {
				intDate.append("--");
			}
		}
		return intDate.toString();
	}

	/**
	 * 格式化时间
	 * 
	 * @param cn
	 * @param time
	 * @return
	 */
	public static String formatTime(String cn, String time) {
		String[] time_item = time.split(":");
		int parseInt = Integer.parseInt(time_item[0]);
		if (12 > parseInt
				&& ("下午".equals(cn) || "傍晚".equals(cn) || "晚上".equals(cn))) {
			time = parseInt + 12 + ":" + time_item[1];
		} else {
			time = (time_item[0].length() < 2 ? "0" + time_item[0]
					: time_item[0])
					+ ":"
					+ (time_item[1].length() < 2 ? "0" + time_item[1]
							: time_item[1]);
		}

		return time;
	}

	/**
	 * 将中文日期转化为阿拉伯数字的日期 2015年3月4号 -> 2015-03-04
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		String date_yyyy_mm_dd = "";
		int index_year = date.indexOf("年");
		int index_month = date.indexOf("月");
		int index_day1 = date.indexOf("日");
		int index_day2 = date.indexOf("号");

		String year = "0000";
		String month = "00";
		String day = "00";
		if (-1 != index_year) {
			year = date.substring(0, 4);
			month = date.substring(index_year + 1, index_month);
		} else {
			year = getTime_YYYY(0);
			month = date.substring(0, index_month);
		}
		if (-1 != index_day1) {
			day = date.substring(index_month + 1, index_day1);
		} else {
			day = date.substring(index_month + 1, index_day2);
		}

		date_yyyy_mm_dd = year + "-"
				+ (month.length() < 2 ? "0" + month : month) + "-"
				+ (day.length() < 2 ? "0" + day : day);

		return date_yyyy_mm_dd;
	}

	/**
	 * 获取系统当前“YYYY”格式的日期
	 * 
	 * @param deviation
	 *            年份偏差 1 明年 0 今年 -1 去年 -2 前年
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime_YYYY(int deviation) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

		// 得到一个Calendar的实例
		Calendar ca = Calendar.getInstance();
		// 设置时间为当前时间
		ca.setTime(new Date());
		// 天数偏差
		ca.add(Calendar.YEAR, deviation);
		// 结果
		Date curDate = ca.getTime();
		String time_YYYY = formatter.format(curDate);
		return time_YYYY;
	}

	/**
	 * 获取系统当前“YYYY_MM”格式的日期
	 * 
	 * @param deviation
	 *            月份偏差 1 上个月 0 本月 -1 下个月
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime_YYYY_MM(int deviation) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");

		// 得到一个Calendar的实例
		Calendar ca = Calendar.getInstance();
		// 设置时间为当前时间
		ca.setTime(new Date());
		// 天数偏差
		ca.add(Calendar.MONTH, deviation);
		// 结果
		Date curDate = ca.getTime();
		String time_YYYY_MM = formatter.format(curDate);
		return time_YYYY_MM;
	}

	/**
	 * 获取系统当前“YYYY_MM_dd”格式的日期
	 * 
	 * @param deviation
	 *            日期偏差 1 明天 0 今天 -1 昨天 -2 前天
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime_YYYY_MM_dd(int deviation) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// 得到一个Calendar的实例
		Calendar ca = Calendar.getInstance();
		// 设置时间为当前时间
		ca.setTime(new Date());
		// 天数偏差
		ca.add(Calendar.DAY_OF_MONTH, deviation);
		// 结果
		Date curDate = ca.getTime();
		String time_YYYY_MM_dd = formatter.format(curDate);
		return time_YYYY_MM_dd;

	}

	/**
	 * 获取文字开头的时间信息 今天/昨天/前天……->xxxx-xx-xx (xxxx年)xx月xx日/号……-〉xxxx-xx-xx
	 * 
	 * @param text
	 * @return
	 */
	public static String getDateFromTextStart(String text) {
		String date;
		if (text.startsWith("今天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(0);
		} else if (text.startsWith("昨天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(-1);
		} else if (text.startsWith("前天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(-2);
		} else if (text.startsWith("大前天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(-3);
		} else if (text.startsWith("明天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(1);
		} else if (text.startsWith("后天")) {
			date = TimeUtils.getTime_YYYY_MM_dd(2);
		} else {
			date = TimeUtils.formatDate(text);
		}
		return date;
	}

	/**
	 * 获取周期 0 没有周期 1 每天 2 每周 3 每月 4 每年
	 * 
	 * @param string
	 * @return
	 */
	public static String getPeriod(String string) {
		String period = "";
		if (string.startsWith("大前天") || string.startsWith("前天")
				|| string.startsWith("昨天") || string.startsWith("今天")
				|| string.startsWith("明天") || string.startsWith("后天")
				|| string.startsWith("大后天")) {
			period = "0";
		} else if (string.startsWith("每天")) {
			period = "1";
		} else if (string.startsWith("每周") || string.startsWith("每星期")) {
			period = "2";
		} else if (string.startsWith("每月")) {
			period = "3";
		} else if (string.startsWith("每年")) {
			period = "4";
		} else {
			period = "0";
		}
		return period;
	}

	/**
	 * 获取日期
	 * 
	 * @param string
	 * @return
	 */
	public static String getDate(String string) {
		String date = "";
		if (string.equals("大前天")) {
			date = getTime_YYYY_MM_dd(-3);
		} else if (string.equals("前天")) {
			date = getTime_YYYY_MM_dd(-2);
		} else if (string.equals("昨天")) {
			date = getTime_YYYY_MM_dd(-1);
		} else if (string.equals("今天") || TextUtils.isEmpty(string)
				|| string.equals("每天")) {
			date = getTime_YYYY_MM_dd(0);
		} else if (string.equals("明天")) {
			date = getTime_YYYY_MM_dd(1);
		} else if (string.equals("后天")) {
			date = getTime_YYYY_MM_dd(2);
		} else if (string.equals("大后天")) {
			date = getTime_YYYY_MM_dd(3);
		} else if (string.startsWith("每周") || string.startsWith("每星期")) {
			date = getDateByWeek(string);
		} else if (string.startsWith("每月")) {
			date = getDateByMonth(string);
		} else if (string.startsWith("每年")) {
			date = getDateByYear(string);
		} else if (Pattern.compile("^(\\d{1,2}月\\d{1,2}(日|号))$")
				.matcher(string).matches()) {
			date = "xxxx-00-00";
		} else if (Pattern.compile("^(\\d{4}年\\d{1,2}月\\d{1,2}(日|号))$")
				.matcher(string).matches()) {
			date = "0000-00-00";
		}
		return date;
	}

	/**
	 * 通过周期获取日期 每周三 -> 2015-03-15
	 * 
	 * @param week
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private static String getDateByWeek(String week) {
		Calendar cal = Calendar.getInstance();
		// 获取今天是周几
		cal.setTime(new Date());
		int i = cal.get(Calendar.DAY_OF_WEEK);

		if (week.equals("每周日") || week.equals("每星期日") || week.equals("每星期天")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 1 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		} else if (week.equals("每周一") || week.equals("每星期一")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 2 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else if (week.equals("每周二") || week.equals("每星期二")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 3 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		} else if (week.equals("每周三") || week.equals("每星期三")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 4 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		} else if (week.equals("每周四") || week.equals("每星期四")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 5 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		} else if (week.equals("每周五") || week.equals("每星期五")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 6 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} else if (week.equals("每周六") || week.equals("每星期六")) {
			// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
			cal.add(Calendar.DATE, i <= 7 ? 0 : 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		}

		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return date;
	}

	/**
	 * 通过周期获取日期 每月3号 -> 2015-04-03
	 * 
	 * @param week
	 * @return
	 */
	private static String getDateByMonth(String string) {
		// 获取今天是这个月的第多少天
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String date = string.substring(2, string.length() - 1);
		if (Pattern.compile("^(\\d{1,2})$").matcher(date).matches()) {
		} else {
			date = stringDate2IntDate(date);
		}

		if (Integer.parseInt(date) >= day) {
			date = getTime_YYYY_MM(0) + "-"
					+ (date.length() < 2 ? "0" + date : date);
		} else {
			date = getTime_YYYY_MM(1) + "-"
					+ (date.length() < 2 ? "0" + date : date);
		}

		return date;
	}

	/**
	 * 通过周期获取日期 每年3月4号 -> 2015-03-04
	 * 
	 * @param string
	 * @return
	 */
	private static String getDateByYear(String string) {
		// 获取当前月份是第几个月
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);

		String month = string.substring(2, string.indexOf("月"));
		if (!Pattern.compile("^(\\d{1,2})$").matcher(month).matches()) {
			month = stringDate2IntDate(month);
		}
		String day = string.substring(string.indexOf("月") + 1,
				string.length() - 1);
		if (!Pattern.compile("^(\\d{1,2})$").matcher(day).matches()) {
			day = stringDate2IntDate(day);
		}

		String date = getTime_YYYY_MM_dd(0);
		if (Integer.parseInt(month) > m
				|| (Integer.parseInt(month) == m && Integer.parseInt(day) >= d)) {
			date = getTime_YYYY(0) + "-"
					+ (month.length() < 2 ? "0" + month : month) + "-"
					+ (day.length() < 2 ? "0" + day : day);
		} else {
			date = getTime_YYYY(1) + "-"
					+ (month.length() < 2 ? "0" + month : month) + "-"
					+ (day.length() < 2 ? "0" + day : day);
		}
		return date;
	}

	/**
	 * 计算时间差
	 * 
	 * @param startTime
	 *            开始时间
	 * @param i
	 *            0 当天 1 次日
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static String deviationTime(String startTime, int i, String endTime) {
		String[] start = startTime.split(":");
		String[] end = endTime.split(":");
		String hour = "-1";
		String minute = "-1";
		int startTimeHour = Integer.parseInt(start[0]);
		int startTimeMinute = Integer.parseInt(start[1]);
		int endTimeHour = Integer.parseInt(end[0]);
		int endTimeMinute = Integer.parseInt(end[1]);
		if (0 == i) {
			// 当天时差
			if (endTimeHour >= startTimeHour
					&& endTimeMinute >= startTimeMinute) {
				minute = endTimeMinute - startTimeMinute + "";
				hour = endTimeHour - startTimeHour + "";
			} else if (endTimeHour >= startTimeHour
					&& endTimeMinute < startTimeMinute) {
				minute = endTimeMinute + 60 - startTimeMinute + "";
				hour = endTimeHour - 1 - startTimeHour + "";
			}

		} else if (1 == i) {
			// 隔天时差
			if (endTimeMinute >= startTimeMinute) {
				minute = endTimeMinute - startTimeMinute + "";
				hour = endTimeHour + 24 - startTimeHour + "";
			} else if (endTimeMinute < startTimeMinute) {
				minute = endTimeMinute + 60 - startTimeMinute + "";
				hour = endTimeHour + 24 - 1 - startTimeHour + "";
			}
		} else {

		}
		if (hour.startsWith("-")) {
			return "00:00";
		} else {
			return (hour.length() < 2 ? "0" + hour : hour) + ":"
					+ (minute.length() < 2 ? "0" + minute : minute);
		}
	}
}
