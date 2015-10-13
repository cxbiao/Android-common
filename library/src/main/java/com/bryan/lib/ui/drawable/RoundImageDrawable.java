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

public class RoundImageDrawable extends Drawable{

	
	
	private Paint mPaint;
	private Bitmap mBitmap;
	private float roundWidth=30;
	private float roundHeight=30;
	
	private RectF rectF;
	
	public RoundImageDrawable(Bitmap bitmap){
		setUpShader(bitmap);
		
	}

	public RoundImageDrawable(Bitmap bitmap,float width,float height){
		setUpShader(bitmap);
		this.roundWidth=width;
		this.roundHeight=height;


	}

	private void setUpShader(Bitmap bitmap){
		mBitmap=bitmap;
		BitmapShader bitmapShader=new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(rectF, roundWidth, roundHeight, mPaint);
		
		
	}
	
	
	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		rectF=new RectF(left,top,right,bottom);
	}
	
	/**
	 * getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，
	 * 提供一下尺寸，默认为-1可不是我们希望的。setBounds就是去设置下绘制的范围。
	 */
	@Override
	public int getIntrinsicHeight() {
		return mBitmap.getHeight();
	}
	
	@Override
	public int getIntrinsicWidth() {
		return mBitmap.getWidth();
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
