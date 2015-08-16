package main.bindroid.com.bindroid.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import main.bindroid.com.bindroid.R;
import main.bindroid.com.bindroid.fragments.BaseMaterialFragment;
import main.bindroid.com.bindroid.fragments.LeftMenuFragment;

/**
 * Created by amanbindlish on 01/08/15.
 */
public abstract class BaseMaterialActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;
	private FrameLayout leftDrawer;
	private boolean shouldFinish;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getActivityContentLayout());
		// setup navigationDrawer
		setUpNavDrawer();
		// getSupportFragmentManager().addOnBackStackChangedListener(this);
	}

	abstract int getActivityContentLayout();

	/**
	 * Setup Drawer Layout if exist *
	 */
	private void setUpNavDrawer() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_NONE);

		transaction.replace(R.id.navigationDrawer, new LeftMenuFragment(),
				"leftNavFrag");
		transaction.commit();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (mDrawerLayout == null) {
			return;
		}

		leftDrawer = (FrameLayout) findViewById(R.id.navigationDrawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
				R.color.status_bar_color));

		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				FragmentManager frag = getSupportFragmentManager();
				Fragment fragment = frag
						.findFragmentById(R.id.navigationDrawer);
				if (fragment instanceof LeftMenuFragment) {
					LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragment;
				}
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				FragmentManager frag = getSupportFragmentManager();
				Fragment fragment = frag
						.findFragmentById(R.id.navigationDrawer);
				if (fragment != null && fragment instanceof LeftMenuFragment) {
					LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragment;
				}
			}

			@Override
			public void onDrawerStateChanged(int newState) {

			}
		});
	}

	@Override
	public void onBackPressed() {
		if (closeDrawer() && !onPopBackStack()) {

			if (shouldFinish) {
				// super.onBackPressed();
				mHandler.removeCallbacks(cancelFinish);
				finish();
			} else {
				// /show f_exit message
				shouldFinish = true;
				mHandler.postDelayed(cancelFinish, 2000);
				Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private boolean closeDrawer() {
		if (mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
			return false;
		}
		return true;
	}

	private Runnable cancelFinish = new Runnable() {

		@Override
		public void run() {
			shouldFinish = false;
		}
	};

	/**
	 * @return return true if event is consumed.
	 **/
	protected boolean onPopBackStack() {

		FragmentManager fragmentManager = getSupportFragmentManager();
		// check for
		if (fragmentManager.executePendingTransactions()) {
			return true;
		}
		BaseMaterialFragment fragment = (BaseMaterialFragment) fragmentManager
				.findFragmentById(R.id.fragment_container);
		boolean isEventHandledByFragment = fragment != null
				&& fragment.onPopBackStack();
		if (fragmentManager.getBackStackEntryCount() > 1
				&& !isEventHandledByFragment) {
			fragmentManager.popBackStack();
			return true;
		}
		return isEventHandledByFragment;
	}

}
