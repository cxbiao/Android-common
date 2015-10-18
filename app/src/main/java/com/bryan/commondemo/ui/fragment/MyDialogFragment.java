package com.bryan.commondemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.bryan.commondemo.R;
import com.bryan.commondemo.ui.dialog.ShareBottomDialog;
import com.bryan.commondemo.ui.dialog.ShareTopDialog;
import com.bryan.lib.dialog.FastDialog;
import com.bryan.lib.util.T;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.CustomBottomDialog;
import com.flyco.dialog.widget.CustomCenterDialog;
import com.flyco.dialog.widget.CustomTopDialog;
import com.flyco.dialog.widget.base.BaseDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bryan on 2015/9/5.
 */
public class MyDialogFragment extends BaseFragment{

    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bas_in=new FadeEnter();
        bas_out=new FadeExit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_me, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.baseDialog)
    public void baseDialog(View v){
        FastDialog.ShowNormalDialog(context, "提示", "是否确定退出程序?",true,null, new OnBtnRightClickL() {
            @Override
            public void onBtnRightClick() {
                T.showShort(context, "确定");
            }
        });

    }

    @OnClick(R.id.tipDialog)
    public void tipDialog(View v){

        FastDialog.ShowTipDialog(context, "提示", "你今天的抢购名额已用完~",true, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                T.showShort(context, "确定");
            }
        });


    }
    @OnClick(R.id.listDialog)
    public void listDialog(View v){

        final String[] stringItems = {"收藏", "下载", "分享", "删除", "歌手", "专辑"};
        FastDialog.ShowListDialog(context, stringItems, true,new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(context, stringItems[position]);
            }
        });


    }
    @OnClick(R.id.actionSheetDialog)
    public void actionSheetDialog(View v){
        final String[] stringItems = {"版本更新", "帮助与反馈", "退出QQ"};
        FastDialog.ShowActionSheetDialog(context, stringItems, true,new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(context, stringItems[position]);
            }
        });

    }
    @OnClick(R.id.bottomDialog)
    public void bottomDialog(View v){
        final ShareBottomDialog dialog = new ShareBottomDialog(context,null);
        dialog.show();//

    }
    @OnClick(R.id.topDialog)
    public void topDialog(View v){
        final ShareTopDialog dialog = new ShareTopDialog(context);
        dialog.show();
    }

    @OnClick(R.id.bottomNoAnimDialog)
    public void bottomNoAnimDialog(View v){
        CustomBottomDialog dialog=new CustomBottomDialog(context);
        dialog.contentView(getContentView(dialog))
                .isNeedInnerAnim(false)
                .show();
    }
    @OnClick(R.id.topNoAnimDialog)
    public void topNoAnimDialog(View v){
        CustomTopDialog dialog=new CustomTopDialog(context);
        dialog.contentView(getContentView(dialog))
                .isNeedInnerAnim(false)
                .show();
    }
    @OnClick(R.id.centerDialog)
    public void centerDialog(View v){
        CustomCenterDialog dialog=new CustomCenterDialog(context);
        dialog.contentView(getContentView(dialog))
                .showAnim(new ZoomInEnter())
                .dismissAnim(new ZoomInExit())
                .widthScale(0.8f)
                .show();
    }

    private View getContentView(final BaseDialog dialog){

        LinearLayout ll_wechat_friend_circle;
        LinearLayout ll_wechat_friend;
        LinearLayout ll_qq;
        LinearLayout ll_sms;
        View inflate = View.inflate(context, R.layout.dialog_share, null);
        ll_wechat_friend_circle = (LinearLayout) inflate.findViewById(R.id.ll_wechat_friend_circle);
        ll_wechat_friend = (LinearLayout) inflate.findViewById(R.id.ll_wechat_friend);
        ll_qq = (LinearLayout) inflate.findViewById(R.id.ll_qq);
        ll_sms = (LinearLayout) inflate.findViewById(R.id.ll_sms);

        ll_wechat_friend_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "朋友圈");
                dialog.dismiss();
            }
        });
        ll_wechat_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "微信");
                dialog.dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "QQ");
                dialog.dismiss();
            }
        });
        ll_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "短信");
                dialog.dismiss();
            }
        });
        return  inflate;
    }


}
