package cn.com.lenovo.speechservice.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {

	private static ByteArrayOutputStream arrayOutputStream;

	public static String readStreamToString(InputStream inputStream) {
		try {
			// 字节数组的输出流 用来转换字符串
			arrayOutputStream = new ByteArrayOutputStream();

			// 缓存
			byte[] buffer = new byte[1024];

			// 读取字节数
			int len = 0;

			// 循环读流数据
			while (-1 != (len = inputStream.read(buffer))) {
				arrayOutputStream.write(buffer, 0, len);
			}

			// 转成String字符串
			String result = new String(arrayOutputStream.toByteArray());

			// 返回结果
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			// 转换出现异常 返回空字符串
			return "";
		} finally {
			if (null != arrayOutputStream) {
				try {
					// 关闭流
					arrayOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
