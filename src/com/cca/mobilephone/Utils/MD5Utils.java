package com.cca.mobilephone.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5����һ���ַ����õ���ϣֵ
 * @author Administrator
 *
 */
public class MD5Utils {

	/**
	 * ����md5�����㷨��������
	 * @param text
	 * @return
	 */
	public static String encode(String text){
		
		try {
			MessageDigest digest=MessageDigest.getInstance("md5");
			
			byte[] result=digest.digest(text.getBytes());
			StringBuffer sb=new StringBuffer();
			for(byte b:result){
				String hex=Integer.toHexString(b&0xff)+2;
				if(hex.length()==1){
					sb.append("0");
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
