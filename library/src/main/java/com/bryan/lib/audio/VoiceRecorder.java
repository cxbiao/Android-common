/*
 *
 * COPYRIGHT NOTICE
 * Copyright (C) 2015, bryan <690158801@qq.com>
 * https://github.com/cxbiao/Android-common
 *
 * @license under the Apache License, Version 2.0
 *
 * @version 1.0
 * @author  bryan
 * @date    2015/11/25
 *
 */

package com.bryan.lib.audio;

import android.media.MediaRecorder;

public class VoiceRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000; 

	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	public VoiceRecorder(String path){
		this.path = path;
	}



	public boolean start() {
		try{

			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setAudioChannels(1);
			recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
			recorder.setAudioEncodingBitRate(64);
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
			if(recorder!=null){
				recorder.stop();
				recorder.release();
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return  false;
		}
		
	}
	
	public double getAmplitude() {		
		if (recorder != null){			
			return  recorder.getMaxAmplitude();
		}
		else			
			return 0;	
		}
}