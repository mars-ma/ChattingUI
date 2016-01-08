package dev.mxw.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public View rootView;
	public Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews();
		return rootView;
	}

	public void setRootView(View v) {
		rootView = v;
	}

	public void setRootView(int layoutId) {
		rootView = inflate(layoutId);
	}

	public View findViewById(int resId) {
		return rootView.findViewById(resId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initRQs();
	}

	public View inflate(int resId) {
		return getActivity().getLayoutInflater().inflate(resId, null);
	}

	public abstract void initViews();

	public abstract void initRQs();

}
