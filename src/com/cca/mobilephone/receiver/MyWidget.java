package com.cca.mobilephone.receiver;


import com.cca.mobilephone.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {//������һ������widget��ʱ�����
		super.onReceive(context, intent);
	}
	
	//ÿ�δ���widgetʱ�������
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	//ɾ��widget�����
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	//��widget����ʼ��ʱ����
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	//����widget�ǵ��ã���ɨβ����
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}
}
