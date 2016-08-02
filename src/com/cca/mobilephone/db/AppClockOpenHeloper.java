package com.cca.mobilephone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppClockOpenHeloper extends SQLiteOpenHelper {

	/**
	 * 创建一个应用程序的数据库时，数据库文件的名称叫applock
	 * @param context
	 */
	
	public AppClockOpenHeloper(Context context) {
		super(context, "appclock.db", null, 1);
	}

	//当数据库第一次被创建的时候调用下面的方法，适合做数据库表结构的初始化
	// blacknumberinfo 表名
	//_id 数据库的主键，自增长
	//phone 黑名单里面的电话号码
	//mode 拦截模式：1、电话拦截 2、短信拦截 3、全部拦截
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table appclockinfo (_id integer primary key autoincrement, packname varchar(20))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
