package com.lenovo.main.MIService;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.google.gson.Gson;
import com.lenovo.main.TaskInterface.AnotherInterface;
import com.lenovo.main.TaskInterface.AnswerPhoneInterface;
import com.lenovo.main.TaskInterface.BackToDockTaskInterface;
import com.lenovo.main.TaskInterface.ChatWithVisualServoInterface;
import com.lenovo.main.TaskInterface.DanceTaskInterface;
import com.lenovo.main.TaskInterface.DetectPersonInterface;
import com.lenovo.main.TaskInterface.FcaeMove;
import com.lenovo.main.TaskInterface.FollowMeTaskInterface;
import com.lenovo.main.TaskInterface.LocationForProjectionInterface;
import com.lenovo.main.TaskInterface.PeopleFace;
import com.lenovo.main.TaskInterface.ProjectorInterface;
import com.lenovo.main.TaskInterface.RecognizePersonInterface;
import com.lenovo.main.TaskInterface.ReminderTaskInterface;
import com.lenovo.main.TaskInterface.RemoteControlInterface;
import com.lenovo.main.TaskInterface.StopAnyTasksInterface;
import com.lenovo.main.TaskInterfaceInfo.AnotherInfo;
import com.lenovo.main.TaskInterfaceInfo.AnswerPhoneInfo;
import com.lenovo.main.TaskInterfaceInfo.BackToDockTaskInfo;
import com.lenovo.main.TaskInterfaceInfo.ChatWithVisualServoInfo;
import com.lenovo.main.TaskInterfaceInfo.DanceTaskInfo;
import com.lenovo.main.TaskInterfaceInfo.DetectPersonInfo;
import com.lenovo.main.TaskInterfaceInfo.FcaeMoveInfo;
import com.lenovo.main.TaskInterfaceInfo.FollowMeTaskInfo;
import com.lenovo.main.TaskInterfaceInfo.LocationForProjectionInfo;
import com.lenovo.main.TaskInterfaceInfo.PeopleFaceInfo;
import com.lenovo.main.TaskInterfaceInfo.ProjectorInfo;
import com.lenovo.main.TaskInterfaceInfo.RecognizePersonInfo;
import com.lenovo.main.TaskInterfaceInfo.ReminderTaskInfo;
import com.lenovo.main.TaskInterfaceInfo.ReminderTaskInfo_new;
import com.lenovo.main.TaskInterfaceInfo.RemoteControlInfo;
import com.lenovo.main.TaskInterfaceInfo.StopAnyTasksInfo;
import com.lenovo.main.activityVideo.CameraActivity;
import com.lenovo.main.activityVideo.TesterApplication;
import com.lenovo.main.bin.PeopleListBin;
import com.lenovo.main.bin.PeopleListBin.PeopleList;
import com.lenovo.main.net.ServerConnect;
import com.lenovo.main.util.HandlerMassageUtils;
import com.lenovo.main.util.MyToast;
import com.lenovo.main.util.PresentationClass;
import com.lenovo.main.util.ShowTitleBar;
import com.lenovo.main.util.SpeechCompound;
import com.lenovo.main.util.StreamTools;
import com.lenovo.mi.plato.comm.MoTransport;

//import android.util.Slog;
//import android.os.ServiceManager;
//import android.os.ILenovoDarwin;

/**
 * 这是一个基础的 service 也是一个基类,把通用的方法抽取到父类中来完成, 不知道怎么实现的方法留给子类自己来完成 (1,创建 titlebar
 * 2,绑定底层的 service 3,连接服务器 )这三个都是每一个子类都需要干的事情,抽取在父类中完成.父类往外暴露一个方法,给每一个子类自己去完成
 * 自己的业务逻辑.以后这个 baseService 就不需要在改动,只要改动每一个子类就可以
 * 
 * 当这个服务一开启的时候,就把相应的任务对象初始化完成,使用静态变量进行保存.这样的话,就可以做到一次初始化到处使用
 * 
 * @author Administrator
 */

@SuppressLint("UseSparseArrays")
public abstract class BaseService extends Service {
	public MoTransport myMt;
	public Gson gson;
	public MyToast myToast;
	ServerConnect connect;
	public static ArrayList<PeopleList> peopleLists;
	private MediaPlayer mMediaPlayer = null;

	// 接口对象 子类可以直接使用,一下定义的 变量
	// public static ILenovoDarwin LeDarwinService = null;
	public static AnswerPhoneInterface answerPhoneInterface;
	public static FollowMeTaskInterface followMeTaskInterface;
	public static LocationForProjectionInterface locationForProjectionInterface;
	public static RemoteControlInterface remoteControlInterface;
	public static DetectPersonInterface detectPersonInterface;
	public static RecognizePersonInterface recognizePersonInterface;
	public static AnotherInterface anotherInterface;
	public static DanceTaskInterface danceTaskInterface;
	public static BackToDockTaskInterface backToDockTaskInterface;
	public static HandlerMassageUtils handlerMassageUtils;
	public static ReminderTaskInterface reminderTaskInterface;
	public static ChatWithVisualServoInterface chatWithVisualServoInterface;
	public static StopAnyTasksInterface anyTasksInterface;

	public static ProjectorInterface projectorInterface;
	public static ReminderTaskInterface taskInterface;

	public static PeopleFace peopleFace;

	public static FcaeMove fcaeMove;

	// 语音合成对象
	public static SpeechCompound compound;
	// 投影帮助类
	public static PresentationClass presentationClass;

	// private int status;
	// private UserStatusThread statusThread = null;
	// private Thread thread2 = null;
	// private boolean isStart = true;
	public static Map<Integer, String> peopleMap = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		peopleMap = new HashMap<Integer, String>();

		gson = new Gson();

		// 每一个自服务都需要创建 titleBar
		ShowTitleBar showTitleBar = new ShowTitleBar(this);
		showTitleBar.showBar();

		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}

		// 连接服务器
		connect = new ServerConnect(this);
		connect.startConnect();
		myMt = ServerConnect.mt;

		try {
			InputStream open = this.getAssets().open("peoplelist");
			String readFromStream = StreamTools.readFromStream(open);
			PeopleListBin fromJson = gson.fromJson(readFromStream,
					PeopleListBin.class);
			peopleLists = fromJson.peopleList;
			for (PeopleList peopleList : peopleLists) {
				peopleMap.put(peopleList.peopleId, peopleList.name);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 绑定底层service的地方,就是这一个地方
		 */
		// LeDarwinService = ILenovoDarwin.Stub.asInterface(ServiceManager
		// .getService("Lenovo MI Service"));
		// try {
		// LeDarwinService.OpenSSHD();
		// } catch (Exception e) {
		// }

		myToast = new MyToast(this);

		initTaskInterface();

		initData();

	}

	/**
	 * 一个地方初始化到处都可以使用,使用静态变量,面向接口编程
	 */
	private void initTaskInterface() {
		compound = new SpeechCompound(this) {
			@Override
			public void speakProgress(int percent, int beginPos, int endPos) {
				// TODO Auto-generated method stub
			}

			@Override
			public void completed() {
				// TODO Auto-generated method stub
			}
		};
		// 初始化电话任务
		answerPhoneInterface = new AnswerPhoneInfo(this, compound, myMt);

		// 初始化跟人任务
		followMeTaskInterface = new FollowMeTaskInfo();

		// 初始化定点任务
		locationForProjectionInterface = new LocationForProjectionInfo(this,
				myMt);

		// 初始化遥控任务
		remoteControlInterface = new RemoteControlInfo();

		// 初始化,测量身高的任务
		detectPersonInterface = new DetectPersonInfo(this, compound);

		// 初始化人脸识别任务
		recognizePersonInterface = new RecognizePersonInfo(this, compound, gson);

		// 初始化其他任务
		anotherInterface = new AnotherInfo(this);

		// 跳舞任务
		danceTaskInterface = new DanceTaskInfo(this);

		// 回充电站任务
		backToDockTaskInterface = new BackToDockTaskInfo();

		// 初始化投影对象
		presentationClass = new PresentationClass(this);

		// handler 消息处理类
		handlerMassageUtils = new HandlerMassageUtils(this);

		// reminderTaskInterface 任务
		reminderTaskInterface = new ReminderTaskInfo(this, compound, gson);

		chatWithVisualServoInterface = new ChatWithVisualServoInfo();

		// 打开投影的任务接口
		projectorInterface = new ProjectorInfo();

		anyTasksInterface = new StopAnyTasksInfo();

		taskInterface = new ReminderTaskInfo_new(this, compound, gson);

		peopleFace = new PeopleFaceInfo();

		fcaeMove = new FcaeMoveInfo();
	}

	public abstract void initData();

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/**
	 * 开启视频通话的activity,flag 是标记,用标记来显示不同风格的界面效果,是视频通话还是视频监控 抽取到父类中完成
	 * 
	 * @param flag
	 */
	public void startCamareActivity(int flag) {
		// 表示的控制
		((TesterApplication) this.getApplication()).setVideoState(flag);
		// 直接拨打电话
		((TesterApplication) this.getApplication())
				.setRtcEngine((((TesterApplication) this.getApplication()))
						.getVendorKey());

		Intent toChannel = new Intent(this, CameraActivity.class);
		toChannel.putExtra(ServerConnect.EXTRA_TYPE,
				ServerConnect.CALLING_TYPE_VIDEO);
		toChannel.putExtra(ServerConnect.EXTRA_CHANNEL,
				ServerConnect.ROOM_NUMBER);
		toChannel.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(toChannel);
	}
}
