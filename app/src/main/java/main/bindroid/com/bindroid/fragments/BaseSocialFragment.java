package main.bindroid.com.bindroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by amanbindlish on 16/08/15.
 */
public abstract class BaseSocialFragment extends BaseMaterialFragment {

	private CallbackManager callbackManager;
	private LoginButton fbLoginButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		callbackManager = CallbackManager.Factory.create();

		// fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
		LoginManager.getInstance().logInWithReadPermissions(
				getActivity(),
				Arrays.asList("user_friends", "user_birthday", "user_location",
						"user_likes", "user_photos"));
		fbLoginButton.registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult loginResult) {
						GraphRequest request = GraphRequest.newMeRequest(
								AccessToken.getCurrentAccessToken(),
								new GraphJSONObjectCallback() {

									@Override
									public void onCompleted(
											JSONObject jsonObject,
											GraphResponse graphResponse) {
										Log.d("Bindroid", "Graph JsonObject "
												+ jsonObject.toString());

									}
								});
						Bundle bundle = new Bundle();
						bundle.putString("fields", "id,name,link,picture");
						request.setParameters(bundle);
						request.executeAsync();
						Log.e("Bindroid", "Facebook Login Successful!");
						Log.e("Bindroid", "Logged in user Details : ");
						Log.e("Bindroid", "--------------------------");
						Log.e("Bindroid", "User ID  : "
								+ loginResult.getAccessToken().getUserId());
						Log.e("Bindroid", "Authentication Token : "
								+ loginResult.getAccessToken().getToken());
						Toast.makeText(getActivity(), "Login Successful!",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCancel() {
						Toast.makeText(getActivity(),
								"Login cancelled by user!", Toast.LENGTH_LONG)
								.show();
						Log.e("Bindroid", "Facebook Login failed!!");

					}

					@Override
					public void onError(FacebookException e) {
						Toast.makeText(getActivity(), "Login unsuccessful!",
								Toast.LENGTH_LONG).show();
						Log.e("Bindroid", "Facebook Login failed!!");
					}
				});
	}

	protected void loginWithFacebook(){

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(getActivity());
	}
}
