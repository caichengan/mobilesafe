package com.cca.mobilephone.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;

public class PhoneFangDaoActivity extends Activity {

	private TextView anquan_haoma;
	private ImageView yesno_lock;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.shouji_fangdao_jiemian);
		
		anquan_haoma=(TextView) findViewById(R.id.anquan_haoma);
		yesno_lock=(ImageView) findViewById(R.id.yesno_lock);
		
		sp=getSharedPreferences("config",MODE_PRIVATE);
		
		anquan_haoma.setText(sp.getString("phone", ""));
		boolean protecting=sp.getBoolean("protecting", false);
		if(protecting){
			yesno_lock.setImageResource(R.drawable.ic_action_lock_closed);
		}else{
			yesno_lock.setImageResource(R.drawable.ic_action_lock_open);
		}
		
	}
	/**
	 * 重新进入设置界面向导
	 * @param view
	 */
	public void reEnterSetup(View view){
		IntentUtils.startActivityAndFinish(PhoneFangDaoActivity.this, Setup1Activity.class);
	}
	
}
