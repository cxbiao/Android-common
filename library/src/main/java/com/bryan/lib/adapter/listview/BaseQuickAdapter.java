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
 * @date    2015/09/25
 *
 */

package com.bryan.lib.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseQuickAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public BaseQuickAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount()
	{
		return (mDatas==null||mDatas.size()==0)?0:mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		if (position >= mDatas.size()) return null;
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final BaseAdapterHelper viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}

	public void setItems(List<T> data){
		this.mDatas=data;
	}

	public abstract void convert(BaseAdapterHelper helper, T item);

	private BaseAdapterHelper getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		return BaseAdapterHelper.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
