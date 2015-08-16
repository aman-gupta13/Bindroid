package main.bindroid.com.bindroid.activity;

import android.os.Bundle;

import main.bindroid.com.bindroid.R;
import main.bindroid.com.bindroid.networking.NetworkConstants;
import main.bindroid.com.bindroid.networking.NetworkManager;

public class ParallaxMainActivity extends BaseMaterialActivity {

	private NetworkManager networkManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		networkManager = NetworkManager.newInstance(this,
				NetworkConstants.BASE_URL);
	}

	@Override
	int getActivityContentLayout() {
		return R.layout.activity_main;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}
}
