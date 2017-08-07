package com.bryan.commondemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bryan.commondemo.R;
import com.bryan.commondemo.adapter.HomeAdapter;
import com.bryan.commondemo.model.ItemInfo;
import com.bryan.lib.adapter.recyclerview.BaseQuickAdapter;
import com.bryan.lib.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 代码库的演示工程
 */
public class MainActivity extends BaseActivity {



    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemInfo> mDatas = new ArrayList<ItemInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDatas();
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new HomeAdapter(this, mDatas, R.layout.main_item);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new BaseQuickAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (mDatas.get(position).isActivity) {
                        Class clazz = Class.forName(mDatas.get(position).activityName);
                        Intent intent = new Intent(MainActivity.this, clazz);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(MainActivity.this, SingleFragmentActivity.class);
                        intent.putExtra("iteminfo", mDatas.get(position));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void initDatas() {

        ItemInfo itemInfo0 = new ItemInfo();
        itemInfo0.info = "广告图";
        itemInfo0.fragmentName = "com.bryan.commondemo.fragment.AdvFragment";
        mDatas.add(itemInfo0);


        ItemInfo itemInfo1 = new ItemInfo();
        itemInfo1.info = "图像裁剪";
        itemInfo1.isActivity = true;
        itemInfo1.activityName = "com.bryan.commondemo.activity.CropImageActivity";
        mDatas.add(itemInfo1);

        ItemInfo itemInfo2 = new ItemInfo();
        itemInfo2.info = "各类对话框";
        itemInfo2.fragmentName = "com.bryan.commondemo.fragment.MyDialogFragment";
        mDatas.add(itemInfo2);

        ItemInfo itemInfo3 = new ItemInfo();
        itemInfo3.info = "wheelview实例";
        itemInfo3.fragmentName = "com.bryan.commondemo.fragment.WheelViewFragment";
        mDatas.add(itemInfo3);


        ItemInfo itemInfo4 = new ItemInfo();
        itemInfo4.info = "多媒体";
        itemInfo4.fragmentName = "com.bryan.commondemo.fragment.MediaFragment";
        mDatas.add(itemInfo4);

        ItemInfo itemInfo5 = new ItemInfo();
        itemInfo5.info = "进度度";
        itemInfo5.isActivity = true;
        itemInfo5.activityName = "com.bryan.commondemo.activity.ProgressBarActivity";
        mDatas.add(itemInfo5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
