package com.cca.mobilephone.db.dao;

import java.util.ArrayList;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.cca.mobilephone.db.AppClockOpenHeloper;

public class AppClockDao {
	
	private AppClockOpenHeloper helper;
	private Context context;

	/**
	 * 数据库的构造函数
	 * @param context
	 */
	public AppClockDao(Context context) {
		this.context=context;
		helper=new AppClockOpenHeloper(context);
	}
	/**
	 * 插入一条数据包名
	 * @param packageName
	 * @return
	 */
	public boolean insert(String  packname){
		
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("packname", packname);
		long result=db.insert("appclockinfo", null, values);
		
		db.close();
		if(result!=-1){
			 //大声发送一个消息
			 Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
			 context.getContentResolver().notifyChange(uri, null);
				return true;
		}else{
			return false;
		}
	}
	/**
	 * 删除一条包名的数据
	 * @param packageName
	 * @return
	 */
	public boolean delete(String  packname){
		
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		int result=db.delete("appclockinfo", "packname=?", new String[]{packname});
		
		db.close();
		if(result>0){
			//大声发送一个消息
			 Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
			 context.getContentResolver().notifyChange(uri, null);
			return true;
		}else{
			return false;
		}
	
	}

	/**
	 * 查找一条数据应用程序的包名是否需要被锁定
	 * @param packageName
	 * @return
	 */
	public boolean find(String packname){
		boolean result =false;
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("appclockinfo", null, "packname=?",new String[]{ packname}, null, null, null);
		 result=cursor.moveToNext();
		
		 cursor.close();
		db.close();
		return result;
		
	}
	/**
	 * 查找所有应用程序的包名
	 * @param packageName
	 * @return
	 */
	public List<String> findAll(){
		List<String>packagename=new ArrayList<String>();
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("appclockinfo", new String[]{"packname"}, null,null, null, null, null);
		while( cursor.moveToNext()){
			String packag=cursor.getString(0);
			packagename.add(packag);
		}
		 cursor.close();
		db.close();
		return packagename;
		
	}
}












