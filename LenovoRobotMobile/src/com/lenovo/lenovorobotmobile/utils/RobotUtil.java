package com.lenovo.lenovorobotmobile.utils;

/**
 *地图比例转化使用的类
 */
import java.nio.ByteBuffer;

public class RobotUtil {
	// 大地图的长宽
	// public static final int MAP_SIZE_X = 700;
	// public static final int MAP_SIZE_Y = 740;

	// 小地图的长宽
	// public static final int MAP_SIZE_X = 693;
	// public static final int MAP_SIZE_Y = 662;

	// yy演示用的 mapyy
	public static final int MAP_SIZE_X = 800;
	public static final int MAP_SIZE_Y = 400;

	// public static final int MAP_SIZE_X = 800;
	// public static final int MAP_SIZE_Y = 800;

	// public static final int ORIGIN_IN_PIX_X = 200;
	// public static final int ORIGIN_IN_PIX_Y = 600;

	// 0912
	public static final int ORIGIN_IN_PIX_X = 200;
	public static final int ORIGIN_IN_PIX_Y = 200;

	public static double RATIO_OF_PIX_TO_PHYSICAL = 20.2;
	// public static double RATIO_OF_PIX_TO_PHYSICAL = 18.5;
	static final double RATIO_OF_PHYSICAL_TO_PIX = 1.0 / RATIO_OF_PIX_TO_PHYSICAL;

	static byte[] DoubletoByteArray(double value, int start, int byteCount) {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes, start, byteCount).putDouble(value);
		return bytes;
	}

	static double ByteArraytoDouble(byte[] bytes, int start, int byteCount) {
		return ByteBuffer.wrap(bytes, start, byteCount).getDouble();
	}

	static int ByteArraytoInt(byte[] bytes, int start, int byteCount) {
		return ByteBuffer.wrap(bytes, start, byteCount).getInt();
	}

	public static double toPhysicalCoordX(double x) {
		return (x - ORIGIN_IN_PIX_X) * RATIO_OF_PHYSICAL_TO_PIX;
	}

	public static double toPhysicalCoordY(double y) {
		return (ORIGIN_IN_PIX_Y - y) * RATIO_OF_PHYSICAL_TO_PIX;
	}

	static double toPixCoordX(double x) {
		return x * RATIO_OF_PIX_TO_PHYSICAL + ORIGIN_IN_PIX_X;
	}

	static double toPixCoordY(double y) {
		return ORIGIN_IN_PIX_Y - y * RATIO_OF_PIX_TO_PHYSICAL;
	}

	public static double yyToPixCoordX(double x) {
		return x * 0.05 - 10;
	}

	public static double yyToPixCoordY(double y) {
		return ((401 - y) * 0.05 - 10);
	}
}
