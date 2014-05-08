package com.mty.demo.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.util.FileUtils;
import com.mty.demo.R;
import com.mty.demo.dynamic.DexFile;
import com.mty.demo.utils.DexUtil;
import com.mty.demo.utils.HttpUtil;

import dalvik.system.DexClassLoader;

public class DynamicFrag extends Fragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_dynamic, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().findViewById(R.id.button1).setOnClickListener(this);
		getActivity().findViewById(R.id.button2).setOnClickListener(this);
		getActivity().findViewById(R.id.button3).setOnClickListener(this);
		getActivity().findViewById(R.id.button4).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			exeCode();
			break;
		case R.id.button2:
			exeClass();
			break;
		case R.id.button3:
//			exeJar();
			break;
		case R.id.button4:
			exeApk();
			break;

		default:
			break;
		}
	}

	private void exeCode() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				byte[] input = HttpUtil.get("http://def.so/p/content" + "?r="
						+ System.currentTimeMillis());
				if (input != null) {
					return new String(input);
				} else {
					return null;
				}
			}

			@SuppressLint("NewApi")
			protected void onPostExecute(String result) {
				try {
					System.out.println("inputString " + result);
					JSONObject jObj = new JSONObject(result);
					JSONObject data = (JSONObject) jObj.opt("data");

					String className = data.optString("className");
					String methodName = data.optString("methodName");
					String methodBody = data.optString("methodBody");
					System.out.println("className " + className);
					System.out.println("methodName " + methodName);
					System.out.println("methodBody " + methodBody);

					data = (JSONObject) jObj.opt("result");
					String des = data.optString("des");
					System.out.println("des " + des);

					System.out.println("methodBody " + methodBody);
					// 构建新的类池
					ClassPool cp = ClassPool.getDefault(getActivity()
							.getApplicationContext());
					// ClassPool cp = new ClassPool();
					// cp.appendClassPath(new
					// DalvikClassClassPath(getActivity().getApplicationContext()));
					// 完善类文件
					String s = android.content.Intent.ACTION_VIEW;
					CtClass cc = cp.makeClass(className);
					CtConstructor ccons = new CtConstructor(null, cc);
					ccons.setBody("{}");
					cc.addConstructor(ccons);
					cc.addMethod(CtMethod.make(methodBody, cc));
					// 把类转为class
					cc.writeFile(getActivity().getFilesDir().getAbsolutePath());
					// cc.defrost();
					cc.detach();
					// 将class打包入dex
					File dexFile = DexUtil.putClassIntoDex(
							DexUtil.getClassFileByName(className), null);

					// 从dex中载入
					DexClassLoader dcl = new DexClassLoader(
							dexFile.getAbsolutePath(),
							getActivity().getCacheDir().getAbsolutePath(),
							getActivity().getApplicationInfo().nativeLibraryDir,
							ClassLoader.getSystemClassLoader());
					Class claxx = dcl.loadClass(className);

					// 反射运行
					Method m = claxx.getDeclaredMethod(methodName,
							android.content.Context.class);
					System.out.println("outter: "
							+ m.invoke(claxx.newInstance(), getActivity()));
					File dexCache = DexUtil.getDexCacheFileByName("demo");
					dexCache.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.execute();
	}

	private void exeClass() {

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				byte[] input = HttpUtil.get("http://def.so/p/upload/demo.class"
						+ "?r=" + System.currentTimeMillis());
				return DexUtil.saveToFileDir(input,
						DexUtil.getClassFileByName("demo"));
			}

			@SuppressLint("NewApi")
			protected void onPostExecute(Boolean result) {
				if (!result) {
					System.out.println("保存失败！！！！！！！");
					return;
				}
				try {
					String classFileName = "ViewProDuctor";
					String classPackageName = "com/mty/demo/utils/";
					String className = classPackageName + classFileName;
					String methodName = "getView";

					File demoFile = DexUtil.getClassFileByName("demo");
					File classFile = DexUtil.getClassFileByName(className);
					if (!classFile.getParentFile().exists()) {
						classFile.getParentFile().mkdirs();
					}
					demoFile.renameTo(classFile);
					File dexFile = DexUtil.putClassIntoDex(
							DexUtil.getClassFileByName(className),
							classPackageName);

					// 从dex中载入
					DexClassLoader dcl = new DexClassLoader(
							dexFile.getAbsolutePath(),
							getActivity().getCacheDir().getAbsolutePath(),
							getActivity().getApplicationInfo().nativeLibraryDir,
							ClassLoader.getSystemClassLoader());
					// pcl.loadClass("");
					Class claxx = dcl.loadClass(className);
					// 反射运行
					Method m = claxx.getDeclaredMethod(methodName,
							android.content.Context.class);
					Object obj = m.invoke(claxx.newInstance(), getActivity());
					System.out.println("outter: " + obj);
					if (obj != null) {
						View view = (View) obj;
						getActivity().setContentView(view);
					}

					File dexCache = DexUtil.getDexCacheFileByName("demo");
					System.out.println("删除CacheDex文件：" + dexCache.delete());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.execute();
	}

//	private void exeJar() {
//		new AsyncTask<Void, Void, Boolean>() {
//
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				byte[] input = HttpUtil.get("http://def.so/p/upload/demo.jar"
//						+ "?r=" + System.currentTimeMillis());
//				return DexUtil.saveToFileDir(input,
//						DexUtil.getJarFileByName("demo"));
//			}
//
//			@SuppressLint("NewApi")
//			protected void onPostExecute(Boolean result) {
//				if (!result) {
//					System.out.println("保存失败！！！！！！！");
//					return;
//				}
//				try {
//					// System.out.println("保存！！！！！！！"+ new File(DexUtil
//					// .getJarFileByName("demo").getAbsolutePath()+"/ViewProDuctor.class").exists());
//					// JarFile jarF = new JarFile(getActivity(), DexUtil
//					// .getJarFileByName("demo").getAbsolutePath());
//					String classFileName = "ViewProDuctor";
//					String classPackageName = "com.mty.demo.utils.";
//					String className = classPackageName + classFileName;
//					String methodName = "getView";
//
//					java.util.jar.JarFile jarFile = new java.util.jar.JarFile(
//							DexUtil.getJarFileByName("demo.jar"));
//					Enumeration<JarEntry> enu = jarFile.entries();
//					DexFile df = new DexFile();
//					while (enu.hasMoreElements()) {
//						JarEntry je = enu.nextElement();
//						String name = je.getName();
//						System.out.println("jarEntry Name: " + name);
//						if (name.endsWith(".class")) {
//							ZipEntry ze = jarFile.getEntry(name);
//							
//							DexOptions dexOptions = new DexOptions();
//							com.android.dx.dex.file.DexFile df = new com.android.dx.dex.file.DexFile(
//									dexOptions);
//							ClassDefItem item = CfTranslator.translate(
//									packageName + classFile.getName(),
//									FileUtils.readFile(classFile), new CfOptions(), dexOptions);
//							df.add(item);
//							File dexFile = getDexFileByName("demo");
//							FileOutputStream fos = new FileOutputStream(dexFile);
//							df.writeTo(fos, null, false);
//							fos.close();
//							
//							
//							DexUtil.putClassIntoDex(classFile, packageName);
//							saveClass(cp, name.substring(0, name.indexOf(".")),
//									root, jarFile.getInputStream(ze));
//							File classFile = new File(getActivity()
//									.getFilesDir(), je.getName());
//							df.addClass(classFile);
//						}
//					}
//
//					File dexFile = new File(getActivity().getFilesDir(),
//							"all.dex");
//					df.writeFile(dexFile.getAbsolutePath());
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			};
//		}.execute();
//	}

	private void saveClass(ClassPool cp, String className, String rootPath,
			InputStream is) throws Exception {
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		cp.insertClassPath(new ByteArrayClassPath(className, buf));
		CtClass A = cp.get(className);
		A.writeFile(rootPath);
	}

	private void exeApk() {

	}
}
