package com.lenovo.lenovorobot_new.miinterface;

/**
 * 电话找人接口
 * 
 * @author Administrator
 * 
 */
public interface AnwerPhoneInterface {
	/**
	 * 开始找人,把要找的人的Id给传递进来
	 * 
	 * @param personId
	 */
	public void startFindingPersonTask(int personId);

	/**
	 * 结束掉当前找人的任务
	 */
	public void exitFindingPersonTask();

	/**
	 * 获取找人结果. 1,表示正在找人 2,表示已经是找到人了 5,表示没有找到人
	 * 
	 * @return 找人结果
	 */
	public int getFindingPersonStatus();

	/**
	 * 获取机器人的当前的任务的ID值
	 * 
	 * @return 机器人的ID值
	 */
	public int getRobotCourentTask();
}
