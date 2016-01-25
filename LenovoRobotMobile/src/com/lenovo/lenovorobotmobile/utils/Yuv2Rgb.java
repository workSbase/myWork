package com.lenovo.lenovorobotmobile.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
/**
 * 手机端传递过来的 byte 数组转换成 bitmap
 * @author Administrator
 *
 */
public class Yuv2Rgb {

	public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
		int frameSize = width * height;
		int[] rgba = new int[frameSize];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				int y = (0xff & ((int) data[i * width + j]));
				int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
				int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
				y = y < 16 ? 16 : y;

				int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
				int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
				int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

				r = r < 0 ? 0 : (r > 255 ? 255 : r);
				g = g < 0 ? 0 : (g > 255 ? 255 : g);
				b = b < 0 ? 0 : (b > 255 ? 255 : b);

				rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
			}

		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bmp.setPixels(rgba, 0, width, 0, 0, width, height);
		return bmp;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
