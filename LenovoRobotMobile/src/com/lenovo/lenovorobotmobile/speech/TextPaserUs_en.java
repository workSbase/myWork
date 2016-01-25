package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lenovo.lenovorobotmobile.activity.ChatsActivity;
import com.lenovo.lenovorobotmobile.bin.Command;
import com.lenovo.lenovorobotmobile.bin.Furniture;
import com.lenovo.lenovorobotmobile.bin.Location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public abstract class TextPaserUs_en {

	private Context mContext;
	private Gson gson;

	public abstract void result(String fram, String error);

	public TextPaserUs_en(Context context) {
		mContext = context;
		gson = new Gson();

	}

	/**
	 * 获取发送的数据
	 * 
	 * @param command
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public void getSendMessage(String command) {
		// 将指令转换成小写
		// command = command.toLowerCase();
		// 获取发送的帧
		String fram = makeFrameByLocal(command.toLowerCase());
		if (null != fram) {
			result(fram, null);
			Toast.makeText(mContext, "本地解析完成 fram = " + fram,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 借助wit识别
		new MyWit_en(mContext) {

			@Override
			public void getReJson(String error, String json) {
				// Toast.makeText(mContext, "wit解析完成  json = " + json,
				// Toast.LENGTH_SHORT).show();
				if (null != error) {
					result(null, error);
					Toast.makeText(mContext, "wit解析完成  error = " + error,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (null != json) {
					Toast.makeText(mContext, "wit解析完成  json = " + json,
							Toast.LENGTH_SHORT).show();
					// 生成Frame
					makeFrameByCloud(json);
				}
			}
		}.getMessage(command);
	}

	/**
	 * 生成Frame
	 * 
	 * @param json
	 * @return
	 */
	public void makeFrameByCloud(String json) {
		TextPaserWitInfo_en witInfo = gson.fromJson(json,
				TextPaserWitInfo_en.class);
		StringBuffer frame = new StringBuffer();
		String intent = witInfo.getIntent();
		if ("go".equals(intent)) {
			// 获取位置
			String location = getLocation(witInfo.getLocation());
			frame.append(Constant.US_FRIEND_ID).append(",,")
					.append(Constant.US_ROBOT_ID).append(",,")
					.append(Constant.GO_FRAME_ID).append(",,")
					// 位置
					.append(location).append(",,")
					// 角度
					.append("0");
		} else if ("on_off".equals(intent)) {
			// 获取开关状态
			int action = witInfo.getOnOff();
			if (-1 == action) {
				result(null, "开关状态不明确");
				return;
			}
			// 获取电器编号
			String furnitureId = getFurnitureId(witInfo.getAction());
			if (null == furnitureId) {
				result(null, "电器没有注册");
				return;
			}
			frame.append(Constant.US_FRIEND_ID).append(",,")
					.append(Constant.US_ROBOT_ID).append(",,")
					.append(Constant.OPEN_CLOSE_FRAME_ID).append(",,")
					// 电器编号
					.append(furnitureId).append(",,")
					// 开关状态
					.append(action);
		} else if ("show_reminder".equals(intent)
				&& 0.5 < witInfo.getConfidence()) {
			frame.append(Constant.US_FRIEND_ID).append(",,")
					.append(Constant.SERVER_ID).append(",,")
					.append(Constant.SCHEDULE_FRAME_ID);
		} else if ("del_reminder".equals(intent)
				&& 0.5 < witInfo.getConfidence()) {
			// frame.append(Constant.US_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.DEL_SCHEDULE_FRAME_ID)
			// .append(",,")
			// // 删除第几条
			// .append(witInfo.getOrdinal(0));
			Schedule schedule = ChatsActivity.schedule;
			if (schedule != null) {
				frame.append(Constant.US_FRIEND_ID).append(",,")
						.append(Constant.SERVER_ID).append(",,")
						.append(Constant.DEL_SCHEDULE_FRAME_ID).append(",,")
						// 删除第几条
						.append(schedule.getIdByNum(witInfo.getOrdinal(0)));
			}
		} else if ("report".equals(intent) && 0.5 < witInfo.getConfidence()) {
			// 获取日期
			String date = witInfo.getDate(0, "day");
			if (null == date) {
				result(null, "时间格式不正确");
				return;
			}
			frame.append(Constant.US_FRIEND_ID).append(",,")
					.append(Constant.SERVER_ID).append(",,")
					.append(Constant.REPORT_FRAME_ID).append(",,")
					// 获取日期
					.append(date).append(",,")
					// 机器人ID
					.append(Constant.US_ROBOT_ID);
		} else if ("test".equals(intent) && 0.4 < witInfo.getConfidence()) {
			int robotid = Integer.parseInt(Constant.US_ROBOT_ID);
			intent = "0";
			String text = witInfo.getAction();
			String date = null;
			String time = null;
			String person = null;
			int period = getPeriod(text);
			if (1 == witInfo.getDateTimeSize()) {
				date = witInfo.getDate(0);
				time = witInfo.getTime(0);
				person = witInfo.getPerson(0);
			} else if (2 == witInfo.getDateTimeSize()) {
				/*
				 * wit解析出了两个时间 1.Command格式不对 2.将mother和father解析成了母亲节和父亲节的时间
				 */
				for (int x = 0; x < 2; x++) {
					if (null != witInfo.getDate(x)) {
						date = witInfo.getDate(x);
					}
					if (null != witInfo.getTime(x)) {
						time = witInfo.getTime(x);
					}
					if (witInfo.getAction().contains("mother")) {
						person = "mother";
					} else if (witInfo.getAction().contains("father")) {
						person = "father";
					}
				}
			}
			if (null == date || null == time) {
				result(null, "时间格式不正确");
				return;
			}
			if (null == person) {
				result(null, "没有获取到person信息");
				return;
			}
			frame.append(Constant.US_FRIEND_ID)
					.append(",,")
					.append(Constant.SERVER_ID)
					.append(",,")
					.append(Constant.TEST_FRAME_ID)
					.append(",,")
					// Json
					.append(makeJson(robotid, Integer.parseInt(intent), text,
							period, date, time, person, "", -1, "", -1, -1));
		} else if ("reminder".equals(intent) && 0.4 < witInfo.getConfidence()) {
			int robotid = Integer.parseInt(Constant.US_ROBOT_ID);
			intent = "0";
			String text = witInfo.getAction();
			String date = null;
			String time = null;
			String person = null;
			int period = getPeriod(text);
			String remindText = null;
			if (1 == witInfo.getDateTimeSize()) {
				date = witInfo.getDate(0);
				time = witInfo.getTime(0);
				person = witInfo.getPerson(0);
			} else if (2 == witInfo.getDateTimeSize()) {
				/*
				 * wit解析出了两个时间 1.Command格式不对 2.将mother和father解析成了母亲节和父亲节的时间
				 */
				for (int x = 0; x < 2; x++) {
					if (null != witInfo.getDate(x)) {
						date = witInfo.getDate(x);
					}
					if (null != witInfo.getTime(x)) {
						time = witInfo.getTime(x);
					}
					if (witInfo.getAction().contains("mother")) {
						person = "mother";
					} else if (witInfo.getAction().contains("father")) {
						person = "father";
					}
				}
			}
			remindText = witInfo.getReminder(0);
			if (null == date || null == time) {
				result(null, "时间格式不正确");
				return;
			}
			if (null == person) {
				result(null, "没有获取到person信息");
				return;
			}
			if (null == remindText) {
				result(null, "没有获取到提醒信息");
				return;
			}
			frame.append(Constant.US_FRIEND_ID)
					.append(",,")
					.append(Constant.SERVER_ID)
					.append(",,")
					.append(Constant.REMIDER_FRAME_ID)
					.append(",,")
					// Json
					.append(makeJson(robotid, Integer.parseInt(intent), text,
							period, date, time, person, remindText, -1, "", -1,
							-1));
		} else if ("iot".equals(intent) && 0.4 < witInfo.getConfidence()) {
			int robotid = Integer.parseInt(Constant.US_ROBOT_ID);
			intent = "1";
			String text = witInfo.getAction();
			String date = witInfo.getStartDate();
			String time = witInfo.getStartTime();
			String person = "";
			int period = getPeriod(text);
			String remindText = "";
			int duringHour = witInfo.getDuringHour();
			String device = witInfo.getDevice();
			int action = witInfo.getOnOff();
			int condition = witInfo.getCondition();
			frame.append(Constant.US_FRIEND_ID)
					.append(",,")
					.append(Constant.SERVER_ID)
					.append(",,")
					.append(Constant.IOT_FRAME_ID)
					.append(",,")
					// Json
					.append(makeJson(robotid, Integer.parseInt(intent), text,
							period, date, time, person, remindText, duringHour,
							device, action, condition));
		} else {
			// frame.append(json);
			result(null, "command error");
		}
		Toast.makeText(mContext, "wit解析完成 frame = " + frame, Toast.LENGTH_SHORT)
				.show();
		result(frame.toString(), null);
	}

	public String makeJson(int robotid, int intent, String text, int period,
			String date, String time, String person, String remindText,
			int duringHour, String device, int action, int condition) {
		StringBuffer json = new StringBuffer();
		json.append("{").append("\n");
		json.append("\"robotid\":").append(robotid).append(",").append("\n");
		json.append("\"intent\":").append(intent).append(",").append("\n");
		json.append("\"text\":\"").append(text).append("\"").append(",")
				.append("\n");
		json.append("\"period\":").append(period).append(",").append("\n");
		json.append("\"date\":\"").append(date).append("\"").append(",")
				.append("\n");
		json.append("\"time\":\"").append(time).append("\"").append(",")
				.append("\n");
		json.append("\"person\":\"").append(person).append("\"").append(",")
				.append("\n");
		json.append("\"remindText\":\"").append(remindText).append("\"")
				.append(",").append("\n");
		json.append("\"during hour\":").append(duringHour).append(",")
				.append("\n");
		// json.append("\"duringMinute\":").append(TextUtils.isEmpty(duringMinute)
		// ? -1 : duringMinute).append(",").append("\n");
		json.append("\"device\":\"").append(device).append("\"").append(",")
				.append("\n");
		json.append("\"action\":").append(action).append(",").append("\n");
		json.append("\"condition\":").append(condition).append("\n");
		json.append("}");
		return json.toString();
	}

	/**
	 * 获取周期
	 * 
	 * @param text
	 * @return
	 */
	private int getPeriod(String text) {
		if (text.contains("yesterday") || text.contains("today")
				|| text.contains("tomorrow")) {
			// 周期一次
			return 0;
		} else if (text.contains("everyday") || text.contains("every day")) {
			// 周期每天
			return 1;
		} else if (text.contains("every monday")
				|| text.contains("every tuesday")
				|| text.contains("every wednesday")
				|| text.contains("every thursday")
				|| text.contains("every friday")
				|| text.contains("every saturday")
				|| text.contains("every sunday")) {
			// 周期每周
			return 2;
		} else if (text.contains("every") && text.contains("month")) {
			// 周期每月
			return 3;
		} else if (text.contains("every") && text.contains("year")) {
			// 周期每年
			return 4;
		} else {
			return 0;
		}
	}

	/**
	 * 生成Frame
	 * 
	 * @param json
	 * @return
	 */
	public String makeFrameByLocal(String command) {

		// 获取Intent
		ArrayList<String> intents = getIntents();
		// 遍历Intent
		for (int index = 0; index < intents.size(); index++) {
			String json = FucUtil.readFile(mContext, intents.get(index),
					"utf-8");
			Command com = gson.fromJson(json, Command.class);
			String frame = com.getFram(command);
			if (!TextUtils.isEmpty(frame)) {
				return frame;
			}
		}
		return null;
	}

	/**
	 * 获取位置坐标
	 * 
	 * @param location
	 * @return
	 */
	public String getLocation(String location) {
		String json = FucUtil.readFile(mContext, "LOCATION_US", "utf-8");
		Location lo = gson.fromJson(json, Location.class);
		String coord = lo.getLocation(location);
		return coord;
	}

	/**
	 * 获取家电编号
	 * 
	 * @param action
	 * @return
	 */
	private String getFurnitureId(String text) {
		String furnitureName = null;
		if (text.startsWith("turn on")) {
			furnitureName = text.substring(7).trim();
		} else if (text.startsWith("turn off")) {
			furnitureName = text.substring(8).trim();
		}
		if (null != furnitureName) {
			String json = FucUtil.readFile(mContext, "FURNITUREID_US", "utf-8");
			Furniture furniture = gson.fromJson(json, Furniture.class);
			String furnitureId = furniture.getFurnitureId(furnitureName);
			return furnitureId;
		} else {
			return null;
		}
	}

	/**
	 * 获取Intent
	 * 
	 * @return
	 */
	public ArrayList<String> getIntents() {
		// TODO 获取Intent 暂时使用假数据
		ArrayList<String> intents = new ArrayList<String>();
		intents.add("MOVE_US");
		intents.add("ELECTRIC_US");
		intents.add("DOING_US");
		intents.add("ATHOME_US");
		intents.add("SCHEDULE_US");
		return intents;
	}
}
