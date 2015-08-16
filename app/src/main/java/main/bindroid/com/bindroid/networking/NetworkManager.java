package main.bindroid.com.bindroid.networking;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.GlobalRequestQueueListener;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by amanbindlish on 16/07/15.
 */
public class NetworkManager
		implements
			RequestFilter,
			GlobalRequestQueueListener<Object> {

	private static NetworkManager mInstance;
	private RequestQueue mRequestQueue;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private String BASE_URL;

	public static synchronized NetworkManager getNetworkManager() {
		return mInstance;
	}

	public static NetworkManager newInstance(Context context, String baseUrl) {
		NetworkManager instance = new NetworkManager(context, baseUrl);
		return instance;
	}

	private NetworkManager(Context context, String baseUrl) {
		if (mRequestQueue == null) {
			BASE_URL = TextUtils.isEmpty(baseUrl)
					? NetworkConstants.BASE_URL
					: baseUrl;
			mRequestQueue = Volley.newRequestQueue(context, null,
					Volley.DEFAULT_CACHE_DIR, 3, true, false);
			mRequestQueue.setGlobalRequestQueueListener(this);
			mRequestQueue.start();
		}
	}

	public Request<?> jsonRequestGet(int identifier, Class cls, String urlPath,
			Map<String, String> requestMap, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		String url = generateGetUrl(urlPath, requestMap);
		Log.d("Test", "Inside generate reques " + url);
		return jsonRequest(identifier, cls, url, listener, errorListener,
				shouldCache);
	}

	public Request<?> jsonRequestPost(int identifier, Class cls, String url,
			Map<String, String> requestMap, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(
				requestMap);

		if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
			url = BASE_URL + url;
		}
		// JsonObjectRequest request = new JsonObjectRequest(url, jsonRequest,
		// listener, errorListener);
		GsonRequest gsonRequest = new GsonRequest(url, cls, requestMap,
				listener, errorListener);
		return jsonRequest(gsonRequest, identifier, requestMap, shouldCache);
	}

	// Used for PowerReview API request
	public Request<?> jsonRequest(int identifier, Class cls, String url,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		// JsonObjectRequest request = new JsonObjectRequest(url, null,
		// listener,
		// errorListener);
		GsonRequest gsonRequest = new GsonRequest(url, cls, null, listener,
				errorListener);
		return jsonRequest(gsonRequest, identifier, null, shouldCache);
	}

	public Request<?> jsonRequest(GsonRequest request, int identifier,
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

	public Request<?> stringRequest(StringRequest request, int identifier,
			Map<String, String> header, boolean shouldCache) {
		request.setIdentifier(identifier);
		request.setShouldCache(shouldCache);
		request.setTag(this);
		// request.setHeaders((header != null) ? header :
		// NetworkManager.headers);
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

	public Request<?> stringRequest(int identifier, String url,
			Map<String, String> header, Listener<String> listener,
			ErrorListener errorListener, boolean shouldCache) {
		if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
			url = BASE_URL + url;
		}
		StringRequest request = new StringRequest(url, listener, errorListener);
		return stringRequest(request, identifier, header, shouldCache);
	}

	public void addRequest(final Request<?> request) {
		if (!request.isDeleteCache() && request.hasHadResponseDelivered()) {
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

	public void cancel() {
		getRequestQueue().cancelAll(this);
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
	public void onResponse(Request<Object> request, Object responseObject,
			Response<Object> response) {
		final long startTimeInMillis = response.getStartTimeInMillis();
		final long endTimeInMillis = response.getEndTimeInMillis();
		if (startTimeInMillis > 0 && endTimeInMillis > 0) {
			long diff = (endTimeInMillis - startTimeInMillis);
			Log.d("NetworkManager", "response time taken: " + diff);
		}
	}

	public class GsonRequest<T> extends Request<T> {

		private final Gson gson = new Gson();
		private final Class<T> clazz;
		private final Map<String, String> headers;
		private final Response.Listener<T> listener;

		public GsonRequest(String url, Class<T> clazz,
				Map<String, String> headers, Response.Listener<T> listener,
				Response.ErrorListener errorListener) {
			super(Method.GET, url, errorListener);
			this.clazz = clazz;
			this.headers = headers;
			this.listener = listener;
		}

		@Override
		protected Response<T> parseNetworkResponseUnpacked(
				NetworkResponse response) {
			try {
				String json = new String(response.networkData,
						HttpHeaderParser.parseCharset(response.headers));
				return Response.success(gson.fromJson(json, clazz),
						HttpHeaderParser.parseCacheHeaders(response));
			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError());
			} catch (JsonSyntaxException e) {
				return Response.error(new ParseError());
			}
		}

		@Override
		protected void deliverResponse(Request<T> request, T response,
				Response<T> fullResponse) {
			listener.onResponse(request, response, fullResponse);
		}
	}
}
