package com.mty.demo.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mty.demo.R;

public class HttpFragment extends android.support.v4.app.Fragment implements OnClickListener {
	// public static String URL_TEST =
	// "http://developer.baidu.com/wiki/index.php?title=docs/frontia/contact";
	public static String URL_TEST = "http://www.csdn.net/";
	public static int TIME_OUT = 10000;
	HttpClient client;
	long[][] a = new long[2][20];
	LinkedHashMap<String, String> timeMap = new LinkedHashMap<String, String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// new Thread() {
		// @Override
		// public void run() {
		// try {
		// sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// int i = 0;
		// while (i < a[0].length) {
		// long s = System.currentTimeMillis();
		// System.out.println("urlConn back :  " + urlConn().length());
		// long t = System.currentTimeMillis() - s;
		// a[0][i] = t;
		// timeMap.put("urlConn", timeMap.get("urlConn") + ", "
		// + a[0][i]);
		// s = System.currentTimeMillis();
		// System.out.println("httpClient back :  "
		// + httpClient().length());
		// t = System.currentTimeMillis() - s;
		// a[1][i] = t;
		// timeMap.put("httpClient", timeMap.get("httpClient") + ", "
		// + a[1][i]);
		// i++;
		// }
		//
		// for (Entry<String, String> en : timeMap.entrySet()) {
		// System.out.println("方式" + en.getKey() + ": "
		// + en.getValue());
		// }
		// System.out.println("urlConn时间均值: " + getAverage(a[0]));
		// System.out.println("httpClient时间均值: " + getAverage(a[1]));
		// }
		// }.start();

		View v = inflater.inflate(R.layout.button_list, container);
		
//		bt = (Button) v.findViewById(R.id.bt3);
//		bt.setOnClickListener(this);

		initClient();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initClient() {
		BasicHttpParams param = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(param, TIME_OUT);
		HttpConnectionParams.setSoTimeout(param, TIME_OUT);
		client = new DefaultHttpClient(param);
	}

	public String getUrl() {
		return URL_TEST + "?t=" + System.currentTimeMillis();
	}

	public String urlConn() {
		try {
			URL url = new URL(getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String httpClient() {
		try {
			HttpGet get = new HttpGet(getUrl());
			HttpResponse res = client.execute(get);
			InputStream is = res.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString("");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
