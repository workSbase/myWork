package com.lenovo.lenovorobotmobile.utils;

import java.util.regex.Pattern;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.lenovorobot.R;
import com.lenovo.lenovorobotmobile.speech.TextPaser;
import com.lenovo.lenovorobotmobile.speech.TextPaserUs_en;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * 聊天界面中,英文内容的处理类
 * 
 * @author Administrator
 * 
 */
public class EnglishUtils {

	private MoTransport moTransport;
	private ChatsContentShowTools layout;
	private EditText contentShowEditText;
	private ServerUtils msgToServerHelp;
	private Context context;
	private String sendFram = null;

	public EnglishUtils(Context context, MoTransport moTransport,
			ChatsContentShowTools layout, TextPaser textPaser,
			EditText contentShowEditText, ServerUtils msgToServerHelp) {
		this.moTransport = moTransport;
		this.layout = layout;
		this.contentShowEditText = contentShowEditText;
		this.msgToServerHelp = msgToServerHelp;
		this.context = context;
	}

	/**
	 * 消息处理的方法
	 * 
	 * @param content
	 */
	public void disposeMsg(final String contentText) {
		if (contentText != null && contentText.equals("")) {
			MToast.showToast(context, "Please enter the content", 0);
			return;
		}
		if (contentText.equals("family heat map")
				|| contentText.equals("Family heat map")) {
			layout.addReightView(contentText);
			layout.addLiftView("", true, R.drawable.heartmap_);
			contentShowEditText.setText("");
		} else if (contentText.equals("go to living room")
				|| contentText.equals("Go to living room")
				|| contentText.equals("go to bathroom")
				|| contentText.equals("Go to bathroom")) {
			layout.addReightView(contentText);
			msgToServerHelp.sendMsgToServer("21", "2", "20", "4.0", "0.0",
					"0.0");
			layout.addLiftView("OK", false, 0);
			contentShowEditText.setText("");
		} else if (contentText.equals("go to bed room")
				|| contentText.equals("Go to bed room")
				|| contentText.equals("go to bedroom")
				|| contentText.equals("Go to bedroom")) {
			layout.addReightView(contentText);
			msgToServerHelp.sendMsgToServer("21", "2", "20", "0.0", "1.0",
					"0.0");
			layout.addLiftView("OK", false, 0);
			contentShowEditText.setText("");
		} else if (contentText.equals("go to kitchen")
				|| contentText.equals("Go to kitchen")) {
			layout.addReightView(contentText);
			msgToServerHelp.sendMsgToServer("21", "2", "20", "-2.6", "0.0",
					"0.0");
			layout.addLiftView("OK", false, 0);
			contentShowEditText.setText("");
		} else if (contentText.equals("anybody at home")
				|| contentText.equals("Anybody at home")) {
			layout.addReightView(contentText);
			layout.addLiftView("father in kitchen \n mother in living room",
					false, 0);
			contentShowEditText.setText("");
		} else if (contentText.equals("family habit")
				|| contentText.equals("Family habit")) {
			layout.addReightView(contentText);
			layout.addLiftView("father in kitchen \n mother in living room",
					true, R.drawable.robotbitmap);
			contentShowEditText.setText("");
		} else if (contentText.equals("turn off the light")
				|| contentText.equals("Turn off the light")) {

			layout.addReightView(contentText);
			layout.addLiftView("Ok", false, 0);
			msgToServerHelp.sendMsgToServer("21", "2", "37", "-2.6", "0");

			contentShowEditText.setText("");
		} else if (contentText.equals("turn on the light")
				|| contentText.equals("Turn on the light")) {
			layout.addReightView(contentText);
			layout.addLiftView("Ok", false, 0);
			msgToServerHelp.sendMsgToServer("21", "2", "37", "-2.6", "1");

			contentShowEditText.setText("");
		} else if (contentText.equals("Start dance")
				|| contentText.equals("start dance")) {

			layout.addReightView(contentText);
			layout.addLiftView("Ok", false, 0);
			msgToServerHelp.sendMsgToServer("21", "2", "39", "0");

			contentShowEditText.setText("");

		} else if (contentText.equals("Stop dance")
				|| contentText.equals("stop dance")) {
			layout.addReightView(contentText);
			layout.addLiftView("Ok", false, 0);
			msgToServerHelp.sendMsgToServer("21", "2", "39", "1");
			contentShowEditText.setText("");
		} else if (contentText.equals("Go to dock")
				|| contentText.equals("go to dock")) {
			layout.addReightView(contentText);
			layout.addLiftView("Ok", false, 0);
			msgToServerHelp.sendMsgToServer("21", "2", "39", "2");

			contentShowEditText.setText("");
		} else if (contentText.equals("dump energy")
				|| contentText.equals("Dump energy")) {
			layout.addReightView(contentText);
			msgToServerHelp.sendMsgToServer("21", "2", "38", "0");
			contentShowEditText.setText("");

		} else {
			new TextPaserUs_en(context) {

				@Override
				public void result(String fram, String error) {
					if (null != error) {
						return;
					}
					// sendFram = fram;
					sendFram = fram;
					getFram(contentText, "Tom");
				}
			}.getSendMessage(contentText);
		}
	}

	private void getFram(String contString, String name) {

		if (sendFram == null || sendFram.equals("")) {
			Toast.makeText(context, "请输入符合规范的内容", Toast.LENGTH_SHORT).show();
			return;
		}
		String[] split = sendFram.split(",,");
		MoPacket pack = new MoPacket();
		for (int x = 0; x < split.length; x++) {
			if (isNumeric(split[x])) {
				pack.pushInt32(Integer.parseInt(split[x]));
			} else if (isDouble(split[x])) {
				pack.pushInt32(Integer.parseInt(split[x]));
			} else {
				pack.pushString(split[x], "utf-8");
			}
		}
		moTransport.sendPacket(pack);
		pack = null;
		Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
		if (contString.length() > 0) {
			layout.addReightView(contString);
			contentShowEditText.setText("");
		}
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-//+]?//d+(//.//d*)?|//.//d+$");
		return pattern.matcher(str).matches();

	}
}
