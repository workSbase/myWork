package com.lenovo.main.TaskInterface;

public interface RemoteControlInterface {
	/**
	 * 开始遥控任务
	 * 
	 * @return
	 */
	int StartRemoteCtrlTask();

	/**
	 * 结束遥控任务
	 * 
	 * @return
	 */
	int ExitRemoteCtrl();

	/**
	 * 把方向转给底层
	 * 
	 * @param moveFlag
	 * @return
	 */
	int setRemoteCtrlMove(int moveFlag);// 0,1,2,3,4上下左右停(扩展线速度角速度同时运动)

	/**
	 * 脖子上升
	 * 
	 * @return
	 */
	int setLifterUp(); // 返回值同上,头部上升

	/**
	 * 脖子下降
	 * 
	 * @return
	 */
	int setLifterDown(); // 返回值同上,头部下降

	/**
	 * 头部抬起
	 * 
	 * @return
	 */
	int setHeadLookUp(); // 返回值同上,头部抬起一定角度(弧度)

	/**
	 * 头部底下
	 * 
	 * @return
	 */
	int setHeadBow(); // 返回值同上,头部低下一定角度

	/**
	 * 获取机器人当前任务ID
	 * 
	 * @return
	 */
	int getRobotTaskId();

}
