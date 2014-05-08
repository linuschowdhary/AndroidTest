package com.mty.demo.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import com.mty.demo.Demo;
import com.mty.demo.R;
import com.mty.demo.constant.Constants;

public class MainActivity extends Activity {

    private GridView mGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initViews();
        initData();
    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.gridView);

        DemoAdapter adapter = new DemoAdapter();
        mGridView.setAdapter(adapter);
    }

    private void initData() {

    }

    class DemoAdapter extends BaseAdapter {
        private Demo[]  mDesignPatternsList;
        /**
         * LoD迪米特法则
         */
        private Context mContext;

        public DemoAdapter() {
            mDesignPatternsList = Demo.values();
        }

        @Override
        public int getCount() {
            return mDesignPatternsList.length;
        }

        @Override
        public Demo getItem(int position) {
            return mDesignPatternsList[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button;
            if (convertView == null) {
                if (mContext == null) {
                    mContext = parent.getContext();
                }
                button = new Button(mContext);
                button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Demo demo = (Demo) getItem((Integer) v.getTag());
                        Intent it = new Intent(mContext, DemoActivity.class);
                        it.putExtra(Constants.KEY_DEMO, demo);
                        mContext.startActivity(it);
                    }
                });
            } else {
                button = (Button) convertView;
            }

            button.setTag(position);
            Demo pattern = getItem(position);
            if (pattern != null) {
                button.setText(pattern.getName());
            }
            return button;
        }
    }

}

