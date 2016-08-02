package com.cca.mobilephone.Utils;

import android.app.Activity;
import android.widget.Toast;
/**
 * һ������˾�Ĺ�����
 * @author Administrator
 *
 */
public class ToastUtils {

	
	/**
	 * �����������̣߳��������̺߳����̣߳��и���UI
	 * @param activity ������
	 * @param text �ı�
	 * 
	 */
	public static void show(final Activity activity,final String text){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(activity, text, 0).show();
		}else{
			activity.runOnUiThread(new Runnable(){
				public void run() {
					Toast.makeText(activity, text, 0).show();
				};
			});	
		}
	}
	
	/**
	 * �����������̣߳��������̺߳����̣߳��и���UI
	 * 
	 * @param activity ������
	 * @param text �ı�
	 * @param len ʱ��
	 */

	public static void show(final Activity activity,final String text,final int len){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(activity, text, len).show();
		}else{
			activity.runOnUiThread(new Runnable(){
				public void run() {
					Toast.makeText(activity, text, len).show();
				};
			});	
		}
	}

}
