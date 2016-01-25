package com.lenovo.lenovorobotmobile.MInterface;

/**
 * http 请求用到的回调方法 使用在自己封装的一个http请求的类中,这两个方法都是在 handler 中使用
 * 
 * @author Administrator
 * 
 */
public interface HttpResultInterface {
	/**
	 * 请求正确回调
	 * 
	 * @param result
	 */
	void httpResponseResult(String result);

	/**
	 * 请求错误回调
	 * 
	 * @param resultError
	 */
	void httpResponseResultError(String resultError);
}
