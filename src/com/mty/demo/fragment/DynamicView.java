package com.mty.demo.fragment;

import java.io.BufferedInputStream;
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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.util.FileUtils;
import com.mty.demo.act.MainActivity;
import com.mty.demo.dynamic.DalvikClassClassPath;
import com.mty.demo.dynamic.DexFile;
import com.mty.demo.utils.HttpUtil;
import com.mty.test.animtext.AnimTextActivity;

import dalvik.system.DexClassLoader;

public class DynamicView extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// return exe();
		return test();
		// return getCustomView();
	}

	private String getClassFileName(String name) {
		return name + ".class";
	}

	private String getDexFileName(String name) {
		return name + ".dex";
	}

	private File getDexFileByName(String name) {
		return new File(getActivity().getFilesDir(), getDexFileName(name));
	}

	private File getDexCacheFileByName(String name) {
		return new File(getActivity().getCacheDir(), getDexFileName(name));
	}

	private File getClassFileByName(String name) {
		return new File(getActivity().getFilesDir(), getClassFileName(name));
	}

	@SuppressLint("NewApi")
	private View test() {

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				byte[] input = HttpUtil.get("http://def.so/p/content" + "?r=" + System.currentTimeMillis());
				if (input != null) {
					return new String(input);
				} else {
					return null;
				}
			}

			protected void onPostExecute(String result) {
				handle(result);
			};
		}.execute();

		Button bt = new Button(getActivity());
		bt.setText("编译完成");
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				run(getActivity());
			}
		});
		return bt;
	}

	private void run(final Context context) {
		
//		android.content.Intent it = new android.content.Intent("android.intent.action.DIAL",
//				android.net.Uri.parse("tel:186"));
//		context.startActivity(it);
//		
//		android.content.Intent it = new android.content.Intent("android.intent.action.VIEW",
//				android.net.Uri.parse("http://baidu.com"));
//		context.startActivity(it);
		
//		android.content.Intent intent = new android.content.Intent();   
//	    intent.setComponent(new android.content.ComponentName("com.taobao.ju.android", "com.taobao.ju.android.ui.main.SplashActivity"));   
//	    intent.setAction("android.intent.action.VIEW");   
//	    context.startActivity(intent); 
		
		android.widget.Toast.makeText(context, (java.lang.CharSequence)"哈哈", 0).show();
		android.view.ViewGroup v = (android.view.ViewGroup) ((android.app.Activity)context).findViewById(android.R.id.content);
		Button bt = new Button(context);
		v.addView(bt);
		bt.setText("测试成功 ...");
		System.out.println("showwwwwwwwwwwwwwwwwwwwwwwww");
		OnClickListener onClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("ssssssssssssssssssss");
				context.startActivity(new Intent(context, MainActivity.class));
			}
		};
		bt.setOnClickListener(onClick);
		bt.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				System.out.println("llllllllllllllllllllll");
				Builder b = new Builder(context);
				b.setMessage("hello dialog");
				b.setTitle("title");
				b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(context, " hello toast", 0).show();
					}
				});
				b.show();
				return false;
			}
		});
//		android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context);
//	    b.setTitle((java.lang.CharSequence)"test");
//	    b.setMessage((java.lang.CharSequence)"content");
//	    b.show();
	    
//		context.startActivity(new android.content.Intent(context, com.mty.test.animtext.AnimTextActivity.class));
	    
	}

	
	private void handle(String inputString) {
		try {
			System.out.println("inputString " + inputString);
			JSONObject jObj = new JSONObject(inputString);
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

			//
			// String className = "OutMan";
			// String methodName = "run";
			// String methodBody =
			// "public void run(android.content.Context context){"
			// // + "android.content.Intent it;"
			// //
			// +"android.content.Intent it = new android.content.Intent(android.content.Intent.ACTION_VIEW ,android.net.Uri.parse(\"tel:1111\"));"
			// +
			// "android.content.Intent it = new android.content.Intent(\"android.intent.action.VIEW\",android.net.Uri.parse(\"http://def.so/p\"));"
			// //
			// +"android.content.Intent it = new android.content.Intent(android.content.Intent.ACTION_DIAL,android.net.Uri.parse(\"tel:186\"));"
			// + "context.startActivity(it);}";

			System.out.println("methodBody " + methodBody);
			// 构建新的类池
			ClassPool cp = ClassPool.getDefault(getActivity().getApplicationContext());
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

			File classFile = getClassFileByName(className);
			DexOptions dexOptions = new DexOptions();
			com.android.dx.dex.file.DexFile df = new com.android.dx.dex.file.DexFile(dexOptions);
			ClassDefItem item = CfTranslator.translate(classFile.getName(), FileUtils.readFile(classFile),
					new CfOptions(), dexOptions);
			// ClassDefItem item =
			// CfTranslator.translate(getClassFileName(className),
			// FileUtils.readFile(file),
			// new CfOptions(), dexOptions);
			df.add(item);

			File dexFile = getDexFileByName(className);
			FileOutputStream fos = new FileOutputStream(dexFile);
			df.writeTo(fos, null, false);

			// 从dex中载入
			DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(), getActivity().getCacheDir()
					.getAbsolutePath(), getActivity().getApplicationInfo().nativeLibraryDir, getActivity()
					.getClassLoader());
			Class claxx = dcl.loadClass(className);

			// 反射运行
			Method m = claxx.getDeclaredMethod(methodName, android.content.Context.class);
			System.out.println("outter: " + m.invoke(claxx.newInstance(), getActivity()));
			File dexCache = getDexCacheFileByName(className);
			dexCache.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	private View exe() {
		try {
			// String m1 =
			// "public android.view.View getView(android.content.Context context) {"
			// +
			// "android.widget.Button bt = new android.widget.Button(context);"
			// + "bt.setText(\"hello abc \");"
			// + "return bt;" + "}";

			String className = "A";
			String methodName = "getView";
			String root = getActivity().getFilesDir().getAbsolutePath();

			ClassPool cp = new ClassPool();
			cp.appendClassPath(new DalvikClassClassPath(getActivity().getApplicationContext()));

			// CtClass cls = cp.makeClass(className);
			// final CtConstructor ctor = new CtConstructor(null, cls);
			// ctor.setBody("{}");
			// cls.addConstructor(ctor);
			// cls.addMethod(CtMethod.make(m1, cls));
			// cls.writeFile(root);

			// InputStream is = getActivity().getAssets().open("A.class");
			// byte[] buf = new byte[is.available()];
			// is.read(buf);
			// is.close();
			// cp.insertClassPath(new ByteArrayClassPath(className, buf));
			//
			// is = getActivity().getAssets().open("A$1.class");
			// buf = new byte[is.available()];
			// is.read(buf);
			// is.close();
			// cp.insertClassPath(new ByteArrayClassPath("A$1", buf));
			//
			// CtClass A = cp.get(className);
			// A.writeFile(root);
			//
			// DexFile df = new DexFile();
			// File classFile = new File(getActivity().getFilesDir(), className
			// + ".class");
			// File dexFile = new File(getActivity().getFilesDir(), className +
			// ".dex");
			// File dexCaheFile = new File(getActivity().getCacheDir(),
			// className + ".dex");
			// df.addClass(classFile);
			// df.writeFile(dexFile.getAbsolutePath());

			// insertClassPath(cp, "A", root);
			// insertClassPath(cp, "A$1", root);
			// insertClassPath(cp, "A$2", root);
			// insertClassPath(cp, "A$2$1", root);

			File ajar = new File(getActivity().getFilesDir(), "a.jar");
			FileOutputStream fos = new FileOutputStream(ajar);
			BufferedInputStream bis = new BufferedInputStream(getActivity().getAssets().open("a.jar"));
			byte buffer[] = new byte[8192];
			while (bis.read(buffer) != -1) {
				fos.write(buffer);
			}
			bis.close();
			fos.close();
			java.util.jar.JarFile jarFile = new java.util.jar.JarFile(ajar);

			Enumeration<JarEntry> enu = jarFile.entries();
			DexFile df = new DexFile();
			while (enu.hasMoreElements()) {
				JarEntry je = enu.nextElement();
				String name = je.getName();
				System.out.println(name);
				System.out.println("j---------------------- ");
				if (name.endsWith(".class")) {
					ZipEntry ze = jarFile.getEntry(name);
					saveClass(cp, name.substring(0, name.indexOf(".")), root, jarFile.getInputStream(ze));
					File classFile = new File(getActivity().getFilesDir(), je.getName());
					df.addClass(classFile);
				}
			}

			File dexFile = new File(getActivity().getFilesDir(), "all.dex");
			df.writeFile(dexFile.getAbsolutePath());

			DexClassLoader d = new DexClassLoader(dexFile.getAbsolutePath(), getActivity().getCacheDir()
					.getAbsolutePath(), getActivity().getApplicationInfo().nativeLibraryDir, getActivity()
					.getClassLoader());

			Class<?> Hello = d.loadClass(className);
			Method m = Hello.getDeclaredMethod(methodName, Context.class);
			System.out.println("Success____________________________-");
			
			// dexFile.delete();
			// classFile.delete();
			// dexCaheFile.delete();
			return (View) m.invoke(Hello.newInstance(), getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private void saveClass(ClassPool cp, String className, String rootPath, InputStream is) throws Exception {
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		cp.insertClassPath(new ByteArrayClassPath(className, buf));
		CtClass A = cp.get(className);
		A.writeFile(rootPath);
	}

	private void insertClassPath(ClassPool cp, String className, String rootPath) throws Exception {
		InputStream is = getActivity().getAssets().open(className + ".class");
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		cp.insertClassPath(new ByteArrayClassPath(className, buf));
		CtClass A = cp.get(className);
		A.writeFile(rootPath);
	}

	private void saveToDex(String className) throws Exception {
		DexFile df = new DexFile();
		File classFile = new File(getActivity().getFilesDir(), className + ".class");
		File dexFile = new File(getActivity().getFilesDir(), "all.dex");
		df.addClass(classFile);
		df.writeFile(dexFile.getAbsolutePath());
	}

	public void sayHello() {
		System.out.println("This is From Inner...");
	}

	private View getCustomView() {

		try {
			ViewProDuctor h = new ViewProDuctor();
			return h.getView(getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
