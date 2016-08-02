package com.cca.mobilephone.db.test;

import com.cca.mobilephone.db.dao.numberPhoneAddressdao;

import android.test.AndroidTestCase;

public class NumberAddressTest extends AndroidTestCase {

	public static void testAddress(){
		String location=numberPhoneAddressdao.getAddress("13531829360");
		System.out.println("Address:πÈ Ùµÿ---"+location);
	}
}
