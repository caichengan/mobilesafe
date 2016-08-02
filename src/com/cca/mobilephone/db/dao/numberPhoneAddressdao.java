package com.cca.mobilephone.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 查询电话的归属地区
 * @author Administrator
 *
 */
public class numberPhoneAddressdao {

	/**
	 * 返回电话号码的归属地区
	 * @param number ：电话号码
	 * @return
	 */
	public static String getAddress(String number){
		
		//查询不到就直接返回电话号码
		String location=number;
		//^1\\d{9}$   正则表达式
		if(number.matches("^1[358]\\d{9}$")){
		//获取数据库
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor=db.rawQuery("select location from data2 where id=(select outkey from data1 where id=?) ", new String[]{number.substring(0, 7)});
		if(cursor.moveToFirst()){
			location=cursor.getString(0);
		}
		
		
		}else{
			
			int len=number.length();
			switch(len){
			case 3:
				if(number.subSequence(0, 3).equals("110")){
					return "匪警";
				}else if(number.subSequence(0, 3).equals("119")){
					return "火警";
				}else if(number.subSequence(0, 3).equals("120")){
					return "急救";
				}
				
				break;
				
			case 4:
				return "模拟器";
			case 5:
				return "客服电话";
			case 6:
				if(number.startsWith("0")||number.startsWith("1")){
					return "本地电话";
				}
			case 7:
				if(number.startsWith("0")||number.startsWith("1")){
					return "本地电话";
				}
			default:
					if(number.length()>=10){
						//获取数据库
						SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
						Cursor cursor=db.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 3)});
						if(cursor.moveToNext()){
							location=cursor.getString(0).substring(0, cursor.getString(0).length()-2);
						}
						
						cursor.close();
						
					}
					if(number.length()>=10){
						//获取数据库
						SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
						Cursor cursor=db.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 4)});
						if(cursor.moveToNext()){
							location=cursor.getString(0).substring(0, cursor.getString(0).length()-2);
						}
						
						cursor.close();
						db.close();
					}
					
					return location;
			}
		}
		return location;
	}
}
