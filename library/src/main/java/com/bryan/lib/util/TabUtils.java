/*
 *
 * COPYRIGHT NOTICE
 * Copyright (C) 2015, bryan <690158801@qq.com>
 * https://github.com/cxbiao/Android-common
 *
 * @license under the Apache License, Version 2.0
 *
 * @version 1.0
 * @author  bryan
 * @date    2015/08/26
 *
 */

package com.bryan.lib.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

/**
 * 
 * @author bryan
 * 时间:2015年8月26日
 * 类型:TabUtils
 * 说明:迅速创建底部Tab页
 */
public class TabUtils {
	
	private ViewGroup tabGroup;
	
	private FragmentManager fragmentManager;
	private List<Fragment> fragments;
	private int containerViewId;
	private int currentIndex;
	private OnTabItemListener listener;
	
	public interface OnTabItemListener {
		void onClick(View view, int index);
	}
	
	public TabUtils(FragmentManager fragmentManager, List<Fragment> fragments, int container){
		this.fragmentManager=fragmentManager;
		this.fragments=fragments;
		this.containerViewId=container;
	}
	
	
	
	public void setTabGroup(ViewGroup tabGroup) {
		this.tabGroup = tabGroup;
	}

   
    
	public void setTabListener(OnTabItemListener listener) {
		this.listener = listener;
	}



	public void buildTab(){
		if(tabGroup==null || tabGroup.getChildCount()<1){
			return;
		}
		for(int i=0;i<tabGroup.getChildCount();i++){
			final int index=i;
			tabGroup.getChildAt(i).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setTab(index);
					if(currentIndex!=index && listener != null){
						listener.onClick(v, index);
					}
				}
			});
		}
		getRealChild(tabGroup.getChildAt(0)).setSelected(true);
		fragmentManager.beginTransaction().add(containerViewId,fragments.get(0)).commit();
	}
	
	public void setTab(int index){
		if(currentIndex==index){
			return;
		}
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for(Fragment fragment:fragments){
			if (fragment.isAdded()) {
				transaction.hide(fragment);
			}
		}
		if (!fragments.get(index).isAdded()) {
			transaction.add(containerViewId, fragments.get(index));
		}
		getRealChild(tabGroup.getChildAt(currentIndex)).setSelected(false);
		getRealChild(tabGroup.getChildAt(index)).setSelected(true);
		currentIndex=index;
		transaction.show(fragments.get(index)).commit();
	}
	
	
	private View getRealChild(View child){
		View realChild=null;
		//对于viewgroup而言,内部不要有可以获取点击事件的控件  android:clickable="false"
		if(child instanceof ViewGroup){
			ViewGroup group=(ViewGroup) child;
			
			if(group.getChildCount()==1){
				realChild=((ViewGroup)child).getChildAt(0);
			}
			//viewgroup大于1的时候要设置一个tag作为标记
			else if(group.getChildCount()>1){
				for(int i=0;i<group.getChildCount();i++){
					if(group.getChildAt(i).getTag()!=null){
						realChild=group.getChildAt(i);
						break;
					}
				}
				
			}else{
				realChild=child;
			}
			
		}else if(child instanceof View){
			realChild=child;
		}
		return realChild;
	}
	
}
