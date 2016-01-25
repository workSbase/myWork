package com.lenovo.lenovorobotmobile.utils;

import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;

public class ServerUtils {

	private boolean isConnect;
	private MoTransport moTransport;

	public ServerUtils(boolean isConnect, MoTransport moTransport) {
		this.isConnect = isConnect;
		this.moTransport = moTransport;
	}

	/**
	 * 只是 发送三个参数的 方法
	 * 
	 * @param sourceID
	 * @param destinyID
	 * @param frameID
	 */
	public void sendMsgToServer(String sourceID, String destinyID,
			String frameID) {
		if (isConnect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(sourceID));
			moPacket.pushInt32(Integer.parseInt(destinyID));
			moPacket.pushInt32(Integer.parseInt(frameID));
			moTransport.sendPacket(moPacket);
			moPacket = null;
		}
	}

	/**
	 * 只是 发送四个参数的 方法
	 * 
	 * @param sourceID
	 * @param destinyID
	 * @param frameID
	 */
	public void sendMsgToServer(String sourceID, String destinyID,
			String frameID, String str) {
		if (isConnect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(sourceID));
			moPacket.pushInt32(Integer.parseInt(destinyID));
			moPacket.pushInt32(Integer.parseInt(frameID));

			if (Integer.parseInt(frameID) == 7
					|| Integer.parseInt(frameID) == 16
					|| Integer.parseInt(frameID) == 39
					|| Integer.parseInt(frameID) == 38
					|| Integer.parseInt(frameID) == 37
					|| Integer.parseInt(frameID) == 17
					|| Integer.parseInt(frameID) == 18
					|| Integer.parseInt(frameID) == 19
					|| Integer.parseInt(frameID) == 22
					|| Integer.parseInt(frameID) == 27
					|| Integer.parseInt(frameID) == 30
					|| Integer.parseInt(frameID) == 35
					|| Integer.parseInt(frameID) == 40
					|| Integer.parseInt(frameID) == 41
					|| Integer.parseInt(frameID) == 34) {
				moPacket.pushInt32(Integer.parseInt(str));
			} else if (Integer.parseInt(frameID) == 9
					|| Integer.parseInt(frameID) == 10
					|| Integer.parseInt(frameID) == 11
					|| Integer.parseInt(frameID) == 13
					|| Integer.parseInt(frameID) == 31) {
				moPacket.pushString(str, "utf-8");
			}
			moTransport.sendPacket(moPacket);
			moPacket = null;
		}
	}

	/**
	 * 五个 参数的方法
	 * 
	 * @param sourceID
	 * @param destinyID
	 * @param frameID
	 * @param str
	 * @param anotherData
	 */
	public void sendMsgToServer(String sourceID, String destinyID,
			String frameID, String str, String anotherData) {
		if (isConnect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(sourceID));
			moPacket.pushInt32(Integer.parseInt(destinyID));
			moPacket.pushInt32(Integer.parseInt(frameID));
			if (Integer.parseInt(frameID) == 25
					|| Integer.parseInt(frameID) == 28) {
				moPacket.pushInt32(Integer.parseInt(str));
				moPacket.pushInt32(Integer.parseInt(anotherData));
			}
			moTransport.sendPacket(moPacket);
			moPacket = null;
		}
	}

	/**
	 * 五个参数的通用方法
	 * 
	 * @param sourceID
	 *            源
	 * @param destinyID
	 *            目标
	 * @param frameID
	 *            帧号
	 * @param callForm
	 *            通话类型
	 * @param callPeopLeId
	 *            要找的人的 Id
	 */
	// public void sendMsgToServer(int sourceID, int destinyID, int frameID,
	// int anotherForm, int callPeopLeId) {
	// if (isConnect) {
	// MoPacket moPacket = new MoPacket();
	// moPacket.pushInt32(sourceID);
	// moPacket.pushInt32(destinyID);
	// moPacket.pushInt32(frameID);
	// moPacket.pushInt32(anotherForm);
	// moPacket.pushInt32(callPeopLeId);
	//
	// moTransport.sendPacket(moPacket);
	// moPacket = null;
	// }
	// }

	/**
	 * 六个参数的方法
	 * 
	 * @param sourceID
	 * @param destinyID
	 * @param frameID
	 * @param str1
	 * @param str2
	 * @param str
	 */
	public void sendMsgToServer(String sourceID, String destinyID,
			String frameID, String str1, String str2, String str) {
		if (isConnect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(sourceID));
			moPacket.pushInt32(Integer.parseInt(destinyID));
			moPacket.pushInt32(Integer.parseInt(frameID));
			switch (Integer.parseInt(frameID)) {
			case 20:
			case 21:
				moPacket.pushDouble(Double.parseDouble(str1));
				moPacket.pushDouble(Double.parseDouble(str2));
				moPacket.pushDouble(Double.parseDouble(str));
			}
			moTransport.sendPacket(moPacket);
			moPacket = null;
		}
	}
	
	/**
	 * 六个参数的方法
	 * 
	 * @param sourceID
	 * @param destinyID
	 * @param frameID
	 * @param str1
	 * @param str2
	 * @param str
	 */
	public void sendMsgToServer(String sourceID, String destinyID,
			String frameID,String flag, String str1, String str2, String str) {
		if (isConnect) {
			MoPacket moPacket = new MoPacket();
			moPacket.pushInt32(Integer.parseInt(sourceID));
			moPacket.pushInt32(Integer.parseInt(destinyID));
			moPacket.pushInt32(Integer.parseInt(frameID));
			moPacket.pushInt32(Integer.parseInt(flag));
			switch (Integer.parseInt(frameID)) {
			case 20:
			case 21:
				moPacket.pushDouble(Double.parseDouble(str1));
				moPacket.pushDouble(Double.parseDouble(str2));
				moPacket.pushDouble(Double.parseDouble(str));
			}
			moTransport.sendPacket(moPacket);
			moPacket = null;
		}
	}
}
