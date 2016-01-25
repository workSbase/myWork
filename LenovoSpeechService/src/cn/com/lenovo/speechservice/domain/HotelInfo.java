package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

/**
 * 保存返回的酒店类型的数据
 * @author kongqw
 *
 */
public class HotelInfo {
	// 状态码
	public int code;
	// 文字内容
	public String text;
	// 酒店信息
	public ArrayList<Hotel> list;

	/**
	 * 酒店信息
	 * @author kongqw
	 *
	 */
	public class Hotel {
		// 酒店名称
		public String name;
		// 价格
		public String price;
		// 满意度
		public String satisfaction;
		// 数量
		public String count;
		// 详情地址
		public String detailurl;
		// 图标地址
		public String icon;
	}
}
