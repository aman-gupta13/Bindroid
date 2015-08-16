package main.bindroid.com.bindroid.fragments;

import android.os.Bundle;

import main.bindroid.com.bindroid.R;

/**
 * Created by amanbindlish on 16/08/15.
 */
public class LeftMenuFragment extends BaseMaterialFragment {

	@Override
	public int getFragmentLayout() {
		return R.layout.fragment_left_menu;
	}

	@Override
	protected void onRestoreInstanceState(BaseFragmentViewHolder viewHolder,
			Bundle savedInstance) {

	}

	@Override
	public void onSaveInstanceState(BaseFragmentViewHolder fragmentViewHolder,
			Bundle outState) {

	}
}
