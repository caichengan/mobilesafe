package com.cca.mobilephone.activity;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SetupBaseActivity extends Activity {

	SharedPreferences sp;
	//先声明一个手势识别器
	private GestureDetector mGestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//2、初始化手势识别器
		mGestureDetector=new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				/**
				 * e1、手指触摸屏幕的一瞬间
				 * e2、手指离开屏幕的一瞬间
				 * velocityX、velocityY：水平方向和竖直方向的速度,单位px/s
				 * 
				 */
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					ToastUtils.show(SetupBaseActivity.this, "动作不合法");
					return true;
				}
				if((e1.getRawX()-e2.getRawX())>150){
					showNext();
					overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
					return true;
				}
				if((e2.getRawX()-e1.getRawX())>150){
					showPre();
					overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
					return true;
				}
	
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	//第三步、是手势识别器识别用户的动作
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	public abstract void showNext();
	public abstract void showPre();
	/**
	 * 下一步
	 * @param view
	 */
	public void next(View view){
		showNext();
		overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
	}
	/**
	 * 上一步
	 * @param view
	 */
	public void pre(View view){
		showPre();
		overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
	}
	
}
