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
			//1����鵱ǰ�ֻ���sim������
			String realsim=tm.getSimSerialNumber();
			//2��ȡ��֮ǰsim�������к�
			String bindsim=sp.getString("phone", "");
			//3���ж����ε����к��Ƿ�һ��
			if(realsim.equals(bindsim)){
				System.out.println("���ε�sim����һ�µ�");
			}else{
				System.out.println("�ֻ�����һ�£��ֻ����ܱ�����");
				//͵͵����ȫ���뷢��һ����Ϣ
				SmsManager.getDefault().sendTextMessage("18924932465", null, "sim changes!", null, null);
			}
		}else{

			System.out.println("�ֻ�û�п����ֻ�������");
		}
	}
}
