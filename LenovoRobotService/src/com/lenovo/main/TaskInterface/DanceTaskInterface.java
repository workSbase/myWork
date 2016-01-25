package com.lenovo.main.TaskInterface;

public interface DanceTaskInterface {

	/**
	 * 开始跳舞
	 * 
	 * @return
	 */
	int StartDance();

	/**
	 * 结束跳舞
	 * 
	 * @return
	 */
	int ExitDance();

	/**
	 * 获取机器人当前ID值
	 * 
	 * @return
	 */
	int getRobotTaskId();

}
