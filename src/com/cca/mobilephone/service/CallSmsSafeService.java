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
	 * ʵ�������ݿ�
	 */
	private BlackNumberDao dao;
	/**
	 * ����㲥������
	 */
	private InnerReceiver receiver;
	/**
	 * ����绰����ķ���
	 */
	private TelephonyManager tm;
	/**
	 * �����绰��״̬
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
		 * ʵ�����绰����ķ���
		 */
		tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//ע��绰�ļ�����
		listener=new MyPhoneStateListener();
		//�����绰��״̬�ĸı�
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		receiver=new InnerReceiver();
		
		/**
		 * ���ŵ�����
		 */
		IntentFilter filter=new IntentFilter();
		//���ù��Ķ��ŵ����Ķ���
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		//����ע��㲥������
		registerReceiver(receiver, filter);
		System.out.println("�������������ؿ����ˣ���������");
		super.onCreate();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		//���ٵ绰״̨̬�ļ�����
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
		listener=null;
		
		receiver=null;
	}
	//�ڲ���㲥������
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			/**
			 * ���ض��ŵ绰
			 */
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])obj);
				String address=smsMessage.getOriginatingAddress();
				String result=dao.find(address);
				if("2".equals(result)||"3".equals(result)){
					System.out.println("��������������ģʽ������");
					abortBroadcast();
					
				}
				//��������
				String body=smsMessage.getMessageBody();
				if(body.contains("��ʹ��")){//�ִ��㷨
					SmsManager.getDefault().sendTextMessage("13531829360", null, "������������ʹ��һ����Ϣ", null, null);
					abortBroadcast();
				}
			}
		}
	}
	/**
	 * �绰״̬�����ı�ļ���
	 * @author Administrator
	 *
	 */
	class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE://����״̬
				break;
				
			case TelephonyManager.CALL_STATE_RINGING://����״̬
				
				String mode=dao.find(incomingNumber);
				if(mode.equals("1")||mode.equals("3")){
					System.out.println("�Ҷϵ绰");
					//�Ҷϵ绰
					endCall();//�Ҷ��˵绰���ܼ�¼��û������
					//���Ӻ��м�¼�ĵ����ݿ⣬��ʲôʱ�������ˣ�
					//�Ͱ���ɾ��
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
			case TelephonyManager.CALL_STATE_OFFHOOK://����״̬
				break;
			}
		}

		/**
		 * �Ҷϵ绰�ķ��������÷���
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
	 * ɾ�����ż�¼
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {

		ContentResolver resolver=getContentResolver();
		Uri uri=Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
}
