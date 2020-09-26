package com.busedc.flybirdfly;

import android.app.ActionBar;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.busedc.flybirdfly.Game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Save battery life
		config.useAccelerometer = false;
		config.useCompass = false;
		//Enforce full screen
		config.useImmersiveMode = true;

		int width = 1440, height = 2560;

		initialize(new Game(width, height), config);
	}

	//Used to keep the screen on while debugging
	@Override protected void onResume() {
		super.onResume();
		if (BuildConfig.DEBUG) { // don't even consider it otherwise
			if (Debug.isDebuggerConnected()) {
				Log.d("SCREEN", "Keeping screen on for debugging");
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				Log.d("SCREEN", "Keeping screen on for debugging is now deactivated.");
			}
		}
	}
}
