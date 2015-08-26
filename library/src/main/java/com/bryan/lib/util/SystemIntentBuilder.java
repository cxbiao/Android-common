package com.bryan.lib.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class SystemIntentBuilder {
	
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
