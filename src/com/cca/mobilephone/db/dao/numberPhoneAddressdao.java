package com.cca.mobilephone.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * ��ѯ�绰�Ĺ�������
 * @author Administrator
 *
 */
public class numberPhoneAddressdao {

	/**
	 * ���ص绰����Ĺ�������
	 * @param number ���绰����
	 * @return
	 */
	public static String getAddress(String number){
		
		//��ѯ������ֱ�ӷ��ص绰����
		String location=number;
		//^1\\d{9}$   ������ʽ
		if(number.matches("^1[358]\\d{9}$")){
		//��ȡ���ݿ�
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
					return "�˾�";
				}else if(number.subSequence(0, 3).equals("119")){
					return "��";
				}else if(number.subSequence(0, 3).equals("120")){
					return "����";
				}
				
				break;
				
			case 4:
				return "ģ����";
			case 5:
				return "�ͷ��绰";
			case 6:
				if(number.startsWith("0")||number.startsWith("1")){
					return "���ص绰";
				}
			case 7:
				if(number.startsWith("0")||number.startsWith("1")){
					return "���ص绰";
				}
			default:
					if(number.length()>=10){
						//��ȡ���ݿ�
						SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
						Cursor cursor=db.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 3)});
						if(cursor.moveToNext()){
							location=cursor.getString(0).substring(0, cursor.getString(0).length()-2);
						}
						
						cursor.close();
						
					}
					if(number.length()>=10){
						//��ȡ���ݿ�
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
