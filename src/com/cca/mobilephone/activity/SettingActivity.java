package com.cca.mobilephone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ServicerUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.service.CallSmsSafeService;
import com.cca.mobilephone.service.ShowAddressService;
import com.cca.mobilephone.service.WatchDogLockService;
import com.cca.mobilephone.ui.SettingChangedView;
import com.cca.mobilephone.ui.SettingCheckView;

public class SettingActivity extends Activity {
	/**
	 * �����Զ�����
	 */
	private SettingCheckView set_check;
	/**
	 * ��������������
	 */
	private SettingCheckView set_blacknumber;
	/**
	 * �����Զ�����ģʽ
	 */
	private SettingCheckView set_showaddresss;
	/**
	 * ������ʾ�������
	 */
	private SettingChangedView set_changedview;
	/**
	 * �������������
	 */
	private SettingCheckView set_whatchdogapplock;
	private CheckBox check;
	private final String items[]={"������","ǳ��ɫ","���ɫ","ǳ��ɫ","����ɫ"};
	boolean update;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		//�����ļ��洢����
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//��ʼ���ؼ�
		set_check= (SettingCheckView) findViewById(R.id.set_update_text);
		set_blacknumber=(SettingCheckView) findViewById(R.id.set_blacknumber_text);
		set_showaddresss=(SettingCheckView) findViewById(R.id.set_showaddresss);
		set_changedview=(SettingChangedView) findViewById(R.id.set_changedview);
		set_whatchdogapplock=(SettingCheckView) findViewById(R.id.set_whatchdogapplock);
		
		set_changedview.setContent(items[sp.getInt("which", 0)]);
		/**
		 * �����Զ����µ��߼�
		 */
		check=(CheckBox) findViewById(R.id.cb_set_update);
		//��ȡ��ǰcheckbox�Ƿ񱻵��
		update=sp.getBoolean("update", false);
		check.setChecked(update);
		
		set_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor edit=sp.edit();
				if(check.isChecked()){
					check.setChecked(false);
					ToastUtils.show(SettingActivity.this, "�ر��Զ�����");
					edit.putBoolean("update", false);
				}else{
					check.setChecked(true);
					ToastUtils.show(SettingActivity.this, "�����Զ�����");
					edit.putBoolean("update", true);
				}
				edit.commit();
			}
			
		});
		
		/**
		 * ��������ģʽ���߼�
		 */
		//�ٴ���������ʱ���ж���������Ƿ��ں�̨����
		set_blacknumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean setblacknumber=set_blacknumber.isChecked();
				if(setblacknumber){
					set_blacknumber.setChecked(false);
					ToastUtils.show(SettingActivity.this, "�ر�����ģʽ");
					Intent intent=new Intent(SettingActivity.this,CallSmsSafeService.class);
					stopService(intent);
					
				}else{
					set_blacknumber.setChecked(true);
					ToastUtils.show(SettingActivity.this, "��������ģʽ");
					Intent intent=new Intent(SettingActivity.this,CallSmsSafeService.class);
					startService(intent);
					
				}
			}
		});
		/**
		 * �����绰������������ʾ
		 */
		set_showaddresss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(set_showaddresss.isChecked()){
					set_showaddresss.setChecked(false);
					ToastUtils.show(SettingActivity.this, "�رչ�������ʾ");
					Intent intent=new Intent(SettingActivity.this,ShowAddressService.class);
					stopService(intent);
				}else{
					set_showaddresss.setChecked(true);
					ToastUtils.show(SettingActivity.this, "�򿪹�������ʾ");
					Intent intent=new Intent(SettingActivity.this,ShowAddressService.class);
					startService(intent);
				}
			}
		});
		/**
		 * �������Ź��������
		 */
		set_whatchdogapplock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(set_whatchdogapplock.isChecked()){
					set_whatchdogapplock.setChecked(false);
					ToastUtils.show(SettingActivity.this, "�رտ��Ź��������");
					Intent intent=new Intent(getApplicationContext(),WatchDogLockService.class);
					stopService(intent);
				}else{
					set_whatchdogapplock.setChecked(true);
					ToastUtils.show(SettingActivity.this, "�������Ź��������");
					Intent intent=new Intent(getApplicationContext(),WatchDogLockService.class);
					startService(intent);
				}
			}
		});
	}
	/**
	 * �����ط����ѡ��
	 */
	public void settingChanged(View view){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("ѡ������ط��");
		builder.setSingleChoiceItems(items, sp.getInt("witch",  0), new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Editor edit=sp.edit();
				edit.putInt("which", which);
				edit.commit();
				set_changedview.setContent(items[which]);
				dialog.dismiss();
				
			}
		});
		builder.show();
	}

	/**
	 * ���´�ҳ��ʱ
	 */
	@Override
	protected void onStart() {
		
		/**
		 * �ж�Ӧ�õķ����Ƿ��ں�̨���ڣ��ھ�����Ϊtrue���������Ϊfalse
		 */
		boolean result=ServicerUtils.isServiceRunning(this, "com.cca.mobilephone.service.CallSmsSafeService");
		if(result){
			set_blacknumber.setChecked(true);
		}else{
			set_blacknumber.setChecked(false);
		}

		/**
		 * �ж�Ӧ�õķ����Ƿ��ں�̨���ڣ��ھ�����Ϊtrue���������Ϊfalse
		 */
		boolean resultaddress=ServicerUtils.isServiceRunning(this, "com.cca.mobilephone.service.ShowAddressService");
		if(resultaddress){
			set_showaddresss.setChecked(true);
		}else{
			set_showaddresss.setChecked(false);
		}
		/**
		 * �жϿ��Ź��Ƿ������ں�̨���������״̬
		 */
		boolean resultwatchdog=ServicerUtils.isServiceRunning(this, "com.cca.mobilephone.service.WatchDogLockService");
		if(resultwatchdog){
			set_whatchdogapplock.setChecked(true);
		}else{
			set_whatchdogapplock.setChecked(false);
		}
		super.onStart();
	}
}
