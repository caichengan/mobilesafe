package com.cca.mobilephone.Utils;

import android.app.Activity;
import android.content.Intent;
/**
 * ��ͼ�����ļ��ַ�ʽ
 * @author Administrator
 *
 */
public class IntentUtils {
	
	/**
	 * ����һ������
	 */
	public static void startActivityInfo(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
	}
	
	/**
	 * ����һ�����沢�����Լ�
	 */
	public static void startActivityAndFinish(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
		context.finish();
	}
	/**
	 * �ӳٽ������һ������
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
	 * �ӳٽ��������һ�����沢�����������
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
