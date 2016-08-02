package com.cca.mobilephone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppClockOpenHeloper extends SQLiteOpenHelper {

	/**
	 * ����һ��Ӧ�ó�������ݿ�ʱ�����ݿ��ļ������ƽ�applock
	 * @param context
	 */
	
	public AppClockOpenHeloper(Context context) {
		super(context, "appclock.db", null, 1);
	}

	//�����ݿ��һ�α�������ʱ���������ķ������ʺ������ݿ��ṹ�ĳ�ʼ��
	// blacknumberinfo ����
	//_id ���ݿ��������������
	//phone ����������ĵ绰����
	//mode ����ģʽ��1���绰���� 2���������� 3��ȫ������
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table appclockinfo (_id integer primary key autoincrement, packname varchar(20))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
