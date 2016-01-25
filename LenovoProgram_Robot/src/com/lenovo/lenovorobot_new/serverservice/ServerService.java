package com.lenovo.lenovorobot_new.serverservice;

import com.lenovo.lenovorobot_new.BaseClass.BaseService;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * 主要的任务是和服务器保持连接,发送和接收服务器相关信息,这是一个Service
 * 
 * @author Administrator
 * 
 */
public class ServerService extends BaseService {

	private AcceptServerMessage acceptServerMessageUtils;

	// 连接服务器对象
	private MoTransport mtServer;

	// 要想和该server之间有互动,获取该handler对象,传递数据即可
	public static ServerSendMessageHandler serverSendMessageHandler;

	@Override
	public void initService() {
		// 获取传输器对象
		mtServer = new MoTransport();
		// 接收服务器发送过来的数据,并对数据进行处理
		acceptServerMessageUtils = new AcceptServerMessage(
				getApplicationContext(), mtServer);

		serverSendMessageHandler = new ServerSendMessageHandler(mtServer);
	}

	@Override
	public void initServiceDate() {

	}

	@Override
	public void onDestroy() {
		if (acceptServerMessageUtils != null) {
			// 断开和服务器之间的联系
			acceptServerMessageUtils.stopServer();
		}
		super.onDestroy();
	}
}
