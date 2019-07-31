package com.bryan.commondemo.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bryan.commondemo.R;
import com.bryan.commondemo.widget.AdvBanner;
import com.bryan.commondemo.widget.ClipViewPager;
import com.bryan.commondemo.widget.ScalePageTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bryan on 2016-02-19.
 */
public class AdvFragment extends BaseFragment {


    @BindView(R.id.advBanner)
    AdvBanner advBanner;
    @BindView(R.id.ll_pg)
    LinearLayout ll_Pg;
    @BindView(R.id.viewpager)
    ClipViewPager mViewPager;
    @BindView(R.id.page_container)
    RelativeLayout pageContainer;
    private Integer[] imgSrcs = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4, R.mipmap.img5};
    private MyAdapter mPagerAdapter;

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
        setAdvBanner();
        setQQBanner();
    }

    private void setQQBanner() {
        mViewPager.setPageTransformer(true, new ScalePageTransformer());

        pageContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.img1);
        list.add(R.mipmap.img2);
        list.add(R.mipmap.img3);
        list.add(R.mipmap.img4);
        list.add(R.mipmap.img5);
        list.add(R.mipmap.img6);
        list.add(R.mipmap.img7);
        list.add(R.mipmap.img8);

        //设置OffscreenPageLimit


        mViewPager.setOffscreenPageLimit(list.size());
        mViewPager.setPageMargin(40);
        mPagerAdapter = new MyAdapter(list);
        mViewPager.setAdapter(mPagerAdapter);

        //setCurrentItem会改变viewpager中child的索引，除非给child设置tag,用tag找child
        mViewPager.setCurrentItem(list.size() / 2);
        mViewPager.setListener(new ClipViewPager.ViewClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdvBanner() {

        advBanner.setSource(Arrays.asList(imgSrcs));
        for (int i = 0; i < imgSrcs.length; i++) {

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



    public class MyAdapter extends PagerAdapter {

        private List<Integer> list;

        public MyAdapter(List<Integer> data) {
            list = data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setTag(position);
            view.setImageResource(list.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
