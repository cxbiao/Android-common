package com.bryan.commondemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bryan.commondemo.R;
import com.bryan.commondemo.adapter.HomeAdapter;
import com.bryan.commondemo.domain.ItemInfo;
import com.bryan.lib.adapter.recyclerview.BaseQuickAdapter;
import com.bryan.lib.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 代码库的演示工程
 */
public class MainActivity extends BaseActivity {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemInfo> mDatas=new ArrayList<ItemInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDatas();
        layoutManager=new LinearLayoutManager(this);
        mAdapter=new HomeAdapter(this,mDatas,R.layout.main_item);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new BaseQuickAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if(mDatas.get(position).isActivity()==1){
                        Class clazz=Class.forName(mDatas.get(position).getActivityName());
                        Intent intent=new Intent(MainActivity.this,clazz);
                        startActivity(intent);

                    }else{
                        Intent intent=new Intent(MainActivity.this,SingleFragmentActivity.class);
                        intent.putExtra("iteminfo",mDatas.get(position));
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
        ItemInfo itemInfo1=new ItemInfo();
        itemInfo1.setInfo("图像裁剪");
        itemInfo1.setIsActivity(1);
        itemInfo1.setActivityName("com.bryan.commondemo.ui.CropImageActivity");
        mDatas.add(itemInfo1);

        ItemInfo itemInfo2=new ItemInfo();
        itemInfo2.setInfo("各类对话框");
        itemInfo2.setFragmentName("com.bryan.commondemo.ui.fragment.MyDialogFragment");
        mDatas.add(itemInfo2);

        ItemInfo itemInfo3=new ItemInfo();
        itemInfo3.setInfo("wheelview实例");
        itemInfo3.setFragmentName("com.bryan.commondemo.ui.fragment.WheelViewFragment");
        mDatas.add(itemInfo3);


        ItemInfo itemInfo4=new ItemInfo();
        itemInfo4.setInfo("多媒体");
        itemInfo4.setFragmentName("com.bryan.commondemo.ui.fragment.MediaFragment");
        mDatas.add(itemInfo4);
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
