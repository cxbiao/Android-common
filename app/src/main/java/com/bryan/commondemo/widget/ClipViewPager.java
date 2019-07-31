package com.bryan.commondemo.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by HanHailong on 15/9/27.
 */
public class ClipViewPager extends ViewPager {


    private ViewClickListener listener;
    public ClipViewPager(Context context) {
        super(context);
    }

    public ClipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewClickListener getListener() {
        return listener;
    }

    public void setListener(ViewClickListener listener) {
        this.listener = listener;
    }


    float downX;
    float downY;
    long downTime;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case  MotionEvent.ACTION_DOWN:
                downX=ev.getX();
                downY=ev.getY();
                downTime = System.currentTimeMillis();
                break;
            case  MotionEvent.ACTION_UP:
                int index = viewOfClickOnScreen(ev);
                if (index != -1) {
                    float upX =  ev.getX();
                    float upY=ev.getY();
                    long upTime = System.currentTimeMillis();
                    if (Math.abs(upX - downX)< ViewConfiguration.get(getContext()).getScaledTouchSlop()
                            &&Math.abs(upY - downY)< ViewConfiguration.get(getContext()).getScaledTouchSlop()
                          &&upTime - downTime < 500) {
                        if(listener!=null){
                            listener.onClick(index);
                        }
                    }
                    if (getCurrentItem() != index) {
                        setCurrentItem(index);
                    }
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * @param ev
     * @return
     */
    private int viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        int[] pageLocation=new int[2];
        getLocationOnScreen(pageLocation);
        for (int i = 0; i < childCount; i++) {
            View v = findViewWithTag(i);
            if(v==null) continue;
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = location[1];

            int maxX = location[0] + v.getWidth();
            int maxY = location[1]+ v.getHeight();

            float x = ev.getRawX();
            float y = ev.getRawY();

            //如果坐标位于viewpager内部，则直接返回当前view,不再继续查找
            if ((x >=pageLocation[0] && x <=pageLocation[0]+getWidth()) && (y >=pageLocation[1] && y <=pageLocation[1] +getHeight())) {
                 return getCurrentItem();
            }
            //父控件传递来的事件
            else if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                  return i;
            }


        }
        return -1;
    }

     public  interface ViewClickListener {
         void onClick(int position);
    }
}
