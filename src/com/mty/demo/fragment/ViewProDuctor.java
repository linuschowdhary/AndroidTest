package com.mty.demo.fragment;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ViewProDuctor {
	public static View getView(final Context context) {
		Button bt = new Button(context);
		bt.setText("电放费");
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "这段代码来自外部", 0).show();
			}
		});
		return bt;
	}

	public void a() {
	}
}