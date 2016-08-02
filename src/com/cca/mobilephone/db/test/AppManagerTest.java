package com.cca.mobilephone.db.test;

import com.cca.mobilephone.engine.AppManagerInfos;

import android.test.AndroidTestCase;

public class AppManagerTest extends AndroidTestCase {

	public void testGetApp()throws Exception{
		
		AppManagerInfos.getAppManagerInfos(getContext());
		
	}
}
