package com.cca.mobilephone.receiver;


import com.cca.mobilephone.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {//创建第一个桌面widget的时候调用
		super.onReceive(context, intent);
	}
	
	//每次创建widget时都会调用
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	//删除widget会调用
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	//对widget做初始化时调用
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	//结束widget是调用，做扫尾工作
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}
}
