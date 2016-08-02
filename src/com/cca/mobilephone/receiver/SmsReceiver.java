package com.cca.mobilephone.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.cca.mobilephone.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//拿到超级设备管理员的实例
    	DevicePolicyManager dpm=(DevicePolicyManager) 
    			context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    	ComponentName who=new ComponentName(context,Admin.class);
		Object[] objs=(Object[]) intent.getExtras().get("pdu");
		for(Object obj:objs){
			SmsMessage smsMessage=SmsMessage.createFromPdu( (byte[]) obj);
			String body=smsMessage.getMessageBody(); 
			if("#*location*#".equals(body)){
				Intent service=new Intent(context,GPSService.class);
				context.startService(service);
				
				abortBroadcast();
			}else if("#*alarm*#".equals(body)){
				/*MediaPlayer mediaplayer=MediaPlayer.create(context, R.raw.uri);
				//循环播放
				mediaplayer.setLooping(true);
				//设置音量最大声
				mediaplayer.setVolume(1.0f, 1.0f);
		
				mediaplayer.start();*/
				abortBroadcast();
			}else if("#*wipedate*#".equals(body)){
				if(dpm.isAdminActive(who)){
					dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				}
				abortBroadcast();
			}else if("#*lockscreen*#".equals(body)){
				if(dpm.isAdminActive(who)){
					dpm.resetPassword("123", 0);
					dpm.lockNow();
				}
				abortBroadcast();
			}		
		}
	}
}
