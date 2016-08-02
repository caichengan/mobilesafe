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
 * ��ȡ�����������еĽ�����Ϣ��ҵ���߼�
 * 
 * @author Administrator
 * 
 */
public class TaskInfoProvifer {

	/**
	 * ����һ�����ϣ�����ȫ���������еĽ���
	 * @param context
	 * @return
	 */

	public static List<ProcessInfo> getRunningProcessInfo(Context context) {
		List<ProcessInfo> process = new ArrayList<ProcessInfo>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();

		// ��ȡ�������еĽ��̼���
		List<RunningAppProcessInfo> processrunninginfoinfo = am.getRunningAppProcesses();
		// ��������
		for (RunningAppProcessInfo runninginfo : processrunninginfoinfo) {
			ProcessInfo processinfo = new ProcessInfo();
			// ���̰���
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
					// ��������
					String processName = packageinfo.applicationInfo.loadLabel(pm).toString();
					processinfo.setProcessName(processName);

					// ����ͼ��
					Drawable icon = packageinfo.applicationInfo.loadIcon(pm);
				
					if (icon == null) {
						// ϵͳӦ���еĿ���û��ͼ�� ����һ��Ĭ�ϵ�ͼ��
						processinfo.setIcon(context.getResources().getDrawable(
								R.drawable.ic_launcher));

					} else {
						processinfo.setIcon(icon);
					}

					if ((packageinfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM) != 0) {
						// ϵͳ����
						processinfo.setUserProcess(false);
					} else {
						// �û�����
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
