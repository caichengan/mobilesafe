package com.cca.mobilephone.activity;

import android.content.Context;


import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.ui.SettingCheckView;

public class Setup2Activity extends SetupBaseActivity {

	private SettingCheckView setcheck;
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		//获取手机电话的服务
		tm=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		setcheck = (SettingCheckView) findViewById(R.id.set_bind_text);
		final String sim=sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			ToastUtils.show(this, "手机还没有绑定手机号");
			
		}else{
			setcheck.setChecked(true);
		}
		
		setcheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor edit=sp.edit();
				
				if(TextUtils.isEmpty(sim)){
					setcheck.setChecked(true);
					String sim1=tm.getSimSerialNumber();
					edit.putString("sim", sim1);
				}else{
					setcheck.setChecked(false);
					edit.putString("sim", null);
				}
				edit.commit();
			}
		});
	}
	
	@Override
	public void showNext() {
		String sim=sp.getString("sim",null);
		if(TextUtils.isEmpty(sim)){
			ToastUtils.show(this,"开启手机防盗，必须绑定手机序列号");
		}else{
			IntentUtils.startActivityAndFinish(Setup2Activity.this, Setup3Activity.class);
		}
	}
	@Override
	public void showPre() {
		IntentUtils.startActivityAndFinish(Setup2Activity.this, Setup1Activity.class);
				
	}
}
