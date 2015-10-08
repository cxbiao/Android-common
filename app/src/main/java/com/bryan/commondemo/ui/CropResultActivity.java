package com.bryan.commondemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bryan.commondemo.R;
import com.bryan.lib.ui.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CropResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_result);
        Intent intent=getIntent();
        String img=intent.getStringExtra("img");
        ImageLoader.getInstance().displayImage("file://"+img, (ImageView) findViewById(R.id.result_image));

    }
}
