package com.cca.mobilephone.service;

import java.util.Timer;
import java.util.TimerTask;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ProcessInfoUtils;
import com.cca.mobilephone.receiver.MyWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

/**
 * ����С�ؼ��ķ�������
 * @author Administrator
 *
 */
public class UpdateWidgetService extends Service {

	private Timer timer;
	private TimerTask task;
	private AppWidgetManager am;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		am = AppWidgetManager.getInstance(getApplicationContext());
		timer=new Timer();
		task=new TimerTask(){

			@Override
			public void run() {

				System.out.println("����widget���������");
				/*
				 * ���̼�ͨѶ
				 */
				ComponentName provider=new ComponentName(getApplicationContext(), MyWidget.class);
				/*
				 * �������沼���ļ�ȥ������
				 */
				RemoteViews views=new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "�������е����"+ProcessInfoUtils.getRunningProcessCount(getApplicationContext())+"��");
				String availstr=Formatter.formatFileSize(getApplicationContext(), ProcessInfoUtils.getAvialRam(getApplicationContext()));
				views.setTextViewText(R.id.process_memory, "�����ڴ棺"+availstr);
				
				//ע��һ���Զ���Ĺ㲥������
				Intent intent=new Intent();
				intent.setAction("com.cca.mobilephone");
				PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				
				am.updateAppWidget(provider, views);
			
			}};
			timer.schedule(task, 0, 5000);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer=null;
		task=null;
	}

}
