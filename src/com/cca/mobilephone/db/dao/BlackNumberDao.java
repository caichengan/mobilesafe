package com.cca.mobilephone.db.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cca.mobilephone.db.BlackNumberOpenHeloper;
import com.cca.mobilephone.domain.BlackNumberInfo;

public class BlackNumberDao {
	
	private BlackNumberOpenHeloper openhelper;

	/**
	 * ���ݿ�Ĺ��캯��
	 * @param context
	 */
	public BlackNumberDao(Context context) {
		super();
		openhelper=new BlackNumberOpenHeloper(context);
	}
	
	/**
	 * �����ݿ������Ӻ���
	 * @param phone ���ӵĵ绰����
	 * @param mode  ģʽ
	 * @return
	 */
	public boolean add(String phone,String mode){
		SQLiteDatabase db=openhelper.getWritableDatabase();		
		ContentValues values=new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		
		long id=db.insert("blacknumberinfo", null, values);
		db.close();
		if(id!=-1){
			return true;
		}else{
		return false;
		}
		
	}
	

	/**
	 * �޸ĺ�����������ģʽ
	 * @param phone Ҫ�޸ĵĺ���������
	 * @param newmode �µ�����ģʽ
	 * @return �޸��Ƿ�ɹ�
	 */
	public boolean update(String phone,String newmode){
		
		SQLiteDatabase db=openhelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("mode", newmode);
		int rowcount=db.update("blacknumberinfo", values, "phone=?", new String[]{phone});
		db.close();
		if(rowcount==0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * ���Һ��������������ģʽ
	 * @param phone Ҫ���ҵĵ绰����
	 * @return  �������ص�ģʽ
	 */
	public String find(String phone){
		String mode=null;
		SQLiteDatabase db=openhelper.getReadableDatabase();
		Cursor cursor=db.query("blacknumberinfo", null, "phone=?", new String[]{phone},null, null, null);
		if(cursor.moveToNext()){
			mode=cursor.getString(cursor.getColumnIndex("mode"));;
		}
		cursor.close();
		db.close();
		return mode;
		
	}
	
	/**
	 * ɾ������������
	 * @param phone  Ҫɾ���ĺ���
	 * @return  �Ƿ�ɾ���ɹ�
	 */
	 
	public boolean delete(String phone){
		SQLiteDatabase db=openhelper.getWritableDatabase();
		int rowcount=db.delete("blacknumberinfo", "phone=?", new String[]{phone});
		db.close();
		if(rowcount==0){
			return false;
		}{
			return true;
		}
	}
	/**
	 * ����ȫ���ĺ�������Ϣ
	 * @return
	 */
	public List<BlackNumberInfo> findAll(){
		
		SQLiteDatabase db=openhelper.getReadableDatabase();
		Cursor cursor=db.query("blacknumberinfo", null, null, null, null, null, "_id desc");
		List<BlackNumberInfo> infos=new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			String phone=cursor.getString(cursor.getColumnIndex("phone"));
			String mode=cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info=new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			infos.add(info);
		}
		cursor.close();
		db.close();
		return infos;
	}
	
	
	/**
	 * �������ط��صĺ�������Ϣ
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int startIndex,int maxCount){
		
		SQLiteDatabase db=openhelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select _id,phone,mode from blacknumberinfo order by _id desc limit ? offset ?", new String[]{
				String.valueOf(maxCount),String.valueOf(startIndex)
		});
		
		List<BlackNumberInfo> infos=new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			String phone=cursor.getString(cursor.getColumnIndex("phone"));
			String mode=cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info=new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			infos.add(info);
		}
		cursor.close();
		db.close();
		return infos;
	}
	/**
	 * ��ҳ���ط��صĺ�������Ϣ
	 * @return
	 */
	public List<BlackNumberInfo> findPagper(int pagper){
		SQLiteDatabase db=openhelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select _id,phone,mode from blacknumberinfo order by _id desc limit ? offset ?", new String[]{
				String.valueOf(10),String.valueOf(pagper*10)
		});
		
		List<BlackNumberInfo> infos=new ArrayList<BlackNumberInfo>();
		while(cursor.moveToNext()){
			String phone=cursor.getString(cursor.getColumnIndex("phone"));
			String mode=cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info=new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			infos.add(info);
		}
		cursor.close();
		db.close();
		return infos;
	}
	/**
	 * ��ȡȫ������Ŀ
	 * @return
	 */
	
	public int getTotalCount(){
		
		SQLiteDatabase db=openhelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select count(*) from blacknumberinfo ",null);

		cursor.moveToNext();
		int total=cursor.getInt(0);
		cursor.close();
		db.close();
		return total;
	}
}












