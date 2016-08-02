package com.cca.mobilephone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteBroadCastReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {

		TelephonyManager tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		sp=context.getSharedPreferences("config", 0);
		
		boolean protecting=sp.getBoolean("protecting", false);
		if(protecting){
			//1、检查当前手机的sim卡序列
			String realsim=tm.getSimSerialNumber();
			//2、取出之前sim卡的序列号
			String bindsim=sp.getString("phone", "");
			//3、判断两次的序列号是否一致
			if(realsim.equals(bindsim)){
				System.out.println("两次的sim卡是一致的");
			}else{
				System.out.println("手机卡不一致，手机可能被盗！");
				//偷偷的向安全号码发送一条消息
				SmsManager.getDefault().sendTextMessage("18924932465", null, "sim changes!", null, null);
			}
		}else{

			System.out.println("手机没有开启手机防盗！");
		}
	}
}
