package com.mty.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mty.demo.R;
import com.mty.demo.view.TouchLockView;

/**
 * @author matianyu
 *
 * 2013-10-23上午11:36:34
 */
public class NumLockFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflater.inflate(R.layout.frag_touch_lock, container);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		TouchLockView mTouchView = (TouchLockView) getActivity().findViewById(R.id.touch_lock_view);
		mTouchView.setBackgroundResource(android.R.color.transparent);
		super.onActivityCreated(savedInstanceState);
	}
}
