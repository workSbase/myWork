package cn.com.lenovo.speechservice.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cn.com.lenovo.speechservice.domain.AppInfo;
import cn.com.lenovo.speechservice.domain.AppInfo.State;
import cn.com.lenovo.speechservice.utils.Constant;
import cn.com.lenovo.speechservice.utils.FucUtil;
import cn.com.lenovo.speechservice.utils.GsonUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * 管理授权应用的类
 * @author kongqw
 *
 */
public class Applicationer {
	// 上下文
	private Context mContext;
	// 注册应用信息的JavaBean
	private static AppInfo mApplicationInfo;
	/*
	 * 所有注册应用的信息
	 * K 应用名
	 * V 对应的JavaBean
	 */
	private static HashMap<String, AppInfo> appInfoMap;
	/*
	 * 保存打开应用的关键词
	 * K 应用名
	 * V 打开应用的关键字的集合
	 */
	private static HashMap<String, ArrayList<String>> openWordsMap;
	/*
	 * 保存关闭应用的关键词
	 * K 应用名
	 * V 关闭应用的关键字的集合
	 */
	private static HashMap<String, ArrayList<String>> closeWordsMap;
	/*
	 * 保存Activity的注册信息
	 * K Activity全名
	 * V Activity注册信息
	 */
	private static HashMap<String, State> activityInfo;
	// 管理任务栈的对象
	private static Tasker tasker;

	/**
	 * 构造方法
	 * @param context
	 */
	public Applicationer(Context context) {
		// 获取上下文
		this.mContext = context;
		// 创建管理任务栈的对象
		tasker = new Tasker(mContext);
		// 获取授权应用信息
		getApplicationInfo();
	}

	/**
	 * 获取到注册应用的信息
	 */
	private void getApplicationInfo() {
		String json = "";
		// TODO 暂时为了测试方便，等数据来自后台以后，使用集合管理JSON
		/*
		 * 保存"达尔文"的注册信息
		 */
		json = FucUtil.readFile(mContext, "Darwin", "utf-8");
		saveApplicationInfo(json);
		/*
		 * 保存"三星桌面"的注册信息
		 */
		json = FucUtil.readFile(mContext, "SamsungTable", "utf-8");
		saveApplicationInfo(json);
		/*
		 * 保存"三星桌面"的注册信息
		 */
		json = FucUtil.readFile(mContext, "LenovoTable", "utf-8");
		saveApplicationInfo(json);


		/*
		 *  当有新的注册信息加载进来的时候获取一次，
		 *  然后保存到静态变量中，不用每次解析都解析JavaBean，
		 *  提高执行效率
		 */
		// 获取打开应用的关键词
		getOpenWords();
		// 获取关闭应用的关键词
		getCloseWords();
		// 获取注册的Activity的信息
		getActivityInfo();

		// TODO 判断词表是否有变化

		if (!Constant.UPLOAD_WORD_STATE) {
			// 中文
			// 判断网络状态
			if (Constant.NETWORK_STATE) {
				// 有网络上传词表
				Worder worder = new Worder(mContext);
				worder.uploadeWords(getUploadWords());
			} else {
				// 没有网络 不上传词表
				Toast.makeText(mContext, "没有网络，暂不上传词表", Toast.LENGTH_SHORT).show();
				// 设置标志位 词表没有上传成功
				Constant.UPLOAD_WORD_STATE = false;
			}
		}

	}

	/**
	 * 获取注册的Activity的信息
	 */
	private void getActivityInfo() {
		// 保存注册的Activity信息
		activityInfo = new HashMap<String, State>();

		for (Map.Entry<String, AppInfo> entry : appInfoMap.entrySet()) {
			// 遍历到每一个应用
			AppInfo appInfo = entry.getValue();
			// 获取每个应用注册的Activity的个数
			int activityCount = appInfo.state.size();
			// 遍历每个Activity
			for (int activityIndex = 0; activityIndex < activityCount; activityIndex++) {
				String activityName = appInfo.state.get(activityIndex).activityName;
				State state = appInfo.state.get(activityIndex);
				activityInfo.put(activityName, state);
			}
		}
	}

	/**
	 * 保存注册应用的信息
	 * @param json 获取到的JSON数据
	 */
	private void saveApplicationInfo(String json) {
		// TODO 检测JSON格式是否正确
		if (true) {
			if (null == appInfoMap) {
				appInfoMap = new HashMap<String, AppInfo>();
			}
			// 将JSON中的数据封装到JavaBean
			mApplicationInfo = GsonUtil.json2Bean(json, AppInfo.class);
			// 将注册的应用添加到Map中
			appInfoMap.put(mApplicationInfo.name, mApplicationInfo);
		}
	}

	/**
	 * 获取词表上传的词语
	 * K 应用名
	 * V 应用中要上传的词语
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getUploadWords() {
		// 保存返回值
		HashMap<String, ArrayList<String>> upludeWordsInfo = new HashMap<String, ArrayList<String>>();
		// 遍历注册应用信息 提取上传的词语
		if (null != appInfoMap) {
			for (Map.Entry<String, AppInfo> entry : appInfoMap.entrySet()) {
				// 获取到应用名
				String appName = entry.getKey();
				// 获取到应用信息
				AppInfo appInfo = entry.getValue();
				// 保存当前应用要上传的词语
				ArrayList<String> appWords = new ArrayList<String>();
				// 保存打开应用的关键词
				appWords.addAll(appInfo.openApplication);
				// 保存关闭应用的关键词
				appWords.addAll(appInfo.closeApplication);

				/*
				 * 保存各个界面的关键词
				 */
				// 获取注册界面的个数
				int activityCount = appInfo.state.size();
				// 遍历每个界面 获取要上传的词表
				for (int activityIndex = 0; activityIndex < activityCount; activityIndex++) {
					// 获取词表个数
					int keyWordCount = appInfo.state.get(activityIndex).keyWord.size();
					// 遍历每个词表 获取要上传的词语
					for (int keyWordIndex = 0; keyWordIndex < keyWordCount; keyWordIndex++) {
						// 获取词表中词语的个数
						int wordCount = appInfo.state.get(activityIndex).keyWord.get(keyWordIndex).patternValue.size();
						// 遍历每个词
						for (int wordIndex = 0; wordIndex < wordCount; wordIndex++) {
							// 获取到要上传的词语
							String uplodeWord = appInfo.state.get(activityIndex).keyWord.get(keyWordIndex).patternValue.get(wordIndex).key;
							// 保存要上传的词语
							appWords.add(uplodeWord);
						}
					}
				}

				// 保存当前应用的应用名和要上传的词语
				upludeWordsInfo.put(appName, appWords);
			}
		}
		return upludeWordsInfo;
	}

	/**
	 * TODO 用于检测JSON格式是否正确，功能未加入
	 * 获取应用的Activity中对应的pattern中合法字段 
	 * eg：
	 * 	cn.com.lenovo.speechservice.test.MainActivity	ABCD
	 * 	cn.com.lenovo.speechservice.test.SecondActivity	A
	 * @param appName 应用名
	 * @return
	 */
	public HashMap<String, HashSet<String>> getPatternColumn(String appName) {
		//	private HashMap<String, HashSet<String>> getPatternColumn(String appName) {
		/*
		 * K 对应的activity 
		 * V 对应的activity中的合法的字段
		 */
		HashMap<String, HashSet<String>> patternColumn = new HashMap<String, HashSet<String>>();
		// 通过应用名获取到对应的注册信息
		AppInfo appInfo = appInfoMap.get(appName);
		// 注册信息中没有此应用
		if (null == appInfo) {
			Toast.makeText(mContext, "注册信息中没有[" + appName + "]应用", Toast.LENGTH_SHORT).show();
			return null;
		}
		// 获取到应用中注册的Activity的个数
		int activityCount = appInfo.state.size();
		// 遍历注册的Activity，获取到pattern字段
		for (int activityIndex = 0; activityIndex < activityCount; activityIndex++) {
			// 获取到Activity全名
			String activityName = appInfo.state.get(activityIndex).activityName;
			// 获取到合法的Pattern个数
			int patternCount = appInfo.state.get(activityIndex).pattern.size();

			// HashSet去除重复 保存整个Activity中所有pattern中定义的字段
			HashSet<String> hashSet = new HashSet<String>();
			// 遍历获取每个pattern的值
			for (int patternIndex = 0; patternIndex < patternCount; patternIndex++) {
				// 获取到pattern的值
				String pattern = appInfo.state.get(activityIndex).pattern.get(patternIndex);
				// 使用"&"分割字符串 获取到pattern的字段
				String[] column = pattern.split("&");
				for (int index = 0; index < column.length; index++) {
					hashSet.add(column[index]);
				}
				patternColumn.put(activityName, hashSet);
			}
			//Toast.makeText(mContext, "activityName = " + activityName + "\nhashSet = " + hashSet, Toast.LENGTH_LONG).show();
		}
		return patternColumn;
	}

	/**
	 * 获取打开应用的关键词
	 */
	private void getOpenWords() {
		openWordsMap = new HashMap<String, ArrayList<String>>();
		// 遍历获取应用名和打开应用的关键词
		for (Map.Entry<String, AppInfo> entry : appInfoMap.entrySet()) {
			// 获取应用名
			String appName = entry.getKey();
			// 获取注册应用对象
			AppInfo appInfo = entry.getValue();
			// 获取打开应用关键词
			ArrayList<String> openWords = appInfo.openApplication;
			// 将关键词存入Map
			openWordsMap.put(appName, openWords);
		}
	}

	/**
	 * 获取关闭应用的关键词
	 */
	private void getCloseWords() {
		closeWordsMap = new HashMap<String, ArrayList<String>>();
		// 遍历获取应用名和打开应用的关键词
		for (Map.Entry<String, AppInfo> entry : appInfoMap.entrySet()) {
			// 获取应用名
			String appName = entry.getKey();
			// 获取注册应用对象
			AppInfo appInfo = entry.getValue();
			// 获取关闭应用关键词
			ArrayList<String> closeWords = appInfo.closeApplication;
			// 存入Map
			closeWordsMap.put(appName, closeWords);
		}
	}

	/**
	 * 将应用名转换为包名
	 * @param appName 应用名
	 * @return 包名
	 */
	private String appName2packageName(String appName) {
		return appInfoMap.get(appName).packageName;
	}

	/**
	 * 通过Pattern字段名获取对应可以识别的词
	 * @param state 注册界面的信息
	 * @param patternkey 查询的pattern字段
	 * @return 各个字段(词表)中每个词和对应的发送码
	 */
	private ArrayList<HashMap<String, String>> getPatternValue(State state, String patternkey) {
		ArrayList<HashMap<String, String>> patternValue = new ArrayList<HashMap<String, String>>();
		// 获取页面注册词表个数
		int keyWordCount = state.keyWord.size();
		// 遍历词表
		for (int keyWordIndex = 0; keyWordIndex < keyWordCount; keyWordIndex++) {
			// 词表匹配 获取对应的词表
			if (patternkey.equals(state.keyWord.get(keyWordIndex).patternKey)) {
				// 获取关键词个数
				int wordCount = state.keyWord.get(keyWordIndex).patternValue.size();
				// 遍历每个词
				for (int wordIndex = 0; wordIndex < wordCount; wordIndex++) {
					HashMap<String, String> patternValueMap = new HashMap<String, String>();
					// 获取识别词
					String word = state.keyWord.get(keyWordIndex).patternValue.get(wordIndex).key;
					// 获取识别词的发送码
					String munber = state.keyWord.get(keyWordIndex).patternValue.get(wordIndex).value;
					patternValueMap.put(word, munber);
					patternValue.add(patternValueMap);
				}
			}
		}

		return patternValue;
	}

	/**
	 * 判断是否打开应用
	 * @param speechText 语音录入的文字
	 * @return
	 */
	public String isOpenApplication(String speechText) {
		String packageName = null;
		// 遍历Map获取应用对应的关键词
		for (Map.Entry<String, ArrayList<String>> entry : openWordsMap.entrySet()) {
			// 获取每个Map的每个值
			ArrayList<String> words = entry.getValue();
			// 判断语音录入的信息是否是打开应用的关键字
			for (String word : words) {
				// 判断录入的文字是否是打开 应用的关键字
				if (speechText.equals(word)) {
					// 获取应用名
					String appName = entry.getKey();
					// 将应用名转换为包名
					packageName = appName2packageName(appName);
				}
			}
		}
		return packageName;
	}

	/**
	 * 判断是否关闭应用
	 * @param speechText 语音录入的文字
	 * @return
	 */
	public String isCloseApplication(String speechText) {
		String packageName = null;
		for (Map.Entry<String, ArrayList<String>> entry : closeWordsMap.entrySet()) {
			// 获取每个Map的每个值
			ArrayList<String> words = entry.getValue();
			// 判断语音录入的信息是否是打开应用的关键字
			for (String word : words) {
				// 判断录入的文字是否是打开 应用的关键字
				if (speechText.equals(word)) {
					// 获取应用名
					String appName = entry.getKey();
					// 将应用名转换为包名
					packageName = appName2packageName(appName);
				}
			}
		}
		return packageName;
	}

	/**
	 * 通过包名打开应用
	 * @param openPackageName
	 */
	public void openApplication(String openPackageName) {
		if (!tasker.getRunningTask().equals(openPackageName)) {
			Toast.makeText(mContext, "打开应用" + openPackageName, Toast.LENGTH_SHORT).show();
			// 获取到包的管理者
			PackageManager packageManager = mContext.getPackageManager();
			// 获取到打开一个应用程序的意图
			Intent intentForPackage = packageManager.getLaunchIntentForPackage(openPackageName);
			if (null != intentForPackage) {
				// 打开程序
				mContext.startActivity(intentForPackage);
			} else {
				if ("com.miui.home".equals(openPackageName) || "com.sec.android.app.launcher".equals(openPackageName)) {
					// 返回桌面
					Intent intent = new Intent();
					intent.setAction("android.intent.action.MAIN");
					intent.addCategory("android.intent.category.HOME");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		} else {
			//Toast.makeText(mContext, "程序已经运行", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 通过包名关闭应用
	 * @param closePackageName
	 */
	public void closeApplication(String closePackageName) {
		Toast.makeText(mContext, "关闭 closePackageName = " + closePackageName, Toast.LENGTH_SHORT).show();
		/*
		 * TODO 关闭应用
		 * 暂时使用发广播实现
		 * 关闭第三方应用，需要获取到Root权限
		 */
		Intent closeApp_intent = new Intent();
		closeApp_intent.setAction(closePackageName);
		closeApp_intent.putExtra(closePackageName, "closeApp");
		// 发送广播
		mContext.sendBroadcast(closeApp_intent);
	}

	/**
	 * 判断屏幕当前显示的应用是否授权
	 * @return
	 */
	public boolean isAuthorisedForRunningApplication() {
		boolean isAuthorised = false;
		// 获取当前正在运行的应用名
		String runningAppName = tasker.getRunningAppName();
		// 通过应用名找到的注册信息非空
		if (null != appInfoMap.get(runningAppName)) {
			isAuthorised = true;
		}
		return isAuthorised;
	}

	/**
	 * 判断屏幕当前显示的界面(Activity)是否授权
	 * @return
	 */
	public State isAuthorisedForRunningActivity() {
		// 获取当前正在运行的Activity
		String runningActivity = tasker.getRunningActivity();
		// 返回State
		return activityInfo.get(runningActivity);
	}

	/**
	 * 通过State对象获取对应正则表达式
	 * @param activityInfo 注册页面的对象
	 * @return
	 *     K pattern
	 *     V 正则表达式
	 */
	public HashMap<String, String> getPatternInfo(State activityInfo) {
		/*
		 * 用来保存pattern以及正则表达式
		 * K pattern
		 * V 正则表达式
		 */
		HashMap<String, String> patternInfo = new HashMap<String, String>();

		// 获取当前正在运行的授权Activity的所有pattern
		ArrayList<String> patterns = activityInfo.pattern;
		for (String pattern : patterns) {
			// 用于拼接正则表达式
			StringBuffer buffer = new StringBuffer();
			buffer.append("^");
			// 对pattern使用&进行分割
			String[] patternColumn = pattern.split("&");
			// 遍历Pattern每个字段 获取字段值 拼接正则表达式
			for (int index = 0; index < patternColumn.length; index++) {
				buffer.append("(");
				/*
				 * 匹配规则
				 * "^((打电话)|(发短信))((给)|(to))((张三)|(李四))$"
				 */
				// 通过pattern字段获取对应的词表
				ArrayList<HashMap<String, String>> patternValue = getPatternValue(activityInfo, patternColumn[index]);
				ArrayList<String> words = new ArrayList<String>();
				for (HashMap<String, String> map : patternValue) {
					for (Map.Entry<String, String> entry : map.entrySet()) {
						String word = entry.getKey();
						words.add(word);
					}
				}
				for (int x = 0; x < words.size(); x++) {
					if (x < words.size() - 1) {
						buffer.append("(").append(words.get(x)).append(")").append("|");
					} else {
						buffer.append("(").append(words.get(x)).append(")");
					}
				}

				buffer.append(")");
			}
			buffer.append("$");

			patternInfo.put(pattern, buffer.toString());
		}

		return patternInfo;
	}

	/**
	 * 通过语音录入的文字和匹配的Pattern获取广播放松码
	 * @param State 当前授权界面信息
	 * @param pattern 满足语音录入的文字的pattern
	 * @param speechText 语音录入的文字
	 * @return 广播发送码
	 */
	public String getBroadcastNamber(State state, String pattern, String speechText) {
		// 用于拼接广播发送码
		StringBuffer buffer = new StringBuffer();
		// 分割pattern获取pattern字段
		String[] patternColumn = pattern.split("&");
		// 遍历每个字段，获取发送码
		for (int index = 0; index < patternColumn.length; index++) {
			// 获取到当前界面各个字段(词表)的信息
			ArrayList<HashMap<String, String>> patternValue = getPatternValue(state, patternColumn[index]);
			// 遍历每个词的信息
			for (int x = 0; x < patternValue.size(); x++) {
				for (Map.Entry<String, String> entry : patternValue.get(x).entrySet()) {
					// 匹配与某个词匹配
					if (speechText.startsWith(entry.getKey())) {
						//					if (speechText.equals(entry.getKey())) {
						// 获取发送码
						String number = entry.getValue();
						// 拼接广播发送码
						if (index < patternColumn.length - 1) {
							buffer.append(number).append("&");
						} else {
							buffer.append(number);
						}
						// 切割字符串，将解析完的词语去掉
						speechText = speechText.substring(entry.getKey().length());
					}
				}
			}
		}
		return buffer.toString();
	}

}
