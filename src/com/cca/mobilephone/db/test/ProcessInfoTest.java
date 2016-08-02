package com.cca.mobilephone.db.test;

import com.cca.mobilephone.Utils.ProcessInfoUtils;
import com.cca.mobilephone.engine.TaskInfoProvifer;

import android.test.AndroidTestCase;

public class ProcessInfoTest extends AndroidTestCase {

	public  void TestProcess()throws Exception{
		
		TaskInfoProvifer.getRunningProcessInfo(getContext());
		
	}
}
