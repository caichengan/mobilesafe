package com.cca.mobilephone.activity;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;

public class CallSmsActivity extends Activity implements OnClickListener{

	private Button set_blacknumber;
	private Button call_phone;
	private Button sms_send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_callsms);
		//��ʼ���ؼ�
		set_blacknumber=(Button) findViewById(R.id.call_sms_black);
		call_phone=(Button) findViewById(R.id.call_phone);
		sms_send=(Button) findViewById(R.id.sms_send);
		
		//��������ť���õ���¼�
		set_blacknumber.setOnClickListener(this);
		call_phone.setOnClickListener(this);
		sms_send.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.call_sms_black://���ú�����
			IntentUtils.startActivityInfo(CallSmsActivity.this, CallSmsSafe.class);
			break;
		case R.id.call_phone://����绰
			 Uri uri = Uri.parse("tel:" + "");  
	         Intent intent = new Intent(Intent.ACTION_DIAL, uri);  
	         startActivity(intent);
			
			break;
		case R.id.sms_send://���Ͷ���
			Intent smsintent=new Intent();
			smsintent.setAction("android.intent.action.SEND");
			smsintent.addCategory("android.intent.category.DEFAULT");
			smsintent.setType("text/plain");
			smsintent.putExtra("sms_body", "");
			startActivity(smsintent);
			break;
		}
	}
}
