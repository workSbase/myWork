package com.lenovo.main.TaskInterface;

/**
 * 电话任务接口
 * 
 * @author Administrator
 * 
 */
public interface AnswerPhoneInterface {
	/**
	 * 开始任务
	 * 
	 * @param personId
	 * @return
	 */
	int StartVideoCallTask(int personId);// 1爸爸,2妈妈

	/**
	 * 退出当前任务
	 * 
	 * @return
	 */
	int ExitVideoCallTask();// 机器人端挂断

	/**
	 * 手机端接通
	 * 
	 * @return
	 */
	int PhoneConnected();// 接通

	/**
	 * 手机端挂断
	 * 
	 * @return
	 */
	int PhoneDisconnected();// 手机端挂断

	/**
	 * 返回找人结果
	 * 
	 * @return
	 */
	int getFindPeopleStatus();// 返回值为1,2,3,4,5分别对应正在找人，找到人，没有人，没找到人，错误

	/**
	 * 停止说话
	 */
	public void stopSpeaking();

	/**
	 * 设置找人标志
	 * 
	 * @param flag
	 * 
	 *            public void setIsFindingPeople(boolean flag);
	 */
	/**
	 * 关闭找人线程释放资源
	 * 
	 * void closeFindingThread();
	 */

	/**
	 * 让其找人线程,等待
	 * 
	 * @param isWait
	 */
	void isWait(boolean isWait);

	/**
	 * 发送到手机端的数据
	 * 
	 * @param source
	 * @param framer
	 * @param data
	 */
	public void drawinSendInstructToPhone(int source, int framer, int data);

	/**
	 * 开始说话
	 * 
	 * @param content
	 * @param internationalFlag
	 * @param whileFlag
	 */
	public void startSpeech(String content, boolean internationalFlag,
			boolean whileFlag);
}
