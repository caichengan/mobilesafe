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
 * ����ľ��ǵ�ǰ�ֻ���ʿ��Ӧ�ó���
 * ��b��һ��Ҫע�����嵥�ļ�������
 * @author Administrator
 *
 */
public class MobileSafeApplication extends Application {

	//����أ���ĸ�ӷ���
	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHandler());
	}
	/**
	 * �����쳣��Ϣ���sd�У����ϴ�����������
	 * @author Administrator
	 *
	 */
	private class MyExceptionHandler implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Logger.i("","�������쳣�����粶���ˡ���������");
			//�����ܰ��쳣��������ֻ����Ӧ�ó���ҵ�֮ǰ����һ����������ʱ��
			
			try {   
				//��ȡ�ֻ��������Ϣ
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
				
				///mnt/sdcard/Ringtones/error.logĿ¼��
				FileOutputStream out=new FileOutputStream("/mnt/sdcard/error.log");
				//�����Է�����д���ڴ��У��ڴ������
				StringWriter wr=new StringWriter();
				
				PrintWriter err=new PrintWriter(wr);//��ӡ��������첽�����
				ex.printStackTrace(err);
				String errorlog=wr.toString();
				sb.append(errorlog);
				out.write(sb.toString().getBytes());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//ActivityManager ����ɱ����Ľ��̣�������ɱ����רע����ɱ��	android.os.Process
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
