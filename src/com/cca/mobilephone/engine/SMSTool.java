package com.cca.mobilephone.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * ���Ź���
 * 
 * @author Administrator
 * 
 */
public class SMSTool {

	/**
	 * �ӿڻص�������һЩ���󷽷�
	 * 
	 * @author Administrator
	 * 
	 */
	public interface SmsBackupCallBack {
		/**
		 * �ӿڻص�
		 * 
		 * @param max
		 *            ���ֵ
		 */
		public abstract void callBackMax(int max);

		/**
		 * �ӿڻص�
		 * 
		 * @param progress
		 */
		public abstract void callBackprogress(int progress);

	}

	/**
	 * ���ű��ݵ�ҵ���߼�
	 * 
	 * @param context
	 *            ������
	 * @param filename
	 *            ������ļ���
	 * @return
	 */
	public static boolean SmsBackup(SmsBackupCallBack callBack,
			Context context, String filename) {

		try {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"date", "body", "type" }, null, null, null);
			File file = new File(Environment.getExternalStorageDirectory(),
					filename);
			FileOutputStream fos = new FileOutputStream(file);
			XmlSerializer serialer = Xml.newSerializer();
			// �������л�����
			serialer.setOutput(fos, "utf-8");

			serialer.startDocument("utf-8", true);
			serialer.startTag(null, "info");
			cursor.moveToNext();
			int max = cursor.getCount();
			int progress = 0;
			callBack.callBackMax(max);
			while (cursor.moveToNext()) {
				// cursor.moveToNext();
				serialer.startTag(null, "sms");

				serialer.startTag(null, "address");
				String address = cursor.getString(0);
				serialer.text(address);
				serialer.endTag(null, "address");

				serialer.startTag(null, "date");
				String date = cursor.getString(1);
				serialer.text(date);
				serialer.endTag(null, "date");

				serialer.startTag(null, "body");
				String body = cursor.getString(2);
				serialer.text(body);
				serialer.endTag(null, "body");

				serialer.startTag(null, "type");
				String type = cursor.getString(3);
				serialer.text(type);
				serialer.endTag(null, "type");

				serialer.endTag(null, "sms");
				progress++;
				callBack.callBackprogress(progress);

			}
			cursor.close();
			serialer.startTag(null, "info");
			serialer.endDocument();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}




