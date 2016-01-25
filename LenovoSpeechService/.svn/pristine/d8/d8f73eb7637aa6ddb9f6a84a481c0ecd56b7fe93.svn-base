package cn.com.lenovo.speechservice.receiver;

import cn.com.lenovo.speechservice.service.SpeechService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;

public class MySpeechSwitchBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub speechFlag
		int speechFlag = intent.getIntExtra("speechFlag", 0);
		switch (speechFlag) {
		case 1:
			SpeechService.handler.sendEmptyMessage(1);
			break;
		case 2:
			SpeechService.handler.sendEmptyMessage(2);
			break;
		}
	}

}
