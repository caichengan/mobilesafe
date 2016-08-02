package com.cca.mobilephone.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;

public class EnterPasswordActivity extends Activity {

	private ImageView img_appicon;
	private TextView tv_appname;
	private EditText et_enter;
	private String packageName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_enterpassword);
		img_appicon=(ImageView) findViewById(R.id.img_appicon);
		tv_appname=(TextView) findViewById(R.id.tv_appname);
		et_enter=(EditText) findViewById(R.id.et_enter);
		
		Intent intent=getIntent();
		packageName = intent.getStringExtra("packageName");
		
		PackageManager pm=getPackageManager();
		try {
			//��ȡ������ͼ��
			String appName=pm.getPackageInfo(packageName, 0).applicationInfo.loadLabel(pm).toString();
			Drawable icon=pm.getPackageInfo(packageName, 0).applicationInfo.loadIcon(pm);
			img_appicon.setImageDrawable(icon);
			tv_appname.setText("������Ӧ�ó���:"+appName);
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void onBackPressed() {
		
		Intent intent=new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		
	}
	/**
	 * ���治�ɼ���ʱ����ô˷���
	 */
	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
	/**
	 * ���ȷ�����������Ľ���
	 * @param view
	 */
	public void click(View view){
		
		String enterpasseord=et_enter.getText().toString().trim();
		if(enterpasseord.equals("123")){
			finish();
			//�����Ź�����һ����Ϣ��ʱ����Ҫ�������Զ���㲥������
			Intent intent=new Intent();
			intent.setAction("com.cca.mobilephone.watchdog");
			intent.putExtra("packageName", packageName);
			sendBroadcast(intent);
			
		}else{
			ToastUtils.show(this, "�����������");
		}
		
	}
}
