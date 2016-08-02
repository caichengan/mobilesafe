package com.cca.mobilephone.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.cca.mobilephone.R;
import com.cca.mobilephone.domain.ProcessInfo;

/**
 * 获取所有正在运行的进程信息的业务逻辑
 * 
 * @author Administrator
 * 
 */
public class TaskInfoProvifer {

	/**
	 * 返回一个集合，包含全部正在运行的进程
	 * @param context
	 * @return
	 */

	public static List<ProcessInfo> getRunningProcessInfo(Context context) {
		List<ProcessInfo> process = new ArrayList<ProcessInfo>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();

		// 获取正在运行的进程集合
		List<RunningAppProcessInfo> processrunninginfoinfo = am.getRunningAppProcesses();
		// 遍历集合
		for (RunningAppProcessInfo runninginfo : processrunninginfoinfo) {
			ProcessInfo processinfo = new ProcessInfo();
			// 进程包名
			String packageName = runninginfo.processName;
			processinfo.setPackageName(packageName);

			System.out.println("--------" + packageName + "------");
			long menSize = am
					.getProcessMemoryInfo(new int[] { runninginfo.pid })[0]
					.getTotalPrivateDirty() * 1024;
			processinfo.setMenSize(menSize);
			
	
				try {
					PackageInfo  packageinfo = pm.getPackageInfo(packageName, 0);
					
					//String processName=packageinfo.applicationInfo.processName;
					// 进程名称
					String processName = packageinfo.applicationInfo.loadLabel(pm).toString();
					processinfo.setProcessName(processName);

					// 进程图标
					Drawable icon = packageinfo.applicationInfo.loadIcon(pm);
				
					if (icon == null) {
						// 系统应用有的可能没有图标 设置一个默认的图标
						processinfo.setIcon(context.getResources().getDrawable(
								R.drawable.ic_launcher));

					} else {
						processinfo.setIcon(icon);
					}

					if ((packageinfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM) != 0) {
						// 系统进程
						processinfo.setUserProcess(false);
					} else {
						// 用户进程
						processinfo.setUserProcess(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					processinfo.setProcessName(packageName);
					processinfo.setIcon(context.getResources().getDrawable(
							R.drawable.ic_launcher));
				}
				process.add(processinfo);
		}

		return process;

	}
}
