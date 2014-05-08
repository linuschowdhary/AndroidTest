package com.mty.demo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import android.content.Context;

public class StreamUtil {

	public static int BUFFER_SIZE = 1024;
	
	public static void main(final Context ctx) {
		final LinkedHashMap<String, String> timeMap = new LinkedHashMap<String, String>();
		new Thread() {
			@Override
			public void run() {
				int i = 0;
				long[][] a1 = new long[4][6];
				// int [] a2 = new int[6];
				// int [] a3 = new int[6];
				// int [] a4 = new int[6];
				try {
					while (i < 6) {
						InputStream inputStream = getInput(ctx);
						long s = System.currentTimeMillis();
						scanner(inputStream);
						a1[0][i] = System.currentTimeMillis() - s;
						timeMap.put("scanner", timeMap.get("scanner") + ", "
								+ a1[0][i]);

						inputStream = getInput(ctx);
						s = System.currentTimeMillis();
						buffReader(inputStream);// 1 1 0
						a1[1][i] = System.currentTimeMillis() - s;
						timeMap.put("buffReader", timeMap.get("buffReader")
								+ ", " + a1[1][i]);

						inputStream = getInput(ctx);
						s = System.currentTimeMillis();
						bytesRead(inputStream);
						a1[2][i] = System.currentTimeMillis() - s;
						timeMap.put("bytesRead", timeMap.get("bytesRead")
								+ ", " + a1[2][i]);

						inputStream = getInput(ctx);
						s = System.currentTimeMillis();
						
						bosRead(inputStream);
						a1[3][i] = System.currentTimeMillis() - s;
						timeMap.put("bosRead", timeMap.get("bosRead") + ", "
								+ a1[3][i]);

						i++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (Entry<String, String> en : timeMap.entrySet()) {
					System.out.println("方式" + en.getKey() + ": "
							+ en.getValue());
				}
				for (int j = 0; j < 4; j++) {
					System.out.println("时间均值: " + getAverage(a1[j]));
				}
			}
		}.start();

	}

	private static long getAverage(long[] a) {
		long t = 0;
		for (long b : a) {
			t += b;
		}
		return t / a.length;
	}

	private static InputStream getInput(Context ctx) throws IOException {
		return ctx.getAssets().open("a.txt");
	}


	public static String scanner(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		String text = scanner.useDelimiter("\\A").next();
		scanner.close();
		return text;
		// System.out.println("scanner: " + text);
	}

	public static String buffReader(InputStream inputStream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		boolean firstLine = true;
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			if (!firstLine) {
				stringBuilder.append(System.getProperty("line.separator"));
			} else {
				firstLine = false;
			}
			stringBuilder.append(line);
		}
		bufferedReader.close();
		String text = stringBuilder.toString();
		// System.out.println("buffReader: " + text);
		return text;
	}

	public static String bytesRead(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int readBytes = 0;
		StringBuilder stringBuilder = new StringBuilder();
		while ((readBytes = inputStream.read(buffer)) > 0) {
			stringBuilder.append(new String(buffer, 0, readBytes));
		}
		inputStream.close();
		String text = stringBuilder.toString();
		// System.out.println("bytesRead: " + stringBuilder.toString());
		return text;
	}

	/**
	 * 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String bosRead(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		String sll = new String(outStream.toByteArray());
		in.close();
		return sll;
	}

}
