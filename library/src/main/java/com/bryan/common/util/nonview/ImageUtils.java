/**
 * ImageUtils.java
 * com.android.wollar.util
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-19 		Administrator
 *
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.bryan.common.util.nonview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


// TODO: Auto-generated Javadoc
/**
 * The Class ImageUtils.
 * 
 * @description 处理图片的工具类
 * @version 1.0
 * @author Jeremy
 * @update 2012-12-19 下午03:00:24
 */

public class ImageUtils {

	/** The Constant PHOTO_GRAPH. */
	public static final int PHOTO_GRAPH = 5;// 拍照返回,图片

	/** The Constant PHOTO_ZOOM. */
	public static final int Photo_ALBUM = 6; // 相册返回,图片

	/** The Constant PHOTO_GRAPH. */
	public static final int VIDEO_GRAPH = 9;// 拍照返回,视频

	/** The Constant PHOTO_ZOOM. */
	public static final int VIDEO_ALBUM = 10; // 相册返回,视频

	/** The Constant PHOTO_RESOULT. */
	public static final int PHOTO_RESOULT = 7;// 结果

	/** The Constant NONE. */
	public final static int NONE = 8;

	public final static int PHOTO_ZOOM = 1; // 缩放

	/** The Constant IMAGE_UNSPECIFIED. */
	public static final String IMAGE_UNSPECIFIED = "image/*";

	/** The Constant HEAD_NAME. */
	public static final String HEAD_NAME = "my_head_icon.jpg";

	public static final long IMAGE_SIZE =(long) (0.8*1024*1024);//图片限制800k

	/**
	 * 裁剪图片 Start photo zoom.
	 * 
	 * @param uri
	 *            the uri
	 * @param context
	 *            the context
	 */
	public static void startPhotoZoom(Uri uri, Context context, int outputX, int outputY) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);

		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", false);
		((Activity) context).startActivityForResult(intent, PHOTO_RESOULT);

	}

	public static void startPhotoZoom(Uri uri, Uri saveUri, Context context, int outputX, int outputY) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", false);
		intent.putExtra("scale", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
		((Activity) context).startActivityForResult(intent, PHOTO_RESOULT);

	}

	/**
	 * 保存图片到指定文件夹.
	 * 
	 * @param photo
	 *            the photo
	 * @param path
	 *            the path
	 * @param fileName
	 *            the file name
	 */
	public static String saveImage(Bitmap photo, File path, String fileName) {
		try {
			if (!path.exists()) {
				path.mkdirs();
			}

			File f = new File(path, fileName);
			f.deleteOnExit();
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();

			return f.getPath();
		} catch (Exception e) {
			Log.d("Jeremy", e.toString());
		}

		return null;

	}
	/**
	 * 保存图片到指定文件夹.
	 * 
	 * @param photo
	 *            the photo

	 * @param fileFullName
	 *            the file name
	 */
	public static String saveImage(Bitmap photo, String fileFullName) {
		try {
			File f = new File(fileFullName);
			f.deleteOnExit();
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();

			return f.getPath();
		} catch (Exception e) {
			Log.d("Jeremy", e.toString());
		}

		return null;

	}

	public static Bitmap getImage(File file) {

		Bitmap bmp = null;
		if (file.exists()) {
			bmp = BitmapFactory.decodeFile(file.getPath());
		}

		return bmp;

	}

	/**
	 * 用当前时间给照片命名 Gets the photo file name.
	 * 
	 * @return the photo file name
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

	/**
	 * convert Drawable to Bitmap
	 * 
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 * convert Bitmap to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param scaleWidth
	 *            sacle of width
	 * @param scaleHeight
	 *            scale of height
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
		if (org == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		File file=new File(filePath);
		if(!file.exists()){
			return null;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 100, 80);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 把bitmap转换成String
	public static String bitmapToString(String filePath) {
		try {
			Bitmap bm = getSmallBitmap(filePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			return Base64.encodeToString(b,Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		}

	}

	// 把bitmap转换成String
	public static String bitmapToString(Bitmap bm) {
		try {
			if (bm == null)
				return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			return Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		}

	}

	// 在指定的位图上添加图标
	public static Bitmap addMarkToImageMap(Bitmap src, Bitmap markImg, int x, int y) {
		// int imgMapWidth = src.getWidth();
		// int imgMapHeight = src.getHeight();
		// 创建一个和原图同样大小的位图
		// Bitmap newBitmap = Bitmap.createBitmap(imgMapWidth,imgMapHeight,
		// Bitmap.Config.RGB_565);
		Bitmap newBitmap = src;
		if (!newBitmap.isMutable()) {
			newBitmap = src.copy(Bitmap.Config.RGB_565, true);
		}
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawBitmap(markImg, x, y, /* paint */null);// 插入图标
		canvas.save(Canvas.ALL_SAVE_FLAG);
		// 存储新合成的图片
		canvas.restore();

		return newBitmap;
	}

	// 图片按比例大小压缩方法（根据路径获取图片并压缩）
	private Bitmap getCompressionImage(String srcPath, String fileSavePath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap, fileSavePath);// 压缩好比例大小后再进行质量压缩
	}

	// 图片按比例大小压缩方法（根据Bitmap图片压缩）
	private Bitmap getCompressionImage(Bitmap image, String fileSavePath) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap, fileSavePath);// 压缩好比例大小后再进行质量压缩
	}

	// 压缩图片
	public static Bitmap compressImage(Bitmap image, String fileSavePath, int size) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于size(kb),大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
			if (options < 0) {
				break;
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

		byte[] data = baos.toByteArray();
		if (!TextUtils.isEmpty(fileSavePath)) {
			FileUtil.SaveFile(data, fileSavePath);
		}

		return bitmap;
	}
	//根据图片地址和指定大小，对图片压缩,按比例缩小
	public static Bitmap compressImage(String fileSourcePath, String fileSavePath, int size) {
		File file=new File(fileSourcePath);
		return new ImageUtils().getCompressionImage(file.length(), fileSourcePath, fileSavePath,size*1024);
	}
	public Bitmap compressImage(Bitmap image, String fileSavePath) {
		// 默认大小50k
		return compressImage(image, fileSavePath, 50);
	}

	public Bitmap getCompressionImage(long fileLength, String filePath, String fileSavePath) {
		return getCompressionImage(fileLength, filePath, fileSavePath,IMAGE_SIZE);

	}
	public Bitmap getCompressionImage(long fileLength, String filePath, String fileSavePath,long size) {
		try {
			int percent = (int) (fileLength / size);
			percent = percent + 1;
			InputStream is = new FileInputStream(filePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			// width,hight设为原来的十分一
			options.inSampleSize = percent;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

			int degree = getExifOrientation(filePath);
			if (degree == 90 || degree == 180 || degree == 270) {
				Matrix matrix = new Matrix();
				matrix.postRotate(degree);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}

			Bitmap bitmap2 = getCompressionImage(bitmap, fileSavePath);

			return bitmap2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	// 根据图片地址，返回限定大小的base64
	public String getCompressionImage(String filePath) {
		final int maxLength = 8;
		String base64Return = null;

		base64Return = bitmapToString(filePath);
		if (base64Return.getBytes().length < maxLength * 1024) {
			return base64Return;
		}

		try {
			File captureFile = new File(filePath);
			int percent = (int) (captureFile.length() / (maxLength * 1024));
			percent = percent + 1;
			InputStream is = new FileInputStream(filePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			// width,hight设为原来的十分一
			options.inSampleSize = percent;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

			int degree = getExifOrientation(filePath);
			if (degree == 90 || degree == 180 || degree == 270) {
				Matrix matrix = new Matrix();
				matrix.postRotate(degree);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
			
			base64Return=getCompressionImage(bitmap);

			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return base64Return;

	}

	// 根据图片，返回限定大小的base64
	public String getCompressionImage(Bitmap bitmap) {
		final int maxLength = 8;
		String base64Return = null;
		base64Return = bitmapToString(bitmap);
		
		try{
			if (base64Return.getBytes().length < maxLength * 1024) {
				return base64Return;
			}
			
			Bitmap bitmap2 = null;
			int currentLength = maxLength;
			for(int i=0;i<10;i++){
				bitmap2 = compressImage(bitmap, null, currentLength);
				currentLength--;
				if(currentLength<0){
					return null;
				}
				base64Return = bitmapToString(bitmap2);
				if(base64Return.getBytes().length > maxLength * 1024){
					continue;
				}else{
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return base64Return;

	}
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			// MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
				}
			}
		}

		return degree;
	}

}
