package com.bryan.commondemo;

import com.bryan.commondemo.ui.ErrorActivity;
import com.bryan.lib.ui.BaseApplication;
import com.bryan.lib.util.CrashHolder;

/**
 * Created by bryan on 2015/9/3.
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHolder.getInstance().install(this).setErrorActivityClass(ErrorActivity.class);

    }
}
