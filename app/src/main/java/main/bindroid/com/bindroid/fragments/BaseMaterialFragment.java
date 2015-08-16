package main.bindroid.com.bindroid.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

import main.bindroid.com.bindroid.helper.NetworkManager;

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
	 * Fragment Life method start. **
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		networkManager = NetworkManager.newInstance(getActivity());
		imageRequestManager = ImageRequestManager.getInstance(getActivity());
		menuInflater = new SupportMenuInflater(getActivity());
	}

	protected abstract int getfragmentLayout();

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
}
