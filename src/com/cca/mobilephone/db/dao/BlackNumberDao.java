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
	 * 数据库的构造函数
	 * @param context
	 */
	public BlackNumberDao(Context context) {
		super();
		openhelper=new BlackNumberOpenHeloper(context);
	}
	
	/**
	 * 往数据库中增加号码
	 * @param phone 增加的电话号码
	 * @param mode  模式
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
	 * 修改黑名单的拦截模式
	 * @param phone 要修改的黑名单号码
	 * @param newmode 新的拦截模式
	 * @return 修改是否成功
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
	 * 查找黑名单号码的拦截模式
	 * @param phone 要查找的电话号码
	 * @return  返回拦截的模式
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
	 * 删除黑名单号码
	 * @param phone  要删除的号码
	 * @return  是否删除成功
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
	 * 返回全部的黑名单信息
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
	 * 分批加载返回的黑名单信息
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
	 * 分页加载返回的黑名单信息
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
	 * 获取全部总条目
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












