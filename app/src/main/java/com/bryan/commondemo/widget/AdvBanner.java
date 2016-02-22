package com.bryan.commondemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bryan.lib.ui.widget.BaseRollPager;

/**
 * Author：Cxb on 2016/2/22 09:31
 */
public class AdvBanner extends BaseRollPager<Integer> {


    public AdvBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvBanner(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(ViewGroup container, int position) {
        ImageView img=new ImageView(getContext());
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(list.get(position));
        return img;
    }
}
