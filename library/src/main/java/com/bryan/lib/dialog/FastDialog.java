package com.bryan.lib.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.flyco.dialog.widget.NormalTipDialog;

/**
 * Created by bryan on 2015/9/5.
 */
public class FastDialog {


    public static BaseAnimatorSet bas_in=new FadeEnter();
    public static BaseAnimatorSet bas_out=new FadeExit();

    public static void ShowNormalDialog(Context context,String title,String content, final OnBtnLeftClickL onBtnLeftClickL, final OnBtnRightClickL onBtnRightClickL){
        final NormalDialog dialog = new NormalDialog(context);
        dialog.isTitleShow(true)//
                .title(title)
                .titleTextColor(Color.parseColor("#000000"))
                .titleGravity(Gravity.CENTER)
                .style(NormalDialog.STYLE_CUSTOM)
                .cornerRadius(5)//
                .content(content)//
                .contentTextSize(16f)
                .btnText("取消","确定")
                .btnTextColor(Color.parseColor("#44A2FF"), Color.parseColor("#44A2FF"))
                .widthScale(0.8f)
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnLeftClickL(new OnBtnLeftClickL() {
            @Override
            public void onBtnLeftClick() {
                if (onBtnLeftClickL != null) {
                    onBtnLeftClickL.onBtnLeftClick();
                }
                dialog.dismiss();
            }
        });

        dialog.setOnBtnRightClickL(new OnBtnRightClickL() {
            @Override
            public void onBtnRightClick() {
                if (onBtnRightClickL!=null){
                    onBtnRightClickL.onBtnRightClick();
                }
                dialog.dismiss();
            }
        });
    }


    public static void ShowTipDialog(Context context,String title,String content, final OnBtnClickL onBtnClickL){
        final NormalTipDialog dialog = new NormalTipDialog(context);
        dialog.title(title)
                .titleGravity(Gravity.CENTER)
                .titleTextColor(Color.parseColor("#000000"))
                .cornerRadius(5)
                .content(content)//
                .contentTextSize(16f)
                .style(NormalTipDialog.STYLE_CUSTOM)//
                .btnText("确定")//
                .btnTextColor(Color.parseColor("#44A2FF"))
                .widthScale(0.8f)
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                if (onBtnClickL != null) {
                    onBtnClickL.onBtnClick();
                }
                dialog.dismiss();
            }
        });
    }


    public static void ShowListDialog(Context context,String[] stringItems, final OnOperItemClickL onOperItemClickL){
        final NormalListDialog dialog = new NormalListDialog(context, stringItems);
        dialog.isTitleShow(false)
                .isLayoutAnimation(false)
                .style(NormalListDialog.STYLE_CUSTOM)
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .widthScale(0.75f)//
                .show();


        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onOperItemClickL != null) {
                    onOperItemClickL.onOperItemClick(parent, view, position, id);
                }
                dialog.dismiss();
            }
        });
    }

    public static void ShowActionSheetDialog(Context context,String[] stringItems,final OnOperItemClickL onOperItemClickL){
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.isTitleShow(false)
                .isLayoutAnimation(false)
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(onOperItemClickL!=null){
                   onOperItemClickL.onOperItemClick(parent,view,position,id);
               }
                dialog.dismiss();
            }
        });
    }
}
