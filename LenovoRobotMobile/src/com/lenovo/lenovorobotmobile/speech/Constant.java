package com.lenovo.lenovorobotmobile.speech;

/**
 * 管理程序中用到的常量
 * 
 * @author kongqw
 * 
 */
public class Constant {

	/**
	 * 中英文切换 中文识别
	 */
	public static final String LANGUAGE_CN = "zh_cn";
	/**
	 * 中英文切换 英文识别
	 */
	public static final String LANGUAGE_US = "en_us";
	/**
	 * 中文用户的好友ID=11
	 */
	public static final String CN_FRIEND_ID = "11";
	/**
	 * 英文用户的好友ID=21
	 */
	public static final String US_FRIEND_ID = "21";
	/**
	 * 机器人中文用户ID=1
	 */
	public static final String CN_ROBOT_ID = "1";
	/**
	 * 机器人英文用户ID=2
	 */
	public static final String US_ROBOT_ID = "2";
	/**
	 * 服务器ID=0
	 */
	public static final String SERVER_ID = "0";

	/**
	 * 运动控制帧：手机控制机器人速度。
	 */
	public static final String MOVE_FRAME_ID = "17";

	/**
	 * 导航目标点帧：手机设定机器人导航目标点。
	 */
	public static final String GO_FRAME_ID = "20";

	/**
	 * 机器人状态帧：手机从服务器读取信息。
	 */
	public static final String STATE_FRAME_ID = "12";

	/**
	 * 家中有人帧：手机从服务器读取信息。
	 */
	public static final String ATHOME_FRAME_ID = "14";

	/**
	 * 监控报告帧：手机从服务器读取信息。
	 */
	public static final String REPORT_FRAME_ID = "13";

	/**
	 * 添加提醒日程帧：手机上传服务器
	 */
	public static final String REMIDER_FRAME_ID = "9";
	/**
	 * 添加测血压日程帧：手机上传服务器
	 */
	public static final String TEST_FRAME_ID = "10";

	/**
	 * 添加家电控制日程帧：手机上传服务器
	 */
	public static final String IOT_FRAME_ID = "11";

	/**
	 * 显示当前日程：手机从服务器获取
	 */
	public static final String SCHEDULE_FRAME_ID = "15";

	/**
	 * 删除日历：手机发信息给服务器要求删除某条信息
	 */
	public static final String DEL_SCHEDULE_FRAME_ID = "16";

	/**
	 * 家电实时控制帧：手机文字控制界面输入，通过服务器转发到机器人执行
	 */
	public static final String OPEN_CLOSE_FRAME_ID = "28";
}
