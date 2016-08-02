package com.cca.mobilephone.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * ��ȡ���̵������Ϣ
 * @author Administrator
 *
 */
public class ProcessInfoUtils {

	/**
	 * ��ȡϵͳ���̵�����
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		/*
		 * ��ȡ���̹�����
		 */
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//�����������н��̵ļ���
		List<RunningAppProcessInfo>infos= am.getRunningAppProcesses();
		return infos.size();
		
	}
	/**
	 * ��ȡϵͳ�����ڴ�Ŀ��ÿռ�
	 * @return
	 * @param context �����ģ���ÿ����ڴ�
	 */
	public static long getAvialRam(Context context){
		/*
		 * ��ȡ���̹�����
		 */
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
		
	}
	/**
	 * ��ȡϵͳ�����ڴ���ܿռ�
	 * @return
	 * @param context �����ģ�����ܿռ�
	 */
	public static long getTotalRam(Context context){
		/*
		 * ��ȡ���̹�����
		 */
		/*ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);*/
		
		
		try {
			File file=new File("/proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=br.readLine();
			StringBuffer sb=new StringBuffer();
			for(char c :line.toCharArray()){
				if(c>='0' && c<='9'){
					sb.append(c);
				}
			}
			//���ص����ֽ�
			return Long.parseLong(sb.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
		}
	}
}
