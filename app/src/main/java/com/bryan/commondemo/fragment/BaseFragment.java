package com.bryan.commondemo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2015/10/8.
 */
public class BaseFragment extends Fragment {

    protected Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }
}
