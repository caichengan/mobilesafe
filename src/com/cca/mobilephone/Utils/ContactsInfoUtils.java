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
		//��ȡ�����ṩ�ߵĽ�����
		ContentResolver resolver=context.getContentResolver();
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri=Uri.parse("content://com.android.contacts/data");
		//�α�
		Cursor cursor=resolver.query(uri, new String[]{"contact_id"}, null, null, null);
		//�����α꼯��
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
						//����
						infos.setName(data1);
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					//�绰
						infos.setPhone(data1);
					}else if("vnd.android.cursor.item/email_v2".equals(mimetype)){
					//�绰
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
	       //��ϵ����Ϣ����
	    List<ContactsInfo> contactList  = new ArrayList<ContactsInfo>(); 
     // ��ȡ�ֻ�ͨѶ¼��Ϣ  
     ContentResolver resolver = context.getContentResolver();  
     /** ���е���ϵ����Ϣ */  
	    Cursor  personCur = resolver.query(ContactsContract.Contacts.CONTENT_URI, null,  
             null, null, null);  
     // ѭ����������ȡÿ����ϵ�˵������͵绰����  
     while (personCur.moveToNext()) {  
         // �½���ϵ�˶���  
    	 ContactsInfo cInfor =new ContactsInfoUtils().new ContactsInfo();  
         // ��ϵ������  
         String cname = "";  
         // ��ϵ�˵绰  
         String cnum = "";  
         // ��ϵ��id����  
         String ID;  

         ID = personCur.getString(personCur  
                 .getColumnIndex(ContactsContract.Contacts._ID));  
         // ��ϵ������  
         cname = personCur.getString(personCur  
                 .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  

         // id����������  
         int id = Integer.parseInt(ID);
         if (id > 0) {  
             // ��ȡָ��id����ĵ绰����  
             Cursor c = resolver.query(  
                     ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                     null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
                             + "=" + ID, null, null);  
             // �����α�  
             while (c.moveToNext()) {  
                 cnum = c.getString(c  
                         .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
             }  

             // ��������뵽������  
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


