package com.cca.mobilephone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.db.dao.numberPhoneAddressdao;

public class QuerryActivity extends Activity {

	private EditText ed_number;

	private TextView tv_address;

	private Vibrator vb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_querry);
		
		ed_number=(EditText) findViewById(R.id.ed_number);
		tv_address=(TextView) findViewById(R.id.tv_numbergetaddress);
		vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
	}
	public void QuerrynumberAddress(View view){
		//获取输入的电话号码
		String number=ed_number.getText().toString().trim();
		if(TextUtils.isEmpty(number)){
			ToastUtils.show(this, "电话号码不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        ed_number.startAnimation(shake);
	       // vb.vibrate(2000);//抖动2秒钟
	        vb.vibrate(new long[]{100,1000},1);
			return;
		}
		String location=numberPhoneAddressdao.getAddress(number);
		
		tv_address.setText("电话号码的归属地："+location);
		
	}
}
