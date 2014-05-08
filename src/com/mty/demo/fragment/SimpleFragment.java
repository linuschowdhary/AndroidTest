package com.mty.demo.fragment;

import android.view.View.OnClickListener;

import com.mty.demo.common.log.Log;

public class SimpleFragment extends ButtonFragment implements OnClickListener {

	@Override
	public String getTitle() {
		return "Http连接性能测试";
	}

	@Override
	public String[] getButtonTexts() {
		return new String[]{"HttpUrlConn", "HttpClient"};
	}
	
	@Override
	public Runnable getRunnable(final int id) {
		return new Runnable(){
			@Override
			public void run() {
				switch (id) {
					case 0 :
						Log.i("0 id: "+id);
						break;
					case 1 :
						Log.i("1 id: "+id);
						
						break;

					default :
						break;
				}
			}
		};
	}

	
}
