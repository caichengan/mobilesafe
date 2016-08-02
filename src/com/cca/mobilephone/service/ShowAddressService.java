package com.cca.mobilephone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.db.dao.numberPhoneAddressdao;

public class ShowAddressService extends Service {

	private TelephonyManager tm;
	
	private MyListener listener;
	/**
	 * �ڲ���㲥������
	 */
	private InnerOutCallReceiver innerOutCall;
	/**
	 * ���������
	 */
	private WindowManager wm;
	/**
	 * �Զ������˾view����
	 */
	private View view;
	private WindowManager.LayoutParams params;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener=new MyListener();
		//�����绰��״̬
		tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
		
		
		//ʵ�����㲥������
		innerOutCall=new InnerOutCallReceiver();
		//������ͼ������
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//ע��㲥������
		registerReceiver(innerOutCall, filter);
		
	}
	@Override
	public void onDestroy() {
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		//ȡ���㲥������
		unregisterReceiver(innerOutCall);
		innerOutCall=null;
		super.onDestroy();
	}
	/**
	 * �����绰��״̬---�����绰----�¼�
	 * @author Administrator
	 *
	 */
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING://����״̬
				String address=numberPhoneAddressdao.getAddress(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, 1).show();
				//�Զ�����˾
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(view!=null){
				wm.removeView(view);
				view=null;
				}
				break;
				
			}
			super.onCallStateChanged(state, incomingNumber);
			
		}
		
	}
	
	/**
	 * �ڲ��Ĺ㲥�����ߣ�����---�Ⲧ�绰-----���¼�
	 * @author Administrator
	 *
	 */
	private class InnerOutCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String number=getResultData();
			String address=numberPhoneAddressdao.getAddress(number);
			//�Զ�����˾
			showToast(address);
		//	Toast.makeText(getApplicationContext(), address, 1).show();
		}
	}

	/**
	 * �Զ�����˾
	 * @param address
	 */
	public void showToast(String address) {
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		view = View.inflate(getApplicationContext(), R.layout.item_toast, null);
		/**
		 * ע��һ������������
		 */
		view.setOnTouchListener(new OnTouchListener() {
			int startx ;
			int starty ;
		
			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN://��ָ����
					//��ȡ����
					startx=(int) event.getRawX();
					starty=(int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP://��ָ�뿪
					 SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
					Editor edit=sp.edit();
					edit.putInt("lastx", params.x);
					edit.putInt("lasty", params.y);
					edit.commit();
					
					break;
				case MotionEvent.ACTION_MOVE://��ָ�ƶ�
					//��ȡƫ����
					int newx=(int) event.getRawX();
					int newy=(int) event.getRawY();
					int dx=newx-startx;
					int dy=newy-starty;
					params.x +=dx;
					params.y +=dy;
					if(params.x>(wm.getDefaultDisplay().getWidth()-view.getWidth())){
						params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y>(wm.getDefaultDisplay().getHeight()-view.getHeight())){
						params.x=wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					
					wm.updateViewLayout(view, params);
					//���³�ʼ���ؼ�����
					startx=(int) event.getRawX();
					starty=(int) event.getRawY();
					break;
				}
				
				return true;
			}
		});
		
		//���ñ�����ɫ
		int which=getSharedPreferences("config", 0).getInt("which", 0);
		int bgs[]={R.drawable.btn_gray_normal,R.drawable.btn_green_normal,R.drawable.btn_gray_pressed,R.drawable.call_show_bg,R.drawable.btn_disabled};
		view.setBackgroundResource(bgs[which]);
		
		TextView tv_address=(TextView) view.findViewById(R.id.tv_address);
		tv_address.setText(address);
		params = new WindowManager.LayoutParams();
		 params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         params.format = PixelFormat.TRANSLUCENT;
         
         //���϶���
         params.gravity=Gravity.LEFT+Gravity.TOP;
         SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
         params.x=sp.getInt("lastx", 0);
         params.y=sp.getInt("lasty", 0);
         params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
         params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
       
		wm.addView(view, params);
		
	}
}
