package com.lenovo.main.TaskInterface;

import com.lenovo.main.TaskInterfaceInfo.LocationForProjectionInfo.ShowPPTInterface;

public interface LocationForProjectionInterface {
	/**
	 * 
	 * @param x
	 *            坐标
	 * @param y
	 *            坐标
	 * @param theta
	 *            角度
	 * @param isOpenProjector
	 *            是不是要打开视频,给 1 表示打开给其他值表示不打开
	 * @param isImage
	 *            这个是投影播放的类型的判断,给 1 表示 投影图片.给 2 表示 投影视频,给 3 表示双屏显示视频
	 * @param isNavigationPose
	 *            是否要获取机器人的当前位置信息
	 * 
	 * @param isStartThread
	 *            是否开始获取机器人当前,行驶状态值
	 * @return int
	 */
	int StartLocationForProjection(float x, float y, float theta,
			int isOpenProjector, int isImage, boolean isNavigationPose,
			boolean isStartThread, int poseFlag); // 任务开始

	/**
	 * 定点任务是否正在继续
	 * 
	 * @return
	 */
	int getNavigationStatus();// 返回值为导航状态，1为结束，0为正在导航

	/**
	 * 打开投影
	 * 
	 * @return
	 * 
	 *         int OpenProjector();
	 */
	/**
	 * 关闭投影
	 * 
	 * @return
	 * 
	 *         int CloseProjector();
	 */
	/**
	 * 放回机器人的当前位置信息
	 * 
	 * @return
	 */
	String getNavigationPose();// 返回值为（x,y,yaw）格式的string，变量为double格式,单位为米和弧度

	/**
	 * 结束当前定点任务
	 * 
	 * @return
	 */
	int ExitNavigation();// 任务结束

	/**
	 * 获取当前机器人的任务ID
	 * 
	 * @return
	 */
	int getRobotTaskId();

	/**
	 * 幻灯片图片的下标
	 */
	void setImageViewIndex(int index);

	/**
	 * 幻灯片视频的下标
	 */
	void setViseoIndex(int index);

	/**
	 * 接口回调
	 * 
	 * @param showPPTInterface
	 */
	public void setShowPPTInterface(ShowPPTInterface showPPTInterface);
}
