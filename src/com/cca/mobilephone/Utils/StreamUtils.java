package com.cca.mobilephone.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * �����ǰ�һ��������ת�����ַ�������
 * �����ɹ��򷵻�ֵΪ�ַ��������ɹ���Ϊnull
 */
public class StreamUtils {

	public static String readStream(InputStream is){
		
		try {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte []buffer=new byte[1024];
			int len=0;
			while((len=is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
				
			}
			is.close();
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	} 
	
}
