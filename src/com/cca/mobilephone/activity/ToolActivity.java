package com.cca.mobilephone.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.engine.SMSTool;

public class ToolActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tool);
	}

	/**
	 * 查询号码归属地
	 * 
	 * @param view
	 */
	public void AddessQuerry(View view) {
		IntentUtils.startActivityInfo(this, QuerryActivity.class);
	}

	/**
	 * 常用号码查询
	 * 
	 * @param view
	 */
	public void UsualNumber(View view) {
		IntentUtils.startActivityInfo(this, QuerryUsualNumber.class);
	}

	/**
	 * 短信备份
	 * 
	 * @param view
	 */
	public void SMSBackUp(View view) {
		/**
		 * 直接弹出一个进度条对话框
		 */
		final ProgressDialog pb = new ProgressDialog(this);
		pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pb.setMessage("正在备份......");

		pb.show();// show出来才能看得见
		new Thread() {
			public void run() {

				boolean result = SMSTool.SmsBackup(
						new SMSTool.SmsBackupCallBack() {
							/**
							 * 接口里面的方法
							 */
							@Override
							public void callBackprogress(int progress) {
								pb.setProgress(progress);
							}

							/**
							 * 接口里面的方法
							 */
							@Override
							public void callBackMax(int max) {
								pb.setMax(max);
							}
						}, ToolActivity.this, "back.xml");
				if (result) {
					ToastUtils.show(ToolActivity.this, "备份成功");
				} else {
					ToastUtils.show(ToolActivity.this, "备份失败");

				}
				pb.dismiss();// 是否备份成功对话框都消失
			};
		}.start();

	}
	/**
	 * 短信还原 解析back.xml
	 * @param view
	 */
	public void SMSReturnUp(View view){
		
	}
	/**
	 * 软件安全程序锁定
	 * @param view
	 */
	public void AppLocked(View view){
		Intent intent=new Intent(getApplicationContext(),AppLockedActivity.class);
		startActivity(intent);
	}
}
