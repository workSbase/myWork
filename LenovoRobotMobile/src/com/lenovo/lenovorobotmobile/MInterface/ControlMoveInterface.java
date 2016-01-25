package com.lenovo.lenovorobotmobile.MInterface;

public interface ControlMoveInterface {
	/**
	 * 这两个方法提供给子类使用,想使用的话直接复写.主要是用在 遥控界面,上下左右 四个按键的 使用
	 * 当点击下去的时候,开始发送线程,当抬起的时候,关闭线程
	 */
	public void startControlMoveFlagThread();

	public void stopControlMoveFlagThread();
}
