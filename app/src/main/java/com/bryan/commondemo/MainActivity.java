package com.bryan.commondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bryan.commondemo.adapter.HomeAdapter;
import com.bryan.commondemo.domain.ItemInfo;
import com.bryan.lib.adapter.recyclerview.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 代码库的演示工程
 */
public class MainActivity extends AppCompatActivity {


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
                Toast.makeText(getBaseContext(),position+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void initDatas() {
        for(int i=0;i<10;i++){
            ItemInfo item=new ItemInfo();
            item.setInfo("info"+i);
            mDatas.add(item);
        }
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
