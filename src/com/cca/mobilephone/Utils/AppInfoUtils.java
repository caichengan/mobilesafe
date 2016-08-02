package com.cca.mobilephone.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * ��ȡӦ�ó���İ汾���Ͱ汾��
 * @author Administrator
 *
 */
public class AppInfoUtils {

	/*
	 * 	��ȡ�汾��
	 */
	public static String getAppInfoName(Context context){
		  
		try {
	        	//��ȡpackageManagerʵ��
				PackageManager pm=context.getPackageManager();
				//��ȡ�汾��Ϣ
				PackageInfo packageinfo= pm.getPackageInfo(context.getPackageName(), 0);
				String versionname=packageinfo.versionName;
				return versionname;
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		return "";
		
	}
	
	
	/*
	 * ��ȡ�汾��
	 */
	public static int getAppInfoNumber(Context context){
		  
		try {
	        	//��ȡpackageManagerʵ��
				PackageManager pm=context.getPackageManager();
				//��ȡ�汾��Ϣ
				PackageInfo packageinfo= pm.getPackageInfo(context.getPackageName(), 0);
				int versionnumber=packageinfo.versionCode;
				return versionnumber;
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		return 0;
		
	}
}
