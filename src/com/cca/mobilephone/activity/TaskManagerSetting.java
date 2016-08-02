package com.cca.mobilephone.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ServicerUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.service.AutoKillService;
import com.cca.mobilephone.ui.SettingCheckView;

public class TaskManagerSetting extends Activity {

	private SettingCheckView task_setting;
	private SettingCheckView task_autokill;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_taskmanager_setting);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		task_setting=(SettingCheckView) findViewById(R.id.task_setting);
		task_autokill=(SettingCheckView) findViewById(R.id.task_autokill);
		/**
		 * ����ʾϵͳ����
		 */
		task_setting.setChecked(sp.getBoolean("showcheck", false));
		task_setting.setOnClickListener(new OnClickListener() {
			Editor edit=sp.edit();
			@Override
			public void onClick(View v) {
				if(task_setting.isChecked()){
					task_setting.setChecked(false);
					ToastUtils.show(TaskManagerSetting.this, "�ر���ʾϵͳ����");
					edit.putBoolean("showcheck", false);
				}else{
					task_setting.setChecked(true);
					ToastUtils.show(TaskManagerSetting.this, "����ʾϵͳ����");
					edit.putBoolean("showcheck", true);
				}
				edit.commit();
			}
		});
		/**
		 * �����������
		 */
		task_autokill.setChecked(sp.getBoolean("autokill", true));
		task_autokill.setOnClickListener(new OnClickListener() {
			Editor edit=sp.edit();
			@Override
			public void onClick(View v) {
				if(task_autokill.isChecked()){
					task_autokill.setChecked(false);
					ToastUtils.show(TaskManagerSetting.this, "�رն����������");
					Intent intent=new Intent(TaskManagerSetting.this,AutoKillService.class);
					stopService(intent);
					edit.putBoolean("autokill", false);
				}else{
					task_autokill.setChecked(true);
					ToastUtils.show(TaskManagerSetting.this, "�򿪶����������");
					Intent intent=new Intent(TaskManagerSetting.this,AutoKillService.class);
					startService(intent);
					edit.putBoolean("autokill", true);
				}
				edit.commit();
			}
		});
	}
	@Override
	protected void onStart() {
		super.onStart();
		//�жϷ����Ƿ��ں�̨����
		if(ServicerUtils.isServiceRunning(getApplicationContext(), "com.cca.mobilephone.service.AutoKillService")){
			task_autokill.setChecked(true);
			
		}else{
			task_autokill.setChecked(false);
		}
	}
}
