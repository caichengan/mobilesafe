package com.cca.mobilephone.activity;

import android.content.Intent;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.ToastUtils;

public class Setup3Activity extends SetupBaseActivity {
	private Button contacts_select;
	private EditText ed_select_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		ed_select_number=(EditText) findViewById(R.id.ed_select_number);
		ed_select_number.setText(sp.getString("phone", ""));
		//获取联系人列表
		contacts_select=(Button) findViewById(R.id.btn_select_contacts);
		contacts_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//选择联系人
				//开启一个界面并返回值
				Intent intent=new Intent(Setup3Activity.this,ContactsActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(resultCode==0){
				//获取返回的电话号码并显示到控件上
				String savedphone=data.getStringExtra("phone");
				ed_select_number.setText(savedphone);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void showNext() {
		if(TextUtils.isEmpty(ed_select_number.getText())){
			ToastUtils.show(Setup3Activity.this, "安全号码不能为空");
		}else{
			String phone=ed_select_number.getText().toString().replace("-", "").trim();
			Editor edit=sp.edit();
			edit.putString("phone", phone);
			edit.commit();
			IntentUtils.startActivityAndFinish(Setup3Activity.this, Setup4Activity.class);
		}
	}
	@Override
	public void showPre() {
		IntentUtils.startActivityAndFinish(Setup3Activity.this, Setup2Activity.class);
	}
}
