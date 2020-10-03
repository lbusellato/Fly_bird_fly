package com.busedc.flybirdfly;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

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

		initialize(new MainMenu(), config);
		//initialize(new Game(), config);
	}
}
