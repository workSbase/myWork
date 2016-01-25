package com.lenovo.lenovorobotmobile.MInterface;

import android.graphics.Bitmap;

/**
 * 被观察者 要实现的接口
 * 
 * @author Administrator
 * 
 */
public interface SubjectServerConnectInterface {
	/**
	 * 注册内容观察者
	 */
	void registerObserver(ServerPacketInterfaceObserver interfaceObserver);

	/**
	 * 删除内容观察者
	 */
	void removeObserver(ServerPacketInterfaceObserver interfaceObserver);

	/**
	 * 如果服务器给返回的值是 String,这一个方法,就可以用来接收服务器给返回的值是 String 类型的
	 * 
	 * @param resut
	 */

	public void notifyObserverServerResult(String resut, int flag);

	/**
	 * 如果服务器给返回的值是一张图片的 字节数组,转化成 bitmap 传递 这个方法主要是在 CameraActivity 中使用的
	 * 
	 * @param
	 */
	public void notifyObserverPictureByte(Bitmap bitmap);

	/**
	 * 如果服务器给返回的值是一个 int
	 */
	public void notifyObserverResultInteger(int result, int flag);
}
