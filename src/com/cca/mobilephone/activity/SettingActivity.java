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
	 * 开启自动更新
	 */
	private SettingCheckView set_check;
	/**
	 * 开启黑名单设置
	 */
	private SettingCheckView set_blacknumber;
	/**
	 * 开启自动拦截模式
	 */
	private SettingCheckView set_showaddresss;
	/**
	 * 来电显示归属风格
	 */
	private SettingChangedView set_changedview;
	/**
	 * 开启软件程序锁
	 */
	private SettingCheckView set_whatchdogapplock;
	private CheckBox check;
	private final String items[]={"金属灰","浅绿色","金黄色","浅黑色","纯白色"};
	boolean update;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		//建立文件存储数据
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//初始化控件
		set_check= (SettingCheckView) findViewById(R.id.set_update_text);
		set_blacknumber=(SettingCheckView) findViewById(R.id.set_blacknumber_text);
		set_showaddresss=(SettingCheckView) findViewById(R.id.set_showaddresss);
		set_changedview=(SettingChangedView) findViewById(R.id.set_changedview);
		set_whatchdogapplock=(SettingCheckView) findViewById(R.id.set_whatchdogapplock);
		
		set_changedview.setContent(items[sp.getInt("which", 0)]);
		/**
		 * 开启自动更新的逻辑
		 */
		check=(CheckBox) findViewById(R.id.cb_set_update);
		//获取当前checkbox是否被点击
		update=sp.getBoolean("update", false);
		check.setChecked(update);
		
		set_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor edit=sp.edit();
				if(check.isChecked()){
					check.setChecked(false);
					ToastUtils.show(SettingActivity.this, "关闭自动更新");
					edit.putBoolean("update", false);
				}else{
					check.setChecked(true);
					ToastUtils.show(SettingActivity.this, "开启自动更新");
					edit.putBoolean("update", true);
				}
				edit.commit();
			}
			
		});
		
		/**
		 * 开启拦截模式的逻辑
		 */
		//再打开这个界面的时候，判断这个服务是否在后台运行
		set_blacknumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean setblacknumber=set_blacknumber.isChecked();
				if(setblacknumber){
					set_blacknumber.setChecked(false);
					ToastUtils.show(SettingActivity.this, "关闭拦截模式");
					Intent intent=new Intent(SettingActivity.this,CallSmsSafeService.class);
					stopService(intent);
					
				}else{
					set_blacknumber.setChecked(true);
					ToastUtils.show(SettingActivity.this, "开启拦截模式");
					Intent intent=new Intent(SettingActivity.this,CallSmsSafeService.class);
					startService(intent);
					
				}
			}
		});
		/**
		 * 开启电话归属地区的显示
		 */
		set_showaddresss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(set_showaddresss.isChecked()){
					set_showaddresss.setChecked(false);
					ToastUtils.show(SettingActivity.this, "关闭归属地显示");
					Intent intent=new Intent(SettingActivity.this,ShowAddressService.class);
					stopService(intent);
				}else{
					set_showaddresss.setChecked(true);
					ToastUtils.show(SettingActivity.this, "打开归属地显示");
					Intent intent=new Intent(SettingActivity.this,ShowAddressService.class);
					startService(intent);
				}
			}
		});
		/**
		 * 开启看门狗监视软件
		 */
		set_whatchdogapplock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(set_whatchdogapplock.isChecked()){
					set_whatchdogapplock.setChecked(false);
					ToastUtils.show(SettingActivity.this, "关闭看门狗监视软件");
					Intent intent=new Intent(getApplicationContext(),WatchDogLockService.class);
					stopService(intent);
				}else{
					set_whatchdogapplock.setChecked(true);
					ToastUtils.show(SettingActivity.this, "启动看门狗监视软件");
					Intent intent=new Intent(getApplicationContext(),WatchDogLockService.class);
					startService(intent);
				}
			}
		});
	}
	/**
	 * 归属地风格点击选择
	 */
	public void settingChanged(View view){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("选择归属地风格");
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
	 * 重新打开页面时
	 */
	@Override
	protected void onStart() {
		
		/**
		 * 判断应用的服务是否在后台存在，在就设置为true，否就设置为false
		 */
		boolean result=ServicerUtils.isServiceRunning(this, "com.cca.mobilephone.service.CallSmsSafeService");
		if(result){
			set_blacknumber.setChecked(true);
		}else{
			set_blacknumber.setChecked(false);
		}

		/**
		 * 判断应用的服务是否在后台存在，在就设置为true，否就设置为false
		 */
		boolean resultaddress=ServicerUtils.isServiceRunning(this, "com.cca.mobilephone.service.ShowAddressService");
		if(resultaddress){
			set_showaddresss.setChecked(true);
		}else{
			set_showaddresss.setChecked(false);
		}
		/**
		 * 判断看门狗是否开启，在后台监视软件的状态
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
