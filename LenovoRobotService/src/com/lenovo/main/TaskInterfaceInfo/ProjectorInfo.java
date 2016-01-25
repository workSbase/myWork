package com.lenovo.main.TaskInterfaceInfo;

import com.lenovo.main.MIService.BaseService;
import com.lenovo.main.TaskInterface.ProjectorInterface;

//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;
//import android.util.Slog;

public class ProjectorInfo implements ProjectorInterface {

	@Override
	public int OpenProjector() {
		try {
			//return BaseService.LeDarwinService.OpenProjector();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public int CloseProjector() {
		try {
			//return BaseService.LeDarwinService.CloseProjector();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

}
