package com.cca.mobilephone.Utils;

import android.app.Activity;
import android.widget.Toast;
/**
 * 一个弹吐司的工具类
 * @author Administrator
 *
 */
public class ToastUtils {

	
	/**
	 * 可以在任意线程（包括主线程和子线程）中更新UI
	 * @param activity 上下文
	 * @param text 文本
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
	 * 可以在任意线程（包括主线程和子线程）中更新UI
	 * 
	 * @param activity 上下文
	 * @param text 文本
	 * @param len 时长
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
