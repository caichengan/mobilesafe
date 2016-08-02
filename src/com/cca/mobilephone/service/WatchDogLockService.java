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
 * ���Ź����񣬼������е����
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
 * ��ʱ����Ҫ�����İ���
 */
	private String temppackageName;
	/**
	 * ���ݹ۲���
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
		
		//ע��һ�����ݹ۲���
		Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
		observer=new AppClockDaoObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true, observer);
		
		//ע��һ���㲥������
		receiver=new InnerWatchDogReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.cca.mobilephone.watchdog");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);
		
		intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//��ȡ�������
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
				//��ȡ����վ������������������ջ�������Ϣ�����������ʹ�õ�������ǰ��
					infos= am.getRunningTasks(1);
					packageName=infos.get(0).topActivity.getPackageName();
					if(packname.contains(packageName)){
						//������Ҫ������������һ����������ĶԻ���
						//�ٴ��ж��Ƿ���Ҫ����
						if(packageName.equals(temppackageName)){
							//��ʱ����Ҫ����
						}else{
							//��Ҫ����
							
							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
					}else{
						//������Ҫ������
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
	 * �����ڲ���㲥������
	 * @author Administrator
	 *
	 */
	private class InnerWatchDogReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if("com.cca.mobilephone.watchdog".equals(intent.getAction())){
				temppackageName = intent.getStringExtra("packageName");
			}else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				//��Ļ����
				temppackageName=null;
				flags=false;
			}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
				//��Ļ����
				flags=true;
				showWhatchDogStart();
				
			}
		}
	}
	/**
	 * �������ݹ۲����ڲ���
	 * @author Administrator
	 *
	 */
	private class AppClockDaoObserver extends ContentObserver{
		public AppClockDaoObserver(Handler handler) {
			super(handler);
		}
		//�۲쵽���ݿ����ݷ����仯
		@Override
		public void onChange(boolean selfChange) {

			System.out.println("���ݹ۲��߹۲쵽���ݿⷢ���仯��");
			packname = dao.findAll();
			super.onChange(selfChange);
		}
	}
}
