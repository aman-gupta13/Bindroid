package main.bindroid.com.bindroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import main.bindroid.com.bindroid.R;


public class MainActivity extends AppCompatActivity {

	private CallbackManager callbackManager;
	private LoginButton fbLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_main);

		fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

		fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {

				Log.e("Bindroid", "Facebook Login Successful!");
				Log.e("Bindroid", "Logged in user Details : ");
				Log.e("Bindroid", "--------------------------");
				Log.e("Bindroid", "User ID  : " + loginResult.getAccessToken().getUserId());
				Log.e("Bindroid",
						"Authentication Token : " + loginResult.getAccessToken().getToken());
				Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG)
						.show();
				Log.e("Bindroid", "Facebook Login failed!!");

			}

			@Override
			public void onError(FacebookException e) {
				Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
				Log.e("Bindroid", "Facebook Login failed!!");
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent i) {
		callbackManager.onActivityResult(reqCode, resCode, i);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

}
