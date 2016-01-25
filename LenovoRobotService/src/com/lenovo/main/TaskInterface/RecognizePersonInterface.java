package com.lenovo.main.TaskInterface;

import java.util.Map;

public interface RecognizePersonInterface {
	/**
	 * 开始检测人脸
	 * 
	 * @return
	 */
	int StartRecognizePerson();

	/**
	 * 结束检测人脸
	 * 
	 * @return
	 */
	int ExitRecognizePerson();

	/**
	 * 返回人的ID值
	 * 
	 * @return
	 */
	String RecognizeResult();// 返回值为（1，2，…，5）表示人的id

	/**
	 * 获取机器人的任务ID
	 * 
	 * @return
	 */
	int getRobotTaskId();
	
}
