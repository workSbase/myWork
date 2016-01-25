package com.lenovo.lenovorobotmobile.speech;

import android.text.TextUtils;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intent
 * 
 * @author kongqw
 */
public class Intent {

	public Intent() {
	}

	// Intent
	public String intent;

	// 可信度
	public double confidence;

	// 关键词
	public ArrayList<KeyWord> keyWord;

	public class KeyWord {
		public String patternKey;
		public ArrayList<PatternValue> patternValue;

		public class PatternValue {
			public String key;
			public String value;
		}
	}

	// Pattern
	public ArrayList<String> pattern;

	/**
	 * 获取可信度
	 * 
	 * @return
	 */
	public double getConfidence() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 生成Json
	 * 
	 * @param text
	 * @return
	 */
	public String makeJson(String text) {
		ArrayList<String> patterns = getPattern();
		String json = makeJson(null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
		// 遍历pattern，看是否满足
		for (int index = 0; index < patterns.size(); index++) {

			Pattern pattern = Pattern.compile(patterns.get(index));
			Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				// 语法匹配
				if ("MOVE".equals(intent)) {
					// 移动 前进、后退...
					ArrayList<String> items = getItems(text);
					String direction = items.get(0);
					// 获取方向
					String directionCode = getValueByKey(direction);
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, directionCode, null, null, null, null,
							null, null, null);
				} else if ("GO".equals(intent)) {
					// 去xxx 来xxx
					ArrayList<String> items = getItems(text);
					String location = items.get(1);
					// 获取坐标
					String coord = getValueByKey(location);
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, coord, null, null, null, null, null,
							null, null);
				} else if ("ELECTRIC".equals(intent)) {
					// 电量
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, null, null, null, null, null, null,
							null, null);
				} else if ("LOCATION".equals(intent)) {
					// 位置
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, null, null, null, null, null, null,
							null, null);
				} else if ("DOING".equals(intent)) {
					// 当前任务
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, null, null, null, null, null, null,
							null, null);
				} else if ("CLOSE".equals(intent)) {
					// 家电开关
					json = makeJson(
							Constant.CN_ROBOT_ID,
							intent,
							text,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							(2 == getItems(text).size()) ? getValueByKey(getItems(
									text).get(1))
									: "没有设备", "0", null);
				} else if ("OPEN".equals(intent)) {
					// 家电开关
					json = makeJson(
							Constant.CN_ROBOT_ID,
							intent,
							text,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							(2 == getItems(text).size()) ? getValueByKey(getItems(
									text).get(1))
									: "没有设备", "1", null);
				} else if ("ATHOME".equals(intent)) {
					// 家中人
					if (text.endsWith("在家吗") || text.endsWith("在家么")
							|| text.endsWith("在家没") || text.endsWith("在家不")
							|| text.endsWith("在家嘛")) {
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								null, null, null, null,
								text.substring(0, text.length() - 3), null,
								null, null, null, null, null);
					} else {
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								null, null, null, null, null, null, null, null,
								null, null, null);
					}
				} else if ("REPORT".equals(intent)) {
					// 报告
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							TimeUtils.getDateFromTextStart(text), null, null,
							null, null, null, null, null, null, null);
				} else if ("REMIDER".equals(intent)) {
					// 每天|早上|[时间]|提醒|爸爸|吃饭
					ArrayList<String> items = getItems(text);
					String period = TimeUtils.getPeriod(items.get(0));
					String date = TimeUtils.getDate(items.get(0));
					String time = "00:00";
					if (Pattern.compile("^(\\d{1,2}:\\d{1,2})$")
							.matcher(items.get(2)).matches()) {
						time = TimeUtils.formatTime(items.get(1), items.get(2));
					} else if (Pattern
							.compile(
									"^((一|二|两|三|四|五|六|七|八|九|十|零|1|2|2|3|4|5|6|7|8|9|10|0|11|12){1,3}(点半|点整|点钟|点))$")
							.matcher(items.get(2)).matches()) {
						time = TimeUtils.formatTime(items.get(1),
								TimeUtils.stringTime2IntTime(items.get(2)));
					}
					String person = items.get(4);
					String remideText = items.get(5);
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, period,
							date, time, null, person, remideText, null, null,
							null, null, null);
				} else if ("IOT".equals(intent)) {
					// 每天|上午|[时间]|到|次日|上午|[时间]|如果|家中无人|关|灯
					ArrayList<String> items = getItems(text);
					String period = TimeUtils.getPeriod(items.get(0));
					String date = TimeUtils.getDate(items.get(0));
					String startTime = "00:00";
					String endTime = "00:00";
					if (Pattern.compile("^(\\d{1,2}:\\d{1,2})$")
							.matcher(items.get(2)).matches()) {
						startTime = TimeUtils.formatTime(items.get(1),
								items.get(2));
					} else if (Pattern
							.compile(
									"^((一|二|两|三|四|五|六|七|八|九|十|零){1,3}(点半|点整|点钟|点))$")
							.matcher(items.get(2)).matches()) {
						startTime = TimeUtils.formatTime(items.get(1),
								TimeUtils.stringTime2IntTime(items.get(2)));
					}
					String condition = getCondition(items.get(8));
					String action = getAction(items.get(9));
					String device = items.get(10);
					if (Pattern.compile("^(\\d{1,2}:\\d{1,2})$")
							.matcher(items.get(6)).matches()) {
						endTime = TimeUtils.formatTime((TextUtils.isEmpty(items
								.get(5)) ? items.get(1) : items.get(5)), items
								.get(6));
					} else if (Pattern
							.compile(
									"^((一|二|两|三|四|五|六|七|八|九|十|零){1,3}(点半|点整|点钟|点))$")
							.matcher(items.get(6)).matches()) {
						endTime = TimeUtils.formatTime((TextUtils.isEmpty(items
								.get(5)) ? items.get(1) : items.get(5)),
								TimeUtils.stringTime2IntTime(items.get(6)));
					}

					if (TextUtils.isEmpty(items.get(4))) {
						// 当天
						String[] duringTime = TimeUtils.deviationTime(
								startTime, 0, endTime).split(":");
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								period, date, startTime, null, null, null,
								duringTime[0], duringTime[1], device, action,
								condition);
					} else {
						// 次日/第二天
						String[] duringTime = TimeUtils.deviationTime(
								startTime, 1, endTime).split(":");
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								period, date, startTime, null, null, null,
								duringTime[0], duringTime[1], device, action,
								condition);
					}

				} else if ("TEST".equals(intent)) {
					ArrayList<String> items = getItems(text);
					String period = TimeUtils.getPeriod(items.get(0));
					String date = TimeUtils.getDate(items.get(0));
					// 每天|下午|5：25|给|爷爷|测血压
					String time = "00:00";
					if (Pattern.compile("^(\\d{1,2}:\\d{1,2})$")
							.matcher(items.get(2)).matches()) {
						time = TimeUtils.formatTime(items.get(1), items.get(2));
					} else if (Pattern
							.compile(
									"^((一|二|两|三|四|五|六|七|八|九|十|零){1,3}(点半|点整|点钟|点))$")
							.matcher(items.get(2)).matches()) {
						time = TimeUtils.formatTime(items.get(1),
								TimeUtils.stringTime2IntTime(items.get(2)));
					}
					String person = items.get(4);
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, period,
							date, time, null, person, null, null, null, null,
							null, null);
				} else if ("SCHEDULE".equals(intent)) {
					json = makeJson(Constant.CN_ROBOT_ID, intent, text, null,
							null, null, null, null, null, null, null, null,
							null, null);
				} else if ("DEL_SCHEDULE".equals(intent)) {
					ArrayList<String> items = getItems(text);
					String page = items.get(2);

					if (Pattern.compile("^(\\d{1,5})$").matcher(page).matches()) {
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								null, null, null, page, null, null, null, null,
								null, null, null);
					} else if (Pattern
							.compile(
									"^((零|一|二|两|三|四|五|六|七|八|九|十|百|千|万){1,10})$")
							.matcher(page).matches()) {
						json = makeJson(Constant.CN_ROBOT_ID, intent, text,
								null, null, null, strNum2intNum(page), null,
								null, null, null, null, null, null);
					}
				}
				break;
			} else {
				// 语法不匹配

			}
		}
		return json;
	}

	/**
	 * 解析家中有没有人
	 * 
	 * @param string
	 * @return
	 */
	private String getCondition(String string) {
		String condition = "0";
		if (string.contains("没人") || string.contains("没有人")) {
			condition = "0";
		} else {
			condition = "1";
		}
		return condition;
	}

	/**
	 * 解析开关动作
	 * 
	 * @param string
	 * @return
	 */
	private String getAction(String string) {
		String action = "0";
		if (string.contains("关")) {
			action = "0";
		} else if (string.contains("开")) {
			action = "1";
		}
		return action;
	}

	/**
	 * @param robotid
	 *            机器人ID
	 * @param intent
	 *            意图
	 * @param text
	 *            说的话
	 * @param period
	 *            周期
	 * @param date
	 *            日期
	 * @param time
	 *            时间
	 * @param location
	 *            地点
	 * @param person
	 *            人物
	 * @param remindText
	 *            提醒内容
	 * @param duringHour
	 *            持续时间
	 * @param duringMinute
	 *            持续时间
	 * @param device
	 *            设备名称
	 * @param action
	 *            设备状态(开/关)
	 * @param condition
	 *            家中有没有人
	 * @return
	 */
	public String makeJson(String robotid, String intent, String text,
			String period, String date, String time, String location,
			String person, String remindText, String duringHour,
			String duringMinute, String device, String action, String condition) {
		StringBuffer json = new StringBuffer();
		json.append("{").append("\n");
		// 机器人ID
		json.append("\"robotid\": \"").append(null == robotid ? "" : robotid)
				.append("\",").append("\n");
		// Intent
		json.append("\"intent\": \"").append(null == intent ? "" : intent)
				.append("\",").append("\n");
		// Text
		json.append("\"text\": \"").append(null == text ? "" : text)
				.append("\",").append("\n");
		// 周期
		json.append("\"period\":\"").append(null == period ? "" : period)
				.append("\",").append("\n");
		// 日期
		json.append("\"date\":\"").append(null == date ? "" : date)
				.append("\",").append("\n");
		// 时间
		json.append("\"time\":\"").append(null == time ? "" : time)
				.append("\",").append("\n");
		// 地点
		json.append("\"location\":\"").append(null == location ? "" : location)
				.append("\",").append("\n");
		// 人物
		json.append("\"person\":\"").append(null == person ? "" : person)
				.append("\",").append("\n");
		// 提醒内容
		json.append("\"remindText\":\"")
				.append(null == remindText ? "" : remindText).append("\",")
				.append("\n");
		// 持续时间_小时
		json.append("\"duringHour\":\"")
				.append(null == duringHour ? "" : duringHour).append("\",")
				.append("\n");
		// 持续时间_分钟
		json.append("\"duringMinute\":\"")
				.append(null == duringMinute ? "" : duringMinute).append("\",")
				.append("\n");
		// 设备
		json.append("\"device\":\"").append(null == device ? "" : device)
				.append("\",").append("\n");
		// 设备状态
		json.append("\"action\":\"").append(null == action ? "" : action)
				.append("\",").append("\n");
		// 家中有没有人
		json.append("\"condition\":\"")
				.append(null == condition ? "" : condition).append("\"")
				.append("\n");
		json.append("}");
		return json.toString();
	}

	/**
	 * 获取正则表达式
	 * 
	 * @return
	 */
	private ArrayList<String> getPattern() {
		// 用来保存正则表达式
		ArrayList<String> patterns = new ArrayList<String>();
		// 遍历所有Pattern()
		if (null != pattern) {
			for (int index = 0; index < pattern.size(); index++) {
				// 用于拼接正则表达式
				StringBuffer buffer = new StringBuffer();
				buffer.append("^");
				// 对pattern使用&进行分割
				String[] patternColumn = pattern.get(index).split("&");
				// 遍历Pattern每个字段 获取字段值 拼接正则表达式
				for (int index1 = 0; index1 < patternColumn.length; index1++) {
					buffer.append("(");

					ArrayList<String> patternValues = getPatternValue(patternColumn[index1]);
					for (int index2 = 0; index2 < patternValues.size(); index2++) {
						buffer.append("(").append(patternValues.get(index2))
								.append(")");
						if (index2 < patternValues.size() - 1) {
							buffer.append("|");
						}
					}

					buffer.append(")");
				}
				buffer.append("$");
				patterns.add(buffer.toString());
			}
		}

		return patterns;
	}

	/**
	 * 通过PatternKey获取PatternValue
	 * 
	 * @param PatternKey
	 * @return
	 */
	private ArrayList<String> getPatternValue(String PatternKey) {
		ArrayList<String> patternValues = new ArrayList<String>();

		for (int index = 0; index < keyWord.size(); index++) {
			if (PatternKey.equals(keyWord.get(index).patternKey)) {
				for (int x = 0; x < keyWord.get(index).patternValue.size(); x++) {
					patternValues
							.add(keyWord.get(index).patternValue.get(x).key);
				}
			}
		}

		return patternValues;
	}

	/**
	 * 将指令通过设定的pattern进行分割
	 * 
	 * @param
	 * @param text
	 * @return
	 */
	private ArrayList<String> getItems(String text) {
		ArrayList<String> items = new ArrayList<String>();
		// 遍历所有的pattern
		if (null != pattern) {
			A: for (int index = 0; index < pattern.size(); index++) {
				String textTemp = text;
				items.clear();
				// 对pattern使用&进行分割
				String[] patternColumn = pattern.get(index).split("&");
				// 遍历Pattern每个字段 获取字段值 拼接正则表达式
				B: for (int index1 = 0; index1 < patternColumn.length; index1++) {
					// A B C D……
					ArrayList<String> patternValues = getPatternValue(patternColumn[index1]);
					C: for (int index2 = 0; index2 < patternValues.size(); index2++) {

						Pattern pat = Pattern
								.compile(patternValues.get(index2));
						Matcher mat = pat.matcher(textTemp);

						while (mat.find()) {
							String group = mat.group();
							if (!TextUtils.isEmpty(pat.toString())
									&& !textTemp.startsWith(group)) {
								if (1 == patternValues.size()) {
									continue A;
								} else {
									continue C;
								}
							}
							if (!textTemp.startsWith(group)) {
								if (TextUtils.isEmpty(pat.toString())) {
									items.add("");
									continue B;
								}
								continue A;
							}
							textTemp = textTemp.replaceFirst(group, "");

							items.add(group);
							Log.d("------group", group);
							if (TextUtils.isEmpty(textTemp)) {
								break A;
							}
							continue B;
						}
					}
				}
			}
		}
		return items;
	}

	/**
	 * 通过key值获取value
	 * 
	 * @param key
	 * @return
	 */
	private String getValueByKey(String key) {
		if (null != keyWord) {
			for (int indexKeyWord = 0; indexKeyWord < keyWord.size(); indexKeyWord++) {
				KeyWord keyWord2 = keyWord.get(indexKeyWord);
				for (int x = 0; x < keyWord2.patternValue.size(); x++) {
					if (keyWord2.patternValue.get(x).key.contains(key)) {
						String value = keyWord2.patternValue.get(x).value;
						return value;
					}
				}
			}

		}
		return null;
	}

	/**
	 * 将汉字类型的数据转换为数字类型的数据（目前至支持一百以内） 十八 -> 18
	 * 
	 * @param
	 * @return
	 */
	private String strNum2intNum(String strNum) {
		StringBuffer intNum = new StringBuffer();
		// 一位
		if (1 == strNum.length()) {
			intNum.append(StringUtils.strNum2IntNum(strNum));
		} else if (2 == strNum.length() && strNum.startsWith("十")) {
			intNum.append("1").append(
					StringUtils.strNum2IntNum(strNum.substring(1)));
		} else if (2 == strNum.length() && strNum.endsWith("十")) {
			intNum.append(StringUtils.strNum2IntNum(strNum.substring(0, 1)))
					.append("0");
		} else if (3 == strNum.length() && "十".equals(strNum.substring(1, 2))) {
			intNum.append(StringUtils.strNum2IntNum(strNum.substring(0, 1)))
					.append(StringUtils.strNum2IntNum(strNum.substring(2, 3)));
		}
		return intNum.toString();
	}

}
