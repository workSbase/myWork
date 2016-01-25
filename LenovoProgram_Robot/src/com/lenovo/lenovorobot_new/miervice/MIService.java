package com.lenovo.lenovorobot_new.miervice;

import android.os.IBinder;
import android.os.ILenovoDarwin;
import android.os.ISSService;
import android.os.RemoteCallbackList;
import android.os.ServiceManager;
import com.lenovo.lenovorobot_new.BaseClass.BaseService;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

/**
 * 和底层交互的服务,只要是和底层相关的操作都要从这个service中的Handler中经过,当前的服务只是获取底层的服务,
 * 然后把这个服务对象下发到handler中供使用
 * 
 * @author Administrator
 * 
 */
public class MIService extends BaseService {

	// 获取到的底层的服务对象
	private ILenovoDarwin LeDarwinService = null;
	private ISSService SSService = null;
	private Log_Toast log_Toast;
	public static MIMessageHandler MIMESSAGEHANDLER = null;

	@Override
	public void initService() {
		log_Toast = new Log_Toast(getApplicationContext());
		initMIService();
	}

	private void initMIService() {
		/**
		 * 下面代码,就是和底层的服务进行绑定的地方
		 */
		if (LeDarwinService == null && SSService == null) {

			IBinder service;
			try {
				service = ServiceManager.getService("Lenovo MI Service");
				LeDarwinService = ILenovoDarwin.Stub.asInterface(service);

				SSService = ISSService.Stub.asInterface(ServiceManager
						.getService("SS Service"));
			} catch (Exception e) {
				log_Toast.Toast("绑定底层服务出错了  Exception : " + e.toString(), 0);
			}
		}

		MIMESSAGEHANDLER = new MIMessageHandler(this, LeDarwinService);
	}

	@Override
	public void initServiceDate() {
		try {
			SSService.StartLenovoMI();
		} catch (Exception e) {

		}
	}
}
