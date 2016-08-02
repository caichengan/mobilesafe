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
 * 获取进程的相关信息
 * @author Administrator
 *
 */
public class ProcessInfoUtils {

	/**
	 * 获取系统进程的总数
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		/*
		 * 获取进程管理器
		 */
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//返回正在运行进程的集合
		List<RunningAppProcessInfo>infos= am.getRunningAppProcesses();
		return infos.size();
		
	}
	/**
	 * 获取系统进程内存的可用空间
	 * @return
	 * @param context 上下文，获得可用内存
	 */
	public static long getAvialRam(Context context){
		/*
		 * 获取进程管理器
		 */
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
		
	}
	/**
	 * 获取系统进程内存的总空间
	 * @return
	 * @param context 上下文，获得总空间
	 */
	public static long getTotalRam(Context context){
		/*
		 * 获取进程管理器
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
			//返回的是字节
			return Long.parseLong(sb.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
		}
	}
}
