package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;

import android.text.TextUtils;

public class TextPaserWitInfo_en {
	public String _text;
	public Entities entities;

	public class Entities {

		// 去xx
		public ArrayList<Local_search_query> local_search_query;

		// 位置
		public class Local_search_query {
			public String type;
			public String value;
			public boolean suggested;
		}

		// 时间
		public ArrayList<DateTime> datetime;

		public class DateTime {
			public String grain;
			public String type;
			public String value;

			public From from;

			public class From {
				public String value;
				public String grain;
			}

			public To to;

			public class To {
				public String value;
				public String grain;
			}
		}

		// 开关
		public ArrayList<On_off> on_off;

		public class On_off {
			public String value;
		}

		// 序数 删除第xx条日程
		public ArrayList<Ordinal> ordinal;

		public class Ordinal {
			public int value;
		}

		// 人物
		public ArrayList<Contact> contact;

		public class Contact {
			public String type;
			public String value;
			public boolean suggested;
		}

		// 提醒
		public ArrayList<Reminder> reminder;

		public class Reminder {
			public String type;
			public String value;
			public boolean suggested;
		}
	}

	public String intent;
	public double confidence;

	/**
	 * 获取Intent
	 * 
	 * @return
	 */
	public String getIntent() {
		if (TextUtils.isEmpty(intent)) {
			return null;
		} else {
			return intent;
		}
	}

	/**
	 * 获取可信度
	 * 
	 * @return
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * 获取Action
	 * 
	 * @return
	 */
	public String getAction() {
		if (!TextUtils.isEmpty(_text)) {
			return _text;
		} else {
			return null;
		}
	}

	/**
	 * 获取Location
	 * 
	 * @return
	 */
	public String getLocation() {
		if (null == entities.local_search_query) {
			return null;
		}
		int local_search_querySize = entities.local_search_query.size();
		if (0 < local_search_querySize) {
			if (TextUtils.isEmpty(entities.local_search_query.get(0).value)) {
				return null;
			} else {
				return entities.local_search_query.get(0).value;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取返回的时间的个数
	 * 
	 * @return
	 */
	public int getDateTimeSize() {
		if (null == entities.datetime)
			return 0;
		return entities.datetime.size();
	}

	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public String getDateTime(int index) {
		if (null == entities.datetime)
			return null;
		int dateTimeSize = entities.datetime.size();
		if (index < dateTimeSize) {
			if (TextUtils.isEmpty(entities.datetime.get(index).value)) {
				return null;
			} else {
				return entities.datetime.get(index).value;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取日期
	 * 
	 * @param index
	 * @return
	 */
	public String getDate(int index) {
		String dateTime = getDateTime(index);
		if (null != dateTime && "hour".equals(entities.datetime.get(index).grain) && 10 == dateTime.indexOf("T")) {
			return dateTime.substring(0, 10);
		}
		return null;
	}

	public String getDate(int index, String type) {
		String dateTime = getDateTime(index);
		if (null != dateTime && type.equals(entities.datetime.get(index).grain) && 10 == dateTime.indexOf("T")) {
			return dateTime.substring(0, 10);
		}
		return null;
	}

	/**
	 * 获取时间
	 * 
	 * @param i
	 * @return
	 */
	public String getTime(int i) {
		String dateTime = getDateTime(i);
		if (null != dateTime && "hour".equals(entities.datetime.get(i).grain) && 10 == dateTime.indexOf("T") && 16 <= dateTime.length()) {
			return dateTime.substring(11, 16);
		}
		return null;
	}

	/**
	 * 获取开始时间
	 * 
	 * @return
	 */
	public String getStartDateTime() {
		if (null == entities.datetime)
			return null;
		if (null == entities.datetime.get(0).from)
			return null;
		String startDate = entities.datetime.get(0).from.value;
		return startDate;
	}

	/**
	 * 获取开始日期
	 * 
	 * @return
	 */
	public String getStartDate() {
		String startDateTime = getStartDateTime();
		if (null != startDateTime && 10 == startDateTime.indexOf("T")) {
			return startDateTime.substring(0, 10);
		}
		return null;
	}

	/**
	 * 获取开始时间（具体几点）
	 * 
	 * @return
	 */
	public String getStartTime() {
		String startDateTime = getStartDateTime();
		if (null != startDateTime && 10 == startDateTime.indexOf("T") && 16 <= startDateTime.length()) {
			return startDateTime.substring(11, 16);
		}
		return null;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return
	 */
	public String getEndDateTime() {
		if (null == entities.datetime)
			return null;
		if (null == entities.datetime.get(0).to)
			return null;
		String endDate = entities.datetime.get(0).to.value;
		return endDate;
	}

	/**
	 * 获取结束日期
	 * 
	 * @return
	 */
	public String getEndDate() {
		String endDateTime = getEndDateTime();
		if (null != endDateTime && 10 == endDateTime.indexOf("T")) {
			return endDateTime.substring(0, 10);
		}
		return null;
	}

	/**
	 * 获取结束时间（具体几点）
	 * 
	 * @return
	 */
	public String getEndTime() {
		String endDateTime = getEndDateTime();
		if (null != endDateTime && 10 == endDateTime.indexOf("T") && 16 <= endDateTime.length()) {
			return endDateTime.substring(11, 16);
		}
		return null;
	}

	/**
	 * 获取人名
	 * 
	 * @param i
	 * @return
	 */
	public String getPerson(int index) {
		if (null == entities.contact)
			return null;
		int contactSize = entities.contact.size();
		if (index < contactSize) {
			if (TextUtils.isEmpty(entities.contact.get(index).value)) {
				return null;
			} else {
				return entities.contact.get(index).value;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取开关状态
	 * 
	 * @return
	 */
	public int getOnOff() {
		if (null == entities.on_off) {
			return -1;
		}
		int on_offSize = entities.on_off.size();
		if (0 < on_offSize) {
			if (TextUtils.isEmpty(entities.on_off.get(0).value)) {
				return -1;
			} else {
				String state = entities.on_off.get(0).value;
				if ("on".equalsIgnoreCase(state)) {
					return 1;
				} else if ("off".equalsIgnoreCase(state)) {
					return 0;
				}
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * 获取序数
	 * 
	 * @return
	 */
	public int getOrdinal(int index) {
		if (null == entities.ordinal) {
			return -1;
		}
		int ordinalSize = entities.ordinal.size();
		if (ordinalSize > index) {
			int ordina = entities.ordinal.get(index).value;
			return ordina;
		}
		return -1;
	}

	/**
	 * 获取提醒内容
	 * 
	 * @param i
	 * @return
	 */
	public String getReminder(int i) {
		if (null == entities.reminder) {
			return null;
		}
		int reminderSize = entities.reminder.size();
		if (i < reminderSize) {
			if (TextUtils.isEmpty(entities.reminder.get(i).value)) {
				return null;
			} else {
				return entities.reminder.get(i).value;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取设备名称
	 * 
	 * @return
	 */
	public String getDevice() {
		String[] items = _text.trim().split(" ");
		if (null != items && 3 < items.length) {
			return items[2];
		}
		return null;
	}

	/**
	 * 获取家中有没有人
	 * 
	 * @return
	 */
	public int getCondition() {
		if (_text.contains("nobody") || _text.contains("no people") || _text.contains("nopeople")) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * 获取持续时间
	 * 
	 * @return
	 */
	public int getDuringHour() {
		// 获取开始日期
		String startDate = getStartDate();
		// 获取结束日期
		String endtDate = getEndDate();
		// 获取开始时间
		String startTime = getStartTime();
		// 获取结束时间
		String endTime = getEndTime();
		// 截取出开始的整点数
		String[] startHour = startTime.split(":");
		// 截取出结束的整点数
		String[] endHour = endTime.split(":");
		int duringHour = 0;
		if (startDate.equals(endtDate)) {
			// 同一天
			duringHour = Integer.parseInt(endHour[0]) - Integer.parseInt(startHour[0]);
		} else {
			// TODO 目前只考虑相隔一天
			duringHour = Integer.parseInt(endHour[0]) - Integer.parseInt(startHour[0]) + 24;
		}

		return duringHour > 0 ? duringHour : 0;
	}
}
