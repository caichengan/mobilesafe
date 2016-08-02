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
	 * ��ѯ���������
	 * 
	 * @param view
	 */
	public void AddessQuerry(View view) {
		IntentUtils.startActivityInfo(this, QuerryActivity.class);
	}

	/**
	 * ���ú����ѯ
	 * 
	 * @param view
	 */
	public void UsualNumber(View view) {
		IntentUtils.startActivityInfo(this, QuerryUsualNumber.class);
	}

	/**
	 * ���ű���
	 * 
	 * @param view
	 */
	public void SMSBackUp(View view) {
		/**
		 * ֱ�ӵ���һ���������Ի���
		 */
		final ProgressDialog pb = new ProgressDialog(this);
		pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pb.setMessage("���ڱ���......");

		pb.show();// show�������ܿ��ü�
		new Thread() {
			public void run() {

				boolean result = SMSTool.SmsBackup(
						new SMSTool.SmsBackupCallBack() {
							/**
							 * �ӿ�����ķ���
							 */
							@Override
							public void callBackprogress(int progress) {
								pb.setProgress(progress);
							}

							/**
							 * �ӿ�����ķ���
							 */
							@Override
							public void callBackMax(int max) {
								pb.setMax(max);
							}
						}, ToolActivity.this, "back.xml");
				if (result) {
					ToastUtils.show(ToolActivity.this, "���ݳɹ�");
				} else {
					ToastUtils.show(ToolActivity.this, "����ʧ��");

				}
				pb.dismiss();// �Ƿ񱸷ݳɹ��Ի�����ʧ
			};
		}.start();

	}
	/**
	 * ���Ż�ԭ ����back.xml
	 * @param view
	 */
	public void SMSReturnUp(View view){
		
	}
	/**
	 * �����ȫ��������
	 * @param view
	 */
	public void AppLocked(View view){
		Intent intent=new Intent(getApplicationContext(),AppLockedActivity.class);
		startActivity(intent);
	}
}
