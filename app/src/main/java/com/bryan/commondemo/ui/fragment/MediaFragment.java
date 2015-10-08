package com.bryan.commondemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bryan.commondemo.R;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
