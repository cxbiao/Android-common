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

package com.bryan.lib.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PathUtil {

	private static final String APPNAME = "winstar";


	private static String getCacheDir(Context context){
		File crashDir;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			crashDir=new File(Environment.getExternalStorageDirectory()+"/"+APPNAME);
		}else{
			crashDir=context.getFilesDir();
		}
		if(!crashDir.exists()){
			crashDir.mkdirs();
		}
		return crashDir.getAbsolutePath();
	}

	public static String GetVideoPath(Context context) {
		String filePathString = getCacheDir(context)+"/video";
		File filepath = new File(filePathString);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		return filePathString;
	}

	public static String GetImagePath(Context context) {
		String filePathString = getCacheDir(context)+"/image";
		File filepath = new File(filePathString);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		return filePathString;
	}

	public static String GetAudioPath(Context context) {
		String filePathString = getCacheDir(context)+"/audio";
		File filepath = new File(filePathString);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		return filePathString;
	}



	public static String GetImageName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		return dateFormat.format(date) + ".jpg";
	}

	public static String GetVideoName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		return dateFormat.format(date) + ".mp4";
	}

	public static String GetAudioName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		return dateFormat.format(date) + ".amr";
	}




	// 判断内存卡是否存在
	public static boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

    

	// 转换文件大小
	private static String FormatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	// 取得文件夹大小
	private static long getFileSize(File file) {
		long size = 0;
		if (file.exists() == false) {
			size = 0;
		} else {
			File flist[] = file.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		}

		return size;
	}





}
