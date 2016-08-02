package com.cca.mobilephone.db.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsualNumberDao {

	/**
	 * ���ط���ĸ���
	 * @return
	 */
	public static int getGroupCount(SQLiteDatabase db){
		Cursor cursor =db.rawQuery("select count(*) from classlist", null);
		cursor.moveToNext();
		int count=cursor.getInt(0);
		cursor.close();
		return count;
		
	}
	
	/**
	 * ���ط���λ���еĺ��Ӹ���
	 * @param groupPosition ����λ��
	 * @return
	 */
	public static int getChildGroupCount(SQLiteDatabase db,int groupPosition){
		int newgroupposition=groupPosition+1;
		String tablename="table"+newgroupposition;
		Cursor cursor =db.rawQuery("select count(*) from "+tablename, null);
		cursor.moveToNext();
		int count=cursor.getInt(0);
		cursor.close();
		return count;
		
	}
	/**
	 * ���ݷ����λ�ò�ѯ���������
	 * @param groupPosition ����λ��
	 * @return
	 */
	public static String getNameByGroupCountposition(SQLiteDatabase db,int groupPosition){
		int newgroupposition=groupPosition+1;
		String tablename="table"+newgroupposition;
		Cursor cursor =db.rawQuery("select name from classlist where idx=?", new String[]{String.valueOf(newgroupposition)});
		cursor.moveToNext();
		String name=cursor.getString(0);
		cursor.close();
		return name;
		
	}
	/**
	 * ���ݷ����λ�úͺ��ӵ�λ�ò�ѯ���ӵ�����
	 * @param groupPosition ����λ��
	 * @param 
	 * @return
	 */
	public static String getChildrenNameByPosition(SQLiteDatabase db,int groupPosition,int childrenPosition){
		int newgroupposition=groupPosition+1;
		int newchildrenposition=childrenPosition+1;
		String tablename="table"+newgroupposition;
		Cursor cursor =db.rawQuery("select name,number from "+tablename+" where _id=?", 
				new String[]{String.valueOf(newchildrenposition)});
		cursor.moveToNext();
		String name=cursor.getString(0);
		String number=cursor.getString(1);
		
		cursor.close();
		return name+"\n"+number;
		
	}
}












