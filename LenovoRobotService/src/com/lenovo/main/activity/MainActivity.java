package com.lenovo.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lenovo.lenovoRobotService.R;
import com.lenovo.main.MIService.MyService;

/**
 * 当前的 activity 只是用来,开启一个 service 所有交互的内容都写在, service 中.在初始化,这个activity
 * 的时候,会出现一下黑屏的现象. 是应为,要去加载 application 这个类,而这个类中初始化的东西比较多. 所以会慢点
 * 
 * @author Administrator
 */

public class MainActivity extends Activity {
	private Intent service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/**
		 * 这是用来开启我自己Service 的一个地方,该service一旦被启动,就不关闭一直运行在后台
		 * 
		 */
		service = new Intent(MainActivity.this, MyService.class);
		startService(service);

		finish();
	}
}
