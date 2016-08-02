package com.cca.mobilephone.db.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.cca.mobilephone.db.dao.BlackNumberDao;

public class DBAndroidTest extends AndroidTestCase {
	BlackNumberDao dao;
	@Override
	protected void setUp() throws Exception {
		dao=new BlackNumberDao(getContext());
		super.setUp();
	}
	@Override
	protected void tearDown() throws Exception {
		dao=null;
		super.tearDown();
		
	}
	
	
	public void testAdd()throws Exception{
		
		//boolean result=dao.add("13531829360", "1");
		//assertEquals(true, result);
		long baseNumber=1353189320;
		Random random=new Random();
		for(int i=0;i<200;i++){
			dao.add(String.valueOf(baseNumber+i), String.valueOf(random.nextInt(3)+1));
		}
		
	}
	public void testFind(){
	
		String result=dao.find("1353189044");
		assertEquals("2", result);
	}
	
	public void testUpdate(){

		boolean result=dao.update("1353189003", "2");
		assertEquals(true, result);
	}
	
	public void testDelete(){
	
		boolean result=dao.delete("1353189040");
		assertEquals(true, result);
		
	}
	
}
