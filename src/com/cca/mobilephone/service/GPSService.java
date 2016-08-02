package com.cca.mobilephone.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//��ȡλ�õĹ�����
		final LocationManager lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		//���ø߾���
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//�ߺĵ���
		criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
		
		String provider=lm.getBestProvider(criteria, true);
		
		lm.requestLocationUpdates(provider, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
			
				location.getLongitude();//����
				location.getLatitude();//γ��
				SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
				String text=("jing:"+location.getLongitude()+"wei"+location.getLatitude());
				
				SmsManager.getDefault().sendTextMessage(sp.getString("phone", null), null, text, null, null);
				//�رռ����ͷ���
				lm.removeUpdates(this);
				stopSelf();
			}
		});
		
	}

}
