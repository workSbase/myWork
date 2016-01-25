package cn.com.lenovo.speechservice.engine;

import cn.com.lenovo.speechservice.domain.AppInfo.State;
import cn.com.lenovo.speechservice.utils.Broadcast;
import cn.com.lenovo.speechservice.utils.Constant;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 本类对字符串进行解析，从说的话中提取关键字，根据关键字解析用户意图 调用相应接口，实现语音功能
 * @author kongqw
 *
 */
public class TextParserUs {//implements IWitListener {
	// 上下文
	private Context mContext;
	// 管理注册应用的对象
	private static Applicationer mApplicationer;

	/**
	 * 构造方法
	 * @param context
	 */
	public TextParserUs(Context context) {
		// 上下文
		mContext = context;
		// 创建注册应用的管理对象
		mApplicationer = new Applicationer(mContext);
	}

	/**
	 * 文字解析
	 * @param speechText
	 */
	public void parseSpeech(final String speechText) {

		/*
		 * 判断是否要打开应用
		 * 打开应用的关键词没有界面限制 在任何界面识别到打开应用的关键词都会执行
		 */
		String openPackageName = mApplicationer.isOpenApplication(speechText);
		if (null != openPackageName) {
			// 打开应用
			mApplicationer.openApplication(openPackageName);
			return;
		}

		/*
		 * 判断是否关闭应用
		 * 关闭应用的关键词没有界面限制 在任何界面识别到关闭应用的关键词都会执行
		 */
		String closePackageName = mApplicationer.isCloseApplication(speechText);
		if (null != closePackageName) {
			// 关闭应用
			mApplicationer.closeApplication(closePackageName);
			return;
		}

		/*
		 * 判断屏幕正在显示的应用是否授权
		 */
		if (!mApplicationer.isAuthorisedForRunningApplication()) {
			// 程序没有授权 不提供语音服务
			Toast.makeText(mContext, "[Is Not Authorised]\nNetWorkState ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();
			return;
		}

		/*
		 * 判断屏幕正在显示的界面是否授权
		 */
		State activityInfo = mApplicationer.isAuthorisedForRunningActivity();
		if (null != activityInfo) {
			// 授权
			Toast.makeText(mContext, "[Is Authorised]\nNetWorkState ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();

			if (!TextUtils.isEmpty(speechText) && (speechText.startsWith("Louis") || speechText.startsWith("Lewis") || speechText.startsWith("Louise"))) {
				//				_wit.captureTextIntent(speechText.replaceFirst("Louise", "").replaceFirst("Louis", "").replaceFirst("Lewis", ""));
				String command = speechText.replaceFirst("Louise", "").replaceFirst("Louis", "").replaceFirst("Lewis", "");
				Toast.makeText(mContext, "command:\n" + command, Toast.LENGTH_SHORT).show();
				parserCommand(command.trim());
			}

			return;
		}

		// 未授权
		Toast.makeText(mContext, "[Is Not Authorised]\nNetWorkState ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 执行指令的方法
	 * @param command
	 */
	public void parserCommand(String command) {
		/**
		 * 打电话
		 */
		if (command.startsWith("call") || command.endsWith("call") || command.endsWith("called")) {
			// 发送拨打电话的广播
			Broadcast.mySendBroadcast(mContext, "darwinControlCall", "CALL", "1001&1002&1003");
			return;
		}

		/**
		 * 行为
		 */
		if ("turn left".equalsIgnoreCase(command)) {
			// 发送左转的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "2");
			return;
		} else if ("turn right".equalsIgnoreCase(command)) {
			// 发送右转的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "3");
			return;
		} else if ("go forward".equalsIgnoreCase(command)) {
			// 发送前进的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "0");
			return;
		} else if ("go backward".equalsIgnoreCase(command)) {
			// 发送后退的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "1");
			return;
		} else if ("follow me".equalsIgnoreCase(command)) {
			// 发送跟着我的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "5");
			return;
		} else if ("stop".equalsIgnoreCase(command)) {
			// 发送停止我的广播
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "4");
			return;
		} else if ("stop follow".equalsIgnoreCase(command) || "cancel follow".equalsIgnoreCase(command)) {
			// 发送别跟了
			Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", "6");
			return;
		}

		/**
		 * 到XX去
		 */
		if ("go to kitchen".equalsIgnoreCase(command)) {
			Broadcast.mySendBroadcast(mContext, "darwinControl", "GO", "-2.6,0.04,0.0");
			return;
		} else if ("go to living room".equalsIgnoreCase(command) || "go to livingroom".equalsIgnoreCase(command)) {
			Broadcast.mySendBroadcast(mContext, "darwinControl", "GO", "4.0,0.0,0.0");
			return;
		} else if ("go to bed room".equalsIgnoreCase(command) || "go to bedroom".equalsIgnoreCase(command)) {
			Broadcast.mySendBroadcast(mContext, "darwinControl", "GO", "0.0,1.0,0.0");
			return;
		}

		/**
		 * 家电控制
		 */
		if (command.contains("turn on")) {
			Broadcast.mySendBroadcast(mContext, "darwinControl", "OPEN", "1");
			return;
		} else if (command.contains("turn off")) {
			Broadcast.mySendBroadcast(mContext, "darwinControl", "CLOSE", "1");
			return;
		}
	}

}
