package com.bryan.commondemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bryan.commondemo.R;
import com.bryan.lib.ui.time.OptionsPopupWindow;
import com.bryan.lib.ui.time.TimePopupWindow;
import com.bryan.lib.ui.time.TimePopupWindow.OnTimeSelectListener;
import com.bryan.lib.ui.time.TimePopupWindow.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by bryan on 2015/9/13.
 */
public class WheelViewFragment extends BaseFragment {


    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvOptions)
    TextView tvOptions;
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    TimePopupWindow pwTime;
    OptionsPopupWindow pwOptions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wheelview, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //时间选择器
        pwTime = new TimePopupWindow(context, Type.ALL);
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(getTime(date));
            }
        });


        //选项选择器
        pwOptions = new OptionsPopupWindow(context);

        //选项1
        options1Items.add("广东");
        options1Items.add("湖南");

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<String>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        ArrayList<String> options2Items_02 = new ArrayList<String>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);

        //选项3
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options3Items_01_01 = new ArrayList<String>();
        options3Items_01_01.add("白云");
        options3Items_01_01.add("天河");
        options3Items_01_01.add("海珠");
        options3Items_01_01.add("越秀");
        options3Items_01.add(options3Items_01_01);
        ArrayList<String> options3Items_01_02 = new ArrayList<String>();
        options3Items_01_02.add("南海");
        options3Items_01.add(options3Items_01_02);
        ArrayList<String> options3Items_01_03 = new ArrayList<String>();
        options3Items_01_03.add("常平");
        options3Items_01_03.add("虎门");
        options3Items_01.add(options3Items_01_03);

        ArrayList<String> options3Items_02_01 = new ArrayList<String>();
        options3Items_02_01.add("长沙1");
        options3Items_02.add(options3Items_02_01);
        ArrayList<String> options3Items_02_02 = new ArrayList<String>();
        options3Items_02_02.add("岳1");
        options3Items_02.add(options3Items_02_02);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);

        //三级联动效果
        pwOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
        pwOptions.setLabels("省", "市", "区");
        //设置默认选中的三级项目
        pwOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                tvOptions.setText(tx);
            }
        });

    }

    @OnClick(R.id.tvTime)
    public void showTime(View v) {
        pwTime.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0, new Date());
    }


    @OnClick(R.id.tvOptions)
    public void showCity(View v) {
        pwOptions.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0);
    }


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


}
