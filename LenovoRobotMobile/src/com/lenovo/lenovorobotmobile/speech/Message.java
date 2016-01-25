package com.lenovo.lenovorobotmobile.speech;


import com.google.gson.Gson;


import android.text.TextUtils;

public class Message {
	public String robotid;
	public String intent;
	public String text;
	public String period;
	public String date;
	public String time;
	public String location;
	public String person;
	public String remindText;
	public String duringHour;
	public String duringMinute;
	public String device;
	public String action;
	public String condition;

	public static String scheduleJson;

	public void setScheduleJson(String json) {
		scheduleJson = json;
	}

	/**
	 * 判断语返回的Json是否是有效的
	 * 
	 * @return
	 */
	public boolean isMatching() {
		boolean flag = true;
		if ("NULL".equals(intent) || TextUtils.isEmpty(intent)) {
			flag = false;
		}
		return flag;
	}

	public String getIntent() {
		if ("REMIDER".equalsIgnoreCase(intent) || "TEST".equalsIgnoreCase(intent)) {
			return "0";
		} else if ("IOT".equalsIgnoreCase(intent)) {
			return "1";
		} else {
			return "-1";
		}
	}

	/**
	 * 获取要发送的帧数据
	 * 
	 * @return
	 */
	public String getSendMessage() {

		StringBuffer sendMessage = new StringBuffer();
		if ("MOVE".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.CN_ROBOT_ID).append(",,").append(Constant.MOVE_FRAME_ID)
					.append(",,")
					// 位姿
					.append(location);
		} else if ("GO".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.CN_ROBOT_ID).append(",,").append(Constant.GO_FRAME_ID)
					.append(",,")
					// 位姿
					.append(location);
		} else if ("ELECTRIC".equals(intent) || "DOING".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.STATE_FRAME_ID);
		} else if ("ATHOME".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.ATHOME_FRAME_ID);
		} else if ("REPORT".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.REPORT_FRAME_ID)
					.append(",,")
					// 时间
					.append(date).append(",,")
					// RobotId
					.append(robotid);
		} else if ("REMIDER".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.REMIDER_FRAME_ID)
					.append(",,")
					// JSON
					.append(getJson());
		} else if ("TEST".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.TEST_FRAME_ID)
					.append(",,")
					// JSON
					.append(getJson());
		} else if ("IOT".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.IOT_FRAME_ID).append(",,")
			// JSON
					.append(getJson());
		} else if ("SCHEDULE".equals(intent)) {
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.SCHEDULE_FRAME_ID);
		} else if ("DEL_SCHEDULE".equals(intent)) {

			// TODO start 这里是测试用的数据，模拟拿到从服务器返回的数据
			// String json =
			// "{\"Schedules\":[{\"id\":\"scheduleId001\",\"text\":\"明天早上8点提醒爸爸吃药\"},{\"id\":\"scheduleId002\",\"text\":\"每天早上8点提醒爸爸测血压\"},{\"id\":\"scheduleId003\",\"text\":\"每天晚上7点到次日凌晨8点如果家中无人的话关灯\"},{\"id\":\"scheduleId004\",\"text\":\"每天晚上8点到次日凌晨8点如果家中无人的话关电视\"},{\"id\":\"scheduleId005\",\"text\":\"每天晚上9点到次日凌晨8点如果家中无人的话关空调\"},{\"id\":\"scheduleId006\",\"text\":\"每天晚上10点到次日凌晨8点如果家中无人的话关空气净化器\"},{\"id\":\"scheduleId007\",\"text\":\"每天晚上11点到次日凌晨8点如果家中有人的话开灯\"}]}";
			String json = scheduleJson;
			if (json != null) {
				Gson gson = new Gson();
				Schedule schedule = gson.fromJson(json, Schedule.class);
				// TODO end 这里是测试用的数据，模拟拿到从服务器返回的数据
				sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.DEL_SCHEDULE_FRAME_ID)
						.append(",,")
						// 删除日程的ID
						.append(schedule.getIdByNum(Integer.parseInt(location)));
				
				schedule.deleteSchedule(Integer.parseInt(location) -1);
			} else {
				// 请先显示日程
				
			}
		} else if ("OPEN".equals(intent) || "CLOSE".equals(intent)) {
			// 11，1，29，1，0/1
			sendMessage.append(Constant.CN_FRIEND_ID).append(",,").append(Constant.CN_ROBOT_ID).append(",,").append(Constant.OPEN_CLOSE_FRAME_ID)
					.append(",,")
					// 电器编号
					.append(device).append(",,")
					// 家电开关状态
					.append(action);
		}
		return sendMessage.toString();
	}

	private String getJson() {
		StringBuffer json = new StringBuffer();
		json.append("{").append("\n");
		json.append("\"robotid\":").append(TextUtils.isEmpty(robotid) ? -1 : robotid).append(",").append("\n");
		json.append("\"intent\":").append(TextUtils.isEmpty(getIntent()) ? -1 : getIntent()).append(",").append("\n");
		json.append("\"text\":\"").append(text).append("\"").append(",").append("\n");
		json.append("\"period\":").append(TextUtils.isEmpty(period) ? -1 : period).append(",").append("\n");
		json.append("\"date\":\"").append(date).append("\"").append(",").append("\n");
		json.append("\"time\":\"").append(time).append("\"").append(",").append("\n");
		json.append("\"person\":\"").append(person).append("\"").append(",").append("\n");
		json.append("\"remindText\":\"").append(remindText).append("\"").append(",").append("\n");
		json.append("\"during hour\":").append(TextUtils.isEmpty(duringHour) ? -1 : duringHour).append(",").append("\n");
		// json.append("\"duringMinute\":")
		// .append(TextUtils.isEmpty(duringMinute) ? -1 : duringMinute)
		// .append(",").append("\n");
		json.append("\"device\":\"").append(device).append("\"").append(",").append("\n");
		json.append("\"action\":").append(TextUtils.isEmpty(action) ? -1 : action).append(",").append("\n");
		json.append("\"condition\":").append(TextUtils.isEmpty(condition) ? -1 : condition).append("\n");
		json.append("}");
		return json.toString();
	}
}
