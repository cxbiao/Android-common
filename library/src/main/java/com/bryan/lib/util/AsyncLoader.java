
package com.bryan.lib.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * loader 内部类继承时需要是静态类
 * 执行顺序
 * onCreateLoader
 * loader构造函数
 * onStartLoading
 * loadInBackground
 * deliverResult
 * onLoadFinished
 * onStopLoading
 * onLoaderReset
 * onReset
 * onStopLoading
 * releaseResources
 *
 * @param <D>
 */
public abstract class AsyncLoader<D> extends AsyncTaskLoader<D> {

    private D data;

    /**
     * Create async loader
     *
     * @param context
     */
    public AsyncLoader(final Context context) {
        super(context);
    }

    @Override
    public void deliverResult(final D data) {
        if (isReset())
            // An async query came in while the loader is stopped
            return;

        this.data = data;

        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (data != null)
            deliverResult(data);

        if (takeContentChanged() || data == null)
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        data = null;
    }
}