package com.bryan.lib.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * 自定义viewpager，实现图片轮播
 * @author cxb
 * 时间:2015年6月5日
 */


public abstract  class BaseRollPager<T> extends ViewPager {

    private static final String TAG = "BaseRollPager";
    private  int TOTAL_PAGER_SIZE;
    private LinearLayout pointGroup;
    protected List<T> list = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    private boolean isAutoScrolling=true;
    private int timeSpan=5000;
    protected int scrollSpeed = 450;

    //需要维护的页面指向的索引值
    private int oldPosition = 0;

    public BaseRollPager(Context context) {
        super(context);
        initView();
    }

    public BaseRollPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {

        setScrollSpeed();
        this.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {

                if (pointGroup == null || pointGroup.getChildCount() < 2) {
                    return;
                }
                index--;
               // Log.e(TAG, "index:"+index);
                if(index<0 || index>list.size()-1) return;
                pointGroup.getChildAt(oldPosition).setEnabled(true);
                pointGroup.getChildAt(index).setEnabled(false);
                oldPosition = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }


    //点所在的集合传递进去，由当前类单独管理
    public BaseRollPager(Context context, LinearLayout pointll, OnViewClickListener clickListener) {
        super(context);
        this.clickListener = clickListener;
        this.pointGroup = pointll;
        initView();

    }

    public T setSource(List<T> list) {
        this.list = list;
        if(list!=null){
             TOTAL_PAGER_SIZE=(list.size()==1)?1:list.size()+2;
        }
        return (T) this;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public void setIsAutoScrolling(boolean isAutoScrolling) {
        this.isAutoScrolling = isAutoScrolling;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            int pos = getCurrentItem() + 1;
            if (pos == TOTAL_PAGER_SIZE - 1) {
                setCurrentItem(1,false);
            } else {
                setCurrentItem(pos);
            }
            //一直维护3秒跳跃一次的操作
            goOnScroll();
        }

    };

    private OnViewClickListener clickListener;

    public interface OnViewClickListener {
        //业务逻辑对应操作
        void viewClickListener(int pos);
    }

    //界面移除出界面时候调用的方法
    protected void onDetachedFromWindow() {
        //取消handler中维护任务
        System.out.println("onDetachedFromWindow");
        pauseScroll();
        super.onDetachedFromWindow();
    }






    public void setPointGroup(LinearLayout pointGroup) {
        this.pointGroup = pointGroup;
        if(pointGroup!=null){
            pointGroup.getChildAt(0).setEnabled(false);
        }
        if(pointGroup!=null && pointGroup.getChildCount()<2){
            pointGroup.setVisibility(View.GONE);
        }

    }


    public void setClickListener(OnViewClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public void startRoll() {
        //设置数据适配器
        if (list==null || list.size()<=0) {
            return;
        }
        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter();
            this.setAdapter(myPagerAdapter);
            //切换动画效果,用不用随你,各种效果都能做
           //setPageTransformer(true, new DepthPageTransformer());
        } else {
            myPagerAdapter.notifyDataSetChanged();
        }
        setCurrentItem(1,false);
        goOnScroll();
    }


    public void goOnScroll(){
        if (list == null || list.size() <= 1) {
            return;
        }
        if(!isAutoScrolling) return;
        handler.sendEmptyMessageDelayed(0, timeSpan);
    }

    public void pauseScroll(){
        handler.removeCallbacksAndMessages(null);
    }


    protected abstract View onCreateView(ViewGroup container, int position);

    /** set scroll speed */
    private void setScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            FixedSpeedScroller myScroller = new FixedSpeedScroller(getContext(), interpolator, scrollSpeed);
            mScroller.set(this, myScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
           return TOTAL_PAGER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

           //在总数上多添加2张图片  [5,1,2,3,4,5,1]
            View view;
            if(list.size()==1){
                view=onCreateView(container, 0);
                container.addView(view);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) {
                            clickListener.viewClickListener(0);
                        }
                    }
                });
                return view;
            }

            if(position==0){
                view=onCreateView(container, list.size()-1);
            }else if(position==TOTAL_PAGER_SIZE-1){
                view=onCreateView(container, 0);
            }else {
                view=onCreateView(container, position-1);
            }
            container.addView(view);

            view.setOnTouchListener(new OnTouchListener() {
                private int downX;
                private long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //当前控件要去响应事件
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //手指点中，不能滑动，handler不去维护任务达到不去滑动的目的
                            pauseScroll();
                            downTime = System.currentTimeMillis();
                            downX = (int) event.getX();
                            break;
                        //如果有做滑动操作，最后view不会触发ACTION_UP操作，而会去触发ACTION_CANCEL
                        case MotionEvent.ACTION_UP:

                           goOnScroll();
                            int upX = (int) event.getX();

                            long upTime = System.currentTimeMillis();
                            if (Math.abs(upX - downX)< ViewConfiguration.get(getContext()).getScaledTouchSlop() && upTime - downTime < 500) {
                                if (clickListener != null) {
                                    clickListener.viewClickListener(position-1);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            goOnScroll();
                            break;
                    }
                    return true;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //这种方法加PageTransformer动画，从第一张右滑到最后一张有问题
        @Override
        public void finishUpdate(ViewGroup container) {
            if(list.size()==1) return;
            int position = getCurrentItem();
            Log.d(TAG, "finish update before, position=" + position);
            if (position == 0) {
                position = list.size();
                setCurrentItem(position,false);
            } else if (position == TOTAL_PAGER_SIZE - 1) {
                setCurrentItem(1,false);
            }
            Log.d(TAG, "finish update after, position=" + position);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    //使用了view动画,至少api 11,11以下也有方案,但暂时不需要
    public class DepthPageTransformer implements PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();

            if (position < -1) { // [-&,-1)
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                view.setAlpha(1 - position);

                view.setTranslationX(pageWidth * -position);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+&]
                view.setAlpha(0);
            }
        }
    }
}
