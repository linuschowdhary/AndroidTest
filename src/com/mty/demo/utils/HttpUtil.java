package com.mty.demo.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	// public static byte[] get(String url) {
	// DefaultHttpClient clitent = new DefaultHttpClient();
	// HttpGet get = new HttpGet(url);
	// try {
	// HttpResponse res = clitent.execute(get);
	// if (res.getStatusLine().getStatusCode() == 200) {
	// return EntityUtils.toByteArray(res.getEntity());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }
	public static byte[] get(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setUseCaches(false);
			conn.connect();
			InputStream is = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = is.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, len);
			}
			return baos.toByteArray();

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
}
