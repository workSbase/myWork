package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

import android.text.TextUtils;

public class WitInfo {
	public String _text;
	public Entities entities;

	public class Entities {
		// 打电话给xxx
		public ArrayList<Contact> contact;

		public class Contact {
			public String value;
		}

		// 去xx
		public ArrayList<Local_search_query> local_search_query;

		public class Local_search_query {
			public String value;
		}

		// 时间
		public ArrayList<DateTime> datetime;

		public class DateTime {
			public String grain;
			public String type;
			public String value;
		}

		// 开关
		public ArrayList<On_off> on_off;

		public class On_off {
			public String value;
		}
	}

	public String intent;
	public double confidence;

	/**
	 * 获取Intent
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
	 * 获取拨打电话的姓名
	 * @return
	 */
	public String getName() {
		if (null == entities.contact) {
			return null;
		}
		int contactSize = entities.contact.size();
		if (0 < contactSize) {
			if (TextUtils.isEmpty(entities.contact.get(0).value)) {
				return null;
			} else {
				return entities.contact.get(0).value;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取可信度
	 * @return
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * 获取Action
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
	 * 获取时间
	 * @return
	 */
	public String getDateTime(int index) {
		if (null == entities.datetime) {
			return null;
		}
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
	 * 获取开关状态
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
	 * 获取设备名称
	 * @return
	 */
	public String getDevice() {
		if (_text.contains("turn on")) {
			return _text.substring(_text.indexOf("turn on") + "turn on".length()).trim();
		} else if (_text.contains("turn off")) {
			return _text.substring(_text.indexOf("turn off") + "turn off".length()).trim();
		} else {
			return null;
		}
	}
}
