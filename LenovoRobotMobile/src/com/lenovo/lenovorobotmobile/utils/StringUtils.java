package com.lenovo.lenovorobotmobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringUtils {

	public static String getTextFromStream(InputStream is) {
		byte[] b = new byte[1024];
		int len;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			String text = new String(bos.toByteArray());
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
