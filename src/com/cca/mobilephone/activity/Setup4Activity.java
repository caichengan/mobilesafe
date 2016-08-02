package com.cca.mobilephone.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.receiver.Admin;
import com.cca.mobilephone.ui.SettingCheckView;


public class Setup4Activity extends SetupBaseActivity {

	
	private SettingCheckView set_update_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup4);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		set_update_text=(SettingCheckView) findViewById(R.id.set_update_text);
		set_update_text.setChecked(sp.getBoolean("protecting", false));
		set_update_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor edit=sp.edit();
				if(set_update_text.isChecked()){
					set_update_text.setChecked(false);
					edit.putBoolean("protecting", false);
				}else{
					set_update_text.setChecked(true);
			
					edit.putBoolean("protecting", true);
				}
				edit.commit();
				
			}
		});
	}
	
	
	/*
	 * 点击按钮激活设备管理
	 */
	public void activeAdmin(View view){
		 Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		 ComponentName who=new ComponentName(this, Admin.class);
         intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
         intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"请大家赶紧去激活程序吧，首次激活有大礼包！");
         startActivity(intent);
	}
	
	
	@Override
	public void showNext() {
		boolean protecting=sp.getBoolean("protecting", false);
		Editor edit=sp.edit();
		if(protecting){
			ToastUtils.show(Setup4Activity.this, "设置完成");
			
			edit.putBoolean("finishsetup", true);
			
			//IntentUtils.startActivityAndFinish(Setup4Activity.this, PhoneFangDaoActivity.class);
				
		}else{
			ToastUtils.show(Setup4Activity.this, "你还没有开启手机防盗！");
			edit.putBoolean("finishsetup", false);
		}
		edit.commit();
		IntentUtils.startActivityAndFinish(Setup4Activity.this, PhoneFangDaoActivity.class);
			
	}
	@Override
	public void showPre() {
		IntentUtils.startActivityAndFinish(Setup4Activity.this, Setup3Activity.class);
		
	}
}
