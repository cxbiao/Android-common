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
 * @date    2015/08/25
 *
 */

package com.bryan.lib.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;

public class SystemIntentBuilder {
	/**
	 *<uses-permission android:name="android.permission.CALL_PHONE"/>
	 */
	public static Intent getCall(String telNum) {
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + telNum)); //数据的类型 ： uri格式的数据
		return  intent;
	}

	public static Intent getSms(String phone,String sms) {
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_SENDTO);
		if(!TextUtils.isEmpty(phone)){
			intent.setData(Uri.parse("smsto:"+phone));
		}else{
			intent.setData(Uri.parse("smsto:"));
		}
		intent.putExtra("sms_body", sms);
		return  intent;
	}

	/**
	 * <uses-permission android:name="android.permission.SEND_SMS"/>
	 */
	public static void sendMsg(String phone,String sms){
		SmsManager smsManager=SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, sms, null, null);
	}
	public static Intent getPickImageIntent(){
		Intent intent = new Intent(Intent.ACTION_PICK).setType("image/*");
		return intent;
	}
	
	public static Intent getpickPhotoIntent(Uri photoUri){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		return intent;
	}
	
	public static String getRealPath(Context context,Uri uri){
		Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
		if(cursor.moveToFirst()){
			return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		return "";	
			
	}
	/**
	 * EXTERNAL_CONTENT_URI  sdcard下的多媒体文件 
	 * INTERNAL_CONTENT_URI  system下的多媒体文件
	 * @param context
	 * @return
	 */
	public static Cursor getExternImageCursor(Context context){
		 Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  

         // 只查询jpeg和png的图片  
         Cursor cursor = context.getContentResolver().query(imageUri, null,  
                 MediaStore.Images.Media.MIME_TYPE + "=? or "  
                         + MediaStore.Images.Media.MIME_TYPE + "=?",  
                 new String[] { "image/jpeg", "image/png" },  
                 MediaStore.Images.Media.DATE_MODIFIED);  
         return cursor;
	}
	

}
