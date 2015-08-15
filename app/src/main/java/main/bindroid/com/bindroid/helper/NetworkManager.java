package main.bindroid.com.bindroid.helper;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by amanbindlish on 16/07/15.
 */
public class NetworkManager extends Application {

	private static NetworkManager mInstance;
	private RequestQueue mRequestQueue;

	public static synchronized NetworkManager getNetworkManager() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(mInstance);
		}
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public void addRequest(Request request) {
		getRequestQueue().add(request);
	}

}
