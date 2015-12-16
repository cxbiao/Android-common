package com.bryan.commondemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bryan.commondemo.R;
import com.bryan.commondemo.model.ItemInfo;
import com.bryan.lib.ui.BaseActivity;

/**
 * Created by bryan on 2015/7/12.
 */
public  class SingleFragmentActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fm=getSupportFragmentManager();
        if(fm.findFragmentById(R.id.contatiner)==null){
            fm.beginTransaction().add(R.id.contatiner,createFragment()).commit();
        }

    }

    public  Fragment createFragment(){
       Fragment fragment=null;
        Intent intent=getIntent();
       ItemInfo itemInfo= intent.getParcelableExtra("iteminfo");
        try {
            Class clazz=Class.forName(itemInfo.fragmentName);
            fragment= (Fragment) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }
}
