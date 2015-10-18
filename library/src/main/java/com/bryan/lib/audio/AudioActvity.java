package com.bryan.lib.audio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bryan.lib.R;
import com.bryan.lib.file.FileUtil;
import com.bryan.lib.ui.BaseActivity;

import java.io.File;

/**
 * Created by bryan on 2015-10-18.
 */
public class AudioActvity extends BaseActivity {

    Button audioBtn;
    private String TAG = "AudioActvity";
    private AudioRecorder mr; // 录音
    private Thread recordThread; // 录音线程

    private static int MAX_TIME = 0; // 最长录制时间，单位秒，0为无时间限制
    private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

    private static int RECORD_NO = 0; // 不在录音
    private static int RECORD_ING = 1; // 正在录音
    private static int RECODE_ED = 2; // 完成录音

    private static int RECODE_STATE = 0; // 录音的状态

    private static float recodeTime = 0.0f; // 录音的时间
    private static double voiceValue = 0.0; // 麦克风获取的音量值

    private ImageView dialog_img;
    private String audioName = "";

    private View tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        audioBtn = (Button) findViewById(R.id.audio);
        audioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioBtn.setText("松开结束");

                        if (RECODE_STATE != RECORD_ING) {
                            FileUtil fileUtil = new FileUtil();
                            audioName = fileUtil.GetAudioPath(AudioActvity.this) + "/" + fileUtil.GetAudioName();
                            mr = new AudioRecorder(audioName);
                            RECODE_STATE = RECORD_ING;

                            mr.start();
                            mythread();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        audioBtn.setText("按住开始");
                        if (RECODE_STATE == RECORD_ING) {
                            tips.setVisibility(View.GONE);
                            mr.stop();
                            voiceValue = 0.0;

                            if (recodeTime < MIX_TIME) {
                                // 时间太短
                                Toast.makeText(getBaseContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                                RECODE_STATE = RECORD_NO;
                                File file = new File(audioName);
                                file.delete();
                            } else {
                                Toast.makeText(getBaseContext(), audioName + "录音成功", Toast.LENGTH_SHORT).show();
                            }
                            RECODE_STATE = RECODE_ED;

                        }

                        break;
                }
                return false;
            }
        });
      tips= View.inflate(this, R.layout.activity_audio_tips, null);
      dialog_img=(ImageView) tips.findViewById(R.id.dialog_img);
      tips.setVisibility(View.GONE);
      ViewGroup contentParent= (ViewGroup) findViewById(android.R.id.content);
      FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
      tips.setLayoutParams(params);
      contentParent.addView(tips);

    }







    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if (RECODE_STATE == RECORD_ING){
                Toast.makeText(getBaseContext(), "正在录音,不能退出!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 录音计时线程
     */
    private void mythread() {
        recordThread = new Thread(ImgThread);
        recordThread.start();
    }


    // 录音线程
    private Runnable ImgThread = new Runnable() {
        @Override
        public void run() {
            recodeTime = 0.0f;
            while (RECODE_STATE == RECORD_ING) {
                if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
                    imgHandle.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(200);
                        recodeTime += 0.2;
                        if (RECODE_STATE == RECORD_ING) {
                            voiceValue = mr.getAmplitude();
                            imgHandle.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    Handler imgHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // dialog 的 切换变更
                    if (RECODE_STATE == RECORD_ING) {
                        RECODE_STATE = RECODE_ED;
                        tips.setVisibility(View.GONE);
                        mr.stop();
                        voiceValue = 0.0;
                        if (recodeTime < 1.0) {
                            Toast.makeText(getBaseContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                            RECODE_STATE = RECORD_NO;
                        }
                    }
                    break;
                case 1:
                    if (RECODE_STATE == RECORD_ING){
                        tips.setVisibility(View.VISIBLE);
                        setDialogImage();
                    }
                    break;
                default:
                    break;
            }

        }
    };


    /**
     * 录音Dialog图片随声音大小切换
     */
    private void setDialogImage() {
        if (voiceValue < 200.0) {
            dialog_img.setImageResource(R.drawable.record_animate_01);
        } else if (voiceValue > 200.0 && voiceValue < 400) {
            dialog_img.setImageResource(R.drawable.record_animate_02);
        } else if (voiceValue > 400.0 && voiceValue < 800) {
            dialog_img.setImageResource(R.drawable.record_animate_03);
        } else if (voiceValue > 800.0 && voiceValue < 1600) {
            dialog_img.setImageResource(R.drawable.record_animate_04);
        } else if (voiceValue > 1600.0 && voiceValue < 3200) {
            dialog_img.setImageResource(R.drawable.record_animate_05);
        } else if (voiceValue > 3200.0 && voiceValue < 5000) {
            dialog_img.setImageResource(R.drawable.record_animate_06);
        } else if (voiceValue > 5000.0 && voiceValue < 7000) {
            dialog_img.setImageResource(R.drawable.record_animate_07);
        } else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_08);
        } else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_09);
        } else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_10);
        } else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_11);
        } else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_12);
        } else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_13);
        } else if (voiceValue > 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_14);
        }
    }

}
