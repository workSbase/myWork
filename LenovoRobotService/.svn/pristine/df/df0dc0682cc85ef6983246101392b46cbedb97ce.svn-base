package com.lenovo.main.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	private  Context context;
	public MyToast(Context context){
		this.context = context;
	}
	public  void showToast(int i,String ste){
		if(PublicData.TOASTFLAG){
			if(i == 1){
				Toast.makeText(context, ste, Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(context, ste, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
