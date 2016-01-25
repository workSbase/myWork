package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;

public class Schedule {
	public ArrayList<Schedules> Schedule;

	public class Schedules {
		public int id;
		public String Text;
	}

	/**
	 * 通过序号获得ScheduleID
	 * 
	 * @param num
	 * @return
	 */
	public int getIdByNum(int num) {
		if (null == Schedule)
			return -1;
		if (0 < num && num <= Schedule.size()) {
			// 获取要删除的Schedule的id号
			return Schedule.get(num - 1).id;
		} else {
			// 超出范围
			return -1;
		}
	}

	public ArrayList<String> getTexts() {
		ArrayList<String> scheduleTextList = new ArrayList<String>();
		for (Schedules s : Schedule) {
			scheduleTextList.add(s.Text);
		}
		return scheduleTextList;
	}

	public void deleteSchedule(int i) {
		if (0 < i && i <= Schedule.size())
			Schedule.remove(i);
	}
}
