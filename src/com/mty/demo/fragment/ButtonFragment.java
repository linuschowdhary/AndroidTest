package com.mty.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mty.demo.R;

public abstract class ButtonFragment extends android.support.v4.app.Fragment implements OnClickListener {

	/**
	 * 取标题
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * 取button描述
	 * @return
	 */
	public abstract String[] getButtonTexts();

	/**
	 * 取运行函数，这里一般为switch语句，若getButtonTexts有len个，则case(id)从0,1,2到len-1.
	 * @param id
	 * @return
	 */
	public abstract Runnable getRunnable(int id);
	private TextView mTvSubTitle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LinearLayout vg = (LinearLayout) inflater.inflate(R.layout.button_list, null);
		TextView tv = (TextView) vg.findViewById(R.id.title);
		tv.setText(getTitle());
		mTvSubTitle = (TextView) vg.findViewById(R.id.sub_title);
		
		String[] bttxt = getButtonTexts();
		if(bttxt != null){
			for(int i = 0; i < bttxt.length; i++){
				Button bt = new Button(getActivity());
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(20, 10, 20, 10);
				bt.setId(i);
				bt.setText(bttxt[i]);
				bt.setOnClickListener(this);
				bt.setLayoutParams(lp);
				vg.addView(bt);
			}
		}
		container.addView(vg);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		new Thread(getRunnable(v.getId())).start();
	}
	
	public void setSubTitile(String st) {
		mTvSubTitle.setText(st);
	}
	
}
