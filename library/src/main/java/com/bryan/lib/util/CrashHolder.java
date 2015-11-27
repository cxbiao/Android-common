/*
 *
 * COPYRIGHT NOTICE
 * Copyright (C) 2015, bryan <690158801@qq.com>
 * https://github.com/cxbiao/Android-common
 *
 * @license under the Apache License, Version 2.0
 *
 * @version 1.0
 * @author  bryan
 * @date    2015/11/25
 *
 */

package com.bryan.lib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author cxb
 * @date 2015/9/6 20:15 日志收集器
 */
public class CrashHolder implements Thread.UncaughtExceptionHandler {

	private Class<? extends Activity> errorActivityClass = null;
	private Thread.UncaughtExceptionHandler mOriginHandler;
	private Context mContext;
	private static final String TAG = "CrashHolder";
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	private static CrashHolder mInstance = null;
	private static final String APPNAME = "winstar";

	public static CrashHolder getInstance() {
		if (mInstance == null) {
			synchronized (CrashHolder.class) {
				if (mInstance == null) {
					return new CrashHolder();
				}
			}
		}
		return mInstance;

	}

	public CrashHolder install(Context context) {
		try {
			if (context == null) {
				Log.e(TAG, "Install failed: context is null!");
				return null;
			} 
			mContext=context;
			mOriginHandler = Thread.currentThread().getUncaughtExceptionHandler();
			Thread.currentThread().setUncaughtExceptionHandler(this);
		} catch (Throwable t) {
			Log.e(TAG,"An unknown error occurred while installing CrashHolder!",t);
		}
		return this;
	}

	public void setErrorActivityClass(Class<? extends Activity> errorActivityClass) {
		this.errorActivityClass = errorActivityClass;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (!handleException(throwable) && mOriginHandler != null) {
			mOriginHandler.uncaughtException(thread, throwable);
		}else{
			killCurrentProcess();
		}
	}

	private CrashHolder() {
	}

	private boolean handleException(Throwable throwable) {
		if(throwable==null){
			return false;
		}
		try {
			collectDeviceInfo();
			saveToFile(throwable);
			if (errorActivityClass != null) {
				startCatchActivity(throwable);
			}
			// 还可以上传到服务器

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public void collectDeviceInfo() {
		try {
			PackageInfo pi = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					infos.put(field.getName(), field.get(null).toString());
					Log.d(TAG, field.getName() + " : " + field.get(null));
				} catch (Exception e) {
					Log.e(TAG, "an error occured when collect crash info", e);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "an error occured when collect package info", e);  
		}

	}

	private void startCatchActivity(Throwable throwable) {
		String traces = getStackTrace(throwable);
		Intent intent = new Intent(mContext, errorActivityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("Error", traces);
		mContext.startActivity(intent);
	}

	private static void killCurrentProcess() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	@SuppressLint("SimpleDateFormat")
	private String saveToFile(Throwable throwable) {
		
		StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
        sb.append("==============================\n\n");
		String result=getStackTrace(throwable);
		sb.append(result);
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String time = formatter.format(new Date());
		String fileName = "Crash-" + time + ".log";
		try{
			
		    String crashDir=getLogDir();
			FileOutputStream fos=new FileOutputStream(new File(crashDir,fileName));
			fos.write(sb.toString().getBytes());
			fos.close();
			return crashDir+"/"+fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);  
		}
		
		return null;
	}

	private String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		printWriter.close();
		return writer.toString();
	}
	
	
	private String getLogDir(){
		File crashDir;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			crashDir=new File(Environment.getExternalStorageDirectory()+"/"+APPNAME+"/log");
		}else{
			crashDir=new File(mContext.getFilesDir(),"log");
		}
		if(!crashDir.exists()){
			crashDir.mkdirs();
		}
		return crashDir.getAbsolutePath();
	}
}
