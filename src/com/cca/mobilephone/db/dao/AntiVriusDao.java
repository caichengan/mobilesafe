package com.cca.mobilephone.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 病毒数据库
 * @author Administrator
 *
 */
public class AntiVriusDao {
	/**
	 * 在数据库中查找程序特征码是否存在，存在就是病毒软件，不存在就不是
	 * @param md5
	 * @return
	 */
	public static String isVriusdb(String md5){
		
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/antivirus.db",
									null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor=db.rawQuery("select desc from datable where md5=?", new String[]{md5});
		String desc=null;
		if(cursor.moveToNext()){
			 desc=cursor.getString(0);
		}
		db.close();
		cursor.close();
		return desc;
	}
	/**
	 * 更新数据库版本号
	 * @param md5
	 * @return
	 */
	public static void updateVersion(int newversion){
		
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/antivirus.db",
									null, SQLiteDatabase.OPEN_READONLY);
		ContentValues values=new ContentValues();
		values.put("subcnt", newversion);
		db.update("version", values, null, null);
		db.close();
	}
	/**
	 * 获取数据库版本号
	 * @param md5
	 * @return
	 */
	public static int getVersion(){
		
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/antivirus.db",
									null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor=db.rawQuery("select subcnt from version", null);
		int version=0;
		if(cursor.moveToNext()){
			 version=cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return version;
	}
	/**
	 * 添加一条病毒数据到数据库中
	 * @param md5
	 * @param type
	 * @param desc
	 * @param name
	 */
public static void gaddVriusInfo(String md5,String type,String desc,String name ){
		
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/antivirus.db",
									null, SQLiteDatabase.OPEN_READONLY);
		ContentValues values=new ContentValues();
		values.put("md5", md5);
		values.put("type", type);
		values.put("desc", desc);
		values.put("name", name);
		db.insert("datable", null, values);
		db.close();
	}
}












