package com.cca.mobilephone.log;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.animation.AnimatorSet.Builder;
import android.app.Application;

import com.cca.mobilephone.Utils.Logger;

/**
 * 代表的就是当前手机卫士的应用程序
 * 《b》一定要注意在清单文件中配置
 * @author Administrator
 *
 */
public class MobileSafeApplication extends Application {

	//开天地，老母子方法
	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());
	}
	/**
	 * 捕获异常信息存进sd中，再上传至服务器中
	 * @author Administrator
	 *
	 */
	private class MyExceptionHandler implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Logger.i("","发生了异常，被哥捕获到了。。。。。");
			//并不能把异常消化掉，只是在应用程序挂掉之前，来一个留遗嘱的时间
			
			try {   
				//获取手机适配的信息
				Field[] fields=Builder.class.getDeclaredFields();
				StringBuffer sb=new StringBuffer();
				for(Field field:fields){
					String value=field.get(null).toString();
					String name=field.getName();
					sb.append(value);
					sb.append(":");
					sb.append(name);
					sb.append("\n");
				}
				
				///mnt/sdcard/Ringtones/error.log目录下
				FileOutputStream out=new FileOutputStream("/mnt/sdcard/error.log");
				//阻塞性方法，写到内存中，内存输出流
				StringWriter wr=new StringWriter();
				
				PrintWriter err=new PrintWriter(wr);//打印输出流，异步输出流
				ex.printStackTrace(err);
				String errorlog=wr.toString();
				sb.append(errorlog);
				out.write(sb.toString().getBytes());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//ActivityManager 可以杀死别的进程，不能自杀，而专注于自杀是	android.os.Process
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
