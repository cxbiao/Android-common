package com.bryan.lib.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryan.lib.R;
import com.bryan.lib.dialog.FastDialog;
import com.bryan.lib.file.FileUtil;
import com.bryan.lib.ui.BaseActivity;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;


public class RecordVideoActivity extends BaseActivity implements OnTouchListener {

	public final static String Recording_Finish = "Recording_Finish";
	public final static String Recording_Cancel = "Recording_Cancel";

	final int maxTime = 30;// 最大录制时间
	final int minTime = 2;// 最小录制时间
	final int videoHeight = 240;// 录制分辨率
	final int videoWidth = 320;// 录制分辨率

	RelativeLayout parentRelativeLayout;
	private SurfaceView sv_view;
	private boolean isRecording;// 是否正在录制
	private MediaRecorder mediaRecorder;
	private Camera camera;

	boolean isFocus = false;
	File file;

	private ProgressBar progressBar;// 进度条
	private Button btCamera;
	private SurfaceView cameraView;// 预览窗口
	TextView txtTimer;// 显示剩余时间
	TextView upwardCancel;// 向上取消
	TextView loosenCancel;// 松开取消
	boolean isCancel = false;// 是否取消录制

	boolean isPreview = false;// 摄像头是否预览
	double currentTime = 0;
	int actualVideoHeight = 0;
	int actualVideoWidth = 0;

	private static final String TAG="RecordVideoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_video);

		sv_view = (SurfaceView) findViewById(R.id.sv_view);

		parentRelativeLayout = (RelativeLayout) findViewById(R.id.parentRelativeLayout);
		progressBar = (ProgressBar) findViewById(R.id.recorder_progress);
		txtTimer = (TextView) findViewById(R.id.txtTimer);
		upwardCancel = (TextView) findViewById(R.id.upwardCancel);
		loosenCancel = (TextView) findViewById(R.id.loosenCancel);
		btCamera = (Button) this.findViewById(R.id.press_camera);
		btCamera.setOnTouchListener(this);

		// 声明Surface不维护自己的缓冲区，针对Android3.0以下设备支持
		sv_view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (file == null) {
			String path = FileUtil.GetVideoPath(this)+"/"+ FileUtil.GetVideoName();
			file = new File(path);
		}

		if (file.exists()) {
			// 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
			file.delete();
		}

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				InitCamera();
			}
		}, 200);

	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下的是返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancel();
			return true;
		}
		return false;
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// btn_VideoStop.performClick();
				if (isRecording == true) {
					currentTime=currentTime+0.1;
					if (currentTime <= maxTime) {
						int index = (int) ((double) currentTime / (double) maxTime * 100);
						progressBar.setProgress(index);

						txtTimer.setText((maxTime - (int)currentTime) + "");

						sendEmptyMessageDelayed(0, 100);
					}
				}

				break;

			default:
				break;
			}
		}

	};

	private void InitCamera() {
		try {
			camera = Camera.open();

			// 获取摄像头支持的分辨率列表
			Parameters parameters = camera.getParameters();
			List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();

			// 某些摄像头不支持录制320*240分辨率的视频，则录制该摄像头支持的分辨率中大于320*240的最小的那个分辨率
			for (int i = 0; i < supportedPreviewSizes.size(); i++) {
				Size size = supportedPreviewSizes.get(i);
				if (size.height == videoHeight && size.width == videoWidth) {
					actualVideoHeight = videoHeight;
					actualVideoWidth = videoWidth;
					break;
				} else if (size.height > videoHeight && size.width > videoWidth) {
					if (actualVideoHeight == 0) {
						actualVideoHeight = size.height;
						actualVideoWidth = size.height;
					} else {
						if (size.height < actualVideoHeight) {
							actualVideoHeight = size.height;
							actualVideoWidth = size.width;
						}
					}
				}
			}

			setCameraParams();
			camera.setDisplayOrientation(90);
			camera.setPreviewDisplay(sv_view.getHolder());
			camera.startPreview();
			isPreview = true;
			if (isFocus) {
				camera.autoFocus(null);
			}
			camera.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void InitMediaRecorder() {

		mediaRecorder = new MediaRecorder();

		// 设置摄像头的方向
		mediaRecorder.setOrientationHint(90);

		if (camera != null) {
			mediaRecorder.setCamera(camera);
		}

		mediaRecorder.reset();
		// 设置音频录入源
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置视频图像的录入源
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// 设置录入媒体的输出格式
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		// 设置音频的编码格式
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		// 设置视频的编码格式
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
		mediaRecorder.setVideoSize(actualVideoWidth, actualVideoHeight);

		// 最大录制时间,30秒
		mediaRecorder.setMaxDuration(maxTime * 1000);

		// 设置录制的视频帧率。采样率，每秒4帧，必须放在设置编码和格式的后面，否则报错
		// mediaRecorder.setVideoFrameRate(4);

		// 设置视频比特率,设置低品质
		CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
		int videoBitRate = mProfile.videoBitRate;
		int audioBitRate = mProfile.audioBitRate;
		mediaRecorder.setVideoEncodingBitRate(videoBitRate);
		mediaRecorder.setAudioEncodingBitRate(audioBitRate);

		// 设置录制视频文件的输出路径
		mediaRecorder.setOutputFile(file.getAbsolutePath());
		// 设置捕获视频图像的预览界面
		mediaRecorder.setPreviewDisplay(sv_view.getHolder().getSurface());

		mediaRecorder.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(MediaRecorder mr, int what, int extra) {
				// 发生错误，停止录制
				ReleaseMediaRecorder();
				isRecording = false;
				// btn_VideoStart.setEnabled(true);
				// btn_VideoStop.setEnabled(false);
				Toast.makeText(RecordVideoActivity.this, "录制出错", Toast.LENGTH_SHORT).show();
			}
		});
		mediaRecorder.setOnInfoListener(new OnInfoListener() {

			@Override
			public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void setCameraParams() {
		if (camera != null) {
			Parameters params = camera.getParameters();
			List<String> list = params.getSupportedFocusModes();
			if (list.contains(Parameters.FOCUS_MODE_AUTO)) {
				isFocus = true;
				params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			params.set("orientation", "portrait");
			camera.setParameters(params);
		}
	}

	protected void start() {
		try {
			// 准备、开始

			InitMediaRecorder();

			mediaRecorder.prepare();

			mediaRecorder.start();

			// btn_VideoStart.setEnabled(false);
			// btn_VideoStop.setEnabled(true);
			isRecording = true;
			// Toast.makeText(MainActivity.this,
			// "开始录像",Toast.LENGTH_SHORT).show();

			mHandler.sendEmptyMessageDelayed(0, 100);

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(RecordVideoActivity.this, "摄像头打开失败，请稍后再试", Toast.LENGTH_SHORT).show();
		}

	}

	protected void stop() {
		if (isRecording) {
			// 如果正在录制，停止并释放资源
			ReleaseMediaRecorder();
			ReleaseCamera();
			isRecording = false;

			try {
				int remainSecond = Integer.parseInt(txtTimer.getText().toString());
				int recordSecond = maxTime - remainSecond;
				if (recordSecond < minTime) {
					// 少于3秒无效
					progressBar.setProgress(0);

					txtTimer.setText(maxTime + "");
					Toast.makeText(RecordVideoActivity.this, "录制时间太短，请重新录制", Toast.LENGTH_SHORT).show();
					// 删除文件
					if (file.exists()) {
						// 如果文件存在，删除它
						file.delete();
					}
					InitCamera();
				} else {

					ReturnBack(Recording_Finish);// 1为录制完成，返回地址
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	private void cancel() {
		if (isRecording) {
			// 如果正在录制，停止并释放资源
			ReleaseMediaRecorder();
			ReleaseCamera();

			isRecording = false;
		}

		progressBar.setProgress(0);

		txtTimer.setText(maxTime + "");

		// 删除文件
		if (file.exists()) {
			// 如果文件存在，删除它
			file.delete();
		}
		// InitCamera();
		ReturnBack(Recording_Cancel);// 2为录制取消
	}

	private void ReleaseMediaRecorder() {
		try {
			if (mediaRecorder != null) {
				mediaRecorder.setOnErrorListener(null);
				mediaRecorder.setOnInfoListener(null);
				mediaRecorder.stop();
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ReleaseCamera() {
		try {
			if (camera != null) {
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ReturnBack(String action) {

		if (action.equals(Recording_Finish)) {

			FastDialog.ShowNormalDialog(this, "提示,", "视频文件大小为" + formatCacheSize(file.length()) + ",确定要发送吗?", new OnBtnLeftClickL() {
				@Override
				public void onBtnLeftClick() {
					Intent intent = new Intent();
					intent.setAction(Recording_Cancel);
					setResult(RESULT_OK, intent);
					finish();

				}
			}, new OnBtnRightClickL() {
				@Override
				public void onBtnRightClick() {
					Intent intent = new Intent();
					intent.putExtra("filePath", file.getPath());
				    intent.setAction(Recording_Finish);
					setResult(RESULT_OK, intent);
					finish();
				}
			});


		} else if (action.equals(Recording_Cancel)) {
			Intent intent = new Intent();
			intent.setAction(Recording_Cancel);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	public String formatCacheSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	@Override
	protected void onDestroy() {
		try {
			ReleaseMediaRecorder();
			ReleaseCamera();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			isCancel = false;
			upwardCancel.setVisibility(View.VISIBLE);
			loosenCancel.setVisibility(View.GONE);

			start();
			break;
		case MotionEvent.ACTION_UP:
			if (isCancel) {
				cancel();
				currentTime = 0;
				upwardCancel.setVisibility(View.GONE);
				loosenCancel.setVisibility(View.GONE);
			} else {
				stop();
				currentTime = 0;
				upwardCancel.setVisibility(View.GONE);
				loosenCancel.setVisibility(View.GONE);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getY() < 0) {
				// 当手指移动到view外面，会cancel
				isCancel = true;
				upwardCancel.setVisibility(View.GONE);
				loosenCancel.setVisibility(View.VISIBLE);
			} else {
				isCancel = false;
				upwardCancel.setVisibility(View.VISIBLE);
				loosenCancel.setVisibility(View.GONE);
			}

			break;
		}
		return false;
	}

}
