package com.bryan.commondemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.bryan.commondemo.R;


public class ErrorActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		String error=getIntent().getStringExtra("Error");
		TextView tv=(TextView) findViewById(R.id.error);
		tv.setText(error);
	}

}
