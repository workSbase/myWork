package com.lenovo.lenovorobot_new.miinterface;

public interface MIInterface {
	/**
	 * 下发指令的接口,该接口既可以下发指令也可以获取指令
	 * 
	 * @param nCMDCode
	 *            指令(通过 int 类型的高四位表示)
	 * @param nP1
	 *            参数(底层返回回来的数据,也将会保存在这个数组中)
	 * @param fP2
	 *            参数(底层返回回来的数据,也将会保存在这个数组中)
	 * @param dP3
	 *            参数(底层返回回来的数据,也将会保存在这个数组中)
	 * @param strP4
	 *            参数(底层返回回来的数据,也将会保存在这个数组中)
	 * @return
	 */
	public int SendTask2Kernel(int nCMDCode, int[] nP1, float[] fP2,
			double[] dP3, String[] strP4);
}
