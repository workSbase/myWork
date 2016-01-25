package com.lenovo.lenovorobot_new.miinterfaceinfo;

import android.os.ILenovoDarwin;
import com.lenovo.lenovorobot_new.miinterface.MIInterface;

public class MIClass implements MIInterface {
	private LenovoDarwin LeDarwinService = null;

	public MIClass(ILenovoDarwin LeDarwinService) {
		this.LeDarwinService = LeDarwinService;
	}

	@Override
	public int SendTask2Kernel(int nCMDCode, int[] nP1, float[] fP2,
			double[] dP3, String[] strP4) {
		if (LeDarwinService != null) {
			try {
				return LeDarwinService.SendTask2Kernel(nCMDCode, nP1, fP2, dP3,
						strP4);
			} catch (Exception e) {
			}
		}
		return 0;
	}
}
