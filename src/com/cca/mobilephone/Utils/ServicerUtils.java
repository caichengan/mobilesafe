package com.cca.mobilephone.Utils;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
public class ServicerUtils {

	/**
	 * 判断系统的服务是否在后台运行
	 */
	public static boolean isServiceRunning(Context context,String StringName){
		//获取进程和服务的管理器
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos=am.getRunningServices(1000);
		for(ActivityManager.RunningServiceInfo info:infos){
				String className=info.service.getClassName();
				//System.out.println(className);
				if(StringName.equals(className)){
					return true;
				}
		}
		return false;
	}
}
