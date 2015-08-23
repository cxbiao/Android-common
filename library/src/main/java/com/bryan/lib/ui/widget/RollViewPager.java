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
 * @author bryan
 * 时间:2015年6月5日
 * 类型:RollViewPager
 */
public class RollViewPager extends ViewPager {
	
	
	private List<ImageView> imgList=new ArrayList<ImageView>();
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
				
				index%=imgList.size();
				if(pointGroup!=null){
					pointGroup.getChildAt(index).setEnabled(false);
					pointGroup.getChildAt(oldPosition).setEnabled(true);
				}
				oldPosition=index;
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
	public RollViewPager(Context context, LinearLayout pointll,OnViewClickListener clickListener) {
			super(context);
			this.clickListener = clickListener;	
			this.pointGroup=pointll;
			initView();
			
    }
		
		
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
		
			int pos=getCurrentItem()+1;
			setCurrentItem(pos);
			//一直维护3秒跳跃一次的操作
			startRoll();
		};
	};
	private int downX;
	private int downY;
	private OnViewClickListener clickListener;
	
	public interface OnViewClickListener{
		//业务逻辑对应操作
		public void viewClickListener(int pos);
	}
	
	//界面移除出界面时候调用的方法
	protected void onDetachedFromWindow() {
		//取消handler中维护任务
		System.out.println("onDetachedFromWindow");
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	};
	
	
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
		if(imgList==null || imgList.size()==0){
			return;
		}
		if(myPagerAdapter == null){
			myPagerAdapter = new MyPagerAdapter();
			this.setAdapter(myPagerAdapter);
		}else{
			myPagerAdapter.notifyDataSetChanged();
		}
		
		handler.sendEmptyMessageDelayed(0, 3000);
	}
	
	
	public void stopRoll(){
		handler.removeCallbacksAndMessages(null);
	}
	class MyPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container,  int position) {
			
			
			final int index=position%imgList.size();
			ImageView view=imgList.get(index);		
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
						if(upX == downX && upTime-downTime<500){
							System.out.println(index);
							if(clickListener!=null){
								
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
			container.removeView((View)object);
		}
	}
}
