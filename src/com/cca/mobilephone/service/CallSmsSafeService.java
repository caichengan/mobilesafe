package com.cca.mobilephone.service;

import java.lang.reflect.Method;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.cca.mobilephone.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {
	/**
	 * 实例化数据库
	 */
	private BlackNumberDao dao;
	/**
	 * 定义广播接收者
	 */
	private InnerReceiver receiver;
	/**
	 * 定义电话管理的服务
	 */
	private TelephonyManager tm;
	/**
	 * 监听电话的状态
	 */
	private MyPhoneStateListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		dao=new BlackNumberDao(this);
		/**
		 * 实例化电话管理的服务
		 */
		tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//注册电话的监听器
		listener=new MyPhoneStateListener();
		//监听电话的状态的改变
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		receiver=new InnerReceiver();
		
		/**
		 * 短信的拦截
		 */
		IntentFilter filter=new IntentFilter();
		//设置关心短信到来的动作
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		//代码注册广播接收者
		registerReceiver(receiver, filter);
		System.out.println("黑名单短信拦截开启了！！！！！");
		super.onCreate();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		//销毁电话状态台的监听器
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
		listener=null;
		
		receiver=null;
	}
	//内部类广播接收者
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			/**
			 * 拦截短信电话
			 */
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])obj);
				String address=smsMessage.getOriginatingAddress();
				String result=dao.find(address);
				if("2".equals(result)||"3".equals(result)){
					System.out.println("黑名单短信拦截模式。。。");
					abortBroadcast();
					
				}
				//智能拦截
				String body=smsMessage.getMessageBody();
				if(body.contains("天使客")){//分词算法
					SmsManager.getDefault().sendTextMessage("13531829360", null, "帮你拦截了天使客一条消息", null, null);
					abortBroadcast();
				}
			}
		}
	}
	/**
	 * 电话状态发生改变的监听
	 * @author Administrator
	 *
	 */
	class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE://空闲状态
				break;
				
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				
				String mode=dao.find(incomingNumber);
				if(mode.equals("1")||mode.equals("3")){
					System.out.println("挂断电话");
					//挂断电话
					endCall();//挂断了电话可能记录还没有生成
					//监视呼叫记录的的数据库，看什么时候生成了，
					//就把它删除
					Uri uri=Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,new ContentObserver(new Handler()) {

						@Override
						public void onChange(boolean selfChange) {
							super.onChange(selfChange);
							deleteCallLog(incomingNumber);
						}
					});
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://接听状态
				break;
			}
		}

		/**
		 * 挂断电话的方法，利用反射
		 */
		public void endCall() {
			try {
				Class clazz=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
				Method method=clazz.getDeclaredMethod("getService", String.class);
				IBinder ibinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
				ITelephony iTelephony=ITelephony.Stub.asInterface(ibinder);
				iTelephony.endCall();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 删除拨号记录
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {

		ContentResolver resolver=getContentResolver();
		Uri uri=Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
}
