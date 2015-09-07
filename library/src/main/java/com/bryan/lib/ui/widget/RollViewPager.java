package com.bryan.lib.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
/**
 * 自定义viewpager，实现图片轮播
 *
 * @author cxb
 * 时间:2015年6月5日
 * 类型:RollViewPager
 * <p/>
 * 用法
 * protected void setViewPages(final List<Advertisement> advList) {
 * for (int i = 0; i <advList.size(); i++) {
 * // 添加指示点
 * ImageView point = new ImageView(mActivity);
 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
 * LinearLayout.LayoutParams.WRAP_CONTENT,
 * LinearLayout.LayoutParams.WRAP_CONTENT);
 * <p/>
 * params.rightMargin = 10;
 * point.setLayoutParams(params);
 * point.setBackgroundResource(R.drawable.point_bg);
 * pointGroup.addView(point);
 * }
 * <p/>
 * if(advList.size()<=1){
 * pointGroup.setVisibility(View.GONE);
 * }
 * List<ImageView> imgList=new ArrayList<ImageView>();
 * for(int i=0;i<advList.size();i++){
 * Advertisement adv=advList.get(i);
 * final ImageView img=new ImageView(mActivity);
 * img.setScaleType(ScaleType.CENTER_CROP);
 * ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){
 * @Override public void onLoadingComplete(String imageUri, View view,
 * Bitmap loadedImage) {
 * if (loadedImage != null) {
 * BitmapDrawable drawable = new BitmapDrawable(loadedImage);
 * img.setBackgroundDrawable(drawable);
 * img.setImageBitmap(null);
 * } else {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * }
 * @Override public void onLoadingFailed(String imageUri, View view,
 * FailReason failReason) {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * <p/>
 * });
 * imgList.add(img);
 * }
 * <p/>
 * if(advList.size()==2){
 * <p/>
 * for(int i=0;i<2;i++){
 * Advertisement adv=advList.get(i);
 * final ImageView img=new ImageView(mActivity);
 * img.setScaleType(ScaleType.CENTER_CROP);
 * ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){
 * @Override public void onLoadingComplete(String imageUri, View view,
 * Bitmap loadedImage) {
 * if (loadedImage != null) {
 * BitmapDrawable drawable = new BitmapDrawable(loadedImage);
 * img.setBackgroundDrawable(drawable);
 * img.setImageBitmap(null);
 * } else {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * }
 * @Override public void onLoadingFailed(String imageUri, View view,
 * FailReason failReason) {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * <p/>
 * });
 * imgList.add(img);
 * }
 * <p/>
 * }
 * viewPager.setImgList(imgList);
 * viewPager.setPointGroup(pointGroup);
 * viewPager.startRoll();
 * <p/>
 * <p/>
 * <p/>
 * viewPager.setClickListener(new OnViewClickListener() {
 * @Override public void viewClickListener(int pos) {
 * CommomUtils.goToPages(advList.get(pos), mActivity);
 * }
 * });
 * <p/>
 * isPrepared=true;
 * <p/>
 * }
 * <p/>
 * protected void setViewPages(final List<Advertisement> advList) {
 * for (int i = 0; i <advList.size(); i++) {
 * // 添加指示点
 * ImageView point = new ImageView(mActivity);
 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
 * LinearLayout.LayoutParams.WRAP_CONTENT,
 * LinearLayout.LayoutParams.WRAP_CONTENT);
 * <p/>
 * params.rightMargin = 10;
 * point.setLayoutParams(params);
 * point.setBackgroundResource(R.drawable.point_bg);
 * pointGroup.addView(point);
 * }
 * <p/>
 * if(advList.size()<=1){
 * pointGroup.setVisibility(View.GONE);
 * }
 * List<ImageView> imgList=new ArrayList<ImageView>();
 * for(int i=0;i<advList.size();i++){
 * Advertisement adv=advList.get(i);
 * final ImageView img=new ImageView(mActivity);
 * img.setScaleType(ScaleType.CENTER_CROP);
 * ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){
 * @Override public void onLoadingComplete(String imageUri, View view,
 * Bitmap loadedImage) {
 * if (loadedImage != null) {
 * BitmapDrawable drawable = new BitmapDrawable(loadedImage);
 * img.setBackgroundDrawable(drawable);
 * img.setImageBitmap(null);
 * } else {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * }
 * @Override public void onLoadingFailed(String imageUri, View view,
 * FailReason failReason) {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * <p/>
 * });
 * imgList.add(img);
 * }
 * <p/>
 * if(advList.size()==2){
 * <p/>
 * for(int i=0;i<2;i++){
 * Advertisement adv=advList.get(i);
 * final ImageView img=new ImageView(mActivity);
 * img.setScaleType(ScaleType.CENTER_CROP);
 * ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){
 * @Override public void onLoadingComplete(String imageUri, View view,
 * Bitmap loadedImage) {
 * if (loadedImage != null) {
 * BitmapDrawable drawable = new BitmapDrawable(loadedImage);
 * img.setBackgroundDrawable(drawable);
 * img.setImageBitmap(null);
 * } else {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * }
 * @Override public void onLoadingFailed(String imageUri, View view,
 * FailReason failReason) {
 * img.setBackgroundResource(R.drawable.ic_empty);
 * }
 * <p/>
 * });
 * imgList.add(img);
 * }
 * <p/>
 * }
 * viewPager.setImgList(imgList);
 * viewPager.setPointGroup(pointGroup);
 * viewPager.startRoll();
 * <p/>
 * <p/>
 * <p/>
 * viewPager.setClickListener(new OnViewClickListener() {
 * @Override public void viewClickListener(int pos) {
 * CommomUtils.goToPages(advList.get(pos), mActivity);
 * }
 * });
 * <p/>
 * isPrepared=true;
 * <p/>
 * }
 */
/**
 * protected void setViewPages(final List<Advertisement> advList) {
 for (int i = 0; i <advList.size(); i++) {
 // 添加指示点
 ImageView point = new ImageView(mActivity);
 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
 LinearLayout.LayoutParams.WRAP_CONTENT,
 LinearLayout.LayoutParams.WRAP_CONTENT);

 params.rightMargin = 10;
 point.setLayoutParams(params);
 point.setBackgroundResource(R.drawable.point_bg);
 pointGroup.addView(point);
 }

 if(advList.size()<=1){
 pointGroup.setVisibility(View.GONE);
 }
 List<ImageView> imgList=new ArrayList<ImageView>();
 for(int i=0;i<advList.size();i++){
 Advertisement adv=advList.get(i);
 final ImageView img=new ImageView(mActivity);
 img.setScaleType(ScaleType.CENTER_CROP);
 ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){

@Override public void onLoadingComplete(String imageUri, View view,
Bitmap loadedImage) {
if (loadedImage != null) {
BitmapDrawable drawable = new BitmapDrawable(loadedImage);
img.setBackgroundDrawable(drawable);
img.setImageBitmap(null);
} else {
img.setBackgroundResource(R.drawable.ic_empty);
}
}

@Override public void onLoadingFailed(String imageUri, View view,
FailReason failReason) {
img.setBackgroundResource(R.drawable.ic_empty);
}

});
 imgList.add(img);
 }

 if(advList.size()==2){

 for(int i=0;i<2;i++){
 Advertisement adv=advList.get(i);
 final ImageView img=new ImageView(mActivity);
 img.setScaleType(ScaleType.CENTER_CROP);
 ImageLoader.getInstance().displayImage(adv.getImg(), img,ImageLoaderHelper.getDisplayImageOptions(R.drawable.ic_empty),new SimpleImageLoadingListener(){

@Override public void onLoadingComplete(String imageUri, View view,
Bitmap loadedImage) {
if (loadedImage != null) {
BitmapDrawable drawable = new BitmapDrawable(loadedImage);
img.setBackgroundDrawable(drawable);
img.setImageBitmap(null);
} else {
img.setBackgroundResource(R.drawable.ic_empty);
}
}

@Override public void onLoadingFailed(String imageUri, View view,
FailReason failReason) {
img.setBackgroundResource(R.drawable.ic_empty);
}

});
 imgList.add(img);
 }

 }
 viewPager.setImgList(imgList);
 viewPager.setPointGroup(pointGroup);
 viewPager.startRoll();



 viewPager.setClickListener(new OnViewClickListener() {

@Override public void viewClickListener(int pos) {
CommomUtils.goToPages(advList.get(pos), mActivity);
}
});

 isPrepared=true;

 }
 */

/**
 * 一张图和二张图的时候特殊处理
 * 二张图  1 2 1 2这样变成4张图
 */
public class RollViewPager extends ViewPager {


    private List<ImageView> imgList = new ArrayList<ImageView>();
    private LinearLayout pointGroup;

    private MyPagerAdapter myPagerAdapter;

    //需要维护的页面指向的索引值
    private int oldPosition = 0;

    public RollViewPager(Context context) {
        super(context);
        initView();
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {


        this.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {

                if (pointGroup == null || pointGroup.getChildCount() < 2) {
                    return;
                }
                if (imgList.size() > pointGroup.getChildCount()) {
                    index %= pointGroup.getChildCount();
                } else {
                    index %= imgList.size();
                }
                pointGroup.getChildAt(index).setEnabled(false);
                pointGroup.getChildAt(oldPosition).setEnabled(true);
                oldPosition = index;
                //Log.e("TAG", "old:"+oldPosition);
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
    public RollViewPager(Context context, LinearLayout pointll, OnViewClickListener clickListener) {
        super(context);
        this.clickListener = clickListener;
        this.pointGroup = pointll;
        initView();

    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            int pos = getCurrentItem() + 1;
            setCurrentItem(pos, true);
            //一直维护3秒跳跃一次的操作
            startRoll();
        }

        ;
    };
    private int downX;
    private int downY;
    private OnViewClickListener clickListener;

    public interface OnViewClickListener {
        //业务逻辑对应操作
        public void viewClickListener(int pos);
    }

    //界面移除出界面时候调用的方法
    protected void onDetachedFromWindow() {
        //取消handler中维护任务
        System.out.println("onDetachedFromWindow");
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    ;


    public void setImgList(List<ImageView> imgList) {
        this.imgList = imgList;

    }


    public void setPointGroup(LinearLayout pointGroup) {
        this.pointGroup = pointGroup;
        pointGroup.getChildAt(0).setEnabled(false);
    }


    public void setClickListener(OnViewClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public void startRoll() {
        //设置数据适配器
        if (imgList == null || imgList.size() == 0) {
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
        if (imgList.size() > 2)
            handler.sendEmptyMessageDelayed(0, 3000);
    }


    public void stopRoll() {
        handler.removeCallbacksAndMessages(null);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (imgList.size() < 3)
                return imgList.size();
            else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            //Log.e("TAG", "viewpage:"+position);

            int viewIndex = position % imgList.size();
            ImageView view = imgList.get(viewIndex);
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
                            handler.removeCallbacksAndMessages(null);
                            downTime = System.currentTimeMillis();
                            downX = (int) event.getX();
                            break;
                        //如果有做滑动操作，最后view不会触发ACTION_UP操作，而会去触发ACTION_CANCEL
                        case MotionEvent.ACTION_UP:

                            startRoll();
                            int upX = (int) event.getX();

                            long upTime = System.currentTimeMillis();
                            if (upX == downX && upTime - downTime < 500) {
                                if (clickListener != null) {
                                    int index = 0;
                                    if (pointGroup != null && imgList.size() > pointGroup.getChildCount()) {
                                        index = position % pointGroup.getChildCount();
                                    } else {
                                        index = position % imgList.size();
                                    }
                                    clickListener.viewClickListener(index);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:

                            startRoll();
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