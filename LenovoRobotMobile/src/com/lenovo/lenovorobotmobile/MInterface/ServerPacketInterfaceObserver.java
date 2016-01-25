package com.lenovo.lenovorobotmobile.MInterface;

import android.graphics.Bitmap;

/**
 * 观察者的接口
 * 
 * @author Administrator
 * 
 */
public interface ServerPacketInterfaceObserver {
	/**
	 * 如果服务器给返回的值是 String,这一个方法,就可以用来接收服务器给返回的值是 String 类型的
	 * 
	 * @param resut
	 */
	public void setServerResult(String resut, int flag);

	/**
	 * 如果服务器给返回的值是一张图片的 字节数组,转化成 bitmap 传递 这个方法主要是在 CameraActivity 中使用的
	 * 
	 * @param
	 */
	public void setPictureByte(Bitmap bitmap);

	/**
	 * 如果服务器给返回的值是一个 int
	 */

	public void setResultInteger(int result, int flag);

}
