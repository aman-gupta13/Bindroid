package main.bindroid.com.bindroid.helper;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by amanbindlish on 16/07/15.
 */
public class NetworkManager
		implements
			RequestFilter,
			Listener<Response>,
			ErrorListener {

	private static NetworkManager mInstance;
	private RequestQueue mRequestQueue;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public static synchronized NetworkManager getNetworkManager() {
		return mInstance;
	}

	public static NetworkManager newInstance(Context context) {
		NetworkManager instance = new NetworkManager(context);
		return instance;
	}

	private NetworkManager(Context context) {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context, null,
					Volley.DEFAULT_CACHE_DIR, 3, true, false);
			mRequestQueue.start();
		}
	}

	public Request<?> jsonRequestGet(int identifier, String urlPath,
			Map<String, String> requestMap, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		String url = generateGetUrl(urlPath, requestMap);
		Log.d("Test", "Inside generate reques " + url);
		return jsonRequest(identifier, url, listener, errorListener,
				shouldCache);
	}

	// Used for PowerReview API request
	public Request<?> jsonRequest(int identifier, String url,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		JsonObjectRequest request = new JsonObjectRequest(url, null, listener,
				errorListener);
		return jsonRequest(request, identifier, null, shouldCache);
	}

	public Request<?> jsonRequest(JsonObjectRequest request, int identifier,
			Map<String, String> requestMap, boolean shouldCache) {
		request.setIdentifier(identifier);
		request.setShouldCache(shouldCache);
		request.setTag(this);
		// request.setHeaders(headers);
		request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 2,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		addRequest(request);
		return request;
	}

	public static String generateGetUrl(String url, Map<String, String> params) {
		Builder uriBuilder = Uri.parse(url).buildUpon();

		if (params != null) {
			Set<String> keySet = new TreeSet<>(params.keySet());
			for (String key : keySet) {
				String value = params.get(key);
				if (TextUtils.isEmpty(value)) {
					value = "";
				}
				uriBuilder.appendQueryParameter(key, value);
			}
		}
		// uriBuilder.appendQueryParameter(APP_VERSION, appVersionName);
		return uriBuilder.toString();
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public void addRequest(final Request<?> request) {
		if (request.hasHadResponseDelivered()) {
			throw new UnsupportedOperationException(
					"Cannot reuse Request which has already served the request");
		}
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				getRequestQueue().add(request);
			}
		});
	}

	@Override
	public boolean apply(Request<?> request) {
		// TODO Auto-generated method stub
		return request.getTag() == this;
	}

	@Override
	public void onErrorResponse(Request request, VolleyError error) {

	}

	@Override
	public void onResponse(Request<Response> request, Response responseObject,
			Response<Response> response) {

	}
}
