package com.mty.demo.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.mty.demo.Demo;
import com.mty.demo.R;
import com.mty.demo.act.pullrefresh.ScrollViewActivity;
import com.mty.demo.constant.Constants;
import com.mty.demo.fragment.*;
import com.mty.demo.utils.AppUtil;
import com.mty.demo.utils.DexUtil;

public class DemoActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_demol);
		Object obj = getIntent().getSerializableExtra(Constants.KEY_DEMO);
		if (obj != null) {
			Demo demo = (Demo) obj;
			Fragment fragment = handleDemo(demo);
			if (fragment != null) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.content, fragment).commit();
			} else {
				Toast.makeText(this, "无Fragment", 0).show();
				finish();
			}
		}
	}

	private Fragment handleDemo(Demo demo) {
		switch (demo) {
			case DanymicView :
				return new DynamicFrag();
			case Scheme :
				// AppUtil.startOtherApp(this);
				AppUtil.startAppByPkg(this, "com.taobao.ju.android");
				break;
			case LoadAllClass :
				DexUtil.loadAllClass(this, "com.mty");
				break;
			case NumLock :
				return new NumLockFragment();
			case Http :
				return new HttpTestFragment();
			case MusicPlayer :
				return null;
			case JsonTest :
				return new JsonTestFragment();
			case SwipeRefresh:
                return new SwipeRefreshFragment();
            case PullToRefresh:
                startActivity(new Intent(this, ScrollViewActivity.class));
                break;
			case Adapter :

				break;
			case Bridge :

				break;
			case Builder :

				break;
			case ChainOfResponsibility :

				break;

			default :
				return null;
		}
		return null;
	}

}