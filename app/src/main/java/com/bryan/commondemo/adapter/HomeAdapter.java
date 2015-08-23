package com.bryan.commondemo.adapter;

import android.content.Context;

import com.bryan.commondemo.R;
import com.bryan.commondemo.domain.ItemInfo;
import com.bryan.lib.adapter.recyclerview.BaseAdapterHelper;
import com.bryan.lib.adapter.recyclerview.BaseQuickAdapter;

import java.util.List;

/**
 * Created by bryan on 2015/8/23.
 */
public class HomeAdapter extends BaseQuickAdapter<ItemInfo>{


    public HomeAdapter(Context context, List<ItemInfo> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(BaseAdapterHelper helper, ItemInfo item) {
        helper.getTextView(R.id.info).setText(item.getInfo());

    }
}
