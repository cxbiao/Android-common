/**
 * ImageUtils.java
 * com.android.wollar.util
 * <p/>
 * Function： TODO
 * <p/>
 * ver     date      		author
 * ──────────────────────────────────
 * 2012-12-19 		Administrator
 * <p/>
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.bryan.lib.image;

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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageUtils {


    /** The Constant PHOTO_RESOULT. */
    public static final int PHOTO_RESOULT = 7;// 结果

    /** The Constant IMAGE_UNSPECIFIED. */
    public static final String IMAGE_UNSPECIFIED = "image/*";


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

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }


    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromFile(String pathName,
                                                   int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }


    public static Bitmap loadImage(Context context,String pathName) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        int reqWidth=context.getResources().getDisplayMetrics().widthPixels;
        int reqHeight=context.getResources().getDisplayMetrics().heightPixels;
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }

    public static boolean compressImage(Context context,int quality, String srcImaPath, String destImgPath) {
        InputStream in = null;
        Bitmap bit = null;
        try {
            bit =loadImage(context,srcImaPath);
            FileOutputStream fileOutputStream = new FileOutputStream(destImgPath);
            bit.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.close();
            ///File file=new File(destImgPath);
            //long aaa=file.length()/1024;
            //Log.e("大小", file.length()/1024+"");
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        } finally {
            if(bit!=null)
                bit.recycle();
        }
        return true;
    }


    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public ImageSize getImageViewWidth(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = imageView.getContext()
                .getResources().getDisplayMetrics();
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();

        int width = params.width == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getWidth(); // Get actual image width
        if (width <= 0)
            width = params.width; // Get layout width parameter
        if (width <= 0)
            width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
        // maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = params.height == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getHeight(); // Get actual image height
        if (height <= 0)
            height = params.height; // Get layout height parameter
        if (height <= 0)
            height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
        // maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;

    }


    private class ImageSize {
        int width;
        int height;
    }

    /**
     * 反射获得ImageView设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;

                Log.e("TAG", value + "");
            }
        } catch (Exception e) {
        }
        return value;
    }


    public static Bitmap getSmallBitmap(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

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
            return Base64.encodeToString(b, Base64.DEFAULT);
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
