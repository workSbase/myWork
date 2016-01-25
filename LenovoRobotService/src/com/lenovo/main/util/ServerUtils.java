package com.lenovo.main.util;

import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * Created by Administrator on 2015/6/17. 发送数据的公共的类
 */
public class ServerUtils {
	private final MoTransport moTransport;
	private final boolean isConntect;

	public ServerUtils(MoTransport moTransport, boolean isConntect) {
		this.moTransport = moTransport;
		this.isConntect = isConntect;
	}

	/**
	 * 专门用来发送图片的方法
	 * 
	 * @param yuan
	 * @param target
	 * @param frames
	 * @param pictureBy
	 */
	public void sendBitmap(String yuan, String target, String frames,
			byte[] pictureBy) {
		// TODO Auto-generated method stub
		if (isConntect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(yuan));
			moPacket.pushInt32(Integer.parseInt(target));
			moPacket.pushInt32(Integer.parseInt(frames));
			if (pictureBy != null) {
				moPacket.pushByteArray(pictureBy, pictureBy.length);
				moTransport.sendPacket(moPacket);
			}
			moPacket = null;
		}
	}
}
