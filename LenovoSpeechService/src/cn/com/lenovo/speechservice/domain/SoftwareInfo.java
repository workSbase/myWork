package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

/**
 * 保存返回的软件下载类型的数据
 * @author kongqw
 *
 */
public class SoftwareInfo {
	// 状态码
	public int code;
	// 文字内容
	public String text;
	// 软件信息
	public ArrayList<Software> list;

	/**
	 * 软件信息
	 * @author kongqw
	 *
	 */
	public class Software {
		// 软件名称
		public String name;
		// 软件下载量
		public String count;
		// 软件详情
		public String detailurl;
		// 图标地址
		public String icon;
	}
}
