package com.cca.mobilephone.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoKillService extends Service {

	private InnerScrrenOffReceiver receiver;
	private Timer timer;
	private TimerTask task;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver=new InnerScrrenOffReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		
		/**
		 * 常用定时器
		 */
		timer=new Timer();
		task=new TimerTask() {
			
			@Override
			public void run() {
			//System.out.println("每1秒执行一次");
			}
		};
		timer.schedule(task,0, 1000);
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		unregisterReceiver(receiver);
		receiver=null;
	}
	
	private class InnerScrrenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//System.out.println("哈哈，屏幕锁屏了");
			ActivityManager am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> infos=	am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infos){
				am.killBackgroundProcesses(info.processName);
				//System.out.println("哈哈，屏幕锁屏了");
			}
			
		}
	}
}
