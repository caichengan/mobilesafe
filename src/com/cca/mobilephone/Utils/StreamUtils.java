package com.cca.mobilephone.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 功能是把一个流对象转换成字符串对象
 * 解析成功则返回值为字符串，不成功则为null
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
