package com.bryan.commondemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryan.commondemo.R;
import com.bryan.commondemo.activity.AutoAudioActivity;
import com.bryan.lib.audio.AudioActivity;
import com.bryan.lib.camera.RecordVideoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/10/8.
 */
public class MediaFragment extends BaseFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.activity_media, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.videoRecord)
        public void videoRecord(View v){
        Intent intent=new Intent(context, RecordVideoActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.audioRecord)
    public void audioRecord(View v){
        Intent intent=new Intent(context, AudioActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.autoRecord)
    public void autoRecord(View v){
        Intent intent=new Intent(context, AutoAudioActivity.class);
        startActivity(intent);
    }


}
