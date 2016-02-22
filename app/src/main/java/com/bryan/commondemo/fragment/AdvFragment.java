package com.bryan.commondemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bryan.commondemo.R;
import com.bryan.commondemo.widget.AdvBanner;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bryan on 2016-02-19.
 */
public class AdvFragment extends BaseFragment {

    @Bind(R.id.advBanner)
    AdvBanner advBanner;
    @Bind(R.id.ll_pg)
    LinearLayout ll_Pg;

    private Integer[] imgSrcs={R.mipmap.img1,R.mipmap.img2,R.mipmap.img3,R.mipmap.img4,R.mipmap.img5};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adv, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewPages();
    }

    private void setViewPages() {

         advBanner.setSource(Arrays.asList(imgSrcs));
         for(int i=0;i<imgSrcs.length;i++){

             ImageView point = new ImageView(getActivity());
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
             params.rightMargin = 10;
             point.setLayoutParams(params);
             point.setBackgroundResource(R.drawable.point_bg);
             ll_Pg.addView(point);
         }
        advBanner.setPointGroup(ll_Pg);
        advBanner.startRoll();
        advBanner.setClickListener(new AdvBanner.OnViewClickListener() {
            @Override
            public void viewClickListener(int pos) {
                Toast.makeText(getActivity(), pos + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
