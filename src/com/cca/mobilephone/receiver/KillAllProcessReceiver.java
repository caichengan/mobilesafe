package com.cca.mobilephone.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 接收清理进程的广播接收者
 * @author Administrator
 *
 */
public class KillAllProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		for(RunningAppProcessInfo info:infos){
			am.killBackgroundProcesses(info.processName);
			
		}
		Toast.makeText(context, "清理完毕", 0).show();
		
		
	}

}
