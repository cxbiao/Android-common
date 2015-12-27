package com.bryan.commondemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bryan.commondemo.R;
import com.bryan.lib.image.ImageUtils;
import com.bryan.lib.ui.BaseActivity;


public class CropResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_result);
        Intent intent=getIntent();
        final String img=intent.getStringExtra("img");
        final ImageView iv=(ImageView) findViewById(R.id.result_image);
        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap bitmap=ImageUtils.decodeSampledBitmapFromFile(img, iv.getWidth(), iv.getHeight());
                iv.setImageBitmap(bitmap);
                iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }
}
