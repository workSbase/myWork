package cn.com.lenovo.speechservice.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;
import cn.com.lenovo.speechservice.domain.ResultBin;
import cn.com.lenovo.speechservice.service.SpeechService;

class KeyWord {
	public String key;
	public String SecKey;
	public String judgeKey;
	public float sc1;
	public float sc2;
	public String sendCMD;
	public int nHandleFuncIdx;
	public int nBroadCastFlag;
}

/**
 * 对关键词处理的类,广播全部发送到,底层的service中进行处理
 * @author alex
 *
 */
public class KeyWordString {

	static int mWordNum = 100;

	static KeyWord[] mWordArray = new KeyWord[mWordNum];

	static int mLastIdx;
	static int mSpcWordStartIdx = 0;
	static int mSpcWordEndIdx;
	static int mOneWordStartIdx;
	static int mOneWordEndIdx;
	static int mMulWordStartIdx;
	static int mMulWordEndIdx;
	static int mWordType;

	KeyWordString() {

	}

	//nType 0:special word, 1 one word, 2 mul word
	static int SetWordArray(int nType, String key, String SecKey, String judgeKey, float sc1, float sc2, String sendCMD, int nHandleFuncIdx, int nBroadCastFlag) {
		int nIdx = 0;
		if (nType == 0) {
			nIdx = mSpcWordEndIdx;
			mSpcWordEndIdx++;
		} else if (nType == 1) {
			nIdx = mOneWordEndIdx;
			mOneWordEndIdx++;
		} else if (nType == 2) {
			nIdx = mMulWordEndIdx;
			mMulWordEndIdx++;
		}
		mWordArray[nIdx] = new KeyWord();

		mWordArray[nIdx].key = key;
		mWordArray[nIdx].SecKey = SecKey;
		mWordArray[nIdx].judgeKey = judgeKey;
		mWordArray[nIdx].sc1 = sc1;
		mWordArray[nIdx].sc2 = sc2;
		mWordArray[nIdx].sendCMD = sendCMD;
		mWordArray[nIdx].nHandleFuncIdx = nHandleFuncIdx;
		mWordArray[nIdx].nBroadCastFlag = nBroadCastFlag;

		return 0;
	}

	public static int initWordArray() {
		mSpcWordStartIdx = 0;
		mSpcWordEndIdx = 0;
		SetWordArray(0, "是的", "", "", 30, 1000, "", 0, 0);
		SetWordArray(0, "不要", "", "", 50, 1000, "", 0, 0);

		mOneWordStartIdx = mSpcWordEndIdx;
		mOneWordEndIdx = mSpcWordEndIdx;
		SetWordArray(1, "返回", "", "要返回主菜单吗", 50, 30, "", 101, 0);
		SetWordArray(1, "功能返回", "", "要返回主菜单吗", 50, 30, "", 101, 0);
		SetWordArray(1, "跟我走", "", "您是要我跟你走吗", 50, 40, "12", 0, 0);
		SetWordArray(1, "别跟了", "", "不要我跟随您了吗", 50, 30, "13", 0, 0);
		SetWordArray(1, "跳个舞", "", "您是想看一段机器舞吗", 50, 40, "22", 0, 0);
		SetWordArray(1, "结束跳舞", "", "这么好看的机器舞您不看了吗", 50, 30, "23", 0, 0);
		SetWordArray(1, "开始播放", "", "您是想开始播放视频吗", 50, 40, "0", 0, 1);
		SetWordArray(1, "上一条", "", "您是想播放上一条视频吗", 50, 30, "1", 0, 1);
		SetWordArray(1, "下一条", "", "您是想播放下一条视频吗", 50, 30, "2", 0, 1);
		SetWordArray(1, "开始找人", "", "您是要开始找人吗", 50, 30, "27", 0, 0);
		SetWordArray(1, "人脸随动", "", "您想人脸随动吗", 50, 30, "28", 0, 0);
		SetWordArray(1, "结束随动", "", "您想结束人脸随动吗", 50, 30, "29", 0, 0);
		mMulWordStartIdx = mOneWordEndIdx;
		mMulWordEndIdx = mOneWordEndIdx;

		SetWordArray(2, "打开", "投影", "您是要我打开投影吗", 50, 40, "5", 0, 0);
		SetWordArray(2, "关闭", "投影", "您是要我关闭投影吗", 50, 40, "8", 0, 0);
		SetWordArray(2, "播放", "视频", "是否为您播放视频", 50, 40, "11", 0, 0);
		SetWordArray(2, "测量", "身高", "您是要测量身高吗", 50, 40, "1", 0, 0);
		SetWordArray(2, "检测", "身高", "您是要测量身高吗", 50, 40, "1", 0, 0);
		SetWordArray(2, "测量", "心率", "您是要测量心率吗", 50, 40, "3", 0, 0);
		SetWordArray(2, "检测", "心率", "您是要测量心率吗", 50, 40, "3", 0, 0);
		SetWordArray(2, "识别", "人脸", "您是要让我认识您吗", 50, 40, "2", 0, 0);
		SetWordArray(2, "检测", "人脸", "您是要让我认识您吗", 50, 40, "2", 0, 0);
		SetWordArray(2, "打开", "电灯", "您是要我打开电灯吗", 50, 40, "6", 0, 0);
		SetWordArray(2, "关闭", "投影", "您是要我关闭电灯吗", 50, 40, "7", 0, 0);
		SetWordArray(2, "打开", "视频通话", "您是要我打开视频通话吗", 50, 40, "4", 0, 0);
		SetWordArray(2, "你能", "做什么", "您是问我能干什么吗", 50, 40, "0", 0, 0);
		SetWordArray(2, "你能", "干什么", "您是问我能干什么吗", 50, 40, "0", 0, 0);

		return 0;
	}

	private static void setYes(Context context) {
		if (mLastIdx >= 0) {
			sendBroadCast(context, mWordArray[mLastIdx].sendCMD, mWordArray[mLastIdx].nBroadCastFlag);
		}
	}

	private static boolean sendBroadCast(Context context, String key, int index) {
		Toast.makeText(context, "发送成功  : " + key, Toast.LENGTH_SHORT).show();
		if (index == 0) {
			Intent intent = new Intent("com.lenvovo.YY");
			intent.putExtra("key", key);
			context.sendBroadcast(intent);
		} else {
			Intent intent = new Intent("com.lenvovo.Video");
			intent.putExtra("key", key);
			context.sendBroadcast(intent);
		}
		return true;
	}

	private static void speakingText(String text, int nIdx) {
		mLastIdx = nIdx;
		SpeechService.handler.sendEmptyMessage(1);
		SpeechService.compound.speaking(text);
	}

	public static boolean handleWord(Context context, ResultBin fromJson, float sc, int nIdx) {
		if (sc > mWordArray[nIdx].sc1) {
			mLastIdx = -1;
			if (mWordArray[nIdx].nHandleFuncIdx == 101) {
				PackageManager packageManager = context.getPackageManager();
				// 获取到打开一个应用程序的意图
				Intent intentForPackage = packageManager.getLaunchIntentForPackage("cn.com.lenovo.homepager");
				if (null != intentForPackage) {
					// 打开程序
					context.startActivity(intentForPackage);
				}
			} else {
				sendBroadCast(context, mWordArray[nIdx].sendCMD, mWordArray[nIdx].nBroadCastFlag);
			}

		} else if (sc > mWordArray[nIdx].sc2 && sc <= mWordArray[nIdx].sc1) {
			//speakingText(mWordArray[nIdx].judgeKey, nIdx);
		}
		return true;
	}

	public static boolean handleKeyWord(Context context, ResultBin fromJson, float sc) {
		int i = 0;
		if (fromJson != null) {
			if (fromJson.ws.size() == 1) {
				String result = fromJson.ws.get(0).cw.get(0).w;
				if (result.equals(mWordArray[0].key)) {
					if (sc > mWordArray[0].sc1) {
						setYes(context);
						return true;
					}
				} else if (result.equals(mWordArray[1].key)) {
					if (sc > mWordArray[1].sc1) {
						SpeechService.compound.speaking("随时准备着,有事您吩咐");
						return true;
					}
				}
				for (i = mOneWordStartIdx; i < mOneWordEndIdx; i++) {
					if (result.equals(mWordArray[i].key)) {
						handleWord(context, fromJson, sc, i);
						return true;
					}
				}
			} else if (fromJson != null && fromJson.ws.size() > 1) {
				String result1 = fromJson.ws.get(0).cw.get(0).w;
				String result2 = fromJson.ws.get(1).cw.get(0).w;

				for (i = mMulWordStartIdx; i < mMulWordEndIdx; i++) {
					if (result1.equals(mWordArray[i].key) && result2.equals(mWordArray[i].SecKey)) {
						handleWord(context, fromJson, sc, i);
						return true;
					}
				}
			}
		}
		return true;
	}
}
