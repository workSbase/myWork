package cn.com.lenovo.speechservice.engine;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.com.lenovo.speechservice.domain.EtityInfo;
import cn.com.lenovo.speechservice.domain.FlightInfo;
import cn.com.lenovo.speechservice.domain.HotelInfo;
import cn.com.lenovo.speechservice.domain.NewsInfo;
import cn.com.lenovo.speechservice.domain.PriceInfo;
import cn.com.lenovo.speechservice.domain.SoftwareInfo;
import cn.com.lenovo.speechservice.domain.TextInfo;
import cn.com.lenovo.speechservice.domain.TrainInfo;
import cn.com.lenovo.speechservice.domain.UrlInfo;
import cn.com.lenovo.speechservice.utils.Constant;
import cn.com.lenovo.speechservice.utils.GsonUtil;
import cn.com.lenovo.speechservice.utils.StreamTools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**在人机交互界面打开的时候,拿到通过讯飞录制到的文字.发送到wait上面去拿到相应的识别结果,返回在进行语音的播报
 * 人机交互
 * @author rgj
 *
 */
public class AISpeech {
	// 上下文
	private Context mContext;
	// 语音合成对象
	private SpeechCompound mSpeechCompound;
	// 管理任务栈的对象
	private static Tasker mtasker;
	// handler 用来接收子线程发送过来的信息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;
			switch (msg.what) {
			case Constant.AISPEECH_SUCCESS:
				Toast.makeText(mContext, "达尔文 ：\n" + message, Toast.LENGTH_SHORT).show();
				// 语音播报
				if (null != message)
					mSpeechCompound.speaking(message);
				break;
			case Constant.AISPEECH_ERROR:
				Toast.makeText(mContext, "达尔文 ：error\n" + message, Toast.LENGTH_SHORT).show();
				break;
			case Constant.AISPEECH_FAILURE:
				Toast.makeText(mContext, "达尔文 ：\n网络繁忙，请稍后重试。", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 构造方法
	 * @param context
	 */
	public AISpeech(Context context) {
		// 获取上下文
		mContext = context;
		// 创建管理任务栈的对象
		mtasker = new Tasker(mContext);
		// 获取语音合成对象
		//		mSpeechCompound = SpeechCompound.getInstance(mContext);
		mSpeechCompound = new SpeechCompound(mContext) {

			@Override
			public void speakProgress(int percent, int beginPos, int endPos) {

			}

			@Override
			public void completed() {

			}

		};
	}

	/**
	 * 人机交互
	 * @param speechText
	 */
	public void speechToRobot(final String speechText) {
		/*
		 * 连接网络
		 * 开启子线程
		 */
		new Thread() {
			// 请求地址
			String path = Constant.TULING_URL + "?key=" + Constant.APPKEY + "&info=" + speechText;
			// http://www.tuling123.com/openapi/api?key=a8ee1cb964fd2dbb868958f3a9aa26aa&info=
			// 接收返回的流数据
			private InputStream inputStream;
			// 保存要合成的语音
			private String speechCompoundText;

			// 子线程
			public void run() {
				try {
					// 创建请求对象
					HttpClient httpClient = new DefaultHttpClient();
					// 设置请求网址
					HttpGet http = new HttpGet(path);
					// 请求数据
					HttpResponse httpResponse = httpClient.execute(http);
					// 获取相应码
					int code = httpResponse.getStatusLine().getStatusCode();
					// 判断相应码
					if (200 == code) {
						// 获取返回的流数据
						inputStream = httpResponse.getEntity().getContent();
						// 将输入流数据转换为String类型数据
						String result = StreamTools.readStreamToString(inputStream);
						// 获取要合成语音的文字
						speechCompoundText = getSpeechCompoundText(result);
						// 创建消息
						Message msg = Message.obtain();
						// 添加要传递的信息
						msg.what = Constant.AISPEECH_SUCCESS;
						msg.obj = speechCompoundText;
						// 给Handler传递消息
						handler.sendMessage(msg);
					} else {
						// 创建消息
						Message msg = Message.obtain();
						// 添加要传递的信息
						msg.what = Constant.AISPEECH_FAILURE;
						// 给Handler传递消息
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					// 创建消息
					Message msg = Message.obtain();
					// 添加要传递的信息
					msg.what = Constant.AISPEECH_ERROR;
					msg.obj = e.toString();
					// 给Handler传递消息
					handler.sendMessage(msg);
					e.printStackTrace();
				} finally {
					// 关闭流
					if (null != inputStream) {
						try {
							inputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}

	/**
	 * 通过返回的Json获取要播报的字符串
	 * @param result
	 * @return
	 */
	protected String getSpeechCompoundText(String result) {
		// 返回的json
		String json = result;
		// 获取code字段所在JSON串的位置
		int code_indexOf = result.indexOf("\"code\":", 0);
		// 截取字符串 截取完字符串以状态码开头
		result = result.substring(code_indexOf + "\"code\":".length(), result.length());
		// 获取","所在位置
		int comma_indexOf = result.indexOf(",", 0);
		// 获取状态码
		String codeStr = result.substring(0, comma_indexOf);
		Pattern pattern = Pattern.compile("^[0-5]\\d{5}$");
		Matcher matcher = pattern.matcher(codeStr);

		int code = 0;
		if (matcher.matches()) {
			// 获取返回的JSON中的状态码
			code = Integer.parseInt(codeStr);
		}

		// 判断状态码
		switch (code) {
		case 100000:
			// 返回文本类型的JSON
			TextInfo textInfo = GsonUtil.json2Bean(json, TextInfo.class);
			result = textInfo.text;
			break;
		case 200000:
			// 返回链接类型的JSON
			UrlInfo urlInfo = GsonUtil.json2Bean(json, UrlInfo.class);
			result = urlInfo.text;
			// 给达尔文发送广播 打开网址
			sendBroadcast(urlInfo.code, json);
			break;
		case 302000:
			// 返回新闻类型的JSON
			NewsInfo newsInfo = GsonUtil.json2Bean(json, NewsInfo.class);
			result = newsInfo.text;
			// 给达尔文发送广播 打开新闻
			sendBroadcast(newsInfo.code, json);
			break;
		case 304000:
			// 返回软件下载类型的JSON
			SoftwareInfo softwareInfo = GsonUtil.json2Bean(json, SoftwareInfo.class);
			result = softwareInfo.text;
			break;
		case 305000:
			// 返回列车类型的JSON TrainInfo
			TrainInfo trainInfo = GsonUtil.json2Bean(json, TrainInfo.class);
			result = trainInfo.text;
			break;
		case 306000:
			// 返回航班类型的JSON Flight
			FlightInfo flightInfo = GsonUtil.json2Bean(json, FlightInfo.class);
			result = flightInfo.text;
			break;
		case 308000:
			// 返回电影、视频、菜谱类型的JSON 
			EtityInfo etityInfo = GsonUtil.json2Bean(json, EtityInfo.class);
			result = null;
			// 给达尔文发送广播
			sendBroadcast(etityInfo.code, json);
			break;
		case 309000:
			// 返回酒店类型的JSON
			HotelInfo hotelInfo = GsonUtil.json2Bean(json, HotelInfo.class);
			result = hotelInfo.text;
			break;
		case 311000:
			// 返回价格类型的JSON  PriceInfo
			PriceInfo priceInfo = GsonUtil.json2Bean(json, PriceInfo.class);
			result = priceInfo.text;
			break;
		case 40001:
			// key的长度错误（32位）
			TextInfo erroe_40001 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40001.text;
			break;
		case 40002:
			// 请求内容为空
			TextInfo erroe_40002 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40002.text;
			break;
		case 40003:
			// key错误或帐号未激活
			TextInfo erroe_40003 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40003.text;
			break;
		case 40004:
			// 当天请求次数已用完
			TextInfo erroe_40004 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40004.text;
			break;
		case 0:
		case 40005:
			// 暂不支持该功能或者该功能已经停用
			TextInfo erroe_40005 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40005.text;
			break;
		case 40006:
			// 服务器升级中
			TextInfo erroe_40006 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40006.text;
			break;
		case 40007:
			// 服务器数据格式异常
			TextInfo erroe_40007 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_40007.text;
			break;
		case 50000:
			// 机器人设定的“学用户说话”或者“默认回答”
			TextInfo erroe_50000 = GsonUtil.json2Bean(json, TextInfo.class);
			result = erroe_50000.text;
			break;
		default:
			// 未知状态码
			result = "你说的是什么？类型返回码：" + code;
			break;
		}
		return result;
	}

	/**
	 * 给达尔文发送广播
	 * @param json
	 */
	private void sendBroadcast(int code, String json) {
		Intent showList_intent = new Intent();
		showList_intent.setAction(mtasker.getRunningTask());
		showList_intent.putExtra(Integer.toString(code), json);
		// 发送广播
		mContext.sendBroadcast(showList_intent);
	}
}
