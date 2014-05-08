package com.mty.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import android.content.Context;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.util.FileUtils;
import com.mty.demo.App;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class DexUtil {

	/**
	 * 装载初始化某个包下每一个类
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void loadAllClass(Context context, String packageName) {
		try {
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			DexFile df = new DexFile(context.getPackageCodePath());
			PathClassLoader pcl = new PathClassLoader(df.getName(), cl);
			for (Enumeration<String> iter = df.entries(); iter
					.hasMoreElements();) {
				String s = iter.nextElement();
				System.out.println(s);
				if (s.startsWith(packageName)) {
					Class.forName(s, true, pcl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getClassFileName(String name) {
		return name + ".class";
	}

	public static String getDexFileName(String name) {
		return name + ".dex";
	}
	
	public static String getJarFileName(String name) {
		return name + ".jar";
	}

	public static File getJarFileByName(String name) {
		return new File(App.getApp().getFilesDir(), getJarFileName(name));
	}
	
	public static File getDexFileByName(String name) {
		return new File(App.getApp().getFilesDir(), getDexFileName(name));
	}

	public static File getDexCacheFileByName(String name) {
		return new File(App.getApp().getCacheDir(), getDexFileName(name));
	}

	public static File getClassFileByName(String name) {
		return new File(App.getApp().getFilesDir(), getClassFileName(name));
	}

	/**
	 * 将一个class文件保存到Dex文件中
	 * 
	 * @param classFile
	 * @return
	 * @throws Exception
	 */
	public static File putClassIntoDex(File classFile, String packageName)
			throws Exception {
		packageName = packageName == null ? "" : packageName;
		DexOptions dexOptions = new DexOptions();
		com.android.dx.dex.file.DexFile df = new com.android.dx.dex.file.DexFile(
				dexOptions);
		ClassDefItem item = CfTranslator.translate(
				packageName + classFile.getName(),
				FileUtils.readFile(classFile), new CfOptions(), dexOptions);
		df.add(item);
		File dexFile = getDexFileByName("demo");
		FileOutputStream fos = new FileOutputStream(dexFile);
		df.writeTo(fos, null, false);
		fos.close();
		return dexFile;
	}

	/**
	 * 将流保存为文件
	 * 
	 * @param input
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static boolean saveToFileDir(byte[] input, File fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(input);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
