package com.mty.demo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mty.demo.common.assist.Averager;
import com.mty.demo.common.assist.TimeCounter;
import com.mty.demo.test.gson.Cart;
import com.mty.demo.test.gson.LineItem;

/**
 * @author matianyu
 * 
 *         2013-10-23上午11:36:34
 */
public class JsonTestFragment extends Fragment implements OnClickListener {
	EditText et;
	EditText et1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(container.getContext());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(10, 20, 10, 0);

		et = new EditText(container.getContext());
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setText("1");
		ll.addView(et);

		et1 = new EditText(container.getContext());
		et1.setInputType(InputType.TYPE_CLASS_NUMBER);
		et1.setText("1");
		ll.addView(et1);

		Button bt = new Button(container.getContext());
		bt.setText("Mock数据");
		bt.setId(100);
		ll.addView(bt);

		Button bt1 = new Button(container.getContext());
		bt1.setText("Gson性能");
		bt1.setId(0);
		ll.addView(bt1);

		Button bt2 = new Button(container.getContext());
		bt2.setText("FastJson性能");
		bt2.setId(1);
		ll.addView(bt2);

		Button bt3 = new Button(container.getContext());
		bt3.setText("Jackson性能");
		bt3.setId(2);
		ll.addView(bt3);

		bt.setOnClickListener(this);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		return ll;
	}

	TimeCounter tc = new TimeCounter();
	ArrayList<Cart> list;
	Averager ave1 = new Averager();
	Averager ave2 = new Averager();
	int c = 10;
	String json;
	@Override
	public void onClick(View v) {
		final int id = v.getId();
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				switch (id) {
					case 100 :
						int listSize = Integer.parseInt(et.getText().toString().trim());
						int listInnerSize = Integer.parseInt(et1.getText().toString().trim());

						tc.go();
						list = mockData(listSize, listInnerSize);
						long t = tc.stop();
						// ave.add(t);
						break;
					case 0 :
						
						ave1.clear();
						ave2.clear();
						Gson gson = new Gson();
						for (int k = 0; k < c; k++) {
							tc.go();
							json = gson.toJson(list);
							t = tc.stop();
							ave1.add(t);

							tc.go();
							list = gson.fromJson(json, new TypeToken<ArrayList<Cart>>() {
							}.getType());
							t = tc.stop();
							ave2.add(t);
						}
						System.out.println("测试Json数据长度： " + json.getBytes().length);
						System.out.println("\n----- Gson ---- ");
						System.out.println("对象 TO Json： " + ave1.getAverage());
						System.out.println("Json TO 对象： " + ave2.getAverage());
						break;
					case 1 :
						
						ave1.clear();
						ave2.clear();
						for (int k = 0; k < c; k++) {
							tc.go();
							json = JSON.toJSONString(list);
							t = tc.stop();
							ave1.add(t);

							tc.go();
							
							list = JSON.parseObject(json, new ArrayList<Cart>().getClass());
							t = tc.stop();
							ave2.add(t);
						}
						System.out.println("\n----- FastJson ---- ");
						System.out.println("对象 TO Json： " + ave1.getAverage());
						System.out.println("Json TO 对象： " + ave2.getAverage());
						break;
					case 2 :
						try {
							ObjectMapper maper = new ObjectMapper();
							
							ave1.clear();
							ave2.clear();
							for (int k = 0; k < c; k++) {
								tc.go();
								json = maper.writeValueAsString(list);
								t = tc.stop();
								ave1.add(t);

								tc.go();
								maper.readValue(json, new TypeReference<ArrayList<Cart>>() {
								});
								t = tc.stop();
								ave2.add(t);
							}
							System.out.println("\n----- Jackson ---- ");
							System.out.println("对象 TO Json： " + ave1.getAverage());
							System.out.println("Json TO 对象： " + ave2.getAverage());
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					default :
						break;
				}
			};
		}.start();
	}

	public ArrayList<Cart> mockData(int listSize, int listInnerSize) {
		ArrayList<Cart> list = new ArrayList<Cart>();
		for (int i = 0; i < listSize; i++) {
			List<LineItem> lineItems = new ArrayList<LineItem>();
			for (int j = 0; j < listInnerSize; j++) {
				LineItem li = new LineItem("n" + j, j, j + 1000, "c" + j);
				lineItems.add(li);
			}
			HashMap<Integer, LineItem> lineMap = new HashMap<Integer, LineItem>();
			for (int j = 0; j < listInnerSize; j++) {
				LineItem li = new LineItem("n" + j, j, j + 1000, "c" + j);
				lineMap.put(j, li);
			}
			// Cart c = new Cart(null, null, "bn " + i, "cd " + i);
			Cart c = new Cart(lineItems, lineMap, "bn " + i, "cd " + i);
			list.add(c);
		}
		return list;
	}

	public String initJson(int listSize, int listInnerSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < listSize; i++) {
			sb.append("{\"buyer\":\"bn ").append(i).append("\",\"creditCard\":\"cd ").append(i)
					.append("\",\"lineItems\":[");
			for (int j = 0; j < listInnerSize; j++) {
				if (j == listInnerSize - 1) {
					sb.append("{\"currencyCode\":\"c").append(j).append("\",\"name\":\"n").append(j)
							.append("\",\"priceInMicros\":100").append(j).append(",\"quantity\":").append(j)
							.append("}");
				} else {
					sb.append("{\"currencyCode\":\"c").append(j).append("\",\"name\":\"n").append(j)
							.append("\",\"priceInMicros\":100").append(j).append(",\"quantity\":").append(j)
							.append("},");
				}
			}
			sb.append("],\"lineMap\":{");
			for (int j = 0; j < listInnerSize; j++) {
				if (j == listInnerSize - 1) {
					sb.append("\"").append(j).append("\":{\"currencyCode\":\"c").append(j).append("\",\"name\":\"n")
							.append(j).append("\",\"priceInMicros\":100").append(j).append(",\"quantity\":").append(j)
							.append("}");
				} else {
					sb.append("\"").append(j).append("\":{\"currencyCode\":\"c").append(j).append("\",\"name\":\"n")
							.append(j).append("\",\"priceInMicros\":100").append(j).append(",\"quantity\":").append(j)
							.append("},");
					sb.append("");
				}
			}
			if (i == listSize - 1) {
				sb.append("}}");
			} else {
				sb.append("}},");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
