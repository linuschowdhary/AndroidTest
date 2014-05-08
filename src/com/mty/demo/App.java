package com.mty.demo;

import android.app.Application;
import android.util.Log;

public class App extends Application{

	static final String TAG = "App";
	private static App app;
	
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		Log.i(TAG, "App is Created....._______________o_________o_____o______");
	}
	
	public static App getApp(){
		return app;
	}
}
