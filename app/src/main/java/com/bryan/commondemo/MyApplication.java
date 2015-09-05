package com.bryan.commondemo;

import android.app.Application;

import com.bryan.lib.image.ImageLoaderHelper;

/**
 * Created by bryan on 2015/9/3.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderHelper.initImageLoaderConfiguration(this);

    }
}
