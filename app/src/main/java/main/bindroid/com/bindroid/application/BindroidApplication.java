package main.bindroid.com.bindroid.application;

import android.app.Application;

import com.facebook.FacebookSdk;

import main.bindroid.com.bindroid.utils.BindroidUtils;

/**
 * Created by amanbindlish on 15/08/15.
 */
public class BindroidApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FacebookSdk.sdkInitialize(getApplicationContext());
		BindroidUtils.getFbKeyHash(getApplicationContext());
	}

}
