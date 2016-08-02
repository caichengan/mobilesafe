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
	 * ���ݿ�Ĺ��캯��
	 * @param context
	 */
	public AppClockDao(Context context) {
		this.context=context;
		helper=new AppClockOpenHeloper(context);
	}
	/**
	 * ����һ�����ݰ���
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
			 //��������һ����Ϣ
			 Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
			 context.getContentResolver().notifyChange(uri, null);
				return true;
		}else{
			return false;
		}
	}
	/**
	 * ɾ��һ������������
	 * @param packageName
	 * @return
	 */
	public boolean delete(String  packname){
		
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		int result=db.delete("appclockinfo", "packname=?", new String[]{packname});
		
		db.close();
		if(result>0){
			//��������һ����Ϣ
			 Uri uri=Uri.parse("content://com.cca.mobilephone.appclockdb");
			 context.getContentResolver().notifyChange(uri, null);
			return true;
		}else{
			return false;
		}
	
	}

	/**
	 * ����һ������Ӧ�ó���İ����Ƿ���Ҫ������
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
	 * ��������Ӧ�ó���İ���
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












