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
	 * 内部类广播接收者
	 */
	private InnerOutCallReceiver innerOutCall;
	/**
	 * 窗体管理器
	 */
	private WindowManager wm;
	/**
	 * 自定义的吐司view对象
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
		//监听电话的状态
		tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
		
		
		//实例化广播接收者
		innerOutCall=new InnerOutCallReceiver();
		//设置意图过滤器
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//注册广播接收器
		registerReceiver(innerOutCall, filter);
		
	}
	@Override
	public void onDestroy() {
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		//取消广播接收者
		unregisterReceiver(innerOutCall);
		innerOutCall=null;
		super.onDestroy();
	}
	/**
	 * 监听电话的状态---接听电话----事件
	 * @author Administrator
	 *
	 */
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				String address=numberPhoneAddressdao.getAddress(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, 1).show();
				//自定义吐司
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
	 * 内部的广播接收者，监听---外拨电话-----的事件
	 * @author Administrator
	 *
	 */
	private class InnerOutCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String number=getResultData();
			String address=numberPhoneAddressdao.getAddress(number);
			//自定义吐司
			showToast(address);
		//	Toast.makeText(getApplicationContext(), address, 1).show();
		}
	}

	/**
	 * 自定义吐司
	 * @param address
	 */
	public void showToast(String address) {
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		view = View.inflate(getApplicationContext(), R.layout.item_toast, null);
		/**
		 * 注册一个触摸监听器
		 */
		view.setOnTouchListener(new OnTouchListener() {
			int startx ;
			int starty ;
		
			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN://手指触摸
					//获取坐标
					startx=(int) event.getRawX();
					starty=(int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP://手指离开
					 SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
					Editor edit=sp.edit();
					edit.putInt("lastx", params.x);
					edit.putInt("lasty", params.y);
					edit.commit();
					
					break;
				case MotionEvent.ACTION_MOVE://手指移动
					//获取偏移量
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
					//重新初始化控件坐标
					startx=(int) event.getRawX();
					starty=(int) event.getRawY();
					break;
				}
				
				return true;
			}
		});
		
		//设置背景颜色
		int which=getSharedPreferences("config", 0).getInt("which", 0);
		int bgs[]={R.drawable.btn_gray_normal,R.drawable.btn_green_normal,R.drawable.btn_gray_pressed,R.drawable.call_show_bg,R.drawable.btn_disabled};
		view.setBackgroundResource(bgs[which]);
		
		TextView tv_address=(TextView) view.findViewById(R.id.tv_address);
		tv_address.setText(address);
		params = new WindowManager.LayoutParams();
		 params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         params.format = PixelFormat.TRANSLUCENT;
         
         //左上对齐
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
