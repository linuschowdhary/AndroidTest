package com.mty.demo.fragment;

import android.os.Bundle;
import android.view.View.OnClickListener;
import com.mty.demo.common.assist.Averager;
import com.mty.demo.common.assist.TimeCounter;
import com.mty.demo.common.log.Log;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpTestFragment extends ButtonFragment implements OnClickListener {
	// public static String URL_TEST =
	// "http://developer.baidu.com/wiki/index.php?title=docs/frontia/contact";
	// public static String URL_TEST = "http://www.csdn.net/";
	// public static String URL_TEST = "http://www.baidu.com/s?wd=it";
	// public static String URL_TEST = "https://www.google.com.hk/search?q=it";
	// public static String URL_TEST = "http://www.brnvc.com/";
	// public static String URL_TEST =
	// "http://www.ithome.com/html/iphone/65982.htm";
	public static String URL_TEST = "https://www.alipay.com";
	// public static String URL_TEST = "http://ma.m.taobao.com/hzuhb";
	public static int TIME_OUT = 10000;
	public static int COUNT = 60;
	public static int BUFF_SIZE = 1024;
	private HttpClient client;
	private OkHttpClient okClient;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		BasicHttpParams param = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(param, TIME_OUT);
		HttpConnectionParams.setSoTimeout(param, TIME_OUT);
		client = new DefaultHttpClient(param);
		okClient = new OkHttpClient();
		// trustAllHosts();
		//com.mty.demo.http.HttpClient hc = new ApacheHttpClient();
//		hc.get(req);
	}
	class input {
		private final int I = 1;
		private String s;
		private Long l = 100L;
		private HttpParamBuilder data = new HttpParamBuilder() {
			
			@Override
			public String buildParams() {
				return "{\"p1\":\""+s+"\",\"p2\":"+l+"}";
			}
		};
	}
	
//	@Target(ElementType.FIELD)
//	@Retention(RetentionPolicy.RUNTIME)
//	@interface CustomBuild{
//		public String build();
//	}
	
	interface HttpParamBuilder{
		public String buildParams();
	}
	
	@Override
	public String getTitle() {
		return "Http连接性能测试";
	}

	@Override
	public String[] getButtonTexts() {
		return new String[]{"HttpUrlConn", "HttpClient", "ClientPool", "okhttp", "My HttpClient"};
	}

	String s = "";
	@Override
	public Runnable getRunnable(final int id) {
		return new Runnable() {
			@Override
			public void run() {
				final Averager av = new Averager();
				final CountDownLatch latch = new CountDownLatch(COUNT);
				TimeCounter counter = new TimeCounter();
				counter.go();
				final AtomicInteger ai = new AtomicInteger(20);
				for (int i = 0; i < COUNT; i++) {
//					new Thread() {
//						public void run() {
							TimeCounter tc = new TimeCounter();
							tc.go();
							switch (id) {
								case 0 :
									s = urlConn();
									break;
								case 1 :
									s = httpClient();
									break;
								case 2 :
									//s = httpClientPool();
									break;
								case 3 :
									s = okhttp();
									break;
								case 4 :
									s = myHttpClient();
									break;
							}
							av.add(tc.stop());
							// Log.v("HttpTest","0 id: " + s );
							int len = s.length();
							if (len == 0) {
								ai.addAndGet(1);
							}
							Log.d("HttpTest", "len : " + len);
							latch.countDown();
						};
//					}.start();
//				}
				try {
					latch.await();
					long t = counter.stop();
					Log.i("HttpTest", "total time : " + t);
					String str = getButtonTexts()[id] + ": " + COUNT + "次,内容长度：" + s.length() + ", 时间均值:"
							+ av.getAverage();
					Log.i("HttpTest", str);
					str += av.print();
					final String s = str;
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							setSubTitile(s);
						}
					});
				} catch (InterruptedException e) {
					Log.d("HttpTest", "Latch Error ");
					e.printStackTrace();
				}
				// ta.print();
				// ta.clear();
			}
		};
	}

	public String getUrl() {
		return URL_TEST + "?t=" + System.currentTimeMillis();
	}
	public String okhttp() {
		ByteArrayOutputStream baos = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(getUrl());
			conn = (HttpURLConnection) okClient.open(url);
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setRequestProperty("User-Agent", "chrome");

			baos = new ByteArrayOutputStream();
			is = conn.getInputStream();
			byte[] buffer = new byte[BUFF_SIZE];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public String urlConn() {
		ByteArrayOutputStream baos = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(getUrl());
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			// conn.setHostnameVerifier(DO_NOT_VERIFY);
			conn.setRequestProperty("User-Agent", "chrome");

			baos = new ByteArrayOutputStream();
			is = conn.getInputStream();
			byte[] buffer = new byte[BUFF_SIZE];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String httpClient() {
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		try {
			HttpGet get = new HttpGet(getUrl());
			HttpResponse res = client.execute(get);
			is = res.getEntity().getContent();
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFF_SIZE];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public String myHttpClient() {
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		try {
			HttpGet get = new HttpGet(getUrl());
			HttpResponse res = client.execute(get);
			is = res.getEntity().getContent();
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFF_SIZE];
			int count = 0;
			while ((count = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, count);
			}
			return baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	//public String httpClientPool() {
	//	ByteArrayOutputStream baos = null;
	//	InputStream is = null;
	//	try {
	//		HttpGet get = new HttpGet(getUrl());
	//		HttpResponse res = HttpManager.execute(get);
	//		is = res.getEntity().getContent();
	//		baos = new ByteArrayOutputStream();
	//		byte[] buffer = new byte[BUFF_SIZE];
	//		int count = 0;
	//		while ((count = is.read(buffer, 0, buffer.length)) != -1) {
	//			baos.write(buffer, 0, count);
	//		}
	//		return baos.toString();
	//	} catch (MalformedURLException e) {
	//		e.printStackTrace();
	//	} catch (IOException e) {
	//		e.printStackTrace();
	//	} finally {
	//		try {
	//			if (is != null) {
	//				is.close();
	//			}
	//			if (baos != null) {
	//				baos.close();
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
	//	return "";
	//}

}
