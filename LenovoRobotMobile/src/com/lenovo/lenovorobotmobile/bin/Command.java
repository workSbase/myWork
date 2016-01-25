package com.lenovo.lenovorobotmobile.bin;

import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lenovo.lenovorobotmobile.speech.Constant;

/**
 * Intent
 * 
 * @author kongqw
 * 
 */
public class Command {

	// Intent
	public String intent;

	// 关键词
	public ArrayList<KeyWord> keyWord;

	public class KeyWord {
		public String key;
		public String frame;
	}

	/**
	 * 获取Fram
	 * 
	 * @param command
	 * @return
	 */
	public String getFram(String command) {
		int keyWordSize = keyWord.size();
		// 遍历每一个关键词
		for (int index = 0; index < keyWordSize; index++) {
			// 获取每一个关键词
			String key = keyWord.get(index).key;
			Pattern pattern = Pattern.compile("^(" + key + ")$");
			Matcher matcher = pattern.matcher(command);
			if (matcher.matches()) {
				// 匹配
				return makeFrame(keyWord.get(index).frame);
			}
		}
		return null;
	}

	/**
	 * 生成Frame
	 * 
	 * @param frame
	 * @return
	 */
	private String makeFrame(String frame) {
		StringBuffer sb = new StringBuffer();
		if ("move_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.US_ROBOT_ID).append(",,").append(Constant.MOVE_FRAME_ID).append(",,")
					.append(frame);
		} else if ("electric_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.STATE_FRAME_ID);
		} else if ("doing_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.STATE_FRAME_ID);
		} else if ("athome_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.ATHOME_FRAME_ID);
		} else if ("on_off_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.US_ROBOT_ID).append(",,").append(Constant.OPEN_CLOSE_FRAME_ID).append(",,")
			// 电器编号
					.append("电器编号").append(",,")
					// 开关状态
					.append(frame);
		} else if ("schedule_us".equalsIgnoreCase(intent)) {
			sb.append(Constant.US_FRIEND_ID).append(",,").append(Constant.SERVER_ID).append(",,").append(Constant.SCHEDULE_FRAME_ID);
		}

		return sb.toString();
	}

}
