package com.lenovo.lenovorobotmobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * 把图片,变成灰色的
 * 
 * @author Administrator
 * 
 */
public class BitmapTools {
	public static final Bitmap grey(Bitmap bitmap) {
		
		
		
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
		return faceIconGreyBitmap;

	}
	public static Bitmap getBitmap(Context context,int id){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), id, options);
		
		Bitmap grey = grey(decodeResource);

		return grey;
	}
}
