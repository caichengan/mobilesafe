package com.cca.mobilephone.Utils;

import android.app.Activity;
import android.content.Intent;
/**
 * 意图开启的几种方式
 * @author Administrator
 *
 */
public class IntentUtils {
	
	/**
	 * 进入一个界面
	 */
	public static void startActivityInfo(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
	}
	
	/**
	 * 进入一个界面并结束自己
	 */
	public static void startActivityAndFinish(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
		context.finish();
	}
	/**
	 * 延迟进入进入一个界面
	 * 
	 */
	public static void startActivityDelay(final Activity context,final Class<?>cls,final long time){
		
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent=new Intent(context,cls);
				context.startActivity(intent);
			}
		}.start();
	}
	
	/**
	 * 延迟进入进入另一个界面并销毁这个界面
	 * 
	 */
	public static void startActivityDelayAndFinish(final Activity context,final Class<?>cls,final long time){
		
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent=new Intent(context,cls);
				context.startActivity(intent);
				context.finish();
			}
		}.start();
	}
}
