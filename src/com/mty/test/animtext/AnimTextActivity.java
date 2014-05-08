package com.mty.test.animtext;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

import com.mty.test.animtext.core.AnimTextContainer;
import com.mty.test.animtext.core.AnimTextFactory;
import com.mty.test.animtext.core.AnimTextListener;

public class AnimTextActivity extends Activity implements AnimTextListener {
	/** Called when the activity is first created. */
	LinearLayout ll;
	AnimTextContainer container;
	ArrayList<String> list;
	LayoutParams p;
	int style = AnimTextFactory.ANIM_STYLE_MIN_NUM;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		ll = new LinearLayout(this);
		setContentView(ll);
//		ll = (LinearLayout) findViewById(R.id.main_ll_body);
		p = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		list = new ArrayList<String>();
		for (int i = 0; i < 13; i++) {
			list.add("塔防" + i);
		}

		container = new AnimTextContainer(this);
		AnimTextFactory.build(container, list, style);
		ll.addView(container, p);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	GestureDetector gd = new GestureDetector(new gestureListener());

	class gestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			System.out.println("fling");
			if (++style > AnimTextFactory.ANIM_STYLE_MAX_NUM) {
				style = AnimTextFactory.ANIM_STYLE_MIN_NUM;
			}
			ll.removeAllViews();
			container = new AnimTextContainer(AnimTextActivity.this);
			ll.addView(container, p);
			AnimTextFactory.build(container, list, style);

			return true;
		}
	}

	@Override
	public void onTextTouchEvent(int i) {
		System.out.println("touch result:" + list.get(i));
	}
}