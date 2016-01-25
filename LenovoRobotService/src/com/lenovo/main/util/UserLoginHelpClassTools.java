//package com.lenovo.main.util;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Handler;
//import android.util.Log;
//
//import com.lenovo.main.NotificationHelper;
//import com.lenovo.vcs.weaver.enginesdk.a.api.WeaverAPI;
//import com.lenovo.vcs.weaver.enginesdk.a.api.WeaverConstants;
//import com.lenovo.vcs.weaver.enginesdk.a.api.WeaverService;
//import com.lenovo.vcs.weaver.enginesdk.a.interfaces.WeaverRequestListener;
//import com.lenovo.vcs.weaver.enginesdk.a.model.WeaverRequest;
//import com.lenovo.vcs.weaver.enginesdk.b.logic.user.UserConstants;
//import com.lenovo.vcs.weaver.enginesdk.b.logic.user.json.UserLoginJsonObject;
//import com.lenovo.vcs.weaver.enginesdk.c.http.WeaverBaseAPI;
//
///**
// * 有约登录的帮助类
// * 
// * @author Administrator
// * 
// */
//@SuppressLint("HandlerLeak")
//public class UserLoginHelpClassTools {
//
//	private MyToast myToast;
//	private Context context;
//	private String userName;
//	private String userPassWorld;
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			int msgWhat = msg.what;
//			switch (msgWhat) {
//			case 1:
//				myToast.showToast(0, "登录成功");
//				break;
//			case -1:
//				// myToast.showToast(0, "登录失败");
//				break;
//			}
//		};
//	};
//
//	public UserLoginHelpClassTools(Context context) {
//
//		myToast = new MyToast(context);
//
//		this.context = context;
//	}
//
//	/**
//	 * 用户登陆的,只要传入用户名和密码即可
//	 * 
//	 * @param name
//	 * @param passWorld
//	 */
//	public void setUserNameAndPassworld(String name, String passWorld) {
//		try {
//			this.userName = name;
//			this.userPassWorld = passWorld;
//			WeaverBaseAPI.setENV(".qinyouyue");
//			WeaverRequest login = WeaverAPI.userLogin(name, passWorld, "10004",
//					"b0863fbb251746cdaa33fe09568cd1ef",
//					UserLoginHelpClassTools.this);
//			WeaverService.getInstance().dispatchRequest(login);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
//	@Override
//	public void onRequestFinshed(WeaverRequest req) {
//		try {
//			// TODO Auto-generated method stub
//			if ((UserConstants.LogicPath.LOGIN.equals(req.getURI().getPath()))) {
//				UserLoginJsonObject response = (UserLoginJsonObject) req
//						.getResponse();
//
//				if (response == null) {
//
//					return;
//				}
//
//				if (req.getResponseCode() == WeaverConstants.RequestReturnCode.OK) {
//					WeaverRequest req1 = WeaverAPI.sipSetAudioMode(
//							Integer.parseInt(response.getPhoneAbility()), null);
//					WeaverService.getInstance().dispatchRequest(req1);
//
//					// 初始化sip模块
//					WeaverRequest req2 = WeaverAPI.sipInit(context,
//							"EngineTester 1.0",
//							"com.lenovo.main.CameraActivity",
//							NotificationHelper.getCallingNotification(context),
//							this);
//					WeaverService.getInstance().dispatchRequest(req2);
//
//					// sip登录
//					String userDomain = "sip" + WeaverBaseAPI.getEnv() + ".com";
//					Log.i("SIPP", userDomain + "..............");
//					WeaverRequest req3 = WeaverAPI.sipSetAccount(userName,
//							userPassWorld, userDomain, this);
//					WeaverService.getInstance().dispatchRequest(req3);
//
//					WeaverRequest req4 = WeaverAPI.friendGetAllList(
//							response.getToken(), this);
//					WeaverService.getInstance().dispatchRequest(req4);
//					// 表示登陆成功
//					handler.sendEmptyMessage(1);
//				} else {
//					// 表示登陆失败
//					handler.sendEmptyMessage(-1);
//				}
//			}
//		} catch (Exception e) {
//
//		}
//	}
// }
