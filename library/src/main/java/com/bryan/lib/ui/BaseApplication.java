package com.bryan.lib.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by bryan on 2015/9/3.
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private int appCount = 0;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        appCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        appCount--;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public int getAppCount() {
        return appCount;
    }


}
