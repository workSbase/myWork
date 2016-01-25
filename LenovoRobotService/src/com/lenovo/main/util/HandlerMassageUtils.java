package com.lenovo.main.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.BaseService;

/**
 * 现在该类都是放在主线程中来完成,语音的播放,和底层的调用. 想使用线程的方式来完成,暂时还没有想到怎么样处理
 * 
 * @author Administrator
 * 
 */
public class HandlerMassageUtils implements OnItemClickListener {
	private Context context;
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

	public HandlerMassageUtils(Context context) {
		this.context = context;
	}

	/**
	 * handler 的消息处理
	 * 
	 * @param key
	 */
	public void setMassage(int key) {

		switch (key) {
		case 0:
			BaseService.compound
					.speaking(
							"您好。我叫萌萌哒达尔文。我是针对于老年人的家庭陪伴机器人。我会的东西可多了，我能识别人脸，检测身高，测量心率，视频通话，还能投影和控制电灯",
							true, false);
			break;
		case 1:
			BaseService.compound.speaking("请站到我前方1.5米处 开启测量身高功能", true, false);

			BaseService.detectPersonInterface.StartDetectPersonInfo();
			break;
		case 2:
			// 请站到我前方1.5米处
			BaseService.compound.speaking("请站到我前方1.5米处开始识别", true, false);
			int robotTaskId = BaseService.reminderTaskInterface
					.getRobotTaskId();
			if (robotTaskId == 11) {
				BaseService.reminderTaskInterface.startReminder_Recognize();
			} else {
				BaseService.recognizePersonInterface.StartRecognizePerson();
			}
			break;
		case 3:
			// 开启心率测量
			BaseService.compound.speaking("请放好手的位置开启心率测量", true, false);
			// 打开测量血压的app,通过别的应用的包名,打开别的应用
			context.startActivity(context.getPackageManager()
					.getLaunchIntentForPackage("com.lenovo.recordvideo"));
			break;
		case 4:
			BaseService.compound.speaking("好的马上连线", true, false);
			// WeaverService.getInstance().dispatchRequest(
			// WeaverAPI.sipMakeCall("18616609483", false, null));
			break;
		case 5:
			// 投影
			BaseService.compound.speaking("投影已经为您打开是否播放视频", true, false);

			// BaseService.locationForProjectionInterface.OpenProjector();
			BaseService.projectorInterface.OpenProjector();
			break;
		case 6:
			// 开灯
			BaseService.compound.speaking("马上给您开启电灯", true, false);

			break;
		case 7:
			// 关灯
			BaseService.compound.speaking("马上给您关闭电灯", true, false);

			break;
		case 8:
			// BaseService.locationForProjectionInterface.CloseProjector();
			BaseService.compound.speaking("投影马上为您关闭", true, false);
			BaseService.projectorInterface.CloseProjector();
			break;
		case 10:
			BaseService.answerPhoneInterface.stopSpeaking();
			break;
		case 11:
			// 打开视频播放列表app
			BaseService.compound.speaking("请选择要播放视频", true, false);
			context.startActivity(context.getPackageManager()
					.getLaunchIntentForPackage("cn.com.lenovo.videoplayer"));
			break;
		case 12:
			// 开始跟人
			BaseService.compound.speaking("好吧那我就跟定你了", true, false);
			BaseService.followMeTaskInterface.StartFollowMeTask();
			break;
		case 13:
			// 结束跟人
			BaseService.compound.speaking("我不要在跟随你了", true, false);
			BaseService.followMeTaskInterface.ExitFollowMeTask();
			break;
		case 14:
			// 前进
			setMoveFlag(0);
			break;
		case 15:
			// 后退
			setMoveFlag(1);
			break;
		case 16:
			// 左转
			setMoveFlag(2);
			break;
		case 17:
			// 右转
			setMoveFlag(3);
			break;
		case 18:
			// 去厨房
			goWhere(-2.6f, 0.04f, 0.0f);
			break;
		case 19:
			// 去卧室
			goWhere(4.0f, 0.0f, 0.0f);
			break;
		case 20:
			// 去阳台
			goWhere(0.0f, 1.0f, 0.0f);
			break;
		case 21:
			// 去客厅
			goWhere(0.0f, 1.0f, 0.0f);
			break;
		case 22:
			// 开始跳舞
			BaseService.danceTaskInterface.StartDance();
			break;
		case 23:
			// 结束跳舞
			BaseService.compound.speaking("舞蹈已经为您结束", true, false);
			BaseService.danceTaskInterface.ExitDance();
			break;
		case 24:
			// 开始视频
			break;
		case 25:
			// 紧急停止
			BaseService.anyTasksInterface.StopAnyTasks();
			break;
		case 26:
			// 接听电话,发给 cameraactivity 中的handler 来完成
			// CameraActivity.handler.sendEmptyMessage(12);
			break;
		case 27:
			// 通过声音来触发这个条件,弹出来一个对话框来给用户做选着
			Toast.makeText(context, "开始找人", 0).show();
			showPeopleDialog();
			break;
		case 28:
			// 这个地方直接可以开启视觉伺服
			Toast.makeText(context, "开始视觉伺服", 0).show();
			BaseService.fcaeMove.MStartVisualServoTask();
			break;
		case 29:
			// 结束人脸随动
			Toast.makeText(context, "关闭视觉伺服", 0).show();
			BaseService.fcaeMove.MExitVisualServoTask();
			break;
		}
	}

	private void goWhere(float x, float y, float yaw) {
		// TODO Auto-generated method stub
		BaseService.locationForProjectionInterface.StartLocationForProjection(
				x, y, yaw, 0, 0, false, false, 0);
	}

	private void setMoveFlag(int flag) {
		int robotTaskId = BaseService.remoteControlInterface.getRobotTaskId();
		if (robotTaskId != 4) {
			BaseService.remoteControlInterface.StartRemoteCtrlTask();
		}
		BaseService.remoteControlInterface.setRemoteCtrlMove(flag);
	}

	private void showPeopleDialog() {
		builder = new Builder(context);
		builder.setTitle("请选着要找的人");
		View view = View.inflate(context, R.layout.peoplelistview, null);
		ListView peopleListView = (ListView) view
				.findViewById(R.id.peopleListView);
		MyAdapter adapter = new MyAdapter();
		peopleListView.setOnItemClickListener(this);
		peopleListView.setAdapter(adapter);
		builder.setView(view);

		alertDialog = builder.show();

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return BaseService.peopleLists.size();
		}

		@Override
		public Object getItem(int position) {

			return BaseService.peopleLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = View
						.inflate(context, R.layout.peoplelistviewchild, null);
			}
			TextView textViewName = (TextView) view
					.findViewById(R.id.nameTextView);
			textViewName.setText(BaseService.peopleLists.get(position).name);
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// 调用底层的方法,开始一边转动一边找人,只要把当前的这个ID给传递进去就行了
		int peopleID = BaseService.peopleLists.get(position).peopleId;
		Toast.makeText(context, "当前人的 ID :" + peopleID, 0).show();
		BaseService.peopleFace.MStartMoveRecognizeTask(peopleID);
		alertDialog.dismiss();
	}
}
