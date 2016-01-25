package com.lenovo.lenovorobotmobile.utils;

import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.util.Log;

public class RobotAngle {
	 Matrix m = new Matrix();
	public  Bitmap adjustPhotoRotation(Bitmap bm, final float orientationAngle)
	{
		final String TAG = "DrawView";
		float degree = (orientationAngle * 180.0f) / 3.1416f;
		Log.i(TAG, "degree: " + degree);
         m.setRotate(-degree + 90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

         try {
        Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

           } catch (OutOfMemoryError ex) {
                 }
            return null;

	}
}
