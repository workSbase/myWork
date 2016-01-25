package com.lenovo.lenovorobot_new.miinterface;

/**
 * 识别人脸的接口
 * 
 * @author Administrator
 * 
 */
public interface RecognizeInterface {
	/**
	 * 开启识别任务
	 */
	public void mstartRecognize();

	/**
	 * 获取识别结果
	 * 
	 * @return
	 */
	public String getRecognzieResult();

	/**
	 * 结束识别任务
	 */
	public void mExitRecognizePersonTask();

	/**
	 * 获取机器人的当前任务ID
	 * 
	 * @return
	 */
	public int getRobotTaskId();

	/**
	 * 测量距离使用的
	 * 
	 * @return
	 */
	public int getDetecDistanceFeedBack();
}
