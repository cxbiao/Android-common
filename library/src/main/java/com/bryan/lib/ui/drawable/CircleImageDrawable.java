package com.bryan.lib.ui.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class CircleImageDrawable extends Drawable{

	
	
	private Paint mPaint;
	private Bitmap mBitmap;
	private int mWidth;
	private RectF rectF;
	
	public CircleImageDrawable(Bitmap bitmap){
		mBitmap=bitmap;
		BitmapShader bitmapShader=new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth=Math.min(mBitmap.getWidth(), mBitmap.getHeight());
		
	}
	

	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2, mPaint);
		//添加边框
//		Paint paint=new Paint();
//		paint.setAntiAlias(true);
//		paint.setStyle(Style.STROKE);
//		paint.setColor(Color.BLUE);
//		paint.setStrokeWidth(4f);
//		canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2-2f, paint);
	}
	
	
	
	/**
	 * getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，
	 * 提供一下尺寸，默认为-1可不是我们希望的。setBounds就是去设置下绘制的范围。
	 */
	@Override
	public int getIntrinsicHeight() {
		return mWidth;
	}
	
	@Override
	public int getIntrinsicWidth() {
		return mWidth;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

}
