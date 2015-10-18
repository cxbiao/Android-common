package com.bryan.lib.audio;

import android.media.MediaRecorder;

public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000; 

	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	public AudioRecorder(String path){
		this.path = path;
	}



	public boolean start() {
		try{

			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
			recorder.setOutputFile(path);
			recorder.prepare();
			recorder.start();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return  false;
		}
		
	}

	public boolean stop() {
		try{
			recorder.stop();
			recorder.release();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return  false;
		}
		
	}
	
	public double getAmplitude() {		
		if (recorder != null){			
			return  (recorder.getMaxAmplitude());		
			}		
		else			
			return 0;	
		}
}