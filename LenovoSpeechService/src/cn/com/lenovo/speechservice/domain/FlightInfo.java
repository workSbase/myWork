package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

/**
 * 保存返回的航班类型的数据
 * @author kongqw
 *
 */
public class FlightInfo {
	// 状态码
	public int code;
	// 文字内容
	public String text;
	// 软件信息
	public ArrayList<Flight> list;

	/**
	 * 航班信息
	 * @author kongqw
	 *
	 */
	public class Flight {
		// 航班
		public String flight;
		// 航班路线
		public String route;
		// 起飞时间
		public String starttime;
		// 到达时间
		public String endtime;
		// 航班状态
		public String state;
		// 详情地址
		public String detailurl;
		// 图标地址
		public String icon;
	}
}
