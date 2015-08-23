package com.bryan.lib.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bryan.lib.R;
import com.bryan.lib.util.ScreenUtils;

/**
 * 自定义的Dialog,不可back或点击外部销毁
 *
 */
public class CustomDialog {
	public final static int SELECT_DIALOG = 1;
	public final static int RADIO_DIALOG = 2;

	/**
	 * 创建一个自定义View的对话框
	 * @param context
	 * @param view
	 * @param gravity
	 * @return
	 */
	public static android.app.Dialog showViewDialog(Context context, View view, int gravity) {
		return ShowDialog(context, view, gravity);
	}
	
	/**
	 * 创建一个内容多选对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param items
	 *            数组
	 * @param dialogItemClickListener
	 *            监听点击的内容结果
	 * @return
	 */
	public static android.app.Dialog showListDialog(Context context, String title, String[] items, final DialogItemClickListener dialogItemClickListener) {
		return ShowDialog(context, title, items, dialogItemClickListener);
	}

	/**
	 * 创建一个单选对话框
	 * 
	 * @param context
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showRadioDialog(Context context, String message, final DialogClickListener dialogClickListener) {
		return ShowDialog(context, "提示", message, dialogClickListener, RADIO_DIALOG);
	}

	/**
	 * 创建一个选择对话框
	 * 
	 * @param context
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context, String message, final DialogClickListener dialogClickListener) {
		return ShowDialog(context, "提示", message, dialogClickListener, SELECT_DIALOG);
	}

	/**
	 * 创建一个选择对话框
	 * 
	 * @param context
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context, String message, String confirm, String cancel, final DialogClickListener dialogClickListener) {
		return ShowDialog(context, "提示", message, confirm, cancel, dialogClickListener, SELECT_DIALOG);
	}
	
	/**
	 * 创建一个选择对话框
	 * 
	 * @param context
	 * @param title
	 *            提示标题
	 * @param message
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context, String title, String message, final DialogClickListener dialogClickListener) {
		return ShowDialog(context, title, message, dialogClickListener, SELECT_DIALOG);
	}
	
	/**
	 * 创建一个选择对话框
	 * 
	 * @param context
	 * @param title
	 *            提示标题
	 * @param message
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context, String title, String message, String confirm, String cancel, final DialogClickListener dialogClickListener) {
		return ShowDialog(context, title, message, confirm, cancel, dialogClickListener, SELECT_DIALOG);
	}

	private static android.app.Dialog ShowDialog(Context context, String title, String message, final DialogClickListener dialogClickListener, int DialogType) {
		return ShowDialog(context, title, message, "", "", dialogClickListener, DialogType);
	}
	
	private static android.app.Dialog ShowDialog(Context context, String title, String message, String confirm, String cancel, final DialogClickListener dialogClickListener, int DialogType) {
		if(((Activity)context).isFinishing() ){
			return null;
		}
			
		final android.app.Dialog dialog = new android.app.Dialog(context, R.style.DialogStyle);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		View view = LayoutInflater.from(context).inflate(R.layout.widget_dialog, null);
		dialog.setContentView(view);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.message)).setText(message);
		if (DialogType == RADIO_DIALOG) {
		} else {
			view.findViewById(R.id.ok).setVisibility(View.GONE);
			view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
		}
		TextView cancelTextView = (TextView) view.findViewById(R.id.cancel);
		if(!TextUtils.isEmpty(cancel)){
			cancelTextView.setText(cancel);
		}
		cancelTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (dialogClickListener != null)
							dialogClickListener.cancel();
					}
				}, 200);
			}
		});
		TextView confirmTextView = (TextView) view.findViewById(R.id.confirm);
		if(!TextUtils.isEmpty(confirm)){
			confirmTextView.setText(confirm);
		}
		confirmTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(dialogClickListener != null)
							dialogClickListener.confirm();
					}
				}, 200);
			}
		});
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (dialogClickListener != null)
							dialogClickListener.confirm();
					}
				}, 200);
			}
		});
		Window mWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
			lp.width = ScreenUtils.getScreenHeight(context) / 10 * 8;
		} else {
			lp.width = ScreenUtils.getScreenWidth(context) / 10 * 8;
		}
		mWindow.setAttributes(lp);
		dialog.show();

		return dialog;
	}

	private static android.app.Dialog ShowDialog(Context context, String title, String[] items, final DialogItemClickListener dialogClickListener) {
		final android.app.Dialog dialog = new android.app.Dialog(context, R.style.DialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		View view = LayoutInflater.from(context).inflate(R.layout.widget_dialog_radio, null);
		dialog.setContentView(view);
		((TextView) view.findViewById(R.id.title)).setText(title);
		// 根据items动态创建
		LinearLayout parent = (LinearLayout) view.findViewById(R.id.dialogLayout);
		parent.removeAllViews();
		int length = items.length;
		for (int i = 0; i < items.length; i++) {
			final int which = i;
			LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params1.rightMargin = 1;
			final TextView tv = new TextView(context);
			tv.setLayoutParams(params1);
			tv.setTextSize(18);
			tv.setText(items[i]);
			tv.setTextColor(context.getResources().getColor(R.color.dialogTxtColor));
			int pad = context.getResources().getDimensionPixelSize(R.dimen.padding10);
			tv.setPadding(pad, pad, pad, pad);
			tv.setSingleLine(true);
			tv.setGravity(Gravity.CENTER);
			if (i != length - 1)
				tv.setBackgroundResource(R.drawable.dialog_menu_center_selector);
			else
				tv.setBackgroundResource(R.drawable.dialog_menu_bottom2_selector);

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
					if(dialogClickListener != null)
						dialogClickListener.confirm(tv.getText().toString(), which);
				}
			});
			parent.addView(tv);
			if (i != length - 1) {
				TextView divider = new TextView(context);
				LayoutParams params = new LayoutParams(-1, (int) 1);
				divider.setLayoutParams(params);
				divider.setBackgroundResource(android.R.color.darker_gray);
				parent.addView(divider);
			}
		}
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (dialogClickListener != null)
					dialogClickListener.cancel();
			}
		});
		Window mWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width = ScreenUtils.getScreenWidth(context);
		mWindow.setGravity(Gravity.BOTTOM);
		// 添加动画
		mWindow.setWindowAnimations(R.style.dialogAnim);
		mWindow.setAttributes(lp);
		dialog.show();
		return dialog;
	}

	private static android.app.Dialog ShowDialog(Context context, View view, int gravity) {
		final android.app.Dialog dialog = new android.app.Dialog(context, R.style.DialogStyle);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.setContentView(view);
		Window mWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width = ScreenUtils.getScreenWidth(context);
		mWindow.setGravity(gravity);
		// 添加动画
		mWindow.setWindowAnimations(R.style.dialogAnim);
		mWindow.setAttributes(lp);
		dialog.show();
		return dialog;
	}
	
	public static void updateItem(Context context,final android.app.Dialog dialog,String[] items,final DialogItemClickListener dialogClickListener){
		LinearLayout parent = (LinearLayout) dialog.findViewById(R.id.dialogLayout);
		parent.removeAllViews();
		int length = items.length;
		for (int i = 0; i < items.length; i++) {
			final int which = i;
			LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params1.rightMargin = 1;
			final TextView tv = new TextView(context);
			tv.setLayoutParams(params1);
			tv.setTextSize(18);
			tv.setText(items[i]);
			tv.setTextColor(context.getResources().getColor(R.color.dialogTxtColor));
			int pad = context.getResources().getDimensionPixelSize(R.dimen.padding10);
			tv.setPadding(pad, pad, pad, pad);
			tv.setSingleLine(true);
			tv.setGravity(Gravity.CENTER);
			if (i != length - 1)
				tv.setBackgroundResource(R.drawable.dialog_menu_center_selector);
			else
				tv.setBackgroundResource(R.drawable.dialog_menu_bottom2_selector);

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
					dialogClickListener.confirm(tv.getText().toString(), which);
				}
			});
			parent.addView(tv);
			if (i != length - 1) {
				TextView divider = new TextView(context);
				LayoutParams params = new LayoutParams(-1, (int) 1);
				divider.setLayoutParams(params);
				divider.setBackgroundResource(android.R.color.darker_gray);
				parent.addView(divider);
			}
		}
		dialog.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				dialogClickListener.cancel();
			}
		});
		Window mWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width = ScreenUtils.getScreenWidth(context);
		mWindow.setGravity(Gravity.BOTTOM);
		// 添加动画
		mWindow.setWindowAnimations(R.style.dialogAnim);
		mWindow.setAttributes(lp);
	
	}
	



	public interface DialogClickListener {
		public abstract void confirm();

		public abstract void cancel();
	}

	public interface DialogItemClickListener {
		public abstract void confirm(String result, int which);

		public abstract void cancel();
	}
}