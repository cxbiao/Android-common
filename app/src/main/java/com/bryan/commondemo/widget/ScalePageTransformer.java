package com.bryan.commondemo.widget;

import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by HanHailong on 15/9/27.
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.0f;
    public static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View page, float position) {


       // Log.e("ScalePageTransformer",position+"");
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        //[-1,1]

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
      // page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
