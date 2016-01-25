package com.lenovo.main.TaskInterface;

public interface ReminderTaskInterface {
	/**
	 * 第一次一开始开始提醒任务
	 * 
	 * @return
	 */
	int StartReminderTask();

	/**
	 * 结束ReminderTask任务
	 * 
	 * @return
	 */
	int ExitReminderTask();

	/**
	 * 开始检查身高的值
	 * 
	 * @return
	 */
	int getFindPeopleStatus();// 返回值为身高，单位m/
								// 如果在remider任务下，收到身高数据（>0）说明有人，语音播报提醒事件

	/**
	 * 开始人脸识别,第二次
	 * 
	 * @return
	 */
	int StartRecognizePerson(); // 此接口同Recognize Task不同

	/**
	 * 结束人脸识别
	 * 
	 * @return
	 */
	int ExitRecognizePerson(); // 此接口同Recognize Task不同

	/**
	 * 返回人脸的值
	 * 
	 * @return
	 */
	String RecognizeResult();// 返回值为（1，2，…，5）表示人的id id>0播报XXX，欢迎光临”
								// id=-1，“尊贵的客人，欢迎光临，首次相见，请多关照”

	/**
	 * 获取机器人的任务ID
	 * 
	 * @return
	 */
	public int getRobotTaskId();
	
	/**
	 * 
	 */
	
	public void startReminder_Recognize();
}
