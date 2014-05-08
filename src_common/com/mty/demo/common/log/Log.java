package com.mty.demo.common.log;

import com.mty.demo.config.AppConfig;

/**
 * the logger
 *
 * @author MaTianyu
 * 2014-1-1下午4:05:39
 */
public class Log {

	private static boolean isPrint = AppConfig.isDebuged;

	private Log() {
	}

	public static int i(String m) {
		return isPrint ? android.util.Log.i("notag", m) : -1;
	}

	public static boolean isPrintable() {
		return isPrint;
	}

	public static void setPrintable(boolean isPrint) {
		Log.isPrint = isPrint;
	}

	public static int v(String tag, String msg) {
		return isPrint ? android.util.Log.v(tag, msg) : -1;
	}

	public static int d(String tag, String msg) {
		return isPrint ? android.util.Log.d(tag, msg) : -1;
	}

	public static int i(String tag, String msg) {
		return isPrint ? android.util.Log.i(tag, msg) : -1;
	}

	public static int w(String tag, String msg) {
		return isPrint ? android.util.Log.w(tag, msg) : -1;
	}

	public static int e(String tag, String msg) {
		return isPrint ? android.util.Log.e(tag, msg) : -1;
	}

	public static int v(String tag, String msg, Throwable tr) {
		return isPrint ? android.util.Log.v(tag, msg, tr) : -1;
	}

	public static int d(String tag, String msg, Throwable tr) {
		return isPrint ? android.util.Log.d(tag, msg, tr) : -1;
	}

	public static int i(String tag, String msg, Throwable tr) {
		return isPrint ? android.util.Log.i(tag, msg, tr) : -1;
	}

	public static int w(String tag, String msg, Throwable tr) {
		return isPrint ? android.util.Log.w(tag, msg, tr) : -1;
	}

	public static int e(String tag, String msg, Throwable tr) {
		return isPrint ? android.util.Log.e(tag, msg, tr) : -1;
	}
}
