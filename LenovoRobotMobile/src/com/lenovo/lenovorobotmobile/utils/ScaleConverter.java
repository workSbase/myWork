package com.lenovo.lenovorobotmobile.utils;

import android.util.Log;

public class ScaleConverter {
	private static final String TAG = "ScaleConverter";
	private double originX = RobotUtil.ORIGIN_IN_PIX_X;
	private double originY = RobotUtil.ORIGIN_IN_PIX_Y;
	
	private double scaleOfScreenAndPhysical = RobotUtil.RATIO_OF_PIX_TO_PHYSICAL;
	public void setScaleOfScreenAndPhysical(double scaleOfScreenAndPhysical) {
		this.scaleOfScreenAndPhysical = scaleOfScreenAndPhysical;
		Log.i(TAG, "bil ===" + scaleOfScreenAndPhysical);
	}
	private double pixel_x ;
	private double pixel_y ;
	
	public void setX(double x){
		this.pixel_x = (double) (originX + (x * scaleOfScreenAndPhysical));
	}
	public double getX(){
		return pixel_x;
	}
	public void setY(double y){
		this.pixel_y = (double) (originY - (y * scaleOfScreenAndPhysical));
	}
	public double getY(){
		return pixel_y;
	}
	
}
