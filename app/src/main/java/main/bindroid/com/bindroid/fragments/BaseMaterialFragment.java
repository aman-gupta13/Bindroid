package main.bindroid.com.bindroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import main.bindroid.com.bindroid.networking.ImageRequestManager;
import main.bindroid.com.bindroid.networking.NetworkConstants;
import main.bindroid.com.bindroid.networking.NetworkManager;

/**
 * Created by amanbindlish on 01/08/15.
 */
public abstract class BaseMaterialFragment extends DialogFragment {

	/**
	 * network manager will available in every fragment, user for making network
	 * volley request
	 * **/
	private NetworkManager networkManager;
	/**
	 * menu inflater for inflating menus. we are not using Action bar and not
	 * setting tool bar into Actvity actionbar , So we have to inflate menu item
	 * ourself. But we are supporting original callback for creating and
	 * selecting menuitem.
	 * **/
	private SupportMenuInflater menuInflater;
	/**
	 * Image request manager, it will avaibale in every child class and
	 * adapters, So dev no need create a another one.
	 * */
	private ImageRequestManager imageRequestManager;
	/*
	 *
	 * Fragment view holder created after {@code onViewCreated} It will be null
	 * in onDestroyView
	 */
	private BaseFragmentViewHolder fragmentViewHolder;

	/**
	 * Fragment Life method start. **
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		networkManager = NetworkManager.newInstance(getActivity(),
				NetworkConstants.BASE_URL);
		imageRequestManager = ImageRequestManager.getInstance(getActivity());
		menuInflater = new SupportMenuInflater(getActivity());
	}

	@Override
	final public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		return inflater.inflate(getFragmentLayout(), container, false);
	}

	/**
	 * return layout id that you want show in this fragment
	 * */
	public abstract int getFragmentLayout();

	@Override
	final public void onViewCreated(View view,
			@Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// create fragment view holder.
		this.fragmentViewHolder = createFragmentViewHolder(view);
		onFragmentViewHolderCreated(this.fragmentViewHolder, savedInstanceState);
		// invalidateOptionMenu();
		// initializeToolBar();
		// if (savedInstanceState != null) {
		// onPostAnimationCleanup(null);
		// }
	}

	/***
	 * override this and return true if child is handling backpress (Toolbar and
	 * device back button)
	 * **/
	public boolean onPopBackStack() {
		return false;
	}

	/**
	 * override this for creating {@code BaseFragmentViewHolder} otherwise
	 * BaseFragmentViewHolder object will be created by default.
	 */
	public BaseFragmentViewHolder createFragmentViewHolder(View view) {
		return new BaseFragmentViewHolder(view);
	}

	public void onFragmentViewHolderCreated(BaseFragmentViewHolder viewHolder,
			@Nullable Bundle savedInstanceState) {
	}

	/**
	 * child class implement this and restore view state from savedInstance
	 * ***/
	protected abstract void onRestoreInstanceState(
			BaseFragmentViewHolder viewHolder, Bundle savedInstance);

	/***
	 * Implement this for saving current state of screen.
	 * **/
	public abstract void onSaveInstanceState(
			BaseFragmentViewHolder fragmentViewHolder, Bundle outState);

	@Override
	final public void onSaveInstanceState(Bundle outState) {
		saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	private void saveInstanceState(Bundle outState) {
		if (outState != null) {
			onSaveInstanceState(this.fragmentViewHolder, outState);
			saveHeaderValues(outState);
		}
	}

	/**
	 * Save all current header state into bundle. Title,logo,navigation
	 * icon,overflow menu status,chilFragment
	 */
	private void saveHeaderValues(Bundle outState) {
		// outState.putBoolean(SHOW_HAMBURGER_MENU, showHamburgerMenu);
		// outState.putString(TITLE, this.title);
		// outState.putBoolean(SHOW_LOGO, this.showLogo);
		// outState.putInt(NAVIGATION_ICON_REG_ID, this.navIconRegId);
		// outState.putIntArray(OVER_FLOW_HIDE_MENU_IDS, this.hideMenuItemIds);
		// outState.putBoolean(IS_CHILD_FRAGMENT, isChildFragment());
	}

	/**
	 * restore current header state from bundle. Title,logo,navigation
	 * icon,overflow menu status,chilFragment
	 */
	private void restoreHeaderValues(Bundle savedInstanceState) {

		// this.showHamburgerMenu = savedInstanceState.getBoolean(
		// SHOW_HAMBURGER_MENU, this.showHamburgerMenu);
		// this.title = savedInstanceState.getString(TITLE, title);
		// this.navIconRegId = savedInstanceState.getInt(NAVIGATION_ICON_REG_ID,
		// navIconRegId);
		// this.showLogo = savedInstanceState.getBoolean(SHOW_LOGO,
		// this.showLogo);
		//
		// if (savedInstanceState.containsKey(OVER_FLOW_HIDE_MENU_IDS)) {
		// this.hideMenuItemIds = savedInstanceState
		// .getIntArray(OVER_FLOW_HIDE_MENU_IDS);
		// }
		//
		// childFragment = savedInstanceState.getBoolean(IS_CHILD_FRAGMENT,
		// childFragment);
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public ImageLoader getImageLoader() {
		return imageRequestManager.getImageLoader();
	}

	public static class BaseFragmentViewHolder {

		private View rootView;
		private Toolbar toolbar;
		private Map<Integer, View> viewMap;
		private View scrollableContainer;
		private View hidableContainer;
		// private DrawShadowFrameLayout shadowFrameLayout;
		private ProgressBar progressBar;
		private View networkErrorView;

		public BaseFragmentViewHolder(View view) {
			this.rootView = view;
			this.viewMap = new HashMap<>();
			// toolbar = (Toolbar) getViewById(R.id.toolBar);
			// scrollableContainer = getViewById(getScrollableContainerId());
			// hidableContainer = getViewById(getHidableContainerId());
			// shadowFrameLayout = (DrawShadowFrameLayout)
			// getViewById(R.id.shadow_frame_layout);
			// progressBar = (ProgressBar)
			// view.findViewById(R.id.materialLoader);
			// networkErrorView = view.findViewById(R.id.networkErrorView);
		}

		// public int getScrollableContainerId() {
		// return R.id.toolBar;
		// }
		//
		// public int getHidableContainerId() {
		// return R.id.toolBar;
		// }
		//
		// private int getScrollableOffset() {
		// if (hidableContainer != null) {
		// return hidableContainer.getHeight();
		// }
		// return 0;
		// }
		//
		// protected int getScrollDownFromPosition() {
		// return SCROLL_DOWN_POSITION_NONE;
		// }
		//
		final public View getRootView() {
			return rootView;
		}

		//
		// final public Toolbar getToolbar() {
		// return toolbar;
		// }

		public View getViewById(int viewId) {
			View view = viewMap.get(viewId);
			if (view == null) {
				view = getRootView().findViewById(viewId);
				if (view != null) {
					this.viewMap.put(viewId, view);
				}
			}
			return view;
		}
	}

	/***
	 * Override this for cleanup,we have already handled views cleanup and
	 * network call cancel.
	 * */
	public void onDestroyFragmentViewHolder(BaseFragmentViewHolder fragmentVH) {
	}

	@Override
	final public void onDestroyView() {
		networkManager.cancel();
		onDestroyFragmentViewHolder(this.fragmentViewHolder);
		this.fragmentViewHolder = null;
		super.onDestroyView();
	}

}
