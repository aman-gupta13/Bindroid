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
		// Initialize the SDK before executing any other operations,
		// especially, if you're using Facebook UI elements.
		FacebookSdk.sdkInitialize(getApplicationContext());
		// To get the hashkey for registering our app on Facebook
		BindroidUtils.getFbKeyHash(getApplicationContext());
	}

}
