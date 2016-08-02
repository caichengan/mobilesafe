package com.cca.mobilephone.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.cca.mobilephone.activity.EnterPasswordActivity;
import com.cca.mobilephone.db.dao.AppClockDao;
/**
 * 看门狗服务，监视运行的软件
 * @author Administrator
 *
 */
public class WatchDogLockService extends Service {

	private ActivityManager am;
	private boolean flags;
	private AppClockDao dao;
	private InnerWatchDogReceiver receiver;
	 private List<RunningTaskInfo> infos ;
	private String packageName;
/**
 * 临时不需要保护的包名
 */
	private String temppackageName;
	/**
	 * 内容观察者
	 */
	private AppClockDaoObserver observer;
	private Intent intent;
	private List<String> packname;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//注册一个内容观察者
		Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
		observer=new AppClockDaoObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true, observer);
		
		//注册一个广播接收者
		receiver=new InnerWatchDogReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.cca.mobilephone.watchdog");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);
		
		intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//获取活动管理器
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		dao=new AppClockDao(this);
		packname = dao.findAll();
		flags=true;
		showWhatchDogStart();
	
	}

	public void showWhatchDogStart() {
		

		new Thread(){
				public void run() {
				while(flags){
				//获取任务站里面的情况，对于任务栈里面的信息进行排序，最近使用的排在最前面
					infos= am.getRunningTasks(1);
					packageName=infos.get(0).topActivity.getPackageName();
					if(packname.contains(packageName)){
						//程序需要被保护，弹出一个输入密码的对话框
						//再次判断是否需要保护
						if(packageName.equals(temppackageName)){
							//暂时不需要保护
						}else{
							//需要保护
							
							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
					}else{
						//程序不需要被保护
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		flags=false;
		unregisterReceiver(receiver);
		getContentResolver().unregisterContentObserver(observer);
		receiver=null;
	}
	/**
	 * 定义内部类广播接收者
	 * @author Administrator
	 *
	 */
	private class InnerWatchDogReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if("com.cca.mobilephone.watchdog".equals(intent.getAction())){
				temppackageName = intent.getStringExtra("packageName");
			}else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				//屏幕锁屏
				temppackageName=null;
				flags=false;
			}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
				//屏幕解锁
				flags=true;
				showWhatchDogStart();
				
			}
		}
	}
	/**
	 * 定义内容观察者内部类
	 * @author Administrator
	 *
	 */
	private class AppClockDaoObserver extends ContentObserver{
		public AppClockDaoObserver(Handler handler) {
			super(handler);
		}
		//观察到数据库内容发生变化
		@Override
		public void onChange(boolean selfChange) {

			System.out.println("内容观察者观察到数据库发生变化了");
			packname = dao.findAll();
			super.onChange(selfChange);
		}
	}
}
