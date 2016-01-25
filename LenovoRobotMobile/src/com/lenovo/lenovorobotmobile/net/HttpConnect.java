package com.lenovo.lenovorobotmobile.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;

import com.lenovo.lenovorobotmobile.MInterface.HttpResultInterface;
import com.lenovo.lenovorobotmobile.utils.StringUtils;

/**
 * 封装了 post 请求的类,主要是用在好友管理
 * 
 * @author Administrator
 * 
 */
public class HttpConnect {

	List<NameValuePair> parameters;
	private HttpResultInterface httpResultInterface;
	private HttpClient mHttpClient;
	private HttpPost mHttpPost;
	private Thread httpConnectThread = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			httpConnectThread = null;
			switch (msg.what) {
			case 1:
				if (httpResultInterface != null) {
					httpResultInterface.httpResponseResult((String) msg.obj);
				}
				break;
			case 2:
				if (httpResultInterface != null) {
					httpResultInterface
							.httpResponseResultError((String) msg.obj);
				}
				break;
			}
		};
	};

	public HttpConnect(String serverPath) {
		// 先创建一个httpclient对象
		mHttpClient = new DefaultHttpClient();
		parameters = new ArrayList<NameValuePair>();
		// 创建post请求对象
		mHttpPost = new HttpPost(serverPath);
	}

	/**
	 * BasicNameValuePair 用来封装想要传递数据的,相当于 一个 map
	 * 
	 * @param mNameValuePairs
	 */
	public void setPostParems(NameValuePair... mNameValuePairs) {
		for (NameValuePair mNameValuePair : mNameValuePairs) {
			parameters.add(mNameValuePair);
		}
	}

	/**
	 * 使用的一个回调接口.只要再别的类实现该接口,并传递进来,就可以拿到请求回来的数据
	 * 
	 * @param httpResultInterface
	 */
	public void setHttpResultInterfaceInfo(
			HttpResultInterface httpResultInterface) {
		this.httpResultInterface = httpResultInterface;
	}

	/**
	 * 开始连接请求
	 */
	public void startHttpConnect() {
		if (httpConnectThread == null) {
			httpConnectThread = new Thread(new HttpThread());
			httpConnectThread.start();
		}
	}

	/**
	 * 连接线程
	 * 
	 * @author Administrator
	 * 
	 */
	class HttpThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mHttpPost.setEntity(new UrlEncodedFormEntity(parameters,
						"utf-8"));
				// 发送post请求
				HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
				// 判断请求码 是否正确
				if (mHttpResponse.getStatusLine().getStatusCode() == 200) {
					InputStream is = mHttpResponse.getEntity().getContent();
					String text = StringUtils.getTextFromStream(is);

					handlerMsg(text, 1);
				} else {
					handlerMsg("clientError", 2);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handlerMsg(String text, int flag) {
		Message msg = handler.obtainMessage();
		msg.obj = text;
		msg.what = flag;
		handler.sendMessage(msg);
	}
}
