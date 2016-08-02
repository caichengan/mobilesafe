package com.cca.mobilephone.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactsInfoUtils {

	public static List<ContactsInfo> getContactsInfo(Context context){
		
	List<ContactsInfo> infocontacts=new ArrayList<ContactsInfo>();
		//获取内容提供者的解析器
		ContentResolver resolver=context.getContentResolver();
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri=Uri.parse("content://com.android.contacts/data");
		//游标
		Cursor cursor=resolver.query(uri, new String[]{"contact_id"}, null, null, null);
		//遍历游标集合
		while(cursor.moveToNext()){
			String id=cursor.getString(cursor.getColumnIndex("contact_id"));
			System.out.println("id"+id);
			
			if(id!=null){
				ContactsInfo infos=new ContactsInfoUtils().new ContactsInfo();
				Cursor datacursor=resolver.query(dataUri, new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{id}, null);
				while(datacursor.moveToNext()){
					String data1=datacursor.getString(0);
					String mimetype=datacursor.getString(1);
					if("vnd.android.cursor.item/name".equals(mimetype)){
						//姓名
						infos.setName(data1);
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					//电话
						infos.setPhone(data1);
					}else if("vnd.android.cursor.item/email_v2".equals(mimetype)){
					//电话
					infos.setEmail(data1);
					}
					
				}
				infocontacts.add(infos);
			}
			return infocontacts;
		  }
		return infocontacts;
		
		
		
	}
	public static List<ContactsInfo> getROMContacts(Context context){
	       //联系人信息集合
	    List<ContactsInfo> contactList  = new ArrayList<ContactsInfo>(); 
     // 获取手机通讯录信息  
     ContentResolver resolver = context.getContentResolver();  
     /** 所有的联系人信息 */  
	    Cursor  personCur = resolver.query(ContactsContract.Contacts.CONTENT_URI, null,  
             null, null, null);  
     // 循环遍历，获取每个联系人的姓名和电话号码  
     while (personCur.moveToNext()) {  
         // 新建联系人对象  
    	 ContactsInfo cInfor =new ContactsInfoUtils().new ContactsInfo();  
         // 联系人姓名  
         String cname = "";  
         // 联系人电话  
         String cnum = "";  
         // 联系人id号码  
         String ID;  

         ID = personCur.getString(personCur  
                 .getColumnIndex(ContactsContract.Contacts._ID));  
         // 联系人姓名  
         cname = personCur.getString(personCur  
                 .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  

         // id的整型数据  
         int id = Integer.parseInt(ID);
         if (id > 0) {  
             // 获取指定id号码的电话号码  
             Cursor c = resolver.query(  
                     ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                     null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
                             + "=" + ID, null, null);  
             // 遍历游标  
             while (c.moveToNext()) {  
                 cnum = c.getString(c  
                         .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
             }  

             // 将对象加入到集合中  
             cInfor.setName(cname);  
             cInfor.setPhone(cnum);  

             contactList.add(cInfor);  
         
         }
     }
     return contactList;
	}
	
	
	public class ContactsInfo{
		public String name;
		public String email;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String phone;
	}
}


