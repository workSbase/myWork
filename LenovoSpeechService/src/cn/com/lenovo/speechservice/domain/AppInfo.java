package cn.com.lenovo.speechservice.domain;

import java.util.ArrayList;

/**
 * 保存注册应用信息的JavaBean
 * @author kongqw
 *
 */
public class AppInfo {
	// 应用名
	public String name;
	// 应用包名
	public String packageName;
	// 应用类型
	public String type;
	// 打开应用的关键字
	public ArrayList<String> openApplication;
	// 关闭应用的关键词
	public ArrayList<String> closeApplication;
	// Activity状态
	public ArrayList<State> state;

	/**
	 *  Activity状态信息的类
	 * @author kongqw
	 *
	 */
	public class State {
		// Activity名字
		public String activityName;
		// Activity注册的关键词
		public ArrayList<KeyWord> keyWord;
		// 合法的pattern
		public ArrayList<String> pattern;
	}

	/**
	 *  pattern字段的合法关键词的信息
	 * @author kongqw
	 *
	 */
	public class KeyWord {
		// pattern字段
		public String patternKey;
		// pattern字段对应的value值
		public ArrayList<PatternValue> patternValue;
	}

	/**
	 * 保存每个关键词信息的类
	 * @author kongqw
	 *
	 */
	public class PatternValue {
		// 关键词
		public String key;
		// 关键词对应的发送码
		public String value;
	}

}
