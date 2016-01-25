package com.lenovo.lenovorobotmobile.utils;

import android.content.Context;
import android.widget.EditText;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.speech.TextPaser;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * 聊天界面中的内容,中文内容处理类
 * 
 * @author Administrator
 * 
 */
public class ChineseUtils {
	private ChatsContentShowTools layout;
	private TextPaser textPaser;
	private EditText contentShowEditText;
	private ServerUtils msgToServerHelp;
	private String[] splits;
	private Context context;

	public ChineseUtils(Context context, MoTransport moTransport,
			ChatsContentShowTools layout, TextPaser textPaser,
			EditText contentShowEditText, ServerUtils msgToServerHelp) {
		this.layout = layout;
		this.textPaser = textPaser;
		this.contentShowEditText = contentShowEditText;
		this.msgToServerHelp = msgToServerHelp;
		this.context = context;
	}

	/**
	 * 消息处理的方法
	 * 
	 * @param contentText
	 */
	public void disposeMsg(String contentText) {
		// 判断 contentText 的内容
		if (stringIsTrue(contentText)) {
			if (contentText.equals("家庭成员热图")) {
				layout.addReightView(contentText);
				layout.addLiftView("", true, R.drawable.heartmap_);
				contentShowEditText.setText("");
			} else if (contentText.equals("家庭成员习惯图")) {
				layout.addReightView(contentText);
				layout.addLiftView("", true, R.drawable.robotbitmap);
				contentShowEditText.setText("");
			} else if (contentText.equals("开始跳舞")) {
				layout.addReightView(contentText);
				contentShowEditText.setText("");
				// 给服务器发送开始跳舞的指令
				msgToServerHelp.sendMsgToServer("11", "1", "39", "0");
				// myToast.showToast(0, "开始跳舞指令");
				layout.addLiftView("指令发送成功", false, 0);
			} else if (contentText.equals("结束跳舞") || contentText.equals("停止跳舞")) {
				layout.addReightView(contentText);
				contentShowEditText.setText("");
				// 给服务器发送结束跳舞的指令
				msgToServerHelp.sendMsgToServer("11", "1", "39", "1");
				// myToast.showToast(0, "结束跳舞指令");
				layout.addLiftView("指令发送成功", false, 0);
			} else if (contentText.equals("回充电站")) {
				layout.addReightView(contentText);
				contentShowEditText.setText("");
				msgToServerHelp.sendMsgToServer("11", "1", "39", "2");
				// myToast.showToast(0, "回充电站指令");
				layout.addLiftView("指令发送成功", false, 0);
			} else {
				String sendFram = textPaser.getSendMessage(contentText);
				if (sendFram != null) {
					// 选着哪一个方法,发送数据
					selectSendMsgMothed(sendFram);
					layout.addReightView(contentText);
					contentShowEditText.setText("");
					showTasckIsOk(splits[2]);
					MToast.showToast(context, "sendFram  : " + sendFram, 0);
				} else {
					MToast.showToast(context, "该指令不存在,请从新下达", 0);
				}
			}
		} else {
			MToast.showToast(context, "请输入规范内容", 0);
		}
	}

	// 判断字符串是否是附和规范的
	private boolean stringIsTrue(String contentText) {
		if (contentText != null && !contentText.equals("")) {
			return true;
		}
		return false;
	}

	private void selectSendMsgMothed(String sendFram) {
		splits = sendFram.split(",,");
		int length = splits.length;
		switch (length) {
		case 3:
			if (splits[2].equals("12")) {
				msgToServerHelp.sendMsgToServer("11", "1", "38", "0");
			} else {
				msgToServerHelp
						.sendMsgToServer(splits[0], splits[1], splits[2]);
			}
			break;
		case 4:
			msgToServerHelp.sendMsgToServer(splits[0], splits[1], splits[2],
					splits[3]);
			break;
		case 5:
			if (splits[2].equals("28")) {
				if (splits[4].equals("1")) {
					msgToServerHelp.sendMsgToServer("11", "0", "37", "1");
				} else if (splits[4].equals("0")) {
					msgToServerHelp.sendMsgToServer("11", "0", "37", "0");
				}
			} else {
				msgToServerHelp.sendMsgToServer(splits[0], splits[1],
						splits[2], splits[3], splits[4]);
			}
			break;
		case 6:
			msgToServerHelp.sendMsgToServer(splits[0], splits[1], splits[2],
					splits[3], splits[4], splits[5]);
			break;
		}
	}

	private void showTasckIsOk(String string) {
		// TODO Auto-generated method stub
		switch (Integer.parseInt(string)) {
		case 17:
		case 20:
			layout.addLiftView("指令发送成功", false, 0);
			break;
		case 14:
			layout.addLiftView("\n妈妈在厨房\n爸爸在卧室\n", false, 0);
			break;
		}
	}
}
