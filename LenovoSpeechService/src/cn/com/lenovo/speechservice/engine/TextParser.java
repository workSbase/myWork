package cn.com.lenovo.speechservice.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.com.lenovo.speechservice.domain.AppInfo.State;
import cn.com.lenovo.speechservice.utils.Broadcast;
import cn.com.lenovo.speechservice.utils.Constant;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 本类对字符串进行解析，从说的话中提取关键字，根据关键字解析用户意图 调用相应接口，实现语音功能
 * @author kongqw
 *
 */
public class TextParser {
	// 上下文
	private Context mContext;
	// 管理注册应用的对象
	private static Applicationer mApplicationer;
	// 管理任务栈的对象
	private static Tasker mtasker;

	/**
	 * http请求全部放在该类中完成
	 */
	private static AISpeech aiSpeech;

	/**
	 * 构造方法
	 * @param context
	 */
	public TextParser(Context context) {
		// 上下文
		mContext = context;
		// 创建注册应用的管理对象
		mApplicationer = new Applicationer(mContext);
		// 创建管理任务栈的对象
		mtasker = new Tasker(mContext);
		// 
		aiSpeech = new AISpeech(mContext);
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
			Toast.makeText(mContext, "[非授权应用]\n网络状态 ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();
			return;
		}

		/*
		 * 判断屏幕正在显示的界面是否授权
		 */
		State activityInfo = mApplicationer.isAuthorisedForRunningActivity();
		if (null != activityInfo) {
			// 授权
			Toast.makeText(mContext, "[授权界面]\n网络状态 ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();
			// 根据pattern解析语音意图 获取发送码
			String broadcastNamber = speechTextParser(speechText, activityInfo);
			if (null != broadcastNamber) {
				if (broadcastNamber.startsWith("Darwin&MOVE")) {
					// 移动指令
					Broadcast.mySendBroadcast(mContext, "darwinControl", "MOVE", broadcastNamber.substring(12));
				} else if (broadcastNamber.startsWith("Darwin&GO")) {
					// 去xx 指令
					Broadcast.mySendBroadcast(mContext, "darwinControl", "GO", broadcastNamber.substring(10));
				} else if (broadcastNamber.equals("cookbook")) {
					// 判断当前页面是不是达尔文
					if ("cn.com.lenovo.darwin.DarwinActivity".equals(mtasker.getRunningActivity())) {
						Intent cook_intent = new Intent();
						cook_intent.setAction(Constant.DARWIN_PACKAGENAME);
						cook_intent.putExtra("foodName", speechText.substring(5));
						mContext.sendBroadcast(cook_intent);
					} else {
						new Thread() {
							public void run() {
								try {
									sleep(1000);
									Intent cook_intent = new Intent();
									cook_intent.setAction(Constant.DARWIN_PACKAGENAME);
									cook_intent.putExtra("foodName", speechText.substring(5));
									mContext.sendBroadcast(cook_intent);
									//									Broadcast.mySendBroadcast(mContext, Constant.DARWIN_PACKAGENAME, "foodName", speechText.substring(5));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							};
						}.start();
					}
				} else if (broadcastNamber.startsWith("Darwin&OPEN")) {
					// 开电器
					Broadcast.mySendBroadcast(mContext, "darwinControl", "OPEN", broadcastNamber.substring(12));
				} else if (broadcastNamber.startsWith("Darwin&CLOSE")) {
					// 关电器
					Broadcast.mySendBroadcast(mContext, "darwinControl", "CLOSE", broadcastNamber.substring(13));
				} else if (broadcastNamber.startsWith("Darwin&1001&1002&1003") || broadcastNamber.startsWith("Darwin&1002&1003&1001")) {
					// 发送拨打电话的广播
					Broadcast.mySendBroadcast(mContext, "darwinControlCall", "CALL", "1001&1002&1003");
				} else {
					// 发送广播
					sendBroadcast(speechText, broadcastNamber);
				}
			}
			return;
		}

		// 未授权
		Toast.makeText(mContext, "[非授权界面]\n网络状态 ： " + Constant.NETWORK_STATE + "\n\n" + speechText + "\n", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 解析语音录入的文字
	 * @param speechText 语音录入的文字
	 * @param activityInfo 授权界面的信息
	 * @return 发送码
	 */
	public String speechTextParser(String speechText, State activityInfo) {
		// 通过Activity的注册信息获取pattern的正则表达式
		HashMap<String, String> patternInfo = mApplicationer.getPatternInfo(activityInfo);
		// 广播发送码
		String broadcastNamber = null;

		for (Map.Entry<String, String> entry : patternInfo.entrySet()) {
			// 取得正则表达式
			String token = entry.getValue();

			Pattern pattern = Pattern.compile(token);
			Matcher matcher = pattern.matcher(speechText);
			// 语法匹配
			if (matcher.matches()) {
				String Pattern = entry.getKey();
				// 获取广播发送码
				broadcastNamber = mApplicationer.getBroadcastNamber(activityInfo, Pattern, speechText);
				if (broadcastNamber.contains("SEARCH")) {
					// 打开达尔文
					mApplicationer.openApplication(Constant.DARWIN_PACKAGENAME);
					// 发送
					// aiSpeech.speechToRobot(speechText.substring(5));
					return "cookbook";
				}
				return broadcastNamber;
			} else {
				/*
				 * 人机交互
				 * 当前网络可用并且当前正在开启达尔文应用
				 */
				if (Constant.NETWORK_STATE && mtasker.getRunningTask().equals(Constant.DARWIN_PACKAGENAME)) {
					if (Pattern.compile("^(宫爆鸡丁|宫保鸡丁|鱼香肉丝|糖醋排骨|地三鲜|麻辣豆腐|锅包肉|回锅肉|西红柿炒鸡蛋|蛋炒饭|红烧肉)$").matcher(speechText).matches()) {
						Intent speechText_intent = new Intent();
						speechText_intent.setAction(mtasker.getRunningTask());
						speechText_intent.putExtra("foodName", speechText);
						mContext.sendBroadcast(speechText_intent);
						//						Broadcast.mySendBroadcast(mContext, mtasker.getRunningTask(), "foodName", speechText);
						return null;
					}
					/*
					 * 判断达尔文状态
					 * 是否开启人机交互
					 */
					if (Constant.DARWIN_STATE) {
						aiSpeech.speechToRobot(speechText);
					}
					/*
					 * 达尔文选择状态
					 */
					if (Constant.DARWIN_SELECT) {
						//						Intent speechText_intent = new Intent();
						//						speechText_intent.setAction(mtasker.getRunningTask());
						//						speechText_intent.putExtra("speechText", speechText);
						//						mContext.sendBroadcast(speechText_intent);
						Broadcast.mySendBroadcast(mContext, mtasker.getRunningTask(), "speechText", speechText);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 发送广播的方法
	 * @param speechText
	 * @param broadcastNamber
	 */
	private void sendBroadcast(String speechText, String broadcastNamber) {
		sendBroadcast(speechText, null, broadcastNamber);
	}

	private void sendBroadcast(String speechText, String key, String broadcastNamber) {
		sendBroadcast(speechText, null, key, broadcastNamber);
	}

	private void sendBroadcast(String speechText, String action, String key, String broadcastNamber) {
		Intent endCall_intent = new Intent();
		String packageName = mtasker.getRunningTask();
		if (null == action) {
			// 发送广播的Action为应用的包名
			endCall_intent.setAction(packageName);
		} else {
			endCall_intent.setAction(action);
		}

		if (null == key) {
			/*
			 * K Activity全名
			 * V 发送码
			 */
			String activityName = mtasker.getRunningActivity();
			key = activityName;
		}
		broadcastNamber = broadcastNamber.startsWith("Darwin&") ? broadcastNamber.replaceFirst("Darwin&", "") : broadcastNamber;
		endCall_intent.putExtra(key, broadcastNamber);
		// 发送广播
		mContext.sendBroadcast(endCall_intent);
		Toast.makeText(mContext, "[" + speechText + "] 的广播发送成功\nAction = " + (action == null ? packageName : action) + "\nKey = " + key + "\nValue = " + broadcastNamber,
				Toast.LENGTH_SHORT).show();
	}

}
