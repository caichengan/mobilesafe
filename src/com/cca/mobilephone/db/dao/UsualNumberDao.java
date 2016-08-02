package com.cca.mobilephone.db.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsualNumberDao {

	/**
	 * 返回分组的个数
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
	 * 返回分组位置中的孩子个数
	 * @param groupPosition 分组位置
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
	 * 根据分组的位置查询分组的名称
	 * @param groupPosition 分组位置
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
	 * 根据分组的位置和孩子的位置查询孩子的名称
	 * @param groupPosition 分组位置
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












