package com.cca.mobilephone.activity;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends SetupBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup1);
	}
	@Override
	public void showNext() {
		IntentUtils.startActivityAndFinish(Setup1Activity.this, Setup2Activity.class);
	
	}
	@Override
	public void showPre() {
		
	}
	
}
