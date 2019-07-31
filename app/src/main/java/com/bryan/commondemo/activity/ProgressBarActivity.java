package com.bryan.commondemo.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.bryan.commondemo.R;
import com.bryan.lib.ui.widget.HorizontalProgressBarWithNumber;
import com.bryan.lib.ui.widget.RoundProgressBarWidthNumber;

/**
 * Author：Cxb on 2017/8/2 10:42
 */

public class ProgressBarActivity extends AppCompatActivity {

    private RoundProgressBarWidthNumber mRoundProgressBar;

    private HorizontalProgressBarWithNumber mProgressBar;
    private static final int MSG_PROGRESS_UPDATE = 0x110;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int progress = mProgressBar.getProgress();
            int roundProgress = mRoundProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
           mRoundProgressBar.setProgress(++roundProgress);
            if (progress >= 100) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);
        mProgressBar = (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);
        mRoundProgressBar = (RoundProgressBarWidthNumber) findViewById(R.id.id_progress02);
        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);

    }


}
